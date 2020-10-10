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

    List<T> findByNomAndPrenom(String nom, String prenom);

    @Query("select e from #{#entityName} e where lower(e.prenom) = lower(:nomOuPrenom) or lower(e.nom) = lower(:nomOuPrenom)")
    List<T> findByNomOrPrenomAllIgnoreCase(@Param("nomOuPrenom") String nomOuPrenom);

    List<T> findByNomIgnoreCase(String nom);

    Page<T> findByNomIgnoreCase(String nom, Pageable pageable);

    List<T> findByDateEmbaucheBefore(LocalDate date);

    List<T> findByDateEmbaucheAfter(LocalDate date);

    List<T> findBySalaireGreaterThanOrderBySalaireDesc(Double salaire);

    @Query(value = "SELECT * FROM Employe WHERE salaire > (SELECT avg(e2.salaire) FROM Employe e2)", nativeQuery = true)
    List<T> findEmployePlusRiches();
}
