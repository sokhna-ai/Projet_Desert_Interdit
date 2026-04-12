import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.util.ArrayList;

/**
 * Panneau qui affiche la grille 5×5 du désert.
 * Chaque case est dessinée via paintComponent pour un rendu précis :
 *  - fond coloré selon le type de zone
 *  - indicateur de sable en haut à droite
 *  - label de type en haut au centre
 *  - joueurs représentés par de gros cercles colorés au centre
 */
class Grille extends JPanel {

    // Palette couleurs par type de zone
    static final Color C_NORMALE   = new Color(0xDBA84C); // sable doré
    static final Color C_OEIL      = new Color(0x7A0000); // rouge sombre
    static final Color C_OASIS     = new Color(0x2D7D46); // vert
    static final Color C_TUNNEL    = new Color(0x5A4020); // brun
    static final Color C_CRASH     = new Color(0xAA2222); // rouge crash
    static final Color C_PISTE     = new Color(0x6688AA); // bleu-gris
    static final Color C_LIGNE     = new Color(0xC8873A); // orange ligne
    static final Color C_COLONNE   = new Color(0xB8773A); // orange colonne
    static final Color C_BLOQUE    = new Color(0x2A1F0E); // sable bloqué

    // Couleurs distinctives pour chaque joueur (cercles)
    static final Color[] JOUEUR_COLORS = {
        new Color(0x3BAEFF), // Bleu vif – Joueur 0
        new Color(0xFF4D4D), // Rouge vif – Joueur 1
        new Color(0x4DFF91), // Vert vif  – Joueur 2
        new Color(0xFFDD00)  // Jaune vif – Joueur 3
    };
    static final String[] JOUEUR_INITIALES = {"A", "B", "C", "D"};

    private Desert desert;
    private ArrayList<ArrayList<CaseUI>> cases = new ArrayList<>();

    Grille(Desert desert) {
        this.desert = desert;
        setLayout(new GridLayout(5, 5, 4, 4));
        setBackground(new Color(0x1A1008));
        setBorder(BorderFactory.createLineBorder(new Color(0x8B6914), 3));
        construireGrille();
    }

    void rafraichir() {
        removeAll();
        cases.clear();
        construireGrille();
        revalidate();
        repaint();
    }

    private void construireGrille() {
        for (int j = 0; j < 5; j++) {
            ArrayList<CaseUI> ligne = new ArrayList<>();
            for (int i = 0; i < 5; i++) {
                Zone z = desert.getZone(i, j);
                CaseUI c = new CaseUI(z, i, j);
                add(c);
                ligne.add(c);
            }
            cases.add(ligne);
        }
    }

    // ---------------------------------------------------------------
    // CaseUI : chaque case de la grille dessinée à la main
    // ---------------------------------------------------------------
    class CaseUI extends JPanel {

        private final Zone zone;
        private final int x, y;

