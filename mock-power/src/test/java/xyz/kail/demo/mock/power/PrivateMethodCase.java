package xyz.kail.demo.mock.power;

public class PrivateMethodCase {

    public String getSomeWorld() {
        return getHello() + " World";
    }

    /**
     * final 方法
     */
    private static String getHello() {
        return "Hello";
    }
}
