package com.yicem.backend.yicem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class YicemApplication{
	@Autowired
	//private CustomerRepository repository;
  
	public static void main(String[] args) {
	  SpringApplication.run(YicemApplication.class, args);
	}

}
