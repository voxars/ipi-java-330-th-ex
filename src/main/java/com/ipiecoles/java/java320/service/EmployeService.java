package com.ipiecoles.java.java320.service;

import com.ipiecoles.java.java320.model.Employe;
import com.ipiecoles.java.java320.repository.EmployeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Service
public class EmployeService {

    @Autowired
    private EmployeRepository employeRepository;

    public Employe findById(Long id){
        Optional<Employe> optionalEmploye = employeRepository.findById(id);
        if(optionalEmploye.isPresent()){
            return optionalEmploye.get();
        } else {
            throw new EntityNotFoundException("Impossible de trouver l'employé d'identifiant " + id);
        }
    }

    public Long countAllEmploye() {
        return employeRepository.count();
    }

    public void deleteEmploye(Long id){
        employeRepository.deleteById(id);
    }

    public <T extends Employe> T creerEmploye(T e) {
        if(employeRepository.findByMatricule(e.getMatricule()) != null){
            throw new EntityExistsException("Il existe déjà un employé avec le matricule " + e.getMatricule());
        }
        return employeRepository.save(e);
    }

    public <T extends Employe> T updateEmploye(Long id, T employe) {
        if(!employeRepository.existsById(id)){
            throw new EntityNotFoundException("Impossible de trouver l'employé d'identifiant " + id);
        }
        if(employeRepository.findByMatricule(employe.getMatricule()) != null && !employe.getId().equals(id)){
            throw new EntityExistsException("Il existe déjà un employé avec le matricule " + employe.getMatricule());
        }
        return employeRepository.save(employe);
    }

    public Page<Employe> findAllEmployes(Integer page, Integer size, String sortProperty, String sortDirection) {
        if(page < 0 || size < 0){
            throw new IllegalArgumentException("Le numéro de page et la taille des pages ne peuvent pas être négatifs !");
        }
        Long nbPageMax = employeRepository.count() / size;
        if(page > nbPageMax){
            throw new IllegalArgumentException("Avec une taille de " + size + ", le numéro de page doit être compris entre 0 et " + nbPageMax);
        }
        try {
            Sort sort = Sort.by(new Sort.Order(Sort.Direction.fromString(sortDirection),sortProperty));
            Pageable pageable = PageRequest.of(page,size,sort);
            return employeRepository.findAll(pageable);
        }
        catch (Exception e) {
            throw new IllegalArgumentException("Erreur lors de la recherche paginée ! Vérifier les paramètres !");
        }
    }

    public Employe findMyMatricule(String matricule) {
        Employe employe = this.employeRepository.findByMatricule(matricule);
        if(employe == null){
            throw new EntityNotFoundException("Impossible de trouver l'employé de matricule " + matricule);
        }
        return employe;
    }

}
