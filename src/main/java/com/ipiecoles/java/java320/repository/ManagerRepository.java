package com.ipiecoles.java.java320.repository;

import com.ipiecoles.java.java320.model.Manager;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.stereotype.Repository;

@Repository
public interface ManagerRepository extends BaseEmployeRepository<Manager> {
}
