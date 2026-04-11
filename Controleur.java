/**
 * Contrôleur MVC : fait le lien entre le modèle (Desert) et la vue (Vue).
 * Toutes les actions du joueur passent par ce contrôleur.
 */
public class Controleur {

    private Desert desert;
    private Vue vue;

    /**
     * Crée un contrôleur avec le modèle et la vue associés.
     */
    public Controleur(Desert desert, Vue vue) {
        this.desert = desert;
        this.vue    = vue;
    }

    /**
     * Déplace le joueur courant dans la direction donnée.
     */
    public void actionDeplacer(Desert.Direction d) {
        boolean ok = desert.deplacerJoueur(d);
        if (ok) {
            Joueur j = desert.getJoueurCourant();
            vue.log(j.getNom() + " se déplace vers " + d);
        } else {
            vue.log("Déplacement impossible !");
        }
        vue.rafraichir();
    }

    /**
     * Le joueur courant fouille sa case.
     */
    public void actionFouiller() {
        Joueur j = desert.getJoueurCourant();
        vue.log(j.getNom() + " fouille la case (" + j.getX() + "," + j.getY() + ").");
        desert.fouillerCase();
        vue.rafraichir();
    }

    /**
     * Le joueur courant déblaye 1 grain de sable sur sa case.
     */
    public void actionDeblayer() {
        Joueur j = desert.getJoueurCourant();
        vue.log(j.getNom() + " déblaye sa case.");
        desert.deblayerCase();
        vue.rafraichir();
    }

    /**
     * Le joueur courant tente de ramasser une pièce sur sa case.
     */
    public void actionRamasser() {
        Joueur j = desert.getJoueurCourant();
        boolean ok = desert.ramasserPiece();
        if (ok) {
            vue.log(j.getNom() + " ramasse une pièce !");
        } else {
            vue.log("Impossible de ramasser une pièce ici.");
        }
        vue.rafraichir();
    }

    /**
     * Termine le tour du joueur courant.
     */
    public void actionFinDeTour() {
        vue.log("--- Fin du tour de " + desert.getJoueurCourant().getNom() + " ---");
        desert.finDeTour();
        vue.log("C'est au tour de " + desert.getJoueurCourant().getNom() + ".");
        vue.rafraichir();
    }
}
