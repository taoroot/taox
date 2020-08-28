package cn.flizi.cloud.mall;

import cn.flizi.cloud.common.security.CustomSecurityConfigurer;
import cn.flizi.cloud.common.security.oauth.CustomOAuth2AuthenticationSuccessHandler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@Import({CustomSecurityConfigurer.class, CustomOAuth2AuthenticationSuccessHandler.class})
@SpringBootApplication
public class MallApplication {

	public static void main(String[] args) {
		SpringApplication.run(MallApplication.class, args);
	}

}
