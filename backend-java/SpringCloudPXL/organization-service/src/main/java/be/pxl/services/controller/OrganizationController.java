package be.pxl.services.controller;

import be.pxl.services.service.IOrganizationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/organization")
@RequiredArgsConstructor
public class OrganizationController {
    private final IOrganizationService organizationService;

    @GetMapping("/{id}")
    public ResponseEntity getOrganization(@PathVariable Long id) {
        return new ResponseEntity(organizationService.getOrganization(id), HttpStatus.OK);
    }

    @GetMapping("/{id}/with-departments")
    public ResponseEntity getOrganizationWithDepartments(@PathVariable Long id) {
        return new ResponseEntity(organizationService.getOrganizationWithDepartments(id), HttpStatus.OK);
    }

    @GetMapping("/{id}/with-departments-and-employees")
    public ResponseEntity getOrganizationWithDepartmentsAndEmployees(@PathVariable Long id) {
        return new ResponseEntity(organizationService.getOrganizationWithDepartmentsAndEmployees(id), HttpStatus.OK);
    }

    @GetMapping("/{id}/with-employees")
    public ResponseEntity getOrganizationWithEmployees(@PathVariable Long id) {
        return new ResponseEntity(organizationService.getOrganizationWithEmployees(id), HttpStatus.OK);
    }
}
