package com.ipiecoles.java.java320.service;

import com.ipiecoles.java.java320.model.Commercial;
import com.ipiecoles.java.java320.model.Employe;
import com.ipiecoles.java.java320.repository.CommercialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.Objects;
import java.util.Optional;

@Service
public class CommercialService {

    @Autowired
    private CommercialRepository commercialRepository;

    public Commercial findById(Long id){
        Optional<Commercial> optionalEmploye = commercialRepository.findById(id);
        if(optionalEmploye.isPresent()){
            return optionalEmploye.get();
        } else {
            throw new EntityNotFoundException("Impossible de trouver le commercial d'identifiant " + id);
        }

    }

    public Commercial creerEmploye(Commercial e) {
        return commercialRepository.save(e);
    }

    public Commercial updateEmploye(Long id, Commercial employe) {
        return commercialRepository.save(employe);
    }


}
