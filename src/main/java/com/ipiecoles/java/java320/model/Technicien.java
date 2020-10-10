package com.ipiecoles.java.java320.model;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.time.LocalDate;
import java.util.Objects;

@Entity
public class Technicien extends Employe implements Comparable<Technicien> {


	@ManyToOne
	private Manager manager;

	private Integer grade;
	
	public Technicien() {

	}
		
	public Technicien(String nom, String prenom, String matricule, LocalDate dateEmbauche, Double salaire, Integer grade) {
		super(nom, prenom, matricule, dateEmbauche, salaire);
		this.setGrade(grade);
	}

	public Double getPrimeAnnuelle() {
		Double salaireBase = Entreprise.primeAnnuelleBase();
		return salaireBase + salaireBase * (1 + (double) grade / 10) + Entreprise.PRIME_ANCIENNETE * this.getNombreAnneeAnciennete();
	}

	public Manager getManager() {
		return manager;
	}

	public void setManager(Manager manager) {
		this.manager = manager;
	}

	public Integer getNbConges() {
		return super.getNbConges() + this.getNombreAnneeAnciennete();
	}

	/**
	 * @return the grade
	 */
	public Integer getGrade() {
		return grade;
	}

	public void setGrade(Integer grade) {
		this.grade = grade;
	}

	@Override
	public void setSalaire(Double salaire) {
		super.setSalaire( salaire * (1 + (double) grade / 10));
	}

	@Override
	public String toString() {
		return "Technicien{" +
				"grade=" + grade +
				"} " + super.toString();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Technicien)) return false;
		if (!super.equals(o)) return false;
		Technicien that = (Technicien) o;
		return Objects.equals(grade, that.grade);
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), grade);
	}

	@Override
	public int compareTo(Technicien o) {
		return Integer.compare(o.getGrade(), this.grade);
	}
}
