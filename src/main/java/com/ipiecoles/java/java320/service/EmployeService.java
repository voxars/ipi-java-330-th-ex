package com.ipiecoles.java.java320.service;

import com.ipiecoles.java.java320.model.Employe;
import com.ipiecoles.java.java320.repository.EmployeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Objects;
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
        return employeRepository.save(e);
    }

    public <T extends Employe> T updateEmploye(Long id, T employe) {
        return employeRepository.save(employe);
    }

    public Page<Employe> findAllEmployes(Integer page, Integer size, String sortProperty, String sortDirection) {
        Sort sort = Sort.by(new Sort.Order(Sort.Direction.fromString(sortDirection),sortProperty));
        Pageable pageable = PageRequest.of(page,size,sort);
        return employeRepository.findAll(pageable);
    }

    public Employe findMyMatricule(String matricule) {
        Employe employe = this.employeRepository.findByMatricule(matricule);
        if(employe == null){
            throw new EntityNotFoundException("Impossible de trouver l'employé de matricule " + matricule);
        }
        return employe;
    }

}
