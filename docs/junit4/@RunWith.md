# @RunWith 注解

> When a class is annotated with `@RunWith` or extends a class annotated with `@RunWith`, JUnit will invoke the class it references to run the tests in that class instead of the runner built into JUnit.
>
> - JavaDoc for @RunWith http://junit.org/javadoc/latest/org/junit/runner/RunWith.html
> - The default runner is `BlockJUnit4ClassRunner` which supersedes the older `JUnit4ClassRunner`
> - Annotating a class with `@RunWith(JUnit4.class)` will always invoke the default JUnit 4 runner in the current version of JUnit, this class aliases the current default JUnit 4 class runner.

## Runner 抽象类

```java
package org.junit.runner;

import org.junit.runner.notification.RunNotifier;

/**
 * 创建自定义 Runner 时，除了实现抽象方法之外，
 * 还必须提供一个构造函数，该构造函数将会接收到被测试类。
 * 
 * @see org.junit.runner.Description
 * @see org.junit.runner.RunWith
 * @since 4.0
 */
public abstract class Runner implements Describable {

    public abstract Description getDescription();

    /**
     * 单元测试 开始、结束、失败 的时候会调用该方法
     */
    public abstract void run(RunNotifier notifier);

    public int testCount() {
        return getDescription().testCount();
    }
}
```

## 常见实现

### @RunWith(JUnit4.**class**)

```java
/**
 * @since 4.5
 */
public final class JUnit4 extends BlockJUnit4ClassRunner {
 
    public JUnit4(Class<?> klass) throws InitializationError {
        super(klass);
    }
}
```

### @RunWith(MockitoJUnitRunner.**class**)

```java
public class MockitoJUnitRunner extends Runner implements Filterable {

    private final RunnerImpl runner;

    public MockitoJUnitRunner(Class<?> klass) throws InvocationTargetException {
        runner = new RunnerFactory().create(klass);
    }

    @Override
    public void run(final RunNotifier notifier) {           
        runner.run(notifier);
    }

    @Override
    public Description getDescription() {
        return runner.getDescription();
    }

   public void filter(Filter filter) throws NoTestsRemainException {
        //filter is required because without it UnrootedTests show up in Eclipse
      runner.filter(filter);
   }
}
```

### @RunWith(SpringRunner.**class**)

```java

/**
 * 注意:要求 JUnit 4.12+
 *
 * @since 4.3
 * @see SpringJUnit4ClassRunner
 * @see org.springframework.test.context.junit4.rules.SpringClassRule
 * @see org.springframework.test.context.junit4.rules.SpringMethodRule
 */
public final class SpringRunner extends SpringJUnit4ClassRunner {

	/**
	 * 初始化 org.springframework.test.context.TestContextManager
	 */
	public SpringRunner(Class<?> clazz) throws InitializationError {
		super(clazz);
	}

}
```

## 内建 Runner

- `@RunWith(Suite.class)` 把多个相关的测试类看做一个测试套件一起测试。配合 `@Suite.SuiteClasses({ TestA.class, TestB.class})` 一起使用
- `@RunWith(Parameterized.class)` 使用多个参数组合多次执行同一个测试用例
- `@RunWith(Categories.class)` 执行一个“类别”。和Suite类似，只是Suite是执行指定类中的所有用例，而Categories执行的范围更小
- `@RunWith(Theories.class)` 提供一组参数的排列组合值作为待测方法的输入参数

## 自定义 Runner 

### MyRunner

```java
public class MyRunner extends BlockJUnit4ClassRunner {

    public MyRunner(Class unitTestClass) throws InitializationError {
        super(unitTestClass);
        System.out.println("MyRunner:" + unitTestClass);
    }

    @Override
    protected Statement withBefores(FrameworkMethod method, Object target, Statement statement) {
        // 在默认的 JUnit 生命周期开始之前，做一些其他的操作
        System.out.println("method Name：" + method.getName());
        System.out.println("target：" + target);

        // @Before
        return super.withBefores(method, target, statement);
    }
}
```

### MyUnitTest

```java
@RunWith(MyRunner.class)
public class MyUnitTest {

    @Test
    public void test() {
			// noting
    }
}
```

### 输出

```java
MyRunner:class xyz.kail.demo.mock.one.MyUnitTest
method Name：test
target：xyz.kail.demo.mock.one.MyUnitTest@3f8f9dd6
```



## Read More

- 官方文档 [@RunWith annotation](https://github.com/junit-team/junit4/wiki/Test-runners#runwith-annotation)
- [JUnit高级用法之@RunWith](https://my.oschina.net/itblog/blog/1550753)

