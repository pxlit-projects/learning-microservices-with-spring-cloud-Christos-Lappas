package be.pxl.services.service;

import be.pxl.services.domain.Employee;
import be.pxl.services.domain.Organization;
import be.pxl.services.domain.dto.OrganizationRequest;
import be.pxl.services.domain.dto.OrganizationResponse;
import be.pxl.services.repository.OrganizationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class OrganizationService implements IOrganizationService {
    private final OrganizationRepository organizationRepository;
//    @Override
//    public List<OrganizationResponse> getAllOrganizations() {
//        List<Organization> organizations = organizationRepository.findAll();
//        return organizations.stream().map(organization -> mapToOrganizationResponse(organization)).toList();
//    }

    private OrganizationResponse mapToOrganizationResponse(Organization organization) {
        return OrganizationResponse.builder()
                .name(organization.getName())
                .address(organization.getAddress())
                .build();
    }

//    @Override
//    public void addOrganization(OrganizationRequest organizationRequest) {
//        Organization organization = Organization.builder()
//                .name(organizationRequest.getName())
//                .address(organizationRequest.getAddress())
//                .build();
//
//        organizationRepository.save(organization);
//
//    }

    @Override
    public OrganizationResponse getOrganization(Long id) {
        Organization organization = organizationRepository.findById(id).get();
        return mapToOrganizationResponse(organization);
    }

    @Override
    public OrganizationResponse getOrganizationWithDepartments(Long id) {
        Organization organization = organizationRepository.findById(id).get();
        return OrganizationResponse.builder()
                .name(organization.getName())
                .address(organization.getAddress())
                .departments(organization.getDepartments())
                .build();
    }

    @Override
    public OrganizationResponse getOrganizationWithDepartmentsAndEmployees(Long id) {
        Organization organization = organizationRepository.findById(id).get();
        return OrganizationResponse.builder()
                .name(organization.getName())
                .address(organization.getAddress())
                .departments(organization.getDepartments())
                .employees(organization.getEmployees())
                .build();
    }

    @Override
    public OrganizationResponse getOrganizationWithEmployees(Long id) {
        Organization organization = organizationRepository.findById(id).get();
        return OrganizationResponse.builder()
                .name(organization.getName())
                .address(organization.getAddress())
                .employees(organization.getEmployees())
                .build();
    }
}
