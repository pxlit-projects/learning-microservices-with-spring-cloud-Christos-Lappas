package be.pxl.services.controller;

import be.pxl.services.domain.dto.LogRequest;
import be.pxl.services.domain.dto.LogResponse;
import be.pxl.services.services.ILogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@RestController
@RequestMapping("/api/log")
@RequiredArgsConstructor
public class LogController {
    private final ILogService logService;
    private static final Logger logger = LoggerFactory.getLogger(LogController.class);

    @GetMapping
    public ResponseEntity getLogs() {
        logger.info("Received GET request at /api/log to fetch logs");

        try {
            List<LogResponse> logs = logService.getAllLogs();
            logger.info("Successfully retrieved {} logs", logs.size());
            return new ResponseEntity<>(logs, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error occurred while fetching logs: {}", e.getMessage(), e);
            return new ResponseEntity<>("Failed to fetch logs", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void addLog(@RequestBody LogRequest logRequest) {
        logger.info("Received POST request at /api/log to add a log entry");

        try {
            logger.debug("Request body: User={}, ProductId={}, Update={}",
                    logRequest.getUser(), logRequest.getProductId(), logRequest.getChanges());
            logService.addLog(logRequest);
            logger.info("Successfully added log entry for user: {} and product ID: {}",
                    logRequest.getUser(), logRequest.getProductId());
        } catch (Exception e) {
            logger.error("Error occurred while adding log entry: {}", e.getMessage(), e);
        }
    }

}
