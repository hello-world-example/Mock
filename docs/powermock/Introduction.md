# PowerMock 简介



Mockito 不能 mock `静态`、`final`、`私有方法` 等，PowerMock 能够完美的弥补 Mockito 的不足。

PowerMock 使用一个 **自定义类加载器** 和 **字节码操作** 来模拟 

- 静态方法
- 构造函数
- final 类 和 方法
- 私有方法
- 去 除静态初始化器
- ...



`@PrepareForTest` 这个注解告诉 `PowerMock` 准备测试某些类，需要使用此注解定义的类通常是**需要进行字节码操作的类**，包括带有 `final`、`private`、`static` 或 `native方法` 的类。

这个注解可以放在 **测试类** 或者 **单独的测试方法** 中。

- 如果放在一个类上，这个测试类中的所有测试方法都将由 PowerMock处理 
- 如果要为单个方法重写此行为，只需在特定测试方法上放置 `@PrepareForTest` 注解。例如，如果您想在测试方法A中修改类X，但在测试方法B中希望X完好无损，那么这很有用。在这种情况下，您在方法B上放置@PrepareForTest，并从value()列表中排除类X
- 有时你需要准备内部类来进行测试，这可以通过提供应该模拟到 `fullyQualifiedNames()` 列表的内部类的完全限定名来完成

```java
Target( { ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface PrepareForTest {
    Class<?>[] value() default IndicateReloadClass.class;

    String[] fullyQualifiedNames() default "";
    
    ByteCodeFramework byteCodeFramework() default ByteCodeFramework.Javassist;
}
```



## 访问内部状态

- `Whitebox.setInternalState(..)` 设置一个实例或类的非公共成员
- `Whitebox.getInternalState(..)` 得到的实例或类的非公共成员
- `Whitebox.invokeMethod(..)` 调用实例或类的非公共方法
- `Whitebox.invokeConstructor(..)` 调用私有构造函数创建类的实例

> 以上这些都可以在不使用 `PowerMock` 的情况下实现，这只是普通的 Java反射



## 抑制不需要的行为

有时候需要 抑制某些 **构造函数**、**方法** 或 **静态初始化器** 的行为，以便单元测试你自己的代码。



### 抑制构造函数

假设我们要测试 `ExampleWithEvilParent` 类的 `getMessage()` 方法，看起来好像很简单。但是这个父类试图加载一个 dll文件，当你为这个 `ExampleWithEvilParent` 类运行一个单元测试时它将不会出现。使用 `PowerMock`，您可以禁止 `EvilParent` 的构造函数，以便您可以单元测试 `ExampleWithEvilParent` 类。

```java
public class EvilParent {
    public EvilParent() {
      	// 试图加载一个 dll文件
        System.loadLibrary("evil.dll");
    }
}
```

```java
@Getter
@AllArgsConstructor
public class ExampleWithEvilParent extends EvilParent {
    private final String message;
}
```

测试用例

```java
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertEquals;
import static org.powermock.api.support.membermodification.MemberModifier.constructor;
import static org.powermock.api.support.membermodification.MemberModifier.suppress;

@RunWith(PowerMockRunner.class)
@PrepareForTest(ExampleWithEvilParent.class)
public class ExampleWithEvilParentTest {

    @Test
    public void testSuppressConstructorOfEvilParent() throws Exception {
        // 抑制构造函数
        suppress(constructor(EvilParent.class));

        final String message = "myMessage";
        ExampleWithEvilParent tested = new ExampleWithEvilParent(message);
        assertEquals(message, tested.getMessage());
    }
}
```



上面的例子在抑制超类构造函数和被测类时起作用。另一种抑制被测试类的构造函数，我们通过`Whitebox.newInstance` 方法实现。例如，如果你自己的代码在它的构造函数中做了一些事情，那么很难进行单元测试。这实例化该类而不调用构造函数。

```java
@Getter
public class ExampleWithEvilConstructor {

    private final String message;

    public ExampleWithEvilConstructor(String message) {
        System.loadLibrary("evil.dll");
        this.message = message;
    }
}
```


也可通过以下方式，**创建类的实例而不调用构造方法**：

```java
ExampleWithEvilConstructor tested = Whitebox.newInstance(ExampleWithEvilConstructor.class);
```



### 抑制私有方法

在某些情况下，需要 **压制一个方法并使其返回一些默认值**，因为它会阻止你对自己的类进行单元测试。看看下面的组装示例：

```java
@AllArgsConstructor
public class ExampleWithEvilMethod {

    private final String message;

    public String getMessage() {
      	// 调用了 getEvilMessage 方法
        return message + getEvilMessage();
    }

    private String getEvilMessage() {
        System.loadLibrary("evil.dll");
        return "evil!";
    }
}
```



如果 `System.loadLibrary("evil.dll")` 在测试 `getMessage()` 方法时执行语句，则测试将失败。避免这种情况的一个简单方法是简单地抑制该 `getEvilMessage` 方法。你可以使用：

```java
suppress(method(ExampleWithEvilMethod.class, "getEvilMessage"));
```


完整的测试如下：

