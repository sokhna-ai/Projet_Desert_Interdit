import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.util.ArrayList;

public class Vue extends JFrame {

    private Desert desert;
    private GridBagLayout layout; // J'utlise GridBagLayout pour les cases
    private Overview overview;

    /**
     * Constructeur
     */
    public Vue(Desert desert) {
        super("Desert");
        this.desert = desert;
        initialiserComposants();
    }

    /**
     * Initialisation des composants
     * 
     * @return
     */
    private boolean initialiserComposants() {
        setLayout();

        return true;
    }

    /**
     * Initialisation du layout
     * 
     * @return
     */
    private boolean setLayout() {

        layout = new GridBagLayout();/* J'utlise GridBagLayout pour les cases */
        this.setLayout(layout);

        GridBagConstraints c = new GridBagConstraints();

        c.weightx = 5.0;
        c.weighty = 1.0;
        c.gridwidth = 2;
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 0;
        c.gridy = 0;
        setOverview();
        this.add(overview, c);

        /* La répartition de la grille et de la barre de log est 3:2 */
        c.gridwidth = 1;
        c.weightx = 3.0;
        c.weighty = 4.0;
        c.gridx = 0;
        c.gridy = 1;
        JPanel grille = new Grille();
        this.add(grille, c);

        c.weightx = 2.0;
        c.gridx = 1;
        c.gridy = 1;
        JPanel barreDeLog = new BarreDeLog();
        this.add(barreDeLog, c);

        c.gridwidth = 2;
        c.weightx = 1.0;
        c.weighty = 1.0;
        c.gridx = 0;
        c.gridy = 2;
        JPanel statusBar = new statusBar();
        this.add(statusBar, c);

        return true;
    }

    /**
     * Initialisation de l'overview (la barre de navigation au dessus)
     * 
     * @return
     */
    private boolean setOverview() {

        overview = new Overview();

        return true;
    }

    /**
     * Mise a jour de l'overview
     * Reste à finir
     * 
     * @return
     */
    public void rafraichir() {
        overview.rafraichir();
    }

    /**
     * 
     * Classe qui représente la barre de navigation au dessus
     */
    private class Overview extends JPanel {
        BlocText jour;
        BlocText sable;
        BlocText tempete;

        public Overview() {
            super();
            this.setLayout(new GridLayout(1, 3));
            jour = new BlocText();
            sable = new BlocText();
            tempete = new BlocText();
            jour.setText("Jour : 0");
            sable.setText("Sable : 0");
            tempete.setText("Tempête : 0");
            this.add(jour);
            this.add(sable);
            this.add(tempete);
            this.setBackground(new Color(0x82B7BD));
        }

        /**
         * Mise a jour de l'overview
         */
        public void rafraichir() {
            jour.setText("Jour : " + desert.getTour());
            sable.setText("Sable : " + desert.getTotalSable());
            tempete.setText("Tempête : " + desert.getNiveauTempete());
        }

        /**
         * Classe qui représente un bloc de texte
         */
        class BlocText extends JPanel {
            JLabel text;

            BlocText() {
                super();
                text = new JLabel("xxxxxx : xxxx");
                this.add(text);
                this.setOpaque(false);
            }

            void setText(String text) {
                this.text.setText(text);
            }
        }

    }

    

    /**
     * Classe qui représente la barre de log
     * 
     */
    class BarreDeLog extends JPanel {
        BarreDeLog() {
            super();
            this.setLayout(new GridLayout(10, 1));
            this.setBackground(new Color(0xF2A7A1));
            for (int i = 0; i < 5; i++) {
                JLabel label = new JLabel("Un log");
                label.setOpaque(false);
                this.add(label);
            }
        }
    }

    class statusBar extends JPanel {
        ArrayList<affichageJoueur> affichageJoueurs = new ArrayList();
        statusBar() {
            super();
            this.setLayout(new GridLayout(1, 5));
            for (int i = 0; i < desert.getJoueurs().size(); i++){
                affichageJoueurs.add(new affichageJoueur(desert.getJoueurs().get(i), i));
                this.add(affichageJoueurs.get(i));
            }
        }
        class affichageJoueur extends JPanel {
            Joueur joueur;
            int indice;
            JLabel text;
            affichageJoueur(Joueur joueur, int indice) {
                super();
                text=new JLabel("Player " + indice + " : " +  " Niveau Eau : 0");
                this.add(text);
            }
            void setText(String text) {
                this.text.setText(text);
            }
            void rafraichir() {
                this.text.setText("Player " + indice + " : " +  " Niveau Eau : " + joueur.getNiveauEau());
            }
        }
    }

}
