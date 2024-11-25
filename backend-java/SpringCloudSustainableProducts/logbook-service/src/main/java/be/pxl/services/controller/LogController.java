package be.pxl.services.controller;

import be.pxl.services.domain.dto.LogRequest;
import be.pxl.services.services.ILogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/log")
@RequiredArgsConstructor
public class LogController {
    private final ILogService logService;

    @GetMapping
    public ResponseEntity getLogs() {
        return new ResponseEntity(logService.getAllLogs(), HttpStatus.OK);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void addLog(@RequestBody LogRequest logRequest) {
        logService.addLog(logRequest);
    }

}
