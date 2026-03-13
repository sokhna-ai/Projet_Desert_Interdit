public abstract class Zone{
    protected int x, y, nbSable;
    protected boolean exploree;
    public Zone(int x, int y){
        this.x = x;
        this.y = y;
        this.nbSable = 0;
        this.exploree = false;
    }
    public int getX(){
        return this.x;
    }
    public int getY(){
        return this.y;
    }
    public int getnbSable(){
        return this.nbSable;
    }
    public void setNbSable(int n){
        this.nbSable = n;
    }
    public boolean getExploree(){
        return this.exploree;
    }
    public void setExploree(boolean z){
        this.exploree = z;
    }
    public boolean estBloquee(){
        return nbSable >= 2;
    }
    public abstract String getSymbole(); //pour afficher
    public void actionSpeciale(Desert d) {}
}