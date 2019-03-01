package com.kish.eshop.datalink;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients
public class DataLinkServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(DataLinkServiceApplication.class, args); 
	}
	
	@Bean
	public JedisPool jedis() {
	    JedisPoolConfig config = new JedisPoolConfig();
	    config.setMaxTotal(100);
	    config.setMaxIdle(5);
	    config.setMaxWaitMillis(1000*10);
	    config.setTestOnBorrow(false);
	    return new JedisPool(config, "192.168.1.40", 1111);
	}
}