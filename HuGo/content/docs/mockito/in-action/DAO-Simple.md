# Mock 持久层依赖



## Maven 依赖

```xml
<dependency>
    <groupId>org.mockito</groupId>
    <artifactId>mockito-core</artifactId>
    <version>1.10.19</version>
</dependency>

<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-test</artifactId>
    <version>4.3.24.RELEASE</version>
</dependency>

<dependency>
    <groupId>com.54chen</groupId>
    <artifactId>paoding-rose-jade</artifactId>
    <version>1.1</version>
</dependency>
```

## 项目结构

```
src/java/xyz.kail.demo
  ├── dao
  │   └── AccountDAO.java
  ├── exception
  │   └── IllegalAccountException.java
  ├── model
  │   └── AccountVO.java
  └── service
		  └── AccountService.java

```

### AccountVO.java

```java
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AccountVO {

    /**
     * 账户名(唯一)
     */
    private String uname;
    /**
     * 账户密码
     */
    private String passwd;

    public AccountVO(String uname) {
        this.uname = uname;
    }

    public AccountVO(String uname, String passwd) {
        this.uname = uname;
        this.passwd = passwd;
    }
}
```



### AccountDAO.java

```java
@DAO
public interface AccountDAO {

    @SQL("SELECT UNAME,PASSWD FROM T_ACCOUNT WHERE 1=1 " +
            " #if(null != :vo.uname){ AND UNAME = :vo:uname }" +
            " #if(null != :vo.passwd){ AND PASSWD = :vo:passwd }" +
            " LIMIT 1")
    AccountVO selectAccountByName(@SQLParam("vo") AccountVO vo);
}
```



### AccountService.java

```java
@Service
public class AccountService {

    @Resource
    private AccountDAO accountDAO;

    /**
     * 验证账户密码是否正确
     */
    public void validateAccount(String uname, String passwd) throws IllegalAccountException {
        // 逻辑1： 参数校验
        if (Objects.isNull(uname) || Objects.isNull(passwd)) {
            throw new IllegalAccountException(IllegalAccountException.ACCOUNT_NOT_EXIST);
        }

        // 根据用户名查询账户信息（这一步对数据库有依赖）
        AccountVO unameAccountQuery = new AccountVO(uname);
        AccountVO unameAccountResult = accountDAO.selectAccountByName(unameAccountQuery);

        // 逻辑2：异常数据
        if (Objects.isNull(unameAccountResult)) {
            throw new IllegalAccountException(IllegalAccountException.ACCOUNT_NOT_EXIST);
        }

        // 逻辑3：校验密码
        String realPasswd = unameAccountResult.getPasswd();
        if (!Objects.equals(realPasswd, md5(passwd))) {
            throw new IllegalAccountException(IllegalAccountException.ILLEGAL_ACCOUNT);
        }

        // 逻辑4：密码正常
    }

    /**
     * 并未 MD5 摘要处理，这里仅做示例
     */
    private static String md5(String data) {
        return "md5:" + data;
    }
}
```



### IllegalAccountException.java

```java
public class IllegalAccountException extends Exception {

    public static final String ACCOUNT_NOT_EXIST = "账户不存在";

    public static final String ILLEGAL_ACCOUNT = "非法的账户信息";

    public IllegalAccountException() {
        super(ILLEGAL_ACCOUNT);
    }

    public IllegalAccountException(String message) {
        super(message);
    }

    public IllegalAccountException(String message, Throwable cause) {
        super(message, cause);
    }
}
```



## 测试用例

```java
/**
 * @RunWith(MockitoJUnitRunner.class) 等同于 MockitoAnnotations.initMocks(this);
 */
@RunWith(MockitoJUnitRunner.class)
public class AccountServiceTest {

    /**
     * Mock 依赖的 DAO 对象
     */
    @Mock
    private AccountDAO mockAccountDAO;
    /**
     * 实例化 AccountService，注入 mock 的 accountDAO
     */
    @InjectMocks
    private AccountService realAccountService;

//    @Before
//    public void before() {
//        // 等同于 @RunWith(MockitoJUnitRunner.class)
//        MockitoAnnotations.initMocks(this);
//    }

    @Test(expected = IllegalAccountException.class)
    public void validateAccountIllegalPasswdError() throws IllegalAccountException {
        // 【验证逻辑3】密码错误，必须抛出异常
        AccountVO readAccountResult = new AccountVO("kail", "md5:123");
        // ❤ 对 DAO.selectAccountByName 的行为进行定义，无论传任何参数，都返回 readAccountResult
        Mockito.when(mockAccountDAO.selectAccountByName(Mockito.any())).thenReturn(readAccountResult);
        realAccountService.validateAccount("kail", "erro passwd :1234");
    }

    @Test
    public void validateAccountSuccess() throws IllegalAccountException {
        // 【验证逻辑4】密码正常
        AccountVO readAccountResult = new AccountVO("kail", "md5:123");
        // ❤ 
        Mockito.when(mockAccountDAO.selectAccountByName(Mockito.any())).thenReturn(readAccountResult);
        realAccountService.validateAccount("kail", "123");
    }
    /**
     * 校验非法的情况(必须抛出 IllegalAccountException 异常)
     * 注意：抛出异常后，单元测试会通过，但是一个测试用例后的后续流程不会再执行，所以对异常的测试不能写在同一个方法内
     */
    @Test(expected = IllegalAccountException.class)
    public void validateAccountIllegalUnameNull() throws IllegalAccountException {
        // 【验证逻辑 1】非法参数，抛出 IllegalAccountException，
        realAccountService.validateAccount(null, "123");
    }

    /**
     * 如果写在同一个方法内，需要通过 try cache 的方式进行操作
     */
    @Test
    public void validateAccountIllegalPasswdNull() throws IllegalAccountException {
        // 【验证逻辑 1】非法参数，密码为空
        try {
            realAccountService.validateAccount("kail", null);
            Assert.fail("密码为空未抛出异常");
        } catch (IllegalAccountException ex) {
            // success
        } catch (Exception ex) {
            Assert.fail("抛出非期待的异常" + ex.getClass());
        }

        // 【验证逻辑 1】非法参数，用户名、密码为空
        try {
            realAccountService.validateAccount(null, null);
            Assert.fail("用户名、密码为空未抛出异常");
        } catch (IllegalAccountException ex) {
            // success
        } catch (Exception ex) {
            Assert.fail("抛出非期待的异常" + ex.getClass());
        }
    }

    @Test(expected = IllegalAccountException.class)
    public void validateAccountIllegalDbNull() throws IllegalAccountException {
        // 【验证逻辑 2】Mock 查不到数据的情况（不管传入任何值，返回都是null）抛出 IllegalAccountException
        Mockito.when(mockAccountDAO.selectAccountByName(Mockito.any())).thenReturn(null);
        realAccountService.validateAccount("kail", "123");
    }
}
```

## Read More

- [Mockito 教程](/mockito/Mockito-Tutorial)



