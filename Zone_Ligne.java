public class Zone_Ligne extends Zone{
    protected int numeroLigne;
    protected Piece pieceReference;
    public Zone_Ligne(int x, int y,  Piece piece){
        super(x,y);
        this.pieceReference = piece;
        this.numeroLigne = piece.getY();
    }
    public int getNumeroLigne(){
        return this.numeroLigne;
    }
    public void setNumeroLigne(int n){
        this.numeroLigne = n;
    }
    public String getpieceReference(){
        return this.pieceReference.getPiece();
    }
    
    @Override
    public String getSymbole(){
        return "L";
    }
    public void actionSpeciale(Desert d) {
        System.out.println(this.pieceReference.getPiece() + " est sur la ligne " + this.numeroLigne);
        pieceReference.setYIndice(true);
    }
}
