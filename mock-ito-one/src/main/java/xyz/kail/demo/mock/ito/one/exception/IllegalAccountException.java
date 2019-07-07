package xyz.kail.demo.mock.ito.one.exception;

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
