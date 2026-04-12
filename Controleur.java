/**
 * Controleur MVC : fait le lien entre le modele (Desert) et la vue (Vue).
 * Toutes les actions du joueur passent par ce controleur.
 * L echange d eau ne coute pas d action (conforme aux regles du sujet).
 */
public class Controleur {

    private Desert desert;
    private Vue vue;

    /**
     * Cree un controleur avec le modele et la vue associes.
     * @param desert le modele de jeu
     * @param vue    la vue associee
     */
    public Controleur(Desert desert, Vue vue) {
        this.desert = desert;
        this.vue    = vue;
    }

    /**
     * Deplace le joueur courant dans la direction donnee.
     * Coute 1 action.
     * @param d la direction du deplacement
     */
    public void actionDeplacer(Desert.Direction d) {
        boolean ok = desert.deplacerJoueur(d);
        if (ok) {
            Joueur j = desert.getJoueurCourant();
            vue.log(j.getNom() + " se deplace vers " + d
                    + " -> (" + j.getX() + "," + j.getY() + ")");
        } else {
            vue.log("Deplacement impossible !");
        }
        vue.rafraichir();
    }

    /**
     * Le joueur courant fouille sa case.
     * Impossible si la case a du sable.
     * Coute 1 action (si reussie).
     */
    public void actionFouiller() {
        Joueur j = desert.getJoueurCourant();
        vue.log(j.getNom() + " fouille (" + j.getX() + "," + j.getY() + ")...");
        desert.fouillerCase();
        vue.rafraichir();
    }

    /**
     * Le joueur courant deblaye 1 grain de sable sur sa case.
     * Coute 1 action.
     */
    public void actionDeblayer() {
        Joueur j = desert.getJoueurCourant();
        vue.log(j.getNom() + " deblaye sa case.");
        desert.deblayerCase();
        vue.rafraichir();
    }

    /**
     * Le joueur courant tente de ramasser une piece sur sa case.
     * Coute 1 action si reussie.
     */
    public void actionRamasser() {
        Joueur j = desert.getJoueurCourant();
        boolean ok = desert.ramasserPiece();
        if (ok) {
            vue.log(j.getNom() + " ramasse une piece ! ["
                    + String.join(", ", j.getPieces()) + "]");
        } else {
            vue.log("Impossible de ramasser une piece ici.");
        }
        vue.rafraichir();
    }

    /**
     * Le joueur courant donne 1 unite d eau a un autre joueur.
     * Ne coute PAS d action (conforme aux regles du sujet section 8).
     * @param indexReceveur index du joueur qui recoit l eau
     */
    public void actionDonnerEau(int indexReceveur) {
        Joueur donneur = desert.getJoueurCourant();
        boolean ok = desert.donnerEau(indexReceveur);
        if (ok) {
            Joueur receveur = desert.getJoueurs().get(indexReceveur);
            vue.log(donneur.getNom() + " donne 1 eau a " + receveur.getNom()
                    + " (gratuit, hors action).");
        } else {
            vue.log("Echange d eau impossible (meme case requise, ou pas assez d eau).");
        }
        vue.rafraichir();
    }

    /**
     * Termine le tour du joueur courant et declenche les evenements de tempete.
     * Le nombre d evenements = partie entiere du niveau de tempete (min 1).
     */
    public void actionFinDeTour() {
        vue.log("=== Fin du tour de " + desert.getJoueurCourant().getNom()
                + " (tempete niv " + String.format("%.1f", desert.getNiveauTempete()) + ") ===");
        desert.finDeTour();
        vue.log(">>> Tour de " + desert.getJoueurCourant().getNom());
        vue.rafraichir();
    }
}
