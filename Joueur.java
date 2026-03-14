public class Joueur {
    private int niveauEau;
    private String nom;
    private int x;
    private int y;

    public Joueur(int niveauEau, int x, int y) {
        this.niveauEau = niveauEau;
        this.x = x;
        this.y = y;
    }

    public int getNiveauEau() {
        return niveauEau;
    }
    public boolean setNiveauEau(int niveauEau) {
        if(niveauEau>4||niveauEau<0){
            return false;
        }
        this.niveauEau = niveauEau;
        return true;
    }
    public boolean setX(int x) {
        if(x>4||x<0){
            return false;
        }
        this.x = x;
        return true;
    }
    public boolean setY(int y) {
        if(y>4||y<0){
            return false;
        }
        this.y = y;
        return true;
    }
    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
}
