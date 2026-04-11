public class main {
    public static void main(String[] args) {
        // Instanciation du modèle
        Desert desert = new Desert();

        // Ajouter des joueurs de test
        Joueur j1 = new Joueur(3, 0, 0);
        j1.setNom("Alice");
        Joueur j2 = new Joueur(3, 4, 4);
        j2.setNom("Bob");
        desert.ajouterJoueur(j1, 0, 0);
        desert.ajouterJoueur(j2, 4, 4);

        // Instanciation de la vue avec le désert
        Vue vue = new Vue(desert);
        vue.setSize(1280, 720);
        vue.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);

        // Instanciation du contrôleur
        Controleur controleur = new Controleur(desert, vue);

        // Connecter le contrôleur à la vue
        vue.setControleur(controleur);

        vue.setVisible(true);
    }
}
