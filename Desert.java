import java.util.ArrayList;
import java.util.Random;

public class Desert {

    private enum Direction { HAUT, DROITE, BAS, GAUCHE }; // enum pour les directions
    final static int NB_PIECES = 4;
    final static String[] PIECES = {"Moteur", "Hélice", "Gouvernail", "Capteur"};
    final static int PAREFEU = 500;

    private Zone[][] zones;
    private ArrayList<Joueur> joueurs;
    private double niveauTempete;
    private int totalSable;
    private int nbPiecesRamasses;
    private int joueurCourant;
    private int actionsRestantes;
    private int xOeil;
    private int yOeil;

    private Random rand;

    // =========================================================
    //  CONSTRUCTEUR
    // =========================================================
    public Desert() {
        this.joueurs        = new ArrayList<>();
        this.zones          = new Zone[5][5];
        this.rand           = new Random();
        this.niveauTempete  = 0.0;
        this.totalSable     = 0;
        this.nbPiecesRamasses = 0;
        this.joueurCourant  = 0;
        this.actionsRestantes = 4;
        this.xOeil = 2;
        this.yOeil = 2;

        // On relance l'initialisation tant qu'elle échoue (pare-feu inclus)
        while (!(initialiserZones()
                && initialiserPieces()
                && initialiserZonesColonneLigne()
                && initialiserOasis()
                && initialiserTunnel()
                && initialiserPisteEtCrash())) {
            // réinitialisation complète avant de réessayer
            this.zones = new Zone[5][5];
        }
    }

    // =========================================================
    //  GETTERS / SETTERS
    // =========================================================
    public Zone getZone(int x, int y) {
        return this.zones[y][x];
    }

    public double getNiveauTempete() {
        return this.niveauTempete;
    }

    public int getActionsRestantes() {
        return this.actionsRestantes;
    }

    public ArrayList<Joueur> getJoueurs() {
        return this.joueurs;
    }

    // =========================================================
    //  GESTION DES JOUEURS
    // =========================================================
    public boolean ajouterJoueur(Joueur j, int x, int y) {
        if (x < 0 || x > 4 || y < 0 || y > 4) return false;
        j.setX(x);
        j.setY(y);
        joueurs.add(j);
        return true;
    }

    // =========================================================
    //  INITIALISATION DE LA GRILLE
    // =========================================================

