package startingGate;

import tools.PlaceholderField;
import javax.swing.*;
import java.awt.*;

public class LoginWindow extends JFrame{


    private final JPanel panel = new JPanel(new GridBagLayout());
    private final JTextField emailField = new JTextField();
    private final JPasswordField passwordField = new JPasswordField();


    public LoginWindow() {
        super("Login window");
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(screenSize.width, screenSize.height);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
        getContentPane().add(panel);
        panel.setBackground(Color.BLACK);

        panel.revalidate();
        panel.repaint();

        fillWindow();
    }


    /*
        Ajout du formulaire de connexion et des éléments graphiques
     */
    public void fillWindow(){

        //CRÉATION DE CONTRAINTE POUR PLACER LES ÉLÉMENTS
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(5, 5, 5, 5); // Ajout de marges
        constraints.gridx = 0 ;
        constraints.gridy = 0 ;


        //AJOUT DU LOGO NETFLIX EN HAUT
        ImageIcon imageIcon = new ImageIcon("img/Netflix.png");
        JLabel label = new JLabel(imageIcon);
        panel.add(label);

        //AJOUT DU BLOC DE SAISIE DES INFOS
        JPanel p = new JPanel(new GridBagLayout());
        p.setBackground(Color.BLACK);

        //AJOUT DE ZONE DE TEXTE POUR EMAIL ET PASSWORD
        PlaceholderField.setPlaceholder(emailField, "email");
        PlaceholderField.setPlaceholder(passwordField, "password");

        p.add(emailField, constraints); constraints.gridy++ ;
        p.add(passwordField, constraints);

        //AJOUT DES BOUTONS DANS UN NOUVEAU BLOC
        JPanel panelButton = new JPanel();
        panelButton.setBackground(Color.BLACK);
        addCancelButton(panelButton);
        addConnexionButton(panelButton); constraints.gridy++ ;
        p.add(panelButton, constraints);

        //AJOUT DU BLOC DE SAISIE DE TEXTE ET BOUTON APRÈS LE LOGO NETFLIX
        constraints.gridx = 0 ;
        constraints.gridy = 1 ;
        panel.add(p, constraints);

        constraints.gridy++ ;
        constraints.anchor = GridBagConstraints.PAGE_END ;
        addRegisterButton(panel, constraints);

        //MISE A JOUR DES AFFICHAGES
        panel.revalidate();
        panel.repaint();
    }



    public void addCancelButton(JPanel p){
        JButton button = new JButton("Cancel");
        p.add(button);

        //Add action listener
        button.addActionListener(e -> {
            emailField.setText("");
            passwordField.setText("");
        });
    }



    public void addConnexionButton(JPanel p){
        JButton button = new JButton("Connexion");
        p.add(button);

        //Add action listener
        button.addActionListener(e -> {
            ConnectAccount account = new ConnectAccount(LoginWindow.this);
            account.connection();
        });
    }

    public void addRegisterButton(JPanel p, GridBagConstraints c){
        JButton button = new JButton("Register now");
        p.add(button, c);

        //Add action listener
        button.addActionListener(e -> {
            RegisterAccount account = new RegisterAccount();
            this.dispose();
            account.createForm();
        });
    }

    public JTextField getEmailField() {
        return emailField;
    }

    public JPasswordField getPasswordField() {
        return passwordField;
    }
}




