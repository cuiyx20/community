package com.nowcoder.community;

import com.nowcoder.community.util.MailClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class MailTest {

    @Autowired
    private MailClient mailClient;


    @Autowired
    private TemplateEngine templateEngine;       //调用thymeleaf模板引擎
    @Test
    public void testTextMail(){
        mailClient.sendMail("17843126916@qq.com","日志","Welcome");
    }

    @Test
    public void testHtmlMail(){
        Context context = new Context();
        context.setVariable("username","sundayday");
        String content = templateEngine.process("/mail/demo",context);
        System.out.println(content);

        mailClient.sendMail("cuiyx20@sina.com","html",content);
    }
}
