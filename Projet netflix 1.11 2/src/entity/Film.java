package entity;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URI;

public class Film {

    private int id ;
    private String titre ;
    private String genre ;
    private int duree ;
    private String langue ;
    private String realisateur ;
    private String producteur ;
    private URI urlFilm ;
    private JLabel affiche ;
    private int annee ;



    public Film() {
    }

    public Film(int id, String titre, String genre, int duree, String langue, String realisateur, String producteur, URI url, JLabel affiche, int annee) {
        this.id = id;
        this.titre = titre;
        this.genre = genre;
        this.duree = duree;
        this.langue = langue;
        this.realisateur = realisateur;
        this.producteur = producteur;
        this.urlFilm = url;
        this.affiche = affiche;
        this.annee = annee;

        // Ajout d'un MouseListener au JLabel
        this.affiche.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Ouverture du lien lorsque l'utilisateur clique sur l'image
                try {
                    Desktop.getDesktop().browse(urlFilm);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }


    public int getId() {
        return id;
    }

    public String getTitre() {
        return titre;
    }

    public String getGenre() {
        return genre;
    }

    public int getDuree() {
        return duree;
    }

    public String getLangue() {
        return langue;
    }

    public String getRealisateur() {
        return realisateur;
    }

    public String getProducteur() {
        return producteur;
    }

    public URI getUrlFilm() {
        return urlFilm;
    }

    public JLabel getAffiche() {
        return affiche;
    }

    public int getAnnee() {
        return annee;
    }

    @Override
    public String toString() {
        return "Film{" +
                "titre='" + titre + '\'' +
                ", genre='" + genre + '\'' +
                ", duree=" + duree +
                ", langue='" + langue + '\'' +
                ", realisateur='" + realisateur + '\'' +
                ", producteur='" + producteur + '\'' +
                ", urlFilm=" + urlFilm +
                ", affiche=" + affiche +
                ", annee=" + annee +
                '}';
    }
}
