package be.pxl.services.client;

import be.pxl.services.domain.Product;
import be.pxl.services.domain.dto.LogRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
@FeignClient(name = "logbook-service")
public interface LogbookClient {

    @PostMapping("/api/log")
    void addLog(@RequestBody LogRequest logRequest);
}
