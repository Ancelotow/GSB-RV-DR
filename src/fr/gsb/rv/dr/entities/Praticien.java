package fr.gsb.rv.dr.entities;

import java.time.LocalDate;
import java.util.Date;

public class Praticien {

    private String numero;
    private String nom;
    private String prenom;
    private String ville;
    private String adresse;
    private String codePostale;
    private double coefNotoriete;
    private LocalDate dateDerniereVisite;
    private int dernierCoedConfiance;

    public Praticien(String numero, String nom, String ville, double coefNotoriete, LocalDate dateDerniereVisite, int dernierCoedConfiance) {
        super();
        this.numero = numero;
        this.nom = nom;
        this.ville = ville;
        this.coefNotoriete = coefNotoriete;
        this.dateDerniereVisite = dateDerniereVisite;
        this.dernierCoedConfiance = dernierCoedConfiance;
    }

    public Praticien(String numero, String nom, String prenom, String ville, String adresse, String codePostale, double coefNotoriete, LocalDate dateDerniereVisite, int dernierCoedConfiance) {
        super();
        this.numero = numero;
        this.nom = nom;
        this.prenom = prenom;
        this.ville = ville;
        this.adresse = adresse;
        this.codePostale = codePostale;
        this.coefNotoriete = coefNotoriete;
        this.dateDerniereVisite = dateDerniereVisite;
        this.dernierCoedConfiance = dernierCoedConfiance;
    }

    public Praticien(){
        super();
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public double getCoefNotoriete() {
        return coefNotoriete;
    }

    public void setCoefNotoriete(double coefNotoriete) {
        this.coefNotoriete = coefNotoriete;
    }

    public LocalDate getDateDerniereVisite() {
        return dateDerniereVisite;
    }

    public void setDateDerniereVisite(LocalDate dateDerniereVisite) {
        this.dateDerniereVisite = dateDerniereVisite;
    }

    public int getDernierCoedConfiance() {
        return dernierCoedConfiance;
    }

    public void setDernierCoedConfiance(int dernierCoedConfiance) {
        this.dernierCoedConfiance = dernierCoedConfiance;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getCodePostale() {
        return codePostale;
    }

    public void setCodePostale(String codePostale) {
        this.codePostale = codePostale;
    }
}
