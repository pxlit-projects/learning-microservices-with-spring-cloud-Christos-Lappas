package be.pxl.services.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "logbook-service")
public interface LogbookClient {
    @PostMapping("/api/log")
    void addLog(@RequestBody String changes);
}
