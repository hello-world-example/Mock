package xyz.kail.demo.mock.ito.one.model;

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
