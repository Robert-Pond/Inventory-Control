package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author Robert Pond
 */
public class Product {

    private ObservableList<Part> associatedParts = FXCollections.observableArrayList();
    private Integer id;
    private String name;
    private Double price;
    private Integer stock;
    private Integer min;
    private Integer max;

    public Product(int ID, String Name, double Price, int Stock, int min, int max) {
        this.id = ID;
        this.name = Name;
        this.price = Price;
        this.stock = Stock;
        this.min = min;
        this.max = max;
    }

    public Integer getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the price
     */
    public Double getPrice() {
        return price;
    }

    /**
     * @param price the price to set
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * @return the stock
     */
    public Integer getStock() {
        return stock;
    }

    /**
     * @param stock the stock to set
     */
    public void setStock(int stock)    {
        this.stock = stock;
    }

    /**
     * @return the min
     */
    public Integer getMin() {
        return min;
    }

    /**
     * @param min the min to set
     */
    public void setMin(int min) {
        this.min = min;
    }

    /**
     * @return the max
     */
    public Integer getMax() {
        return max;
    }

    /**
     * @param max the max to set
     */
    public void setMax(int max) {
        this.max = max;
    }

    public void addAssociatedPart(Part newPart){
        associatedParts.add(newPart);
    }
    public ObservableList getAssociatedParts(){
        return associatedParts;
    }
    public boolean deleteAssociatedPart(Part selectedPart){
        return associatedParts.removeAll(selectedPart);
    }

}