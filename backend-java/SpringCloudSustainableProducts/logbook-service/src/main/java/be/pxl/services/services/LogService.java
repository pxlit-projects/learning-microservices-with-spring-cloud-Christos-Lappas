package be.pxl.services.services;

import be.pxl.services.domain.Log;
import be.pxl.services.domain.dto.LogRequest;
import be.pxl.services.domain.dto.LogResponse;
import be.pxl.services.repository.LogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LogService implements ILogService{
    private final LogRepository logRepository;

    @Override
    public List<LogResponse> getAllLogs() {
        return logRepository.findAll().stream().map(log -> mapToLogResponse(log)).toList();
    }

    private LogResponse mapToLogResponse(Log log) {
        return LogResponse.builder()
                .time(log.getTime())
                .userId(log.getUserId())
                .productId(log.getProductId())
                .update(log.getUpdate())
                .build();
    }

    @Override
    public void addLog(LogRequest logRequest) {
        Log log = Log.builder()
                .time(logRequest.getTime())
                .userId(logRequest.getUserId())
                .productId(logRequest.getProductId())
                .update(logRequest.getUpdate())
                .build();

        logRepository.save(log);

    }
}
