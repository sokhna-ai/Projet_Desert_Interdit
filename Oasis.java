/**
 * Zone Oasis : peut donner de l'eau aux joueurs quand elle est explorée.
 * Une oasis peut être un mirage (pas d'eau réelle).
 */
public class Oasis extends Zone {

    /** true si cette oasis est en réalité un mirage (pas d'eau). */
    protected boolean estMirage;

    public Oasis(int x, int y, boolean m) {
        super(x, y);
        this.estMirage = m;
    }

    public boolean getEstMirage() { return this.estMirage; }
    public void setEstMirage(boolean m) { this.estMirage = m; }

    @Override
    public String getSymbole() { return "O"; }

    /**
     * Action spéciale : si explorée, donne 2 eau à tous les joueurs sur la case
     * (sauf si c'est un mirage).
     */
    @Override
    public void actionSpeciale(Desert d) {
        if (this.exploree) {
            if (this.estMirage) {
                System.out.println("C'est un mirage... l'oasis est à sec !");
            } else {
                System.out.println("Tous les joueurs sur cette case gagnent 2 portions d'eau !");
                d.distribuerEau(this.x, this.y, 2);
            }
        }
    }
}
