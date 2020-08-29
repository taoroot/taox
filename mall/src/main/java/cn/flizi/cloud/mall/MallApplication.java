package cn.flizi.cloud.mall;

import cn.flizi.cloud.common.security.annotation.EnableOAuth2LoginSecurity;
import cn.flizi.cloud.common.security.annotation.EnableOauth2ResourceSecurity;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@EnableOAuth2LoginSecurity
@EnableOauth2ResourceSecurity
@SpringBootApplication
public class MallApplication {

    public static void main(String[] args) {
        SpringApplication.run(MallApplication.class, args);
    }

}
