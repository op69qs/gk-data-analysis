package org.triber.analysis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.ComponentScan;

@EnableZuulProxy
@EnableEurekaClient
@ComponentScan("org.triber")
@SpringBootApplication
public class StatisticalAnalysisApplication {
    public static void main(String[] args) throws Exception {
        SpringApplication.run(StatisticalAnalysisApplication.class,args);
    }

}