        CaseUI(Zone zone, int x, int y) {
            this.zone = zone;
            this.x = x;
            this.y = y;
            setPreferredSize(new Dimension(100, 100));
            setOpaque(false); // on gère tout dans paintComponent
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            int w = getWidth();
            int h = getHeight();

            // ── 1. Fond de la case ──────────────────────────────────
            Color fond = choisirCouleur();
            g2.setColor(fond);
            g2.fillRoundRect(0, 0, w, h, 10, 10);

            // Légère vignette sur les bords
            g2.setColor(new Color(0, 0, 0, 60));
            g2.setStroke(new BasicStroke(3f));
            g2.drawRoundRect(1, 1, w - 2, h - 2, 10, 10);

            // ── 2. Label type de case (haut-centre) ─────────────────
            String labelType = nomCase();
            g2.setColor(new Color(255, 255, 255, 220));
            g2.setFont(new Font("SansSerif", Font.BOLD, 10));
            FontMetrics fm = g2.getFontMetrics();
            int lw = fm.stringWidth(labelType);
            g2.drawString(labelType, (w - lw) / 2, 14);

            // ── 3. Indicateur sable (haut-droite) ────────────────────
            int sable = zone.getnbSable();
            if (sable > 0) {
                boolean bloque = zone.estBloquee() && !(zone instanceof Oeil_Tempete);
                Color cs = bloque ? new Color(0xFF4444) : new Color(0xFFCC66);
                g2.setColor(cs);
                g2.setFont(new Font("SansSerif", Font.BOLD, 9));
                String ts = bloque ? "▓▓" : "▒" + sable;
                g2.drawString(ts, w - fm.stringWidth(ts) - 4, 12);
            }

            // ── 4. Joueurs sur cette case (cercles colorés) ──────────
            ArrayList<Joueur> joueurs = desert.getJoueurs();
            ArrayList<Integer> ici = new ArrayList<>();
            for (int i = 0; i < joueurs.size(); i++) {
                Joueur j = joueurs.get(i);
                if (j.getX() == x && j.getY() == y) ici.add(i);
            }

            if (!ici.isEmpty()) {
                // Disposition selon le nombre de joueurs sur la case
                int n = ici.size();
                int r = Math.min(22, (w - 20) / (2 * n + 1)); // rayon du cercle
                r = Math.max(r, 12);
                int totalW = n * (2 * r) + (n - 1) * 6;
                int startX = (w - totalW) / 2;
                int cy = h / 2 + 6;

                for (int k = 0; k < n; k++) {
                    int idx = ici.get(k);
                    Color c = idx < JOUEUR_COLORS.length ? JOUEUR_COLORS[idx] : Color.WHITE;
                    int cx2 = startX + k * (2 * r + 6) + r;

                    // Ombre du cercle
                    g2.setColor(new Color(0, 0, 0, 100));
                    g2.fillOval(cx2 - r + 2, cy - r + 2, 2 * r, 2 * r);

                    // Remplissage du cercle
                    g2.setColor(c);
                    g2.fillOval(cx2 - r, cy - r, 2 * r, 2 * r);

                    // Contour blanc
                    g2.setColor(Color.WHITE);
                    g2.setStroke(new BasicStroke(1.5f));
                    g2.drawOval(cx2 - r, cy - r, 2 * r, 2 * r);

                    // Initiale du joueur (A, B, C, D)
                    String ini = idx < JOUEUR_INITIALES.length ? JOUEUR_INITIALES[idx] : "?";
                    g2.setFont(new Font("SansSerif", Font.BOLD, Math.max(8, r - 4)));
                    FontMetrics fm2 = g2.getFontMetrics();
                    g2.setColor(new Color(20, 20, 20));
                    g2.drawString(ini, cx2 - fm2.stringWidth(ini) / 2,
                            cy + fm2.getAscent() / 2 - 1);

                    // Indicateur "joueur courant" : étoile/halo
                    if (idx == desert.getIndexJoueurCourant()) {
                        g2.setColor(new Color(255, 255, 100, 180));
                        g2.setStroke(new BasicStroke(2.5f));
                        g2.drawOval(cx2 - r - 3, cy - r - 3, 2 * r + 6, 2 * r + 6);
                    }
                }
            }

            // ── 5. Icône explorée en bas ─────────────────────────────
            if (zone.getExploree() && !(zone instanceof Oeil_Tempete)) {
                g2.setFont(new Font("SansSerif", Font.PLAIN, 8));
                g2.setColor(new Color(255, 255, 255, 140));
                String e = "✔ exploré";
                FontMetrics fme = g2.getFontMetrics();
                g2.drawString(e, (w - fme.stringWidth(e)) / 2, h - 4);
            }

            g2.dispose();
        }

        /** Couleur de fond selon type de zone et niveau de sable. */
        private Color choisirCouleur() {
            if (zone instanceof Oeil_Tempete) return C_OEIL;
            if (zone.estBloquee())            return C_BLOQUE;
            if (zone instanceof Oasis)        return C_OASIS;
            if (zone instanceof Tunnel)       return C_TUNNEL;
            if (zone instanceof Crash_Helicoptere) return C_CRASH;
            if (zone instanceof Piste_Vol)    return C_PISTE;
            if (zone instanceof Zone_Ligne)   return C_LIGNE;
            if (zone instanceof Zone_Colonne) return C_COLONNE;
            return C_NORMALE;
        }

        /** Texte descriptif court de la case. */
        private String nomCase() {
            if (zone instanceof Oeil_Tempete)      return "Oeil Tempete";
            if (zone instanceof Oasis o) {
                if (!o.getExploree()) return "Oasis";
                return o.getEstMirage() ? "Mirage!" : "Oasis (eau)";
            }
            if (zone instanceof Tunnel)            return "Tunnel";
            if (zone instanceof Crash_Helicoptere) return "Crash Helico";
            if (zone instanceof Piste_Vol)         return "Piste Vol";
            if (zone instanceof Zone_Ligne zl) {
                return zone.getExploree() ? "Ligne=" + zl.getNumeroLigne() : "? Ligne";
            }
            if (zone instanceof Zone_Colonne zc) {
                return zone.getExploree() ? "Col.=" + zc.getNumeroColonne() : "? Col.";
            }
            if (zone instanceof Zone_Normale zn) {
                if (!zn.getpieceCachee().equals("") && zone.getExploree())
                    return zn.getpieceCachee();
                return zone.getExploree() ? "Fouille" : "· · ·";
            }
            return zone.getSymbole();
        }
    }
}
