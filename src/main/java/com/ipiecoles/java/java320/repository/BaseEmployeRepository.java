package com.ipiecoles.java.java320.repository;

import com.ipiecoles.java.java320.model.Employe;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface BaseEmployeRepository<T extends Employe> extends PagingAndSortingRepository<T, Long> {
    T findByMatricule(String matricule);
}
