package xyz.kail.demo.mock.one;

import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import java.util.LinkedList;
import java.util.List;

import static org.mockito.Mockito.*;

public class HelloTest2 implements TestRule {

    @Override
    public Statement apply(Statement base, Description description) {
        return null;
    }


}
