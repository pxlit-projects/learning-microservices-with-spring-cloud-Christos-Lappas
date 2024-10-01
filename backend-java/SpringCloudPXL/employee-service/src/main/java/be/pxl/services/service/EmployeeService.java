package be.pxl.services.service;

import be.pxl.services.domain.Employee;
import be.pxl.services.domain.dto.EmployeeRequest;
import be.pxl.services.domain.dto.EmployeeResponse;
import be.pxl.services.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeService implements IEmployeeService{
    private final EmployeeRepository employeeRepository;
    @Override
    public List<EmployeeResponse> getAllEmployees() {
        List<Employee> employees = employeeRepository.findAll();
        return employees.stream().map(employee -> mapToEmployeeResponse(employee)).toList();
    }

    private EmployeeResponse mapToEmployeeResponse(Employee employee) {
        return EmployeeResponse.builder()
                .age(employee.getAge())
                .name(employee.getName())
                .position(employee.getPosition())
                .build();
    }

    @Override
    public void addEmployee(EmployeeRequest employeeRequest) {
        Employee employee = Employee.builder()
                .age(employeeRequest.getAge())
                .name(employeeRequest.getName())
                .position(employeeRequest.getPosition())
                .build();
        employeeRepository.save(employee);
    }

    @Override
    public EmployeeResponse getEmployee(Long id) {
        Employee employee = employeeRepository.findById(id).get();
        return mapToEmployeeResponse(employee);
    }

    @Override
    public EmployeeResponse getEmployeeByDepartmentId(Long departmentId) {
        Employee employee = employeeRepository.findByDepartmentId(departmentId).get();
        return mapToEmployeeResponse(employee);
    }

    @Override
    public EmployeeResponse getEmployeeByOrganizationId(Long organizationId) {
        Employee employee = employeeRepository.findByOrganizationId(organizationId).get();
        return mapToEmployeeResponse(employee);
    }
}
