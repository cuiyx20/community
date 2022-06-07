package com.nowcoder.community;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//这个注解所标识的类就是配置文件
public class CommunityApplication {

	public static void main(String[] args) {
		SpringApplication.run(CommunityApplication.class, args);
		//这句代码底层不仅启动了tomcat服务器，还自动的创建了Spring容器！自动扫描某些包下的某些bean，将这些bean装配到容器中
		//
	}

}
