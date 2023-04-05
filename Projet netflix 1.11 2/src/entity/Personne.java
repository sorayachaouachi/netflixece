package entity;

public class Personne {

    private String nom;
    private String prenom ;
    private int age;
    private char sexe;

    public Personne() {
    }

    public Personne(String nom, String prenom, int age, char sexe) {
        this.nom = nom;
        this.prenom = prenom ;
        this.age = age;
        this.sexe = sexe;
    }

    public String getNom() {
        return nom;
    }

    public int getAge() {
        return age;
    }

    public char getSexe() {
        return sexe;
    }

    public String getPrenom() {
        return prenom;
    }
}