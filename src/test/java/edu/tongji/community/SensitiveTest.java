package edu.tongji.community;


import edu.tongji.community.util.SensitiveFilter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class SensitiveTest {

    @Autowired
    private SensitiveFilter sensitiveFilter;

    @Test
    public void testSensitiveFilter(){
        String text = "上海有疫情，所以不能赌博，嫖娼，没法吸毒";
        text = sensitiveFilter.filter(text);
        System.out.println(text);
    }
}
