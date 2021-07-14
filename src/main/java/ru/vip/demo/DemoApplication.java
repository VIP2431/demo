package ru.vip.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.vip.demo.util.CreatDateBaseEstimate;

@SpringBootApplication
public class DemoApplication implements CommandLineRunner{

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Autowired
	private CreatDateBaseEstimate creatDateBaseEstimate;

	@Override
	public void run(String... args) throws Exception {
		creatDateBaseEstimate.initDataBase();
	}
}
