package be.pxl.services.service;

import be.pxl.services.domain.dto.DepartmentRequest;
import be.pxl.services.domain.dto.DepartmentResponse;

import java.util.List;

public interface IDepartmentService {
    List<DepartmentResponse> getAllDepartments();

    void addDepartment(DepartmentRequest departmentRequest);

    DepartmentResponse getDepartment(Long id);

    DepartmentResponse getDepartmentByOrganization(Long organizationId);
    DepartmentResponse getDepartmentByOrganizationWithEmployees(Long organizationId);
}
