package com.ipiecoles.java.java320.service;

import com.ipiecoles.java.java320.model.Manager;
import com.ipiecoles.java.java320.model.Technicien;
import com.ipiecoles.java.java320.repository.ManagerRepository;
import com.ipiecoles.java.java320.repository.TechnicienRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Service
public class ManagerService {
    @Autowired
    private ManagerRepository managerRepository;

    @Autowired
    private TechnicienRepository technicienRepository;

    public Manager deleteTechniciens(Long idManager, Long idTechnicien) {
        Optional<Manager> m = managerRepository.findById(idManager);
        if(m.isEmpty()){
            throw new EntityNotFoundException("Impossible de trouver le manager d'identifiant " + idManager);
        }
        Optional<Technicien> t = technicienRepository.findById(idTechnicien);
        if(t.isEmpty()){
            throw new EntityNotFoundException("Impossible de trouver le technicien d'identifiant " + idTechnicien);
        }

        Manager manager = m.get();
        Technicien technicien = t.get();

        manager.getEquipe().remove(technicien);
        managerRepository.save(manager);

        technicien.setManager(null);
        technicienRepository.save(technicien);

        return manager;
    }

    public Manager addTechniciens(Long idManager, String matricule) {
        Optional<Manager> m = managerRepository.findById(idManager);
        if(m.isEmpty()){
            throw new EntityNotFoundException("Impossible de trouver le manager d'identifiant " + idManager);
        }
        Technicien t = technicienRepository.findByMatricule(matricule);
        if(t == null){
            throw new EntityNotFoundException("Impossible de trouver le technicien de matricule " + matricule);
        }

        if(t.getManager() != null){
            throw new IllegalArgumentException("Le technicien de matricule " + matricule + " a déjà un manager : " + t.getManager().getPrenom() + " " + t.getManager().getNom()
            + " (matricule " + t.getManager().getMatricule() + ")");
        }

        Manager manager = m.get();

        manager.getEquipe().add(t);
        manager = managerRepository.save(manager);

        t.setManager(manager);
        technicienRepository.save(t);
        return manager;
    }
}
