package be.pxl.services.service;

import be.pxl.services.domain.Department;
import be.pxl.services.domain.Employee;
import be.pxl.services.domain.dto.DepartmentRequest;
import be.pxl.services.domain.dto.DepartmentResponse;
import be.pxl.services.repository.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DepartmentService implements IDepartmentService{
    private final DepartmentRepository departmentRepository;
    @Override
    public List<DepartmentResponse> getAllDepartments() {
        List<Department> departments = departmentRepository.findAll();
        return departments.stream().map(department -> mapToDepartmentResponse(department)).toList();
    }

    private DepartmentResponse mapToDepartmentResponse(Department department) {
        return DepartmentResponse.builder()
                .name(department.getName())
                .position(department.getPosition())
                .build();
    }

    @Override
    public void addDepartment(DepartmentRequest departmentRequest) {
        Department department = Department.builder()
                .name(departmentRequest.getName())
                .position(departmentRequest.getPosition())
                .build();
        departmentRepository.save(department);
    }

    @Override
    public DepartmentResponse getDepartment(Long id) {
        Department department = departmentRepository.findById(id).get();
        return mapToDepartmentResponse(department);
    }

    @Override
    public DepartmentResponse getDepartmentByOrganization(Long organizationId) {
        Department department = departmentRepository.findByOrganizationId(organizationId).get();
        return mapToDepartmentResponse(department);
    }

    @Override
    public DepartmentResponse getDepartmentByOrganizationWithEmployees(Long organizationId) {
        Department department = departmentRepository.findByOrganizationId(organizationId).get();
        return DepartmentResponse.builder()
                .name(department.getName())
                .position(department.getPosition())
                .employees(department.getEmployees())
                .build();
    }
}
