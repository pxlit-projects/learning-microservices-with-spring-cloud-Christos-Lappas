package be.pxl.services.service;

import be.pxl.services.domain.dto.OrganizationRequest;
import be.pxl.services.domain.dto.OrganizationResponse;

import java.util.List;

public interface IOrganizationService {
//    List<OrganizationResponse> getAllOrganizations();
//
//    void addOrganization(OrganizationRequest organizationRequest);

    OrganizationResponse getOrganization(Long id);

    OrganizationResponse getOrganizationWithDepartments(Long id);

    OrganizationResponse getOrganizationWithDepartmentsAndEmployees(Long id);

    OrganizationResponse getOrganizationWithEmployees(Long id);
}
