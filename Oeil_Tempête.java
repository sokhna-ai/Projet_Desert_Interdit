public class Oeil_Tempête extends Zone{
    public Oeil_Tempête(int x, int y){
        super(x,y);
    }
    @Override
    public String getSymbole(){
        return " ";
    }
    @Override
    public boolean estBloquee(){
        return true; //Toujours bloqué
    }
}