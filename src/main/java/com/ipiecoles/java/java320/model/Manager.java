package com.ipiecoles.java.java320.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class,property = "id")

public class Manager extends Employe {

	@JsonIgnoreProperties("manager")
	@OneToMany(mappedBy = "manager")
	private Set<Technicien> equipe = new HashSet<>();

	public Manager(){

	}

	public Manager(String nom, String prenom, String matricule, LocalDate dateEmbauche, Double salaire, HashSet<Technicien> equipe) {
		super(nom, prenom, matricule, dateEmbauche, salaire);
		this.equipe = equipe;
	}

	public void ajoutTechnicienEquipe(Technicien technicien) {
		equipe.add(technicien);
	}

	public void ajoutTechnicienEquipe(String nom, String prenom, String matricule, LocalDate dateEmbauche, Double salaire, Integer grade)  {
		this.ajoutTechnicienEquipe(new Technicien(nom, prenom, matricule, dateEmbauche, salaire, grade));
	}
	
	public void setSalaire(Double salaire) {
		super.setSalaire(salaire * Entreprise.INDICE_MANAGER + (salaire * (double)equipe.size() / 10));
	}

	public Double getPrimeAnnuelle() {
		return Entreprise.primeAnnuelleBase() + equipe.size() * Entreprise.PRIME_MANAGER_PAR_TECHNICIEN;
	}
	
	public void augmenterSalaire(Double pourcentage) {
		super.augmenterSalaire(pourcentage);
		augmenterSalaireEquipe(pourcentage);
	}

	private void augmenterSalaireEquipe(Double pourcentage) {
		for (Technicien technicien : equipe) {
			technicien.augmenterSalaire(pourcentage);
		}
	}

	public List<Technicien> equipeParGrade(){
		return equipe.stream().sorted(Technicien::compareTo).collect(Collectors.toList());
	}

	public double salaireEquipeGrade1(){
		return equipe.stream().filter(t -> t.getGrade().equals(1)).mapToDouble(Technicien::getSalaire).sum();
	}

	/**
	 * @return the equipe
	 */
	public Set<Technicien> getEquipe() {
		return equipe;
	}

	/**
	 * @param equipe the equipe to set
	 */
	public void setEquipe(HashSet<Technicien> equipe) {
		this.equipe = equipe;
	}

	@Override
	public String toString() {
		return "Manager{} " + super.toString();
	}
}
