package com.service.ttucktak;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@OpenAPIDefinition(servers = {
        @Server(url = "http://localhost:6543", description = "개발용 Local Test")
        @Server(url = "https://ttukttak.store", description = "Production URL")
        //TODO 실제 프로덕션 서버 주소 https로 추가해야함 http로는 CORS 걸림
})
@SpringBootApplication
public class TtuckTakApplication {

    public static void main(String[] args) {
        SpringApplication.run(TtuckTakApplication.class, args);
    }

}
