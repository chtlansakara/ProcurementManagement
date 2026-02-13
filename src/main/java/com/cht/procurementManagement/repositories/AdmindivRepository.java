package com.cht.procurementManagement.repositories;

import com.cht.procurementManagement.entities.Admindiv;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdmindivRepository extends JpaRepository<Admindiv, Long> {
    boolean existsByName(String name);
    boolean existsByCode(String code);
}
