package xyz.kail.demo.mock.power;

public class FinalMethodCase {

    public String getSomeWorld() {
        return getHello() + " World";
    }

    /**
     * final 方法
     */
    private String getHello() {
        System.out.println("getHello");
        return "Hello";
    }
}
