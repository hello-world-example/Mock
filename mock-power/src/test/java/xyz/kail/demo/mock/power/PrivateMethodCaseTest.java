package xyz.kail.demo.mock.power;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertEquals;

@RunWith(PowerMockRunner.class)
public class FinalMethodCaseTest {

    @Test
    @PrepareForTest(PrivateMethodCase.class)
    public void testGetSomeWorld() throws Exception {

        PrivateMethodCase mock = PowerMockito.mock(PrivateMethodCase.class);

        // 当调用 getSomeWorld 方法的时候，执行真是的方法
        PowerMockito.when(mock.getSomeWorld()).thenCallRealMethod();
        // Mock 静态方法的行为，Mockito 没有提供这种方式
        PowerMockito.when(mock, "getHello").thenReturn("你好");


        assertEquals("你好 World", mock.getSomeWorld());
    }

}
