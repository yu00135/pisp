package com.mmy.pisp;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.oas.annotations.EnableOpenApi;


/**
 * @author : Mingyu Ma
 * @date : 2022/1/11 20:29
 */

@EnableOpenApi
@SpringBootApplication
public class PispApplication {

    public static void main(String[] args) throws JsonProcessingException {
        SpringApplication.run(PispApplication.class, args);
    }

}
