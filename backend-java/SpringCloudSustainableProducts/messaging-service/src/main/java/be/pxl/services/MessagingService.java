package be.pxl.services;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * MessagingService
 *
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class MessagingService
{
    public static void main( String[] args )
    {
        SpringApplication.run(MessagingService.class, args);
    }
}
