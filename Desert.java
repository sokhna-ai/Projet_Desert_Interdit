import java.util.ArrayList;
import java.util.Random;

public class Desert {

    public static enum Direction { HAUT, DROITE, BAS, GAUCHE } // public pour le Controleur

    final static int NB_PIECES = 4;
    public final static String[] PIECES = {"Moteur", "Hélice", "Gouvernail", "Capteur"};
    final static int PAREFEU = 500;

    private Zone[][] zones;
    private ArrayList<Joueur> joueurs;
    private double niveauTempete;
    private int totalSable;
    private int tour;
    private int nbPiecesRamasses;
    private int joueurCourant;
    private int actionsRestantes;
    private int xOeil;
    private int yOeil;
    private int xPiste;
    private int yPiste;
    private int xCrash;
    private int yCrash;

    private Random rand;

    // -----------------------------------------------------------------
    //  CONSTRUCTEUR
    // -----------------------------------------------------------------
    public Desert() {
        this.joueurs        = new ArrayList<>();
        this.zones          = new Zone[5][5];
        this.rand           = new Random();
        this.niveauTempete  = 2.0;
        this.totalSable     = 0;
        this.nbPiecesRamasses = 0;
        this.joueurCourant  = 0;
        this.actionsRestantes = 4;
        this.xOeil = 2;
        this.yOeil = 2;
        this.xPiste = -1;
        this.yPiste = -1;
        this.xCrash = -1;
        this.yCrash = -1;

        while (!(initialiserZones()
                && initialiserPieces()
                && initialiserZonesColonneLigne()
                && initialiserOasis()
                && initialiserTunnel()
                && initialiserPisteEtCrash())) {
            this.zones = new Zone[5][5];
        }
    }

    // -----------------------------------------------------------------
    //  GETTERS / SETTERS
    // -----------------------------------------------------------------
    /**
     * Retourne la zone aux coordonnées (x, y).
     */
    public Zone getZone(int x, int y) {
        return this.zones[y][x];
    }

    /**
     * Retourne le niveau de tempête actuel.
     */
    public double getNiveauTempete() {
        return this.niveauTempete;
    }

    /**
     * Retourne le nombre d'actions restantes pour le joueur courant.
     */
    public int getActionsRestantes() {
        return this.actionsRestantes;
    }

    /**
     * Retourne la liste de tous les joueurs.
     */
    public ArrayList<Joueur> getJoueurs() {
        return this.joueurs;
    }

    /**
     * Retourne le joueur dont c'est le tour.
     */
    public Joueur getJoueurCourant() {
        return this.joueurs.get(joueurCourant);
    }

    /**
     * Retourne l'index du joueur courant.
     */
    public int getIndexJoueurCourant() {
        return this.joueurCourant;
    }

    /**
     * Retourne la liste de tous les tunnels de la grille.
     */
    public ArrayList<Tunnel> getTunnels() {
        ArrayList<Tunnel> tunnels = new ArrayList<>();
        for (int i = 0; i < 5; i++)
            for (int j = 0; j < 5; j++)
                if (this.zones[i][j] instanceof Tunnel)
                    tunnels.add((Tunnel) this.zones[i][j]);
        return tunnels;
    }

    /**
     * Retourne le nombre total de sable de la grille.
     */
    public int getTotalSable() {
        return this.totalSable();
    }
    /**
     * Retourne le nombre de tours passés depuis le commencement de la partie.
     */
    public int getTour() {
        return this.tour;
    }


    /**
     * Retourne la coordonnée X de la zone Crash_Helicoptere.
     */
    public int getXOeil() { return this.xOeil; }
    public int getYOeil() { return this.yOeil; }
    public int getXCrash() { return this.xCrash; }

    /**
     * Retourne la coordonnée Y de la zone Crash_Helicoptere.
     */
    public int getYCrash() { return this.yCrash; }
    // -----------------------------------------------------------------
    //  GESTION DES JOUEURS
    // -----------------------------------------------------------------
    /**
     * Ajoute un joueur sur la grille aux coordonnées (x, y).
     */
    public boolean ajouterJoueur(Joueur j, int x, int y) {
        if (x < 0 || x > 4 || y < 0 || y > 4) return false;
        j.setX(x);
        j.setY(y);
        joueurs.add(j);
        return true;
    }

