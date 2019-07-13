package xyz.kail.demo.mock.ito.one.service;

import org.springframework.stereotype.Service;
import xyz.kail.demo.mock.ito.one.dao.AccountDAO;
import xyz.kail.demo.mock.ito.one.exception.IllegalAccountException;
import xyz.kail.demo.mock.ito.one.model.AccountVO;

import javax.annotation.Resource;
import java.util.Objects;

@Service
public class AccountService {

    @Resource
    private AccountDAO accountDAO;

    /**
     * 验证账户密码是否正确
     *
     * @param uname  账户
     * @param passwd 密码
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
