package startingGate;

import entity.Account;
import mainPackage.MainPage;
import tools.PlaceholderField;
import tools.RequestDB;

import javax.swing.*;
import java.awt.*;

public class RegisterAccount extends JFrame {

    Account newUser ;
    private final JPanel panel = new JPanel(new GridBagLayout());
    private final JTextField emailField = new JTextField();
    private final JTextField passwordField = new JTextField();
    private final JTextField confirmPasswordField = new JTextField();
    private final JTextField phoneField = new JTextField();

    public RegisterAccount() {
        super("Register new user");
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(screenSize.width, screenSize.height);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
        getContentPane().add(panel);
        panel.setBackground(Color.BLACK);
    }

    /*
        Vérification que le user n'est pas déjà dans la BDD, et insertion
     */
    public void creationUser(){

        String emailUser = getEmailField().getText();
        String passwordUser = getPasswordField().getText();

        //Traitement de la réponse de la request SQL
        int rowsAffected = RequestDB.creationUser(emailUser, passwordUser);

        if (rowsAffected > 0) {
            this.dispose();
            new MainPage(new Account(emailUser, passwordUser));
        }

        //Si le new user a déjà un compte, rien ne se passe et on reste sur la même page

    }




    /*
        Creation des zones de textes et boutons de la page et leur affichage
     */
    public void createForm(){

        //CRÉATION DES CONTRAINTES POUR LE PLACEMENT DES ÉLÉMENTS
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0 ;
        constraints.gridy = 0 ;

        JLabel label = new JLabel("Register new account !");
        label.setFont(new Font("Arial", Font.BOLD, 20));
        label.setForeground(Color.WHITE);

        //AJOUT DES PLACEHOLDER POUR INDIQUER À L'UTILISATEUR CE QU'IL DOIT SAISIR
        PlaceholderField.setPlaceholder(emailField, "Email");
        PlaceholderField.setPlaceholder(passwordField, "Password");
        PlaceholderField.setPlaceholder(confirmPasswordField, "Confirm password");
        PlaceholderField.setPlaceholder(phoneField, "Phone");

        //AJOUT DES ÉLEMENTS DANS L'AFFICHAGE
        panel.add(label, constraints) ; constraints.gridy++ ;
        panel.add(emailField, constraints); constraints.gridy++ ;
        panel.add(passwordField, constraints); constraints.gridy++ ;
        panel.add(confirmPasswordField, constraints); constraints.gridy++ ;
        panel.add(phoneField, constraints); constraints.gridy++ ;

        //CRÉATION NOUVEAU PANEL POUR LES BOUTONS SUR LA MÊME LIGNE
        JPanel panelButton = new JPanel(new GridBagLayout());
        panelButton.setBackground(Color.BLACK);
        addCancelButton(panelButton);
        addConnexionButton(panelButton);
        addLoginPageButton(panelButton);
        panel.add(panelButton, constraints);

        //MISE A JOUR DES AFFICHAGES
        panel.revalidate();
        panel.repaint();
    }


    /*
        Efface toutes les données saisies dans tous les champs
     */
    public void addCancelButton(JPanel p){
        JButton button = new JButton("Cancel");
        p.add(button);

        //Add action listener
        button.addActionListener(e -> {
            emailField.setText("");
            passwordField.setText("");
            confirmPasswordField.setText("");
            phoneField.setText("");
        });
    }
    public void addLoginPageButton(JPanel p){
        JButton button = new JButton("Login page");
        p.add(button);

        //Add action listener
        button.addActionListener(e -> {
            LoginWindow log = new LoginWindow();
        });
    }


    public void addConnexionButton(JPanel p){
        JButton button = new JButton("Create account");
        p.add(button);

        //Add action listener
        button.addActionListener(e -> {

            if(!emailField.getText().isEmpty() && !passwordField.getText().isEmpty()){

                if(passwordField.getText().equals(confirmPasswordField.getText())) {
                    newUser = new Account(emailField.getText(), passwordField.getText(), phoneField.getText());
                    creationUser();
                } else {
                    JOptionPane.showMessageDialog(null, "Saisir le même mot de passe");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Au moins un champ est vide");
            }

            emailField.setText("");
            passwordField.setText("");
            confirmPasswordField.setText("");
            phoneField.setText("");
            PlaceholderField.setPlaceholder(emailField, "Email");
            PlaceholderField.setPlaceholder(passwordField, "Password");
            PlaceholderField.setPlaceholder(confirmPasswordField, "Confirm password");
            PlaceholderField.setPlaceholder(phoneField, "Phone");
        });
    }

    public JTextField getEmailField() {
        return emailField;
    }
    public JTextField getPasswordField() {
        return passwordField;
    }

}
