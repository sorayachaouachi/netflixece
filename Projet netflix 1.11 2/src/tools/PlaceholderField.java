package tools;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

/*
    CETTE CLASSE PERMET D'APPELER DE MANIÈRE STATIC LA MÉTHODE PERMETTANT D'AJOUTER UN PLACEHOLDER
    AU SEIN D'UN TEXTFIELD
 */

public class PlaceholderField {

    public static void setPlaceholder(JTextField textField, String placeholder) {
        textField.setText(placeholder);
        textField.setForeground(Color.GRAY);
        textField.setFont(textField.getFont().deriveFont(Font.ITALIC));

        textField.addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent e) {
                if (textField.getText().equals(placeholder)) {
                    textField.setText("");
                    textField.setForeground(Color.BLACK);
                    textField.setFont(textField.getFont().deriveFont(Font.PLAIN));
                }
            }
            public void focusLost(FocusEvent e) {
                if (textField.getText().isEmpty()) {
                    textField.setText(placeholder);
                    textField.setForeground(Color.GRAY);
                    textField.setFont(textField.getFont().deriveFont(Font.ITALIC));
                }
            }
        });

        textField.setBackground(Color.decode("#989898"));
        textField.setFont(new Font("Arial", Font.PLAIN, 14));
        textField.setPreferredSize(new Dimension(300, 40));
    }
}
