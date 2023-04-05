package startingGate;

import entity.Account;
import mainPackage.MainPage;
import tools.RequestDB;

import javax.swing.*;

public class ConnectAccount {

    private final LoginWindow loginWindow;

    public ConnectAccount(LoginWindow loginWindow) {
        this.loginWindow = loginWindow;
    }


    /*
        Tentative de connexion du compte avec check en base de données
     */
    public void connection() {

        String email = "a";//loginWindow.getEmailField().getText();
        String password = "a";//new String(loginWindow.getPasswordField().getPassword());

        //Traitement du résultat de la request
        int count = RequestDB.connection(email, password);
        if (count == 0) {
            JOptionPane.showMessageDialog(null,"le mot de passe ou l'email est erroné");
        } else {
            loginWindow.dispose();
            new MainPage(new Account(email, password));
        }
    }
}
