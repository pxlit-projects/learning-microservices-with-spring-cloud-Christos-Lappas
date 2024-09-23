package be.pxl.ecommerceplatform;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
public class ECommercePlatformApplication {

    public static void main(String[] args) {
        SpringApplication.run(ECommercePlatformApplication.class, args);
    }

}
