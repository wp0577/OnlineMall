import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @program: WpMall
 * @description:
 * @author: Pan wu
 * @create: 2018-10-02 16:34
 **/
public class ActiveMQTest {
    @Test
    public void testQueueConsumer() throws Exception {
        //初始化spring容器
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-jms.xml");
        //等待
        System.in.read();
    }


}