```java
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertEquals;
import static org.powermock.api.support.membermodification.MemberModifier.method;
import static org.powermock.api.support.membermodification.MemberModifier.suppress;

@RunWith(PowerMockRunner.class)
@PrepareForTest(ExampleWithEvilMethod.class)
public class ExampleWithEvilMethodTest {

    @Test
    public void testSuppressMethod() throws Exception {
        // 抑制私有方法
        suppress(method(ExampleWithEvilMethod.class, "getEvilMessage"));

        final String message = "myMessage";
        ExampleWithEvilMethod tested = new ExampleWithEvilMethod(message);
      	// getEvilMessage 会返回 null
        assertEquals(message + null, tested.getMessage());
    }
}
```



### 抑制静态初代码块

```java
package xyz.kail.demo.mock.power;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ExampleWithEvilStaticInitializer {

    static {
        System.loadLibrary("evil.dll");
    }

    private final String message;
}
```




直接贴出完整测试：

```java
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.SuppressStaticInitializationFor;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertEquals;

@RunWith(PowerMockRunner.class)
public class ExampleWithEvilStaticInitializerTest {

    @Test
    @SuppressStaticInitializationFor("xyz.kail.demo.mock.power.ExampleWithEvilStaticInitializer")
    public void testSuppressStaticInitializer() throws Exception {
        final String message = "myMessage";
        ExampleWithEvilStaticInitializer tested = new ExampleWithEvilStaticInitializer(message);
        assertEquals(message, tested.getMessage());
    }
}
```



### 抑制字段

```java
@Getter
public class MyClass {

    private Map<String, Object> myObject = new HashMap<>();

}
```

抑制

```java
suppress(field(MyClass.class, "myObject"));
```



> 在 `powermock` + `mockit` 中，抑制语法如下：
>
> ```
> PowerMockito.suppress(PowerMockito.method(类.class,"方法名"));
> ```



## Mockito 扩展功能 



### Mock public 静态方法

```java
public class StaticMethodCase {

    public String getSomeWorld() {
        return getHello() + " World";
    }

    /**
     * public 静态方法
     */
    public static String getHello() {
        return "Hello";
    }
}
```

测试用例

```java
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertEquals;

@RunWith(PowerMockRunner.class)
@PrepareForTest(StaticMethodCase.class)
public class StaticMethodCaseTest {

    @Test
    public void testGetSomeWorld() {
        // ❤ 如果没有 Mock 静态方法的行为，静态方法会返回 null ❤
        PowerMockito.mockStatic(StaticMethodCase.class);

        // Mock 静态方法的行为
        PowerMockito.when(StaticMethodCase.getHello()).thenReturn("你好");
        // Mockito 与 PowerMockito 效果一样
        // Mockito.when(StaticMethodCase.getHello()).thenReturn("你好");

        assertEquals("你好 World", new StaticMethodCase().getSomeWorld());
    }

}
```

### Mock final方法

```
public class FinalMethodCase {

    public String getSomeWorld() {
        return getHello() + " World";
    }

    /**
     * final 方法
     */
    public final String getHello() {
        System.out.println("getHello");
        return "Hello";
    }
}
```

使用 Mockito 会报一下错误：

```
org.mockito.exceptions.misusing.MissingMethodInvocationException: 
when() requires an argument which has to be 'a method call on a mock'.
For example:
    when(mock.getArticles()).thenReturn(articles);

Also, this error might show up because:
1. you stub either of: final/private/equals()/hashCode() methods.
   Those methods *cannot* be stubbed/verified.
   Mocking methods declared on non-public parent classes is not supported.
2. inside when() you don't call method on mock but on some other object.
```

使用 PowerMock 可以对 final 方法进行 Mock 

```java

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertEquals;

@RunWith(PowerMockRunner.class)
public class FinalMethodCaseTest {

    @Test
    @PrepareForTest(FinalMethodCase.class)
    public void testGetSomeWorld() {

        FinalMethodCase mock = PowerMockito.mock(FinalMethodCase.class);

        // 当调用 getSomeWorld 方法的时候，执行真实的方法
        PowerMockito.when(mock.getSomeWorld()).thenCallRealMethod();
        // Mock final方法的行为
        PowerMockito.when(mock.getHello()).thenReturn("你好");

        assertEquals("你好 World", mock.getSomeWorld());
    }
}
```

### Mock 私有方法

```java
public class PrivateMethodCase {

    public String getSomeWorld() {
        return getHello() + " World";
    }

    /**
     * final 方法
     */
    private String getHello() {
        return "Hello";
    }
}
```

测试用例

```java
@RunWith(PowerMockRunner.class)
public class PrivateMethodCaseTest {

    @Test
    @PrepareForTest(PrivateMethodCase.class)
    public void testGetSomeWorld() throws Exception {

        PrivateMethodCase mock = PowerMockito.mock(PrivateMethodCase.class);

        // 当调用 getSomeWorld 方法的时候，执行真实的方法
        PowerMockito.when(mock.getSomeWorld()).thenCallRealMethod();
        // ❤ Mock 静态方法的行为，Mockito 没有提供这种方式 ❤
        PowerMockito.when(mock, "getHello").thenReturn("你好");

        assertEquals("你好 World", mock.getSomeWorld());
    }
}
```



## Read More

- [Powermock学习之基本用法](https://blog.csdn.net/weixin_39471249/article/details/80398212)
- [Powermock私有方法](https://blog.csdn.net/devil_wangyu/article/details/78890261)