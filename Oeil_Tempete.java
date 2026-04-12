/**
 * Zone Oeil de la Tempête : toujours bloquée, se trouve au centre du désert.
 * Les joueurs ne peuvent pas s'y déplacer.
 */
public class Oeil_Tempete extends Zone {

    public Oeil_Tempete(int x, int y) {
        super(x, y);
    }

    @Override
    public String getSymbole() { return " "; }

    /** L'oeil de la tempête est TOUJOURS bloqué, peu importe le sable. */
    @Override
    public boolean estBloquee() { return true; }
}
