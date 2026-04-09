import java.util.ArrayList;

public class Tunnel extends Zone {

    public Tunnel(int x, int y) {
        super(x, y);
    }

    @Override
    public String getSymbole() {
        return "T";
    }

    /**
     * Action spéciale du tunnel : quand on fouille un tunnel,
     * le joueur peut se téléporter vers un autre tunnel.
     * Choisit automatiquement le premier autre tunnel disponible.
     */
    @Override
    public void actionSpeciale(Desert d) {
        ArrayList<Tunnel> tunnels = d.getTunnels();
        // Trouver un autre tunnel (pas celui-ci)
        for (Tunnel t : tunnels) {
            if (t.getX() != this.x || t.getY() != this.y) {
                System.out.println("Tunnel trouvé ! Téléportation vers ("
                        + t.getX() + "," + t.getY() + ").");
                d.teleporterJoueur(t.getX(), t.getY());
                return;
            }
        }
        System.out.println("Aucun autre tunnel disponible pour la téléportation.");
    }
}
