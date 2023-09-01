package com.foogui.foo.generator;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

import java.net.InetAddress;
import java.net.UnknownHostException;


/**
 * 基于Mybatis的代码生成
 *
 * @author Foogui
 * @date 2023/05/11
 */
@SpringBootApplication
@Slf4j
public class GeneratorMybatisApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(GeneratorMybatisApplication.class, args);
        printContext(context);
    }

    private static void printContext(ConfigurableApplicationContext context) {
        String address;

        try {
            address = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            address = "";
        }

        String str = "\n--------------------------------------------------------\n" +
                "Application '{}' is running！ Access URLs:\n\t" +
                "Local:\t\t{}://localhost:{}\n\t" +
                "External:\t{}://{}:{}\n\t" +
                "\n-----------------------------------------------------------";

        Environment env = context.getEnvironment();
        String name = env.getProperty("spring.application.name");
        String protocol = env.getProperty("server.ssl.key-store") != null ? "https" : "http";
        String port = env.getProperty("server.port");

        log.info(str, name, protocol, port, protocol, address, port);
    }
}
