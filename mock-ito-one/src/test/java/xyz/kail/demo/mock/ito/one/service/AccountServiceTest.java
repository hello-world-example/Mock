package xyz.kail.demo.mock.ito.one.service;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import xyz.kail.demo.mock.ito.one.dao.AccountDAO;
import xyz.kail.demo.mock.ito.one.exception.IllegalAccountException;
import xyz.kail.demo.mock.ito.one.model.AccountVO;

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

    /**
     * 校验非法的情况(必须抛出 IllegalAccountException 异常)
     * 注意：抛出异常后，单元测试会通过，但是一个测试用例后的后续刘晨不会再执行，所以对异常的测试不能写在同一个方法内
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

    @Test(expected = IllegalAccountException.class)
    public void validateAccountIllegalPasswdError() throws IllegalAccountException {
        // 【验证逻辑3】密码错误，必须抛出异常
        AccountVO readAccountResult = new AccountVO("kail", "md5:123");
        Mockito.when(mockAccountDAO.selectAccountByName(Mockito.any())).thenReturn(readAccountResult);
        realAccountService.validateAccount("kail", "erro passwd :1234");
    }


    @Test
    public void validateAccountSuccess() throws IllegalAccountException {
        // 【验证逻辑4】密码正常
        AccountVO readAccountResult = new AccountVO("kail", "md5:123");
        Mockito.when(mockAccountDAO.selectAccountByName(Mockito.any())).thenReturn(readAccountResult);
        realAccountService.validateAccount("kail", "123");
    }
}