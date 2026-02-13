package com.cht.procurementManagement.repositories;

import com.cht.procurementManagement.entities.Designation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DesignationRepository extends JpaRepository<Designation, Long> {
    boolean existsByCode(String code);
    boolean existsByTitleAndGrade(String title, String grade);
}
