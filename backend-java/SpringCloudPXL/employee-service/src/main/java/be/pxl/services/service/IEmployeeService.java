package be.pxl.services.service;

import be.pxl.services.domain.Employee;
import be.pxl.services.domain.dto.EmployeeRequest;
import be.pxl.services.domain.dto.EmployeeResponse;

import java.util.List;

public interface IEmployeeService {
    List<EmployeeResponse> getAllEmployees();

    void addEmployee(EmployeeRequest employeeRequest);

    EmployeeResponse getEmployee(Long id);

    EmployeeResponse getEmployeeByDepartmentId(Long departmentId);

    EmployeeResponse getEmployeeByOrganizationId(Long organizationId);

}
