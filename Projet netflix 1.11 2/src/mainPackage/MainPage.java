package mainPackage;

import entity.Account;
import entity.Film;
import startingGate.LoginWindow;
import tools.PlaceholderField;
import tools.RequestDB;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

public class MainPage extends JFrame implements ActionListener {

    private final Account user ;
    private final JPanel panel = new JPanel(new BorderLayout());
    private Map<String, List<Film>> films ;
    private final JTextField searchFilmField = new JTextField();
    private JPopupMenu menu;
    private JMenuItem deco;

    public MainPage(Account user) {
        super("Main window");
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(screenSize);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
        getContentPane().add(panel);
        panel.setBackground(Color.BLACK);

        this.user = user;

        mainThread();
    }

    public void mainThread(){

        panel.add(createHeadersBar(), BorderLayout.NORTH);
        panel.add(displayFilms(), BorderLayout.CENTER);

        this.add(panel);
    }



    /*
        Création d'un panel qui couvre toute la bande supérieure
     */
    public JPanel createHeadersBar(){

        //CRÉATION DU PANEL HEADER (TOUTE LA BANDE SUPÉRIEURE)
        JPanel panelHeader = new JPanel(new BorderLayout());
        panelHeader.setPreferredSize(new Dimension(panelHeader.getPreferredSize().width, 100));
        panelHeader.setBackground(Color.BLACK);
        panelHeader.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 50)); // ajoute une marge de 10px sur tous les bords

        //AJOUT DU LOGO NETFLIX À GAUCHE
        ImageIcon netflix = new ImageIcon("img/Netflix.png");
        Image scaledImage = netflix.getImage().getScaledInstance(200, 60, Image.SCALE_SMOOTH);
        JLabel labelLogo = new JLabel(new ImageIcon(scaledImage));

        //AJOUT DE LA BARRE DE RECHERCHE
        JPanel searchFieldPanel = addSearchField();

        //AJOUT DU LOGO LOUPE POUR AFFICHER LA BARRE DE RECHERCHE DE FILMS
        ImageIcon loupeImage = new ImageIcon("img/loupe.png");
        Image scaledImage2 = loupeImage.getImage().getScaledInstance(25, 25, Image.SCALE_SMOOTH);
        JLabel loupeLabel = new JLabel(new ImageIcon(scaledImage2));
        loupeLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(!searchFilmField.isVisible()){
                    searchFilmField.setVisible(true);
                    searchFilmField.requestFocus();

                } else{
                    searchFilmField.setVisible(false);
                }

                revalidate();
                repaint();
            }
        });
        searchFieldPanel.add(loupeLabel);

        menu = new JPopupMenu();
        deco = new JMenuItem("Deconnexion");
        deco.addActionListener(this);
        menu.add(deco);

        //AJOUT DU LOGO ACCOUNT POUR LA DÉCONNEXION
        ImageIcon accountImage = new ImageIcon("img/account.png");
        Image scaledImage3 = accountImage.getImage().getScaledInstance(40, 32, Image.SCALE_SMOOTH);
        JLabel accountLogo = new JLabel(new ImageIcon(scaledImage3));
        accountLogo.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                menu.show(e.getComponent(), e.getX(), e.getY());
            }
        });
        searchFieldPanel.add(accountLogo);

        //AJOUT DANS LE PANEL HEADER
        panelHeader.add(labelLogo, BorderLayout.WEST);
        panelHeader.add(searchFieldPanel, BorderLayout.EAST);

        return panelHeader ;
    }



    /*
        AJOUT DE LA BARRE DE RECHERCHE DE FILMS AVEC SUGGESTIONS EN FONCTION DE LA DISPONIBILITÉ
     */
    public JPanel addSearchField(){

        PlaceholderField.setPlaceholder(searchFilmField, "Search film");
        JList<String> suggestionList = new JList<>(new DefaultListModel<>());
        JPopupMenu suggestionMenu = new JPopupMenu();

        JPanel searchPanel = new JPanel();
        searchPanel.add(searchFilmField);

        searchPanel.setBackground(Color.BLACK);
        searchFilmField.setVisible(false);
        searchFilmField.setPreferredSize(new Dimension(searchFilmField.getPreferredSize().width-30, searchFilmField.getPreferredSize().height));
        searchFilmField.setComponentPopupMenu(suggestionMenu);
        searchFilmField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateSuggestions();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateSuggestions();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateSuggestions();
            }

            private void updateSuggestions() {

                //Clear le menu pour l'update
                suggestionMenu.removeAll();
                suggestionMenu.setVisible(false);

                String searchText = searchFilmField.getText().toLowerCase();

                //AFFICHE LE MENU DE SUGGESTIONS QUE SI L'UTILISATEUR A COMMENCE UNE SAISIE
                if(!searchText.isEmpty()){
                    DefaultListModel<String> model = (DefaultListModel<String>) suggestionList.getModel();
                    model.clear();
                    for (Map.Entry<String, List<Film>> filmsByGenre : films.entrySet()) {
                        for(Film film : filmsByGenre.getValue()){
                            if (film.getTitre().toLowerCase().contains(searchText)) {
                                model.addElement(film.getTitre());
                            }
                        }
                    }

                    //maximum de 10 propositions pour ne pas surcharger
                    int max = 10 ;
                    if(((DefaultListModel<String>) suggestionList.getModel()).size()<max){
                        max = ((DefaultListModel<String>) suggestionList.getModel()).size() ;
                    }

                    //Ajout des suggestions dans le menu
                    for(int i=0 ; i<max ; i++){
                        JMenuItem item = new JMenuItem(((DefaultListModel<String>) suggestionList.getModel()).get(i));
                        item.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                // Code à exécuter lorsque l'utilisateur clique sur l'item
                                JMenuItem selectedItem = (JMenuItem) e.getSource();
                                searchFilmField.setText(selectedItem.getText());
                                suggestionMenu.setVisible(false);
                                displayOneFilm(selectedItem.getText());
                            }
                        });
                        suggestionMenu.add(item);
                    }

                    if(searchFilmField.isVisible()){
                        //Affichage du menu
                        searchFilmField.getComponentPopupMenu().show(searchFilmField, 0, searchFilmField.getHeight());
                        //Déplace le focus sur le JTextField
                        suggestionMenu.setFocusable(false);
                        searchFilmField.requestFocusInWindow();
//                        searchFilmField.setCaretPosition(searchFilmField.getText().length());
                    }

                }
            }
        });

        return  searchPanel ;
    }


    /*
        Affichage des films
     */
    public JScrollPane displayFilms() {

        JPanel allFilmPanel = new JPanel(new GridBagLayout());
        allFilmPanel.setBackground(Color.BLACK);

        //Récupération de tous les films dans la base de données
        List<Film> rqFilms = RequestDB.requestFilms();
        films = getFilmsByGenre(rqFilms);

        GridBagConstraints mainConstraints = new GridBagConstraints();
        mainConstraints.gridx=0 ;
        mainConstraints.gridy=0 ;

        //Ajout des films dans la page
        if (films != null) {
            //Parrcours de toute la map
            for (Map.Entry<String, List<Film>> filmsByGenre : films.entrySet()) {
                //Création d'un panel et de contraintes qui contient tous les films d'un genre
                JPanel panelFilmByGenre = new JPanel(new GridBagLayout());
                panelFilmByGenre.setBackground(Color.BLACK);
                GridBagConstraints genreConstraints = new GridBagConstraints();
                genreConstraints.gridx = 0 ;

                //Affichage du nom du genre au début de la ligne
                JTextArea genreArea = new JTextArea(filmsByGenre.getKey());
                genreArea.setEnabled(false);
                genreArea.setLineWrap(true);
                genreArea.setWrapStyleWord(true);
                panelFilmByGenre.add(genreArea, genreConstraints);
                genreConstraints.gridx++ ;

                //Ajout des films dans le panel de genre
                for(Film film : filmsByGenre.getValue()){
                    genreConstraints.gridy = 0 ;
                    if(film.getAffiche()!=null)
                        panelFilmByGenre.add(film.getAffiche(), genreConstraints);
                    genreConstraints.gridy = 1 ;

                    JTextArea titreArea = new JTextArea(film.getTitre());
                    titreArea.setEnabled(false);
                    titreArea.setLineWrap(true);
                    titreArea.setWrapStyleWord(true);

                    panelFilmByGenre.add(titreArea, genreConstraints);
                    genreConstraints.gridx++;
                }

                //Ajout du panel de genre dans le panel de la page entière
                allFilmPanel.add(panelFilmByGenre, mainConstraints);
                mainConstraints.gridy++ ;
            }
        }

        //Ajout de l'option pour scroll la page
        JScrollPane scrollPane = new JScrollPane(allFilmPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(7);
        scrollPane.setBorder(null);

        return scrollPane ;
    }

    /*
        On trie la list des films par genre
     */
    public Map<String, List<Film>> getFilmsByGenre(List<Film> films) {
        Map<String, List<Film>> filmsByGenre = new HashMap<>();
        for (Film film : films) {
            String genre = film.getGenre();
            if (!filmsByGenre.containsKey(genre)) {
                filmsByGenre.put(genre, new ArrayList<>());
            }
            filmsByGenre.get(genre).add(film);
        }
        return filmsByGenre;
    }


    /*
        AFFICHAGE DES INFORMATIONS DE UN SEUL FILM UNE FOIS QU'IL A ÉTÉ CHERCHÉ DANS LA BARRE DE RECHERCHE
     */
    public void displayOneFilm(String selectedSuggestion){

        Film selectedFilm = getFilmByTitle(selectedSuggestion);

        System.out.println(selectedFilm.toString());

        JPanel oneFilmPanel = new JPanel();
        oneFilmPanel.setBackground(Color.RED);
        oneFilmPanel.add(new JTextArea(selectedFilm.getTitre().toUpperCase()));
        oneFilmPanel.add(new JButton("hello"));
        panel.add(oneFilmPanel, BorderLayout.CENTER);

        //Rend invisible la liste de film
        panel.getComponent(1).setVisible(false);
        revalidate();
        repaint();
    }


    public void actionPerformed(ActionEvent e) {
        // Récupération de l'option sélectionnée
        JMenuItem source = (JMenuItem)(e.getSource());

        // Exécution de l'action associée à l'option sélectionnée
        if (source == deco) {
            int reponse = JOptionPane.showConfirmDialog(null, "Etes-vous sur de vouloir vous déconnecter ?", "Déconnection", JOptionPane.YES_NO_OPTION);

            if(reponse == JOptionPane.YES_OPTION){
                LoginWindow log = new LoginWindow();
            }
        }
    }


    /*
         RETOURNE LE FILM DONT LE TITRE EST PASSÉ EN PARAMÈTRE
     */
    public Film getFilmByTitle(String title) {

        for (Map.Entry<String, List<Film>> entry : films.entrySet()) {
            for (Film film : entry.getValue()) {
                if (film.getTitre().equals(title)) {
                    return film;
                }
            }
        }
        return null;
    }




    public Account getUser() {
        return user;
    }

    public Map<String, List<Film>> getFilms() {
        return films;
    }
}
