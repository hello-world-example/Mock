package xyz.kail.demo.junit5;

import org.junit.jupiter.api.Test;
import org.junit.platform.console.ConsoleLauncher;

public class ConsoleLauncherMain {

    @Test
    public void main() {
        ConsoleLauncher.execute(
                System.out, System.err
                , "--help"
        );
    }

}
