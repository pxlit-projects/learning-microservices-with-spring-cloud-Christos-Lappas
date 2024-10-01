package be.pxl.services.repository;

import be.pxl.services.domain.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Optional<Employee> findByDepartmentId(Long id);
    Optional<Employee> findByOrganizationId(Long id);
}
