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

    /**
     * Méthode cherchant un employé en fonction de son identifiant
     *
     * @param id Identifiant de l'employé à chercher en base
     * @return l'employé d'identifiant celui recherché
     * @throws EntityNotFoundException Si l'identifiant ne correspond à aucun employé
     */
    public Employe findById(Long id){
        Optional<Employe> optionalEmploye = employeRepository.findById(id);
        if(optionalEmploye.isPresent()){
            return optionalEmploye.get();
        } else {
            throw new EntityNotFoundException("Impossible de trouver l'employé d'identifiant " + id);
        }
    }

    /**
     * Méthode retournant le nombre d'employés en base
     *
     * @return le nombre d'employés en base
     */
    public Long countAllEmploye() {
        return employeRepository.count();
    }

    /**
     * Méthode permettant de supprimer un employé en fonction de son identifiant.
     *
     * @param id Identifiant de l'employé à supprimer
     * @throws EntityNotFoundException Si l'identifiant ne correspond à aucun employé
     */
    public void deleteEmploye(Long id){
        if(!employeRepository.existsById(id)){
            throw new EntityNotFoundException("Impossible de trouver l'employé d'identifiant " + id);
        }
        employeRepository.deleteById(id);
    }

    /**
     * Méthode permettant de sauvegarder en base un nouvel employé
     * @param employe Employé à sauvegarder
     * @param <T> Sous-classe concrète de l'employé Technicien, Commercial ou Manager
     * @return L'employé nouvellement créé
     * @throws EntityExistsException Si le matricule existe déjà pour un autre employé
     */
    public <T extends Employe> T creerEmploye(T employe) {
        if(employeRepository.findByMatricule(employe.getMatricule()) != null){
            throw new EntityExistsException("Il existe déjà un employé avec le matricule " + employe.getMatricule());
        }
        return employeRepository.save(employe);
    }

    /**
     * Méthode permettant de sauvegarder les modifications effectuées sur un employés
     *
     * @param id Identifiant de l'employé
     * @param employe Employé à mettre à jour
     * @param <T> Sous-classe concrète de l'employé Technicien, Commercial ou Manager
     * @return L'employé mis à jour
     * @throws EntityExistsException Si le matricule existe déjà pour un autre employé
     * @throws EntityNotFoundException Si l'identifiant ne correspond à aucun employé
     * @throws IllegalArgumentException Si l'identifiant de l'url ne correspond pas à celui de l'employé
     */
    public <T extends Employe> T updateEmploye(Long id, T employe) {
        if(!employeRepository.existsById(id)){
            throw new EntityNotFoundException("Impossible de trouver l'employé d'identifiant " + id);
        }
        if(!employe.getId().equals(id)){
            throw new IllegalArgumentException("Incohérence entre l'identifiant de l'employé et celui de l'URL");
        }
        if(employeRepository.findByMatricule(employe.getMatricule()) != null && !employe.getId().equals(id)){
            throw new EntityExistsException("Il existe déjà un employé avec le matricule " + employe.getMatricule());
        }
        return employeRepository.save(employe);
    }

    /**
     * Affiche de manière paginée tous les employés
     *
     * @param page numéro de page (première page => 0)
     * @param size taille de la page
     * @param sortProperty propriété par laquelle le tri va être effectué
     * @param sortDirection direction pour le tri ascendant 'ASC' ou descendant 'DESC'
     *
     * @return La page d'employés correspondante
     * @throws IllegalArgumentException si les paramètres de pagination sont incorrects
     */
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

    /**
     * Recherche un employé en BDD à partir de son matricule
     *
     * @param matricule Matricule de l'employé
     * @return L'employé portant le matricule recherché
     * @throws EntityNotFoundException Si le matricule ne correspond à aucun employé
     */
    public Employe findMyMatricule(String matricule) {
        Employe employe = this.employeRepository.findByMatricule(matricule);
        if(employe == null){
            throw new EntityNotFoundException("Impossible de trouver l'employé de matricule " + matricule);
        }
        return employe;
    }

}
