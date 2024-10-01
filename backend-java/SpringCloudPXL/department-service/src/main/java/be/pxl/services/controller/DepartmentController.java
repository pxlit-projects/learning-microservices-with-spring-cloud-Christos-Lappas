package be.pxl.services.controller;

import be.pxl.services.domain.dto.DepartmentRequest;
import be.pxl.services.service.IDepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/department")
@RequiredArgsConstructor
public class DepartmentController {

    private final IDepartmentService departmentService;

    @GetMapping
    public ResponseEntity getDepartments() {
        return new ResponseEntity(departmentService.getAllDepartments(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity getDepartment(@PathVariable Long id) {
        return new ResponseEntity(departmentService.getDepartment(id), HttpStatus.OK);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void addDepartment(@RequestBody DepartmentRequest departmentRequest) {
        departmentService.addDepartment(departmentRequest);
    }

    @GetMapping("/organization/{organizationId}")
    public ResponseEntity getDepartmentByOrganization(@PathVariable Long organizationId) {
        return new ResponseEntity(departmentService.getDepartmentByOrganization(organizationId), HttpStatus.OK);
    }

    @GetMapping("/organization/{organizationId}/with-employees")
    public ResponseEntity getDepartmentByOrganizationWithEmployees(@PathVariable Long organizationId) {
        return new ResponseEntity(departmentService.getDepartmentByOrganizationWithEmployees(organizationId), HttpStatus.OK);
    }

}