    /**
     * Téléporte le joueur courant vers la case (x, y).
     */
    public void teleporterJoueur(int x, int y) {
        Joueur j = joueurs.get(joueurCourant);
        j.setX(x);
        j.setY(y);
        System.out.println(j.getNom() + " est téléporté en (" + x + "," + y + ").");
    }

    /**
     * Échange de l'eau entre deux joueurs (le donneur donne 1 au receveur).
     */
    public boolean echangerEau(int joueurDonneurIndex, int joueurReceveurIndex) {
        if (joueurDonneurIndex < 0 || joueurDonneurIndex >= joueurs.size()) return false;
        if (joueurReceveurIndex < 0 || joueurReceveurIndex >= joueurs.size()) return false;
        Joueur donneur   = joueurs.get(joueurDonneurIndex);
        Joueur receveur  = joueurs.get(joueurReceveurIndex);
        return donneur.echangerEau(receveur);
    }

    // -----------------------------------------------------------------
    //  INITIALISATION DE LA GRILLE
    // -----------------------------------------------------------------

    private boolean initialiserZones() {
        for (int i = 0; i < 5; i++)
            for (int j = 0; j < 5; j++)
                this.zones[i][j] = new Zone_Normale(j, i, "");
        this.zones[2][2] = new Oeil_Tempete(2, 2);
        return true;
    }

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

