package be.pxl.services;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * ProductCatalogService
 *
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class ProductCatalogService
{
    public static void main( String[] args ) { SpringApplication.run(ProductCatalogService.class, args); }
}
