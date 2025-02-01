package ru.itis.unogame;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import ru.itis.unogame.thread.ClientReceiver;

@Configuration
@PropertySource("classpath:application.properties")
@ComponentScan({"ru.itis.unogame"})
public class Application {

    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(Application.class);
        ClientReceiver clientReceiver = context.getBean("clientReceiver", ClientReceiver.class);
        clientReceiver.start();
    }
}