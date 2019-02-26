package com.hj.tj.gohome;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author tangj
 * @description
 * @since 2019/2/26 10:35
 */
@SpringBootApplication
@MapperScan("com.hj.tj.gohome.mapper")
public class RobberyTicketPortalApplication {
    public static void main(String[] args) {
        SpringApplication.run(RobberyTicketPortalApplication.class, args);
    }

}
