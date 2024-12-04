package be.pxl.services.services;

import be.pxl.services.domain.Log;
import be.pxl.services.domain.dto.LogRequest;
import be.pxl.services.domain.dto.LogResponse;

import java.util.List;

public interface ILogService {
    List<LogResponse> getAllLogs();

    void addLog(String changes);
}