    // Étape 1 : remplir toutes les cases avec des Zone_Normale vides + placer l'œil
    private boolean initialiserZones() {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                this.zones[i][j] = new Zone_Normale(j, i, "");
            }
        }
        this.zones[2][2] = new Oeil_Tempête(2, 2);
        return true;
    }

    // Étape 2 : placer les 4 pièces dans des Zone_Normale,
    //           au plus 1 pièce par ligne ET par colonne
    private boolean initialiserPieces() {
        int x, y;
        for (int i = 0; i < NB_PIECES; i++) {
            boolean ok = false;
            int essais = 0;
            while (!ok) {
                if (essais++ > PAREFEU) return false;
                x = rand.nextInt(5);
                y = rand.nextInt(5);
                if (this.zones[y][x] instanceof Zone_Normale
                        && ((Zone_Normale) this.zones[y][x]).getpieceCachee().equals("")
                        && getNbPieceEnLigne(y) < 1
                        && getNbPieceEnColonne(x) < 1) {
                    ((Zone_Normale) this.zones[y][x]).setpieceCachee(PIECES[i]);
                    ok = true;
                }
            }
        }
        return true;
    }

    // Étape 3 : pour chaque pièce, placer une Zone_Colonne sur la même ligne
    //           et une Zone_Ligne sur la même colonne
    private boolean initialiserZonesColonneLigne() {
        int x, y;
        int nbEssais = 0;

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                // zones[i][j] : i = ligne (y), j = colonne (x)
                if (this.zones[i][j] instanceof Zone_Normale
                        && !((Zone_Normale) this.zones[i][j]).getpieceCachee().equals("")) {

                    String piece = ((Zone_Normale) this.zones[i][j]).getpieceCachee();

                    // --- Zone_Colonne : même ligne i, colonne x aléatoire ---
                    boolean ok = false;
                    while (!ok) {
                        if (nbEssais++ > PAREFEU) return false;
                        x = rand.nextInt(5);
                        // la case cible doit être une Zone_Normale vide (pas la pièce elle-même)
                        if (this.zones[i][x] instanceof Zone_Normale
                                && ((Zone_Normale) this.zones[i][x]).getpieceCachee().equals("")
                                && x != j) {
                            this.zones[i][x] = new Zone_Colonne(x, i, j, piece);
                            ok = true;
                        }
                    }

                    // --- Zone_Ligne : même colonne j, ligne y aléatoire ---
                    ok = false;
                    while (!ok) {
                        if (nbEssais++ > PAREFEU) return false;
                        y = rand.nextInt(5);
                        if (this.zones[y][j] instanceof Zone_Normale
                                && ((Zone_Normale) this.zones[y][j]).getpieceCachee().equals("")
                                && y != i) {
                            this.zones[y][j] = new Zone_Ligne(j, y, i, piece);
                            ok = true;
                        }
                    }
                }
            }
        }
        return true;
    }

    // Étape 4 : 2 oasis réelles + 1 mirage
    private boolean initialiserOasis() {
        int x, y;
        // 2 vraies oasis
        for (int i = 0; i < 2; i++) {
            boolean ok = false;
            int essais = 0;
            while (!ok) {
                if (essais++ > PAREFEU) return false;
                x = rand.nextInt(5);
                y = rand.nextInt(5);
                if (this.zones[y][x] instanceof Zone_Normale
                        && ((Zone_Normale) this.zones[y][x]).getpieceCachee().equals("")) {
                    this.zones[y][x] = new Oasis(x, y, false);
                    ok = true;
                }
            }
        }
        // 1 mirage
        boolean ok = false;
        int essais = 0;
        while (!ok) {
            if (essais++ > PAREFEU) return false;
            x = rand.nextInt(5);
            y = rand.nextInt(5);
            if (this.zones[y][x] instanceof Zone_Normale
                    && ((Zone_Normale) this.zones[y][x]).getpieceCachee().equals("")) {
                this.zones[y][x] = new Oasis(x, y, true);
                ok = true;
            }
        }
        return true;
    }

    // Étape 5 : 3 tunnels
    private boolean initialiserTunnel() {
        int x, y;
        for (int i = 0; i < 3; i++) {
            boolean ok = false;
            int essais = 0;
            while (!ok) {
                if (essais++ > PAREFEU) return false;
                x = rand.nextInt(5);
                y = rand.nextInt(5);
                if (this.zones[y][x] instanceof Zone_Normale
                        && ((Zone_Normale) this.zones[y][x]).getpieceCachee().equals("")) {
                    this.zones[y][x] = new Tunnel(x, y);
                    ok = true;
                }
            }
        }
        return true;
    }

    // Étape 6 : 1 piste de vol + 1 crash hélicoptère
    private boolean initialiserPisteEtCrash() {
        int x, y;

        // Piste de vol
        boolean ok = false;
        int essais = 0;
        while (!ok) {
            if (essais++ > PAREFEU) return false;
            x = rand.nextInt(5);
            y = rand.nextInt(5);
            if (this.zones[y][x] instanceof Zone_Normale
                    && ((Zone_Normale) this.zones[y][x]).getpieceCachee().equals("")) {
                this.zones[y][x] = new Piste_Vol(x, y);
                ok = true;
            }
        }

        // Crash hélicoptère
        ok = false;
        essais = 0;
        while (!ok) {
            if (essais++ > PAREFEU) return false;
            x = rand.nextInt(5);
            y = rand.nextInt(5);
            if (this.zones[y][x] instanceof Zone_Normale
                    && ((Zone_Normale) this.zones[y][x]).getpieceCachee().equals("")) {
                this.zones[y][x] = new Crash_Helicoptere(x, y);
                ok = true;
            }
        }
        return true;
    }

    // =========================================================
    //  HELPERS COMPTAGE
    // =========================================================
    private int getNbPieceEnLigne(int ligne) {
        int nb = 0;
        for (int i = 0; i < 5; i++) {
            if (this.zones[ligne][i] instanceof Zone_Normale
                    && !((Zone_Normale) this.zones[ligne][i]).getpieceCachee().equals("")) {
                nb++;
            }
        }
        return nb;
    }

    private int getNbPieceEnColonne(int col) {
        int nb = 0;
        for (int i = 0; i < 5; i++) {
            if (this.zones[i][col] instanceof Zone_Normale
                    && !((Zone_Normale) this.zones[i][col]).getpieceCachee().equals("")) {
                nb++;
            }
        }
        return nb;
    }

    // =========================================================
    //  SABLE
    // =========================================================
    public int totalSable() {
        int total = 0;
        for (int i = 0; i < 5; i++)
            for (int j = 0; j < 5; j++)
                total += this.zones[i][j].getnbSable();
        return total;
    }

    /** Retire 1 grain de sable sur la case (x, y) si possible. */
    public boolean deblayer(int x, int y) {
        Zone z = getZone(x, y);
        if (z.getnbSable() > 0) {
            z.setNbSable(z.getnbSable() - 1);
            this.totalSable--;
            return true;
        }
        return false;
    }

    /** Donne de l'eau à tous les joueurs présents sur la case (x, y). */
    public void distribuerEau(int x, int y, int quantite) {
        for (Joueur j : joueurs) {
            if (j.getX() == x && j.getY() == y) {
                int nvEau = Math.min(j.getNiveauEau() + quantite, 5); // max 5 selon le sujet
                j.setNiveauEau(nvEau);
            }
        }
    }

    // =========================================================
    //  TEMPÊTE
    // =========================================================
    private void soufflerVent() {
        int tirage    = rand.nextInt(4);
        int distance  = rand.nextInt(2) + 1;
        Direction dir = Direction.values()[tirage];
        Zone zone;

        switch (dir) {
            case HAUT:
                for (int i = 0; i < distance; i++) {
                    if (this.yOeil != 0) {
                        this.yOeil--;
                        zone = getZone(this.xOeil, this.yOeil);
                        if (zone.getnbSable() < 2) {
                            zone.setNbSable(zone.getnbSable() + 1);
                            this.totalSable++;
                        }
                    }
                }
                break;
            case BAS:
                for (int i = 0; i < distance; i++) {
                    if (this.yOeil != 4) {
                        this.yOeil++;
                        zone = getZone(this.xOeil, this.yOeil);
                        if (zone.getnbSable() < 2) {
                            zone.setNbSable(zone.getnbSable() + 1);
                            this.totalSable++;
                        }
                    }
                }
                break;
            case DROITE:
                for (int i = 0; i < distance; i++) {
                    if (this.xOeil != 4) {
                        this.xOeil++;
                        zone = getZone(this.xOeil, this.yOeil);
                        if (zone.getnbSable() < 2) {
                            zone.setNbSable(zone.getnbSable() + 1);
                            this.totalSable++;
                        }
                    }
                }
                break;
            case GAUCHE:
                for (int i = 0; i < distance; i++) {
                    if (this.xOeil != 0) {
                        this.xOeil--;
                        zone = getZone(this.xOeil, this.yOeil);
                        if (zone.getnbSable() < 2) {
                            zone.setNbSable(zone.getnbSable() + 1);
                            this.totalSable++;
                        }
                    }
                }
                break;
        }
    }

    /** Vague de chaleur : chaque joueur perd 1 unité d'eau. */
    private void vagueDeChaleur() {
        for (Joueur j : joueurs) {
            j.setNiveauEau(Math.max(0, j.getNiveauEau() - 1));
        }
        System.out.println("Vague de chaleur ! Chaque joueur perd 1 eau.");
    }

    /** La tempête se déchaîne : le niveau monte de 0.5. */
    private void tempeteSeDechaine() {
        this.niveauTempete += 0.5;
        System.out.println("La tempête se déchaîne ! Niveau tempête : " + this.niveauTempete);
    }

    // =========================================================
    //  FIN DE TOUR
    // =========================================================
    private void finDeTour() {
        // Pioche une carte tempête selon le niveau
        int tirage = rand.nextInt(6); // simplifié : à adapter avec un vrai deck
        if (tirage < 2) {
            soufflerVent();
        } else if (tirage < 4) {
            vagueDeChaleur();
        } else {
            tempeteSeDechaine();
        }
        // Passage au joueur suivant
        joueurCourant = (joueurCourant + 1) % joueurs.size();
        actionsRestantes = 4;
    }

    // =========================================================
    //  CONDITIONS DE FIN
    // =========================================================
    private boolean estVicoire() {
        if (nbPiecesRamasses < NB_PIECES) return false;
        // Tous les joueurs doivent être sur la piste de vol
        for (Joueur j : joueurs) {
            Zone z = getZone(j.getX(), j.getY());
            if (!(z instanceof Piste_Vol)) return false;
        }
        return true;
    }

    private boolean estEnsable() {
        // Défaite si le total de sable dépasse 43 (règle officielle du sujet)
        return totalSable() > 43;
    }

    private boolean estDefaite() {
        // Défaite si un joueur n'a plus d'eau
        for (Joueur j : joueurs) {
            if (j.getNiveauEau() <= 0) {
                System.out.println(j.getNom() + " est mort de soif !");
                return true;
            }
        }
        // Défaite si trop de sable (> 43)
        if (estEnsable()) {
            System.out.println("Le désert est trop ensablé !");
            return true;
        }
        // Défaite si niveau tempête >= 7
        if (this.niveauTempete >= 7) {
            System.out.println("La tempête est trop puissante !");
            return true;
        }
        return false;
    }

    // =========================================================
    //  ACTIONS D'UN JOUEUR
    // =========================================================

    /** Déplace le joueur courant dans une direction si la case n'est pas bloquée. */
    public boolean deplacerJoueur(Direction dir) {
        Joueur j = joueurs.get(joueurCourant);
        int nx = j.getX();
        int ny = j.getY();

        switch (dir) {
            case HAUT:   ny--; break;
            case BAS:    ny++; break;
            case GAUCHE: nx--; break;
            case DROITE: nx++; break;
        }

        if (nx < 0 || nx > 4 || ny < 0 || ny > 4) {
            System.out.println("Déplacement impossible : hors de la grille.");
            return false;
        }
        Zone cible = getZone(nx, ny);
        if (cible.estBloquee()) {
            System.out.println("Déplacement impossible : case bloquée par le sable.");
            return false;
        }
        j.setX(nx);
        j.setY(ny);
        actionsRestantes--;
        System.out.println(j.getNom() + " se déplace en (" + nx + "," + ny + ").");
        return true;
    }

    /** Le joueur courant fouille la case sur laquelle il se trouve. */
    public void fouillerCase() {
        Joueur j = joueurs.get(joueurCourant);
        Zone z = getZone(j.getX(), j.getY());

        if (!z.getExploree()) {
            z.setExploree(true);
            z.actionSpeciale(this);
        } else {
            System.out.println("Cette case a déjà été fouillée.");
        }
        actionsRestantes--;
    }

    /** Le joueur courant déblaye 1 grain de sable sur sa case. */
    public void deblayerCase() {
        Joueur j = joueurs.get(joueurCourant);
        deblayer(j.getX(), j.getY());
        actionsRestantes--;
        System.out.println(j.getNom() + " déblaye sa case.");
    }

    // =========================================================
    //  AFFICHAGE
    // =========================================================
    public void afficherGrille() {
        System.out.println("  0  1  2  3  4");
        for (int i = 0; i < 5; i++) {
            System.out.print(i + " ");
            for (int j = 0; j < 5; j++) {
                Zone z = this.zones[i][j];
                String s = z.getSymbole();
                // Marquer les joueurs présents
                boolean joueurIci = false;
                for (Joueur jj : joueurs) {
                    if (jj.getX() == j && jj.getY() == i) { joueurIci = true; break; }
                }
                if (joueurIci) s = "J";
                // Ajouter le nombre de sable
                System.out.print("[" + s + z.getnbSable() + "]");
            }
            System.out.println();
        }
        System.out.println("Sable total : " + totalSable()
                + " | Niveau tempête : " + this.niveauTempete
                + " | Actions restantes : " + actionsRestantes);
    }
}
