public class Oasis extends Zone{
    protected boolean estMirage;
    public Oasis(int x, int y, boolean m){
        super(x,y);
        this.estMirage = m;
    }
    public boolean getEstMirage(){
        return this.estMirage;
    }
    public void setEstMirage(boolean m){
        this.estMirage = m;
    }
    @Override
    public String getSymbole(){
        return "O";
    }
    public void actionSpeciale(Desert d) {
        if (this.exploree) { // L'action ne se passe que si on a fouillé la case
            if (this.estMirage) {
                System.out.println("C'est un mirage... l'oasis est à sec !");
            } else {
                System.out.println("Tous les joueurs sur cette case gagnent 2 rations.");
                //on doit ajouter une methode (public void donnerEauCase(int x, int y, int quantite) dans desert)
            }
        }
    }
}