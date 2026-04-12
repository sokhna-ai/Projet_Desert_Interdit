import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Vue MVC : affiche l'état du désert et expose les boutons d'action.
 * Architecture : NORD = infos globales, CENTRE = grille, EST = log+actions,
 * SUD = statut joueurs.
 */
public class Vue extends JFrame {

    // Palette globale
    static final Color BG_DARK = new Color(0x2B1E0E);
    static final Color BG_MID = new Color(0x3B2B14);
    static final Color BG_PANEL = new Color(0x1A1008);
    static final Color GOLD = new Color(0xC8972A);
    static final Color GOLD_LIGHT = new Color(0xF5C842);
    static final Color TEXT_MAIN = new Color(0xEEDDBB);
    static final Color TEXT_DIM = new Color(0x99885A);

    // Couleurs joueurs (mêmes que dans Grille)
    static final Color[] JOUEUR_COLORS = {
            new Color(0x3BAEFF), new Color(0xFF4D4D),
            new Color(0x4DFF91), new Color(0xFFDD00)
    };

    private Desert desert;
    private Controleur controleur;

    private PanneauInfo panneauInfo;
    private Grille grille;
    private PanneauLog panneauLog;
    private PanneauJoueurs panneauJoueurs;
    private PanneauActions panneauActions;
    private PanneauPieces panneauPieces;

    public Vue(Desert desert) {
        super("Le Desert Interdit");
        this.desert = desert;
        construireInterface();
    }

    public void setControleur(Controleur c) {
        this.controleur = c;
        panneauActions.brancherControleur(c);
    }

    public void rafraichir() {
        panneauInfo.rafraichir();
        grille.rafraichir();
        panneauJoueurs.rafraichir();
        panneauActions.rafraichir();
        panneauPieces.rafraichir();
        String etat = desert.etatPartie();
        if (!etat.equals("EN_COURS")) {
            afficherFinPartie(etat);
        }
        revalidate();
        repaint();
    }

    public void log(String message) {
        panneauLog.ajouterLog(message);
    }

    // ------------------------------------------------------------------
    // Construction
    // ------------------------------------------------------------------
    private void construireInterface() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1280, 760);
        setMinimumSize(new Dimension(1000, 680));
        setLocationRelativeTo(null);

        getContentPane().setBackground(BG_DARK);
        setLayout(new BorderLayout(5, 5));
        ((JPanel) getContentPane()).setBorder(
                BorderFactory.createEmptyBorder(5, 5, 5, 5));

        // NORD
        panneauInfo = new PanneauInfo();
        add(panneauInfo, BorderLayout.NORTH);

        // CENTRE : grille centrée avec padding
        grille = new Grille(desert);
        JPanel centreWrapper = new JPanel(new GridBagLayout());
        centreWrapper.setBackground(BG_DARK);
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1.0;
        c.weighty = 1.0;

        centreWrapper.add(grille,c);
        add(centreWrapper, BorderLayout.CENTER);

        // EST : log + actions
        JPanel panneauDroit = new JPanel(new BorderLayout(0, 5));
        panneauDroit.setBackground(BG_DARK);
        panneauDroit.setPreferredSize(new Dimension(290, 0));

        panneauLog = new PanneauLog();
        panneauDroit.add(panneauLog, BorderLayout.CENTER);

        panneauActions = new PanneauActions();
        panneauDroit.add(panneauActions, BorderLayout.SOUTH);

        add(panneauDroit, BorderLayout.EAST);

        // SUD : joueurs
        panneauJoueurs = new PanneauJoueurs();
        add(panneauJoueurs, BorderLayout.SOUTH);
        // OUEST : pieces
        panneauPieces = new PanneauPieces();
        add(panneauPieces, BorderLayout.WEST);
    }

    private void afficherFinPartie(String etat) {
        String msg;
        if (etat.equals("VICTOIRE"))
            msg = "VICTOIRE ! L'equipe s'envole avec la machine fantastique !";
        else if (etat.equals("DEFAITE_SOIF"))
            msg = "DEFAITE : un joueur est mort de soif dans le desert.";
        else if (etat.equals("DEFAITE_SABLE"))
            msg = "DEFAITE : le desert est ensable (plus de 43 tonnes de sable).";
        else
            msg = "DEFAITE : la tempete est devenue incontrôlable (niveau >= 7).";
        JOptionPane.showMessageDialog(this, msg,
                etat.replace("_", " "), JOptionPane.INFORMATION_MESSAGE);
    }

    // ==================================================================
    // Barre d'infos (NORD)
    // ==================================================================
    private class PanneauInfo extends JPanel {
        private JLabel lblTour = info("Tour : 0");
        private JLabel lblSable = info("Sable : 0 / 43");
        private JLabel lblTempete = info("Tempete : 0.0 / 7");
        private JLabel lblEtat = info("EN COURS");

        PanneauInfo() {
            setLayout(new GridLayout(1, 4, 0, 0));
            setBackground(GOLD);
            setBorder(BorderFactory.createEmptyBorder(7, 14, 7, 14));
            add(lblTour);
            add(lblSable);
            add(lblTempete);
            add(lblEtat);
        }

        private JLabel info(String t) {
            JLabel l = new JLabel(t, SwingConstants.CENTER);
            l.setFont(new Font("SansSerif", Font.BOLD, 13));
            l.setForeground(Color.WHITE);
            return l;
        }

        void rafraichir() {
            lblTour.setText("Tour : " + desert.getTour());
            int s = desert.getTotalSable();
            lblSable.setText("Sable : " + s + " / 43");
            lblSable.setForeground(s > 35 ? new Color(0xFF4444) : Color.WHITE);
            double t = desert.getNiveauTempete();
            lblTempete.setText(String.format("Tempete : %.1f / 7", t));
            lblTempete.setForeground(t >= 5 ? new Color(0xFF4444) : Color.WHITE);
            lblEtat.setText(desert.etatPartie().replace("_", " "));
        }
    }

    // ==================================================================
    // Statut des joueurs (SUD)
    // ==================================================================
    private class PanneauJoueurs extends JPanel {
        private ArrayList<BlocJoueur> blocs = new ArrayList<>();

        PanneauJoueurs() {
            setBackground(BG_PANEL);
            setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));
            int n = Math.max(1, desert.getJoueurs().size());
            setLayout(new GridLayout(1, n, 8, 0));
            setPreferredSize(new Dimension(0, 80));
            for (Joueur j : desert.getJoueurs()) {
                BlocJoueur b = new BlocJoueur(j, blocs.size());
                blocs.add(b);
                add(b);
            }
        }

        void rafraichir() {
            int courant = desert.getIndexJoueurCourant();
            for (int i = 0; i < blocs.size(); i++)
                blocs.get(i).rafraichir(i == courant);
        }

        class BlocJoueur extends JPanel {
            private Joueur joueur;
            private int idx;
            private JLabel lblNom = new JLabel("", SwingConstants.LEFT);
            private JLabel lblEau = new JLabel("", SwingConstants.LEFT);
            private JLabel lblPiece = new JLabel("", SwingConstants.LEFT);

            BlocJoueur(Joueur j, int idx) {
                this.joueur = j;
                this.idx = idx;
                setOpaque(true);
                setBackground(BG_MID);
                setBorder(BorderFactory.createLineBorder(TEXT_DIM, 1));
                setLayout(new GridLayout(3, 1, 0, 1));
                setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(TEXT_DIM, 1),
                        BorderFactory.createEmptyBorder(4, 8, 4, 8)));

                Color jc = idx < JOUEUR_COLORS.length ? JOUEUR_COLORS[idx] : Color.WHITE;
                lblNom.setFont(new Font("SansSerif", Font.BOLD, 12));
                lblNom.setForeground(jc);
                lblEau.setFont(new Font("SansSerif", Font.PLAIN, 11));
                lblEau.setForeground(new Color(0x7EC8E3));
                lblPiece.setFont(new Font("SansSerif", Font.ITALIC, 10));
                lblPiece.setForeground(GOLD_LIGHT);

                add(lblNom);
                add(lblEau);
                add(lblPiece);
                rafraichir(false);
            }

            void rafraichir(boolean actif) {
                Color jc = idx < JOUEUR_COLORS.length ? JOUEUR_COLORS[idx] : Color.WHITE;
                String prefix = actif ? ">> " : "   ";
                lblNom.setText(prefix + joueur.getNom());
                lblNom.setForeground(actif ? jc.brighter() : jc);

                int eau = joueur.getNiveauEau();
                StringBuilder sb = new StringBuilder("Eau: ");
                for (int i = 0; i < 5; i++)
                    sb.append(i < eau ? "o" : "_");
                lblEau.setText(sb.toString());

                ArrayList<String> pieces = joueur.getPieces();
                lblPiece.setText(pieces.isEmpty()
                        ? "Aucune piece"
                        : "[" + String.join(", ", pieces) + "]");

                setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(actif ? jc : TEXT_DIM, actif ? 2 : 1),
                        BorderFactory.createEmptyBorder(4, 8, 4, 8)));
            }
        }
    }

    // ==================================================================
    // Journal de log (EST-haut)
    // ==================================================================
    private class PanneauLog extends JPanel {
        private DefaultListModel<String> modele = new DefaultListModel<>();
        private JList<String> liste = new JList<>(modele);

        PanneauLog() {
            setLayout(new BorderLayout());
            setBackground(BG_PANEL);

            JLabel titre = new JLabel("Journal", SwingConstants.CENTER);
            titre.setForeground(GOLD_LIGHT);
            titre.setFont(new Font("SansSerif", Font.BOLD, 12));
            titre.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
            add(titre, BorderLayout.NORTH);

            liste.setBackground(new Color(0x110C05));
            liste.setForeground(TEXT_MAIN);
            liste.setFont(new Font("Monospaced", Font.PLAIN, 11));
            liste.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

            JScrollPane scroll = new JScrollPane(liste);
            scroll.setBorder(BorderFactory.createEmptyBorder());
            scroll.getViewport().setBackground(new Color(0x110C05));
            add(scroll, BorderLayout.CENTER);
        }

        void ajouterLog(String msg) {
            modele.add(0, "-> " + msg);
            if (modele.size() > 60)
                modele.remove(modele.size() - 1);
        }
    }

    // ==================================================================
    // Panneau des boutons d'action (EST-bas)
    // ==================================================================
    private class PanneauActions extends JPanel {
        private JButton btnHaut = btn("Haut");
        private JButton btnBas = btn("Bas");
        private JButton btnGauche = btn("Gauche");
        private JButton btnDroite = btn("Droite");
        private JButton btnFouille = btn("Fouiller");
        private JButton btnDeblaye = btn("Deblayer");
        private JButton btnRamasse = btn("Ramasser");
        private JButton btnDonner = btn("Donner eau");
        private JButton btnFin = btn("Fin de tour");
        private JLabel lblTour = new JLabel("", SwingConstants.CENTER);
        private JLabel lblActions = new JLabel("", SwingConstants.CENTER);

        PanneauActions() {
            setBackground(BG_PANEL);
            setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));
            setLayout(new GridBagLayout());
            GridBagConstraints c = new GridBagConstraints();
            c.fill = GridBagConstraints.HORIZONTAL;
            c.insets = new Insets(2, 2, 2, 2);
            c.weightx = 1.0;

            lblTour.setForeground(GOLD_LIGHT);
            lblTour.setFont(new Font("SansSerif", Font.BOLD, 12));
            lblActions.setForeground(TEXT_MAIN);
            lblActions.setFont(new Font("SansSerif", Font.PLAIN, 11));

            c.gridx = 0;
            c.gridy = 0;
            c.gridwidth = 2;
            add(lblTour, c);
            c.gridy = 1;
            add(lblActions, c);
            c.gridwidth = 1;

            // Croix directionnelle
            c.gridy = 2;
            c.gridx = 0;
            c.gridwidth = 2;
            add(btnHaut, c);
            c.gridwidth = 1;
            c.gridy = 3;
            c.gridx = 0;
            add(btnGauche, c);
            c.gridx = 1;
            add(btnDroite, c);
            c.gridy = 4;
            c.gridx = 0;
            c.gridwidth = 2;
            add(btnBas, c);
            c.gridwidth = 1;

            // Séparateur
            c.gridy = 5;
            c.gridx = 0;
            c.gridwidth = 2;
            add(Box.createVerticalStrut(4), c);
            c.gridwidth = 1;

            // Actions
            c.gridy = 6;
            c.gridx = 0;
            add(btnFouille, c);
            c.gridx = 1;
            add(btnDeblaye, c);

            c.gridy = 7;
            c.gridx = 0;
            c.gridwidth = 2;
            add(btnRamasse, c);

            c.gridy = 8;
            c.gridx = 0;
            c.gridwidth = 2;
            btnDonner.setBackground(new Color(0x1A4A6A));
            add(btnDonner, c);

            // Fin de tour (mis en valeur)
            c.gridy = 9;
            btnFin.setBackground(new Color(0x7A2A0A));
            btnFin.setForeground(GOLD_LIGHT);
            btnFin.setFont(new Font("SansSerif", Font.BOLD, 12));
            add(btnFin, c);

            rafraichir();
        }

        private JButton btn(String texte) {
            JButton b = new JButton(texte);
            b.setBackground(BG_MID);
            b.setForeground(TEXT_MAIN);
            b.setFont(new Font("SansSerif", Font.PLAIN, 11));
            b.setFocusPainted(false);
            b.setBorder(BorderFactory.createLineBorder(GOLD, 1));
            return b;
        }

        void brancherControleur(Controleur c) {
            btnHaut.addActionListener(e -> c.actionDeplacer(Desert.Direction.HAUT));
            btnBas.addActionListener(e -> c.actionDeplacer(Desert.Direction.BAS));
            btnGauche.addActionListener(e -> c.actionDeplacer(Desert.Direction.GAUCHE));
            btnDroite.addActionListener(e -> c.actionDeplacer(Desert.Direction.DROITE));
            btnFouille.addActionListener(e -> c.actionFouiller());
            btnDeblaye.addActionListener(e -> c.actionDeblayer());
            btnRamasse.addActionListener(e -> c.actionRamasser());
            btnDonner.addActionListener(e -> {
                // Donner eau au prochain joueur dans la liste (simplification UI)
                int nb = desert.getJoueurs().size();
                if (nb > 1) {
                    int cible = (desert.getIndexJoueurCourant() + 1) % nb;
                    c.actionDonnerEau(cible);
                }
            });
            btnFin.addActionListener(e -> c.actionFinDeTour());
        }

        void rafraichir() {
            if (!desert.getJoueurs().isEmpty()) {
                Joueur j = desert.getJoueurCourant();
                lblTour.setText("Tour de : " + j.getNom());
            }
            int actions = desert.getActionsRestantes();
            lblActions.setText("Actions restantes : " + actions);
            boolean peutAgir = actions > 0;
            btnHaut.setEnabled(peutAgir);
            btnBas.setEnabled(peutAgir);
            btnGauche.setEnabled(peutAgir);
            btnDroite.setEnabled(peutAgir);
            btnFouille.setEnabled(peutAgir);
            btnDeblaye.setEnabled(peutAgir);
            btnRamasse.setEnabled(peutAgir);
            // donnerEau ne coute pas d action, toujours accessible
            btnDonner.setEnabled(desert.getJoueurs().size() > 1);
        }
    }

    // ==================================================================
    // Affichage des pieces (OUEST)
    // ==================================================================
    private class PanneauPieces extends JPanel {
        private ArrayList<LignePiece> lignes = new ArrayList<>();

        PanneauPieces() {
            setBackground(BG_PANEL);
            setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));
            setPreferredSize(new Dimension(200, 0));
            setLayout(new GridLayout(0, 1, 4, 4));

            for (Piece p : Desert.pieces) {
                LignePiece l = new LignePiece(p);
                lignes.add(l);
                add(l);
            }
        }

        void rafraichir() {
            for (LignePiece l : lignes) {
                l.rafraichir();
            }
        }

        // --------------------------------------------------------------
        class LignePiece extends JPanel {
            private Piece piece;
            private JLabel lblNom = new JLabel();
            private JLabel lblPos = new JLabel();

            LignePiece(Piece p) {
                this.piece = p;

                setLayout(new GridLayout(2, 1));
                setBackground(BG_MID);
                setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(TEXT_DIM, 1),
                        BorderFactory.createEmptyBorder(4, 6, 4, 6)));

                lblNom.setFont(new Font("SansSerif", Font.BOLD, 11));
                lblNom.setForeground(GOLD_LIGHT);

                lblPos.setFont(new Font("SansSerif", Font.PLAIN, 10));
                lblPos.setForeground(TEXT_MAIN);

                add(lblNom);
                add(lblPos);

                rafraichir();
            }

            void rafraichir() {
                lblNom.setText(piece.getPiece());

                if (piece.estRecuperee()) {
                    lblPos.setText("✔ Recuperee");
                    setBorder(BorderFactory.createLineBorder(new Color(0x4CAF50), 2));

                } else if (piece.hasXIndice() && piece.hasYIndice()) {

                    lblPos.setText("Position : ("
                            + piece.getX() + "," + piece.getY() + ")");

                } else {

                    String x = piece.hasXIndice() ? String.valueOf(piece.getX()) : "?";
                    String y = piece.hasYIndice() ? String.valueOf(piece.getY()) : "?";
                    lblPos.setText("Position : (" + x + ", " + y + ")");
                }
            }
        }
    }
}
