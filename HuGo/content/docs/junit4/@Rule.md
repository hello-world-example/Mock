# @Rule 注解

一个 JUnit Rule 就是一个实现了 `TestRule` 的类，这些类的作用类似于 `@Before`、`@After`，是用来**在每个测试方法的执行前后执行一些代码**的一个方法。 @Before、@After 只能作用于一个类，如果同一个 setup 需要在两个类里面同时使用，那么你就要在两个测试类里面定义相同的@Before方法，然后里面写相同的代码，这就造成了代码重复。此外，JUnit Rule还能做一些 @Before、@After这些注解做不到的事情，那就是他们可以动态的获取将要运行的测试类、测试方法的信息。

除了 `@Rule` 外， `@ClassRule` 是类级别的，在执行一个测试类的时候只会调用一次被注解的 Rule。



## 内建实现

- TemporaryFolder：测试可以创建文件与目录并且会在测试运行结束后将其删除。这对于那些与文件系统打交道且独立运行的测试来说很有用。
- ExternalResource：这是一种资源使用模式，它会提前建立好资源并且会在测试结束后将其销毁。这对于那些使用socket、嵌入式服务器等资源的测试来说很有用。
- ErrorCollector：可以让测试在失败后继续运行并在测试结束时报告所有错误。这对于那些需要验证大量独立条件的测试来说很有用（尽管这本身可能是个“test smell”）。
- ExpectedException：可以在测试中指定期望的异常类型与消息。
- Timeout：为类中的所有测试应用相同的超时时间。

## 使用示例

```java
public class RuleTestDemo {

    // 使用Timeout这个Rule
    @Rule
    public Timeout timeout = new Timeout(1000);  

    @Test
    public void testMethod1() throws Exception {
        Thread.sleep(1001);
    }

    @Test
    public void testMethod2() throws Exception {
        Thread.sleep(999);
    }
}
```



## 自定义示例

```java

public class MethodNameRule implements TestRule {

    public Statement apply(final Statement base, final Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                //在测试方法运行之前做一些事情，在base.evaluate()之前
                String className = description.getClassName();
                String methodName = description.getMethodName();

                base.evaluate();  //运行测试方法

                //在测试方法运行之后做一些事情，在base.evaluate()之后
                System.out.println("Class name:"+className+", method name: "+methodName);
            }
        };
    }
}
```



## Read More

- [JUnit 注解之 Rule](http://www.testclass.net/junit/rule)
- [利用 Rule 扩展 JUnit](https://haibin369.iteye.com/blog/2088541)