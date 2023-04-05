package tools;

import entity.Film;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RequestDB {

    static String url = "jdbc:mysql://localhost:8889/netflix";
    static String user = "root";
    static String password = "root";



    /*
        Connexion d'un utilisateur avec un compte déjà existant
     */
    public static int connection(String emailUser, String passwordUser){
        Connection conn;
        PreparedStatement stmt;

        try {
            // Établir la connexion à la base de données
            conn = DriverManager.getConnection(url, user, password);

            // Préparer la requête SQL avec un paramètre pour la valeur à vérifier
            String request = "SELECT COUNT(*) FROM login WHERE email = ? AND password = ?";
            stmt = conn.prepareStatement(request);
            stmt.setString(1, emailUser);
            stmt.setString(2, passwordUser);

            ResultSet resultSet = stmt.executeQuery();
            resultSet.next();

            return resultSet.getInt(1);

        } catch (
                SQLException e) {
            System.out.println("Erreur lors de la connexion à la base de données");
            e.printStackTrace();
        }

        return 0 ;
    }



    /*
    Programme principal de la classe pour créer et enregistrer en BDD le nouvel utilisateur
 */
    public static int creationUser(String emailUser, String passwordUser){

        try {
            Connection connection = DriverManager.getConnection(url, user, password);

            // Vérification si la valeur n'existe pas déjà dans la base de données
            String checkQuery = "SELECT COUNT(*) FROM login WHERE email = ?";
            PreparedStatement checkStatement = connection.prepareStatement(checkQuery);
            checkStatement.setString(1, emailUser);
            ResultSet resultSet = checkStatement.executeQuery();

            if (resultSet.next() && resultSet.getInt(1) > 0) {
                JOptionPane.showMessageDialog(null,"vous avez déjà un compte chez nous");
                return 0;
            }

            // Insertion de la valeur dans la base de données
            return insertionNewUser(emailUser, passwordUser);

        } catch (SQLException e) {
            System.err.println("Une erreur s'est produite : " + e.getMessage());
        }
        return 0 ;
    }


    /*
        Insertion d'un new user dans la BDD
     */
    public static int insertionNewUser(String emailUser, String passwordUser) {

        try {
            Connection connection = DriverManager.getConnection(url, user, password);
            String insertQuery = "INSERT INTO login (email, password) VALUES (?,?)";
            PreparedStatement insertStatement = connection.prepareStatement(insertQuery);
            insertStatement.setString(1, emailUser);
            insertStatement.setString(2, passwordUser);

            return insertStatement.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Une erreur s'est produite : " + e.getMessage());
        }
        return -1;
    }



    public static List<Film> requestFilms() {

        Connection conn;
        PreparedStatement stmt;
        List<Film> films = new ArrayList<>();

        try {
            // Établir la connexion à la base de données
            conn = DriverManager.getConnection(url, user, password);

            // Préparer la requête SQL avec un paramètre pour la valeur à vérifier
            String request = "SELECT * FROM films where id < 30";
            stmt = conn.prepareStatement(request);
            ResultSet resultSet = stmt.executeQuery();

            while(resultSet.next()){

                //Récupération des attributs de la table
                String titre = resultSet.getString("titre");
                String genre = resultSet.getString("genre");
                String realisateur = resultSet.getString("realisateur");
                int annee = resultSet.getInt("annee");
                String producteur = resultSet.getString("producteur");
                int duree = resultSet.getInt("duree");
                int id = resultSet.getInt("id");
                String urlFilm = resultSet.getString("url");
                String langue = resultSet.getString("langue");

                byte[] imageData = resultSet.getBytes("affiche");
                ByteArrayInputStream bis = new ByteArrayInputStream(imageData);
                Image image = ImageIO.read(bis);
                bis.close();
                JLabel afficheLabel = new JLabel(new ImageIcon(image.getScaledInstance(200, 284, Image.SCALE_SMOOTH)));

                if(urlFilm == null){
                    urlFilm = "https://www.google.com";
                }
                //Création du film et ajout dans la liste de films
                Film film = new Film(id, titre, genre, duree, langue, realisateur, producteur, new URI(urlFilm), afficheLabel, annee);
                films.add(film);
            }
            return films ;

        } catch (SQLException e) {
            System.out.println("Erreur lors de la connexion à la base de données");
            e.printStackTrace();
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
        return null ;
    }



}
