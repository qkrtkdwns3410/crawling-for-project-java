package com.crawling.crawlingforprojectjava;

import org.springframework.boot.SpringApplication;

public class TestCrawlingForProjectJavaApplication {
    
    public static void main(String[] args) {
        SpringApplication.from(CrawlingForProjectJavaApplication::main).with(TestcontainersConfiguration.class).run(args);
    }
    
}
