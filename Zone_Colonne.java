public class Zone_Colonne extends Zone{
    protected int numeroColonne;
    protected String pieceReference;
    public Zone_Colonne(int x, int y, int nc, String pr){
        super(x,y);
        this.pieceReference = pr;
        this.numeroColonne = nc;
    }
    public int getNumeroColonne(){
        return this.numeroColonne;
    }
    public void setNumeroColonne(int n){
        this.numeroColonne = n;
    }
    public String getpieceReference(){
        return this.pieceReference;
    }
    public void setpieceReference(String p){
        this.pieceReference = p;
    }
    @Override
    public String getSymbole(){
        return "C";
    }
    public void actionSpeciale(Desert d) {
        System.out.println(this.pieceReference + " est sur la Colonne " + this.numeroColonne);
    }
}