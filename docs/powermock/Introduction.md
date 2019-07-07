# PowerMock 简介



Mockito 不能 mock `静态`、`final`、`私有方法` 等，PowerMock 能够完美的弥补 Mockito 的不足。

PowerMock 使用一个 **自定义类加载器** 和 **字节码操作** 来模拟 

- 静态方法
- 构造函数
- final 类 和 方法
- 私有方法
- 去 除静态初始化器
- ...



`@PrepareForTest` 这个注解告诉 `PowerMock` 准备测试某些类



## Read More

- [Powermock学习之基本用法](https://blog.csdn.net/weixin_39471249/article/details/80398212)