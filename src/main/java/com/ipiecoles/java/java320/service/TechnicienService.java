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
public class TechnicienService {

    @Autowired
    private ManagerRepository managerRepository;

    @Autowired
    private TechnicienRepository technicienRepository;

    public Technicien deleteManager(Long idTechnicien) {
        Optional<Technicien> t = technicienRepository.findById(idTechnicien);
        if(t.isEmpty()){
            throw new EntityNotFoundException("Impossible de trouver le technicien d'identifiant " + idTechnicien);
        }

        Technicien technicien = t.get();

        technicien.setManager(null);
        technicienRepository.save(technicien);

        return technicien;
    }

    public Technicien addManager(Long idTechnicien, String matricule) {
        Optional<Technicien> t = technicienRepository.findById(idTechnicien);
        if(t.isEmpty()){
            throw new EntityNotFoundException("Impossible de trouver le technicien d'identifiant " + idTechnicien);
        }
        Manager m = managerRepository.findByMatricule(matricule);
        if(m == null){
            throw new EntityNotFoundException("Impossible de trouver le manager de matricule " + matricule);
        }

        Technicien technicien = t.get();

        if(technicien.getManager() != null){
            throw new IllegalArgumentException("Le technicien a déjà un manager : " + technicien.getManager().getPrenom() + " " + technicien.getManager().getNom()
                    + " (matricule " + technicien.getManager().getMatricule() + ")");
        }

        m.getEquipe().add(technicien);
        m = managerRepository.save(m);

        technicien.setManager(m);
        technicienRepository.save(technicien);

        return technicien;
    }
}
