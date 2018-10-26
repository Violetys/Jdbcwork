package com.esen.abistudy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.autoconfigure.web.MultipartAutoConfiguration;
import org.springframework.boot.autoconfigure.websocket.WebSocketAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.esen.core.Constants;

/**
 * ABI框架学习程序主入口
 * @author chengf
 * @since 2018年6月25日
 */
@SpringBootApplication(scanBasePackages = Constants.BASE_PACKAGE, exclude = { DataSourceAutoConfiguration.class,
		DataSourceTransactionManagerAutoConfiguration.class, HibernateJpaAutoConfiguration.class,
		WebSocketAutoConfiguration.class, MultipartAutoConfiguration.class })
@EnableTransactionManagement
public class AbiStudyApplication extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(AbiStudyApplication.class);
	}

	public static void main(String[] args) {
		SpringApplication.run(AbiStudyApplication.class, args);
	}
}