    private boolean initialiserZonesColonneLigne() {
        int x, y;
        int nbEssais = 0;
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (this.zones[i][j] instanceof Zone_Normale
                        && !((Zone_Normale) this.zones[i][j]).getpieceCachee().equals("")) {
                    String piece = ((Zone_Normale) this.zones[i][j]).getpieceCachee();
                    boolean ok = false;
                    while (!ok) {
                        if (nbEssais++ > PAREFEU) return false;
                        x = rand.nextInt(5);
                        if (this.zones[i][x] instanceof Zone_Normale
                                && ((Zone_Normale) this.zones[i][x]).getpieceCachee().equals("")
                                && x != j) {
                            this.zones[i][x] = new Zone_Colonne(x, i, j, piece);
                            ok = true;
                        }
                    }
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

    private boolean initialiserOasis() {
        int x, y;
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

    private boolean initialiserPisteEtCrash() {
        int x, y;
        boolean ok = false;
        int essais = 0;
        while (!ok) {
            if (essais++ > PAREFEU) return false;
            x = rand.nextInt(5);
            y = rand.nextInt(5);
            if (this.zones[y][x] instanceof Zone_Normale
                    && ((Zone_Normale) this.zones[y][x]).getpieceCachee().equals("")) {
                this.zones[y][x] = new Piste_Vol(x, y);
                this.xPiste = x;
                this.yPiste = y;
                ok = true;
            }
        }
        ok = false;
        essais = 0;
        while (!ok) {
            if (essais++ > PAREFEU) return false;
            x = rand.nextInt(5);
            y = rand.nextInt(5);
            if (this.zones[y][x] instanceof Zone_Normale
                    && ((Zone_Normale) this.zones[y][x]).getpieceCachee().equals("")) {
                this.zones[y][x] = new Crash_Helicoptere(x, y);
                this.xCrash = x;
                this.yCrash = y;
                ok = true;
            }
        }
        return true;
    }

    // -----------------------------------------------------------------
    //  HELPERS COMPTAGE
    // -----------------------------------------------------------------
    private int getNbPieceEnLigne(int ligne) {
        int nb = 0;
        for (int i = 0; i < 5; i++)
            if (this.zones[ligne][i] instanceof Zone_Normale
                    && !((Zone_Normale) this.zones[ligne][i]).getpieceCachee().equals(""))
                nb++;
        return nb;
    }

    private int getNbPieceEnColonne(int col) {
        int nb = 0;
        for (int i = 0; i < 5; i++)
            if (this.zones[i][col] instanceof Zone_Normale
                    && !((Zone_Normale) this.zones[i][col]).getpieceCachee().equals(""))
                nb++;
        return nb;
    }

    // -----------------------------------------------------------------
    //  SABLE
    // -----------------------------------------------------------------
    /**
     * Retourne le total de grains de sable sur toute la grille.
     */
    public int totalSable() {
        int total = 0;
        for (int i = 0; i < 5; i++)
            for (int j = 0; j < 5; j++)
                total += this.zones[i][j].getnbSable();
        return total;
    }

    /**
     * Retire 1 grain de sable sur la case (x, y) si possible.
     */
    public boolean deblayer(int x, int y) {
        Zone z = getZone(x, y);
        if (z.getnbSable() > 0) {
            z.setNbSable(z.getnbSable() - 1);
            this.totalSable--;
            return true;
        }
        return false;
    }

    /**
     * Donne de l'eau à tous les joueurs présents sur la case (x, y).
     */
    public void distribuerEau(int x, int y, int quantite) {
        for (Joueur j : joueurs) {
            if (j.getX() == x && j.getY() == y) {
                int nvEau = Math.min(j.getNiveauEau() + quantite, 5);
                j.setNiveauEau(nvEau);
            }
        }
    }

    // -----------------------------------------------------------------
    //  TEMPÊTE
    // -----------------------------------------------------------------
    private void soufflerVent() {
        int tirage   = rand.nextInt(4);
        int distance = rand.nextInt(2) + 1;
        Direction dir = Direction.values()[tirage];
        Zone zone;

        switch (dir) {
            case HAUT:
                for (int i = 0; i < distance; i++) {
                    if (this.yOeil != 0) {
                        this.yOeil--;
                        zone = getZone(this.xOeil, this.yOeil);
                        zone.setNbSable(zone.getnbSable() + 1); this.totalSable++;
                    }
                }
                break;
            case BAS:
                for (int i = 0; i < distance; i++) {
                    if (this.yOeil != 4) {
                        this.yOeil++;
                        zone = getZone(this.xOeil, this.yOeil);
                        zone.setNbSable(zone.getnbSable() + 1); this.totalSable++;
                    }
                }
                break;
            case DROITE:
                for (int i = 0; i < distance; i++) {
                    if (this.xOeil != 4) {
                        this.xOeil++;
                        zone = getZone(this.xOeil, this.yOeil);
                        zone.setNbSable(zone.getnbSable() + 1); this.totalSable++;
                    }
                }
                break;
            case GAUCHE:
                for (int i = 0; i < distance; i++) {
                    if (this.xOeil != 0) {
                        this.xOeil--;
                        zone = getZone(this.xOeil, this.yOeil);
                        zone.setNbSable(zone.getnbSable() + 1); this.totalSable++;
                    }
                }
                break;
        }
    }

    private void vagueDeChaleur() {
        for (Joueur j : joueurs) {
            Zone zj = getZone(j.getX(), j.getY());
            if (zj instanceof Tunnel) {
                System.out.println(j.getNom() + " est dans un tunnel, protege de la chaleur.");
            } else {
                j.setNiveauEau(Math.max(0, j.getNiveauEau() - 1));
            }
        }
        System.out.println("Vague de chaleur ! Les joueurs hors tunnel perdent 1 eau.");
    }

    private void tempeteSeDechaine() {
        this.niveauTempete += 0.5;
        System.out.println("La tempête se déchaîne ! Niveau tempête : " + this.niveauTempete);
    }

    // -----------------------------------------------------------------
    //  FIN DE TOUR
    // -----------------------------------------------------------------
    /**
     * Passe au joueur suivant et déclenche un événement tempête.
     */
    public void finDeTour() {
        // +0.5 garanti à chaque fin de tour
        // puis events aléatoires : vent ou chaleur, nombre = floor(niveauTempete), min 2
        tempeteSeDechaine();
        int nbActions = Math.max(2, (int) this.niveauTempete);
        for (int a = 0; a < nbActions; a++) {
            int tirage = rand.nextInt(6);
            if (tirage < 2) soufflerVent();
            else            vagueDeChaleur();
        }
        tour++;
        joueurCourant = (joueurCourant + 1) % joueurs.size();
        actionsRestantes = 4;
    }

    // -----------------------------------------------------------------
    //  CONDITIONS DE FIN
    // -----------------------------------------------------------------
    private boolean estVictoire() {
        if (nbPiecesRamasses < NB_PIECES) return false;
        for (Joueur j : joueurs) {
            Zone z = getZone(j.getX(), j.getY());
            if (!(z instanceof Piste_Vol)) return false;
            if (z.estBloquee()) return false; // piste doit être désensablée
        }
        return true;
    }

    /**
     * Retourne l'état actuel de la partie :
     * "VICTOIRE", "DEFAITE_SOIF", "DEFAITE_SABLE", "DEFAITE_TEMPETE" ou "EN_COURS".
     */
    public String etatPartie() {
        if (estVictoire()) return "VICTOIRE";
        for (Joueur j : joueurs)
            if (j.getNiveauEau() <= 0) return "DEFAITE_SOIF";
        if (totalSable() > 43) return "DEFAITE_SABLE";
        if (this.niveauTempete >= 7) return "DEFAITE_TEMPETE";
        return "EN_COURS";
    }

    // -----------------------------------------------------------------
    //  ACTIONS D'UN JOUEUR
    // -----------------------------------------------------------------

    /**
     * Le joueur courant donne 1 unite d eau a un autre joueur sur la meme case.
     * L echange ne coute pas d action (conforme aux regles).
     * @param indexReceveur index du joueur receveur dans la liste
     * @return true si l echange a pu se faire
     */
    public boolean donnerEau(int indexReceveur) {
        if (indexReceveur < 0 || indexReceveur >= joueurs.size()) return false;
        Joueur donneur  = joueurs.get(joueurCourant);
        Joueur receveur = joueurs.get(indexReceveur);
        // Les deux joueurs doivent etre sur la meme case
        if (donneur.getX() != receveur.getX() || donneur.getY() != receveur.getY()) {
            System.out.println("Echange impossible : les joueurs ne sont pas sur la meme case.");
            return false;
        }
        boolean ok = donneur.echangerEau(receveur);
        if (ok) System.out.println(donneur.getNom() + " donne 1 eau a " + receveur.getNom());
        return ok;
        // Pas de actionsRestantes-- : l echange ne coute pas d action
    }

    /**
     * Déplace le joueur courant dans la direction donnée.
     */
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

    /**
     * Le joueur courant fouille la case sur laquelle il se trouve.
     */
    public void fouillerCase() {
        Joueur j = joueurs.get(joueurCourant);
        Zone z = getZone(j.getX(), j.getY());
        if (z.getnbSable() > 0) {
            System.out.println("Impossible de fouiller : il y a du sable sur cette case. Deblaie d abord !");
            // On ne consomme pas d action (echec)
            return;
        }
        if (!z.getExploree()) {
            z.setExploree(true);
            z.actionSpeciale(this);
        } else {
            System.out.println("Cette case a deja ete fouillee.");
        }
        actionsRestantes--;
    }

    /**
     * Le joueur courant déblaye 1 grain de sable sur sa case.
     */
    public void deblayerCase() {
        Joueur j = joueurs.get(joueurCourant);
        deblayer(j.getX(), j.getY());
        actionsRestantes--;
        System.out.println(j.getNom() + " déblaye sa case.");
    }

    /**
     * Le joueur courant ramasse la pièce sur sa case si les conditions sont remplies.
     * La case doit être une Zone_Normale, explorée, avec une pièce, et non bloquée.
     */
    public boolean ramasserPiece() {
        Joueur j = joueurs.get(joueurCourant);
        Zone z = getZone(j.getX(), j.getY());

        if (!(z instanceof Zone_Normale)) {
            System.out.println("Pas de pièce à ramasser ici.");
            return false;
        }
        Zone_Normale zn = (Zone_Normale) z;
        if (!zn.getExploree()) {
            System.out.println("La case n'a pas encore été fouillée.");
            return false;
        }
        if (zn.getpieceCachee().equals("")) {
            System.out.println("Il n'y a pas de pièce sur cette case.");
            return false;
        }
        // Ramasser la pièce
        String piece = zn.getpieceCachee();
        j.ajouterPiece(piece);
        zn.setpieceCachee(""); // la pièce n'est plus sur la case
        nbPiecesRamasses++;
        actionsRestantes--;
        System.out.println(j.getNom() + " ramasse la pièce : " + piece);
        return true;
    }

    // -----------------------------------------------------------------
    //  AFFICHAGE
    // -----------------------------------------------------------------
    /**
     * Affiche la grille dans la console (debug).
     */
    public void afficherGrille() {
        System.out.println("  0  1  2  3  4");
        for (int i = 0; i < 5; i++) {
            System.out.print(i + " ");
            for (int j = 0; j < 5; j++) {
                Zone z = this.zones[i][j];
                String s = z.getSymbole();
                for (Joueur jj : joueurs)
                    if (jj.getX() == j && jj.getY() == i) { s = "J"; break; }
                System.out.print("[" + s + z.getnbSable() + "]");
            }
            System.out.println();
        }
        System.out.println("Sable total : " + totalSable()
                + " | Niveau tempête : " + this.niveauTempete
                + " | Actions restantes : " + actionsRestantes
                + " | État : " + etatPartie());
    }
}

