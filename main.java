/**
 * Point d'entrée du programme.
 * Instancie le modèle, la vue et le contrôleur (architecture MVC).
 * Tous les joueurs démarrent sur la zone Crash_Helicoptere,
 * conformément aux règles du Désert Interdit.
 */
public class main {
    public static void main(String[] args) {
        // Modèle
        Desert desert = new Desert();

        // Récupérer la position du crash (aléatoire à chaque partie)
        int xCrash = desert.getXCrash();
        int yCrash = desert.getYCrash();

        // Tous les joueurs démarrent sur la zone de crash
        Joueur j1 = new Joueur(5, xCrash, yCrash);
        j1.setNom("Alice");
        Joueur j2 = new Joueur(5, xCrash, yCrash);
        j2.setNom("Bob");
        Joueur j3 = new Joueur(5, xCrash, yCrash);
        j3.setNom("Sokhna");
        Joueur j4  = new Joueur(5, xCrash, yCrash);
        j4.setNom("Song");

        desert.ajouterJoueur(j1, xCrash, yCrash);
        desert.ajouterJoueur(j2, xCrash, yCrash);
        desert.ajouterJoueur(j3, xCrash, yCrash);
        desert.ajouterJoueur(j4, xCrash, yCrash);
        // Vue
        Vue vue = new Vue(desert);

        // Contrôleur
        Controleur controleur = new Controleur(desert, vue);

        // Connecter contrôleur <-> vue
        vue.setControleur(controleur);

        // Rafraîchissement initial
        vue.rafraichir();
        vue.log("Bienvenue dans Le Desert Interdit !");
        vue.log("Depart depuis le crash en (" + xCrash + "," + yCrash + ").");
        vue.log("Retrouvez les 4 pieces et rejoignez la piste de decollage.");

        vue.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
        vue.setVisible(true);
    }
}
