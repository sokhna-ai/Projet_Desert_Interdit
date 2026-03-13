public class Zone_Normale extends Zone{
    protected String pieceCachee;
    public Zone_Normale(int x, int y, String p){
        super(x,y);
        this.pieceCachee = p;
    }
    public String getpieceCachee(){
        return this.pieceCachee;
    }
    public void setpieceCachee(string p){
        this.pieceCachee = p;
    }
    @Override
    public String getSymbole(){
        return "N";
    }
}