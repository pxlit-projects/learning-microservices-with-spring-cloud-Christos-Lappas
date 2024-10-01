package be.pxl.services.controller;

import be.pxl.services.domain.dto.EmployeeRequest;
import be.pxl.services.service.IEmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/employee")
@RequiredArgsConstructor
public class EmployeeController {

    private final IEmployeeService employeeService;

    @GetMapping
    public ResponseEntity getEmployees() {
        return new ResponseEntity(employeeService.getAllEmployees(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity getEmployee(@PathVariable Long id) {
        return new ResponseEntity(employeeService.getEmployee(id), HttpStatus.OK);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void addEmployee(@RequestBody EmployeeRequest employeeRequest) {
        employeeService.addEmployee(employeeRequest);
    }

    @GetMapping("/department/{departmentId}")
    public ResponseEntity getEmployeeByDepartmentId(@PathVariable Long departmentId) {
        return new ResponseEntity(employeeService.getEmployeeByDepartmentId(departmentId), HttpStatus.OK);
    }

    @GetMapping("/organization/{organizationId}")
    public ResponseEntity getEmployeeByOrganizationId(@PathVariable Long organizationId) {
        return new ResponseEntity(employeeService.getEmployeeByOrganizationId(organizationId), HttpStatus.OK);
    }
}
