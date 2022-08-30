package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Inventory {

    private ObservableList<Part> allParts = FXCollections.observableArrayList();
    private ObservableList<Product> allProducts = FXCollections.observableArrayList();

    public Inventory() {

    }

    public void addPart(Part newPart){
        allParts.add(newPart);
    }
    public void addProduct(Product newProduct){
        allProducts.add(newProduct);
    }
    public Part lookupPart(int partID){

        for(int i = 0; i < allParts.size(); i++){
            if(allParts.get(i).getId() == partID)
                return allParts.get(i);
        }
        return null;
    }
    public Product lookupProduct(int productID){

        for(int i = 0; i < allProducts.size(); i++){
            if(allProducts.get(i).getId() == productID)
                return allProducts.get(i);
        }
        return null;
    }
    public ObservableList<Part> lookupPart(String name){

        ObservableList<Part> matches = FXCollections.observableArrayList();

        for(int i = 0; i < allParts.size(); i++){
            if (allParts.get(i).getName().contains(name)){
                matches.add(allParts.get(i));
            }
        }
        return matches;
    }
    public ObservableList<Product> lookupProduct(String name){

        ObservableList<Product> matches = FXCollections.observableArrayList();

        for(int i = 0; i < allProducts.size(); i++){
            if (allProducts.get(i).getName().contains(name)){
                matches.add(allProducts.get(i));
            }
        }
        return matches;
    }
    public void updatePart(int index, Part updatedPart){
        allParts.set(index, updatedPart);
    }
    public void updateProduct(int index, Product updatedProduct){
        allProducts.set(index, updatedProduct);
    }
    public boolean deletePart(Part selectedPart){
        return allParts.remove(selectedPart);
    }
    public boolean deleteProduct(Product selectedProduct){
        return allProducts.remove(selectedProduct);
    }

    public ObservableList<Part> getAllParts() {
        return allParts;
    }

    public ObservableList<Product> getAllProducts() {
        return allProducts;
    }
}
