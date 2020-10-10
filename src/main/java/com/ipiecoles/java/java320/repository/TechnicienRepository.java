package com.ipiecoles.java.java320.repository;

import com.ipiecoles.java.java320.model.Technicien;
import org.springframework.data.domain.Slice;

import java.awt.print.Pageable;
import java.util.List;

public interface TechnicienRepository extends BaseEmployeRepository<Technicien> {

    List<Technicien> findByGradeBetween(Integer gradeLower, Integer gradeUpper);

    Slice<Technicien> findTop5ByGrade(Integer grade);

}
