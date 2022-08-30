package sample;

public class InHouse extends Part {

    private int machineID;

    public InHouse (int ID, String Name, double Price, int Stock, int min, int max, int machID){
        super(ID, Name, Price, Stock, min, max);
        machineID = machID;
    }
    public void setMachineID(int newID){
        machineID = newID;
    }
    public int getMachineID(){
        return machineID;
    }
}
