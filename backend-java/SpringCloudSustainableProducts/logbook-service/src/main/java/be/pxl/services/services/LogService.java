package be.pxl.services.services;

import be.pxl.services.domain.Log;
import be.pxl.services.domain.dto.LogRequest;
import be.pxl.services.domain.dto.LogResponse;
import be.pxl.services.repository.LogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LogService implements ILogService{
    private final LogRepository logRepository;
    private static final Logger logger = LoggerFactory.getLogger(LogService.class);

    @Override
    public List<LogResponse> getAllLogs() {
       logger.info("Fetching all logs from the database");

        long startTime = System.currentTimeMillis();

        List<LogResponse> logResponses;
        try {
            List<Log> logs = logRepository.findAll();
            if (logs.isEmpty()) {
                logger.warn("No logs found in the database");
            } else {
                logger.debug("Fetched {} logs from the database", logs.size());
            }
            logResponses = logs.stream().map(log -> mapToLogResponse(log)).toList();
        } catch (Exception e) {
            logger.error("Error occurred while fetching logs: {}", e.getMessage(), e);
            throw e;
        }

        long endTime = System.currentTimeMillis();
        logger.info("Successfully retrieved {} logs in {} ms", logResponses.size(), (endTime - startTime));

        return logResponses;
    }

    private LogResponse mapToLogResponse(Log log) {
        return LogResponse.builder()
                .time(log.getTime())
                .user(log.getUser())
                .productId(log.getProductId())
                .changes(log.getChanges())
                .build();
    }

    @Override
    public void addLog(LogRequest logRequest) {
        logger.info("Starting to add a new log entry for user: {}, product ID: {}",
                logRequest.getUser(), logRequest.getProductId());

        Log log = Log.builder()
                .time(logRequest.getTime())
                .user(logRequest.getUser())
                .productId(logRequest.getProductId())
                .changes(logRequest.getChanges())
                .build();

        try {
            logRepository.save(log);
            logger.info("Log entry for user '{}' and product ID '{}' successfully saved",
                    logRequest.getUser(), logRequest.getProductId());
        } catch (Exception e) {
            logger.error("Failed to save log entry for user '{}', product ID '{}': {}",
                    logRequest.getUser(), logRequest.getProductId(), e.getMessage(), e);
            throw e;
        }

    }
}
