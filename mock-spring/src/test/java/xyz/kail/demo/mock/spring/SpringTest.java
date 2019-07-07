package xyz.kail.demo.mock.spring;

import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.junit4.rules.SpringClassRule;
import org.springframework.test.context.junit4.rules.SpringMethodRule;

// @RunWith(SpringJUnit4ClassRunner.class)
@RunWith(SpringRunner.class)
public class SpringTest {

    public static void main(String[] args) {
        Class<SpringClassRule> springClassRuleClass = SpringClassRule.class;
        Class<SpringMethodRule> springMethodRuleClass = SpringMethodRule.class;
    }

}
