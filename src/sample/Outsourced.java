package sample;

public class Outsourced extends Part{

    private String companyName;

    public Outsourced(int ID, String Name, double Price, int Stock, int min, int max, String compName){
        super(ID, Name, Price, Stock, min, max);
        companyName = compName;
    }
    public void setCompanyName(String newName){
        companyName = newName;
    }
    public String getCompanyName(){
        return companyName;
    }
}
