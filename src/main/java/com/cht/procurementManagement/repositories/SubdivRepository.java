package com.cht.procurementManagement.repositories;

import com.cht.procurementManagement.entities.Subdiv;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubdivRepository extends JpaRepository<Subdiv, Long> {
    boolean existsByCode(String code);
    List<Subdiv> findByAdmindivId(Long id);

}
