package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML private TableView<Part> partTableView;
    @FXML private TableColumn<Part, Integer> partID;
    @FXML private TableColumn<Part, String> partName;
    @FXML private TableColumn<Part, Double> partPrice;
    @FXML private TableColumn<Part, Integer> partStock;

    @FXML private TableView<Product> productTableView;
    @FXML private TableColumn<Product, Integer> productID;
    @FXML private TableColumn<Product, String> productName;
    @FXML private TableColumn<Product, Double> productPrice;
    @FXML private TableColumn<Product, Integer> productStock;

    @FXML private TextField partSearchBoxField;
    @FXML private TextField productSearchBoxField;

    @FXML private AnchorPane mainAnchorPane;

    @FXML private Button exitButton;

    @FXML private TextFlow mainErrorBox;

    private  Inventory inventory = new Inventory();

    public ObservableList<Part> setParts() {

        inventory.addPart(new InHouse(1, "Brakes", 35.00, 10, 1, 20, 2576));
        inventory.addPart(new Outsourced(2, "Wheel", 22.00, 7, 1, 20, "Michelin"));
        inventory.addPart(new InHouse(3, "Seat", 13.00, 12, 1, 20, 6543));
        inventory.addPart(new InHouse(4, "Tire", 15.00, 20, 1, 20, 6156));
        inventory.addPart(new InHouse(5, "Handle Bar", 32.00, 6, 1, 20, 2346));
        inventory.addPart(new InHouse(6, "Peg", 3.00, 8, 1, 20, 8542));
        inventory.addPart(new InHouse(7, "Helmet", 40.00, 4, 1, 20, 6458));
        inventory.addPart(new Outsourced(8, "Bolt", 2.00, 17, 1, 20, "Generic"));
        inventory.addPart(new Outsourced(9, "Pedal", 22.00, 7, 1, 20, "Fancy Bike"));
        inventory.addPart(new Outsourced(10, "Frame", 220.00, 4, 1, 20, "Amazon"));
        inventory.addPart(new Outsourced(11, "Gear", 15.00, 16, 1, 20, "Banana"));

        return inventory.getAllParts();
    }
    public ObservableList<Product> setProducts() {

        Product fastBike = new Product(1, "Fast Bike", 150.00, 2, 1, 20);
        fastBike.addAssociatedPart(inventory.lookupPart(3));
        fastBike.addAssociatedPart(inventory.lookupPart(7));
        fastBike.addAssociatedPart(inventory.lookupPart(4));
        fastBike.addAssociatedPart(inventory.lookupPart(9));
        inventory.addProduct(fastBike);

        Product slowBike = new Product(2, "Slow Bike", 110.00, 3, 1, 20);
        slowBike.addAssociatedPart(inventory.lookupPart(10));
        slowBike.addAssociatedPart(inventory.lookupPart(11));
        slowBike.addAssociatedPart(inventory.lookupPart(1));
        slowBike.addAssociatedPart(inventory.lookupPart(2));
        inventory.addProduct(slowBike);

        Product roadBike = new Product(3, "Road Bike", 130.00, 8, 1, 20);
        roadBike.addAssociatedPart(inventory.lookupPart(4));
        roadBike.addAssociatedPart(inventory.lookupPart(5));
        roadBike.addAssociatedPart(inventory.lookupPart(7));
        roadBike.addAssociatedPart(inventory.lookupPart(9));
        roadBike.addAssociatedPart(inventory.lookupPart(10));
        roadBike.addAssociatedPart(inventory.lookupPart(2));
        roadBike.addAssociatedPart(inventory.lookupPart(1));
        inventory.addProduct(roadBike);

        inventory.addProduct(new Product(4, "Mountain Bike", 1980.00, 2, 1, 20));

        return inventory.getAllProducts();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb){
        partID.setCellValueFactory(new PropertyValueFactory<Part, Integer>("Id"));
        partName.setCellValueFactory(new PropertyValueFactory<Part, String>("Name"));
        partPrice.setCellValueFactory(new PropertyValueFactory<Part, Double>("Price"));
        partStock.setCellValueFactory(new PropertyValueFactory<Part, Integer>("Stock"));

        productID.setCellValueFactory(new PropertyValueFactory<Product, Integer>("Id"));
        productName.setCellValueFactory(new PropertyValueFactory<Product, String>("Name"));
        productPrice.setCellValueFactory(new PropertyValueFactory<Product, Double>("Price"));
        productStock.setCellValueFactory(new PropertyValueFactory<Product, Integer>("Stock"));

        partTableView.setItems(setParts());
        productTableView.setItems(setProducts());

    }
    public void reciveData(Inventory mainInvetory){
        inventory = mainInvetory;
        partTableView.setItems(inventory.getAllParts());
        productTableView.setItems(inventory.getAllProducts());
    }
    public void partSearchBoxUsed(){
        if(partSearchBoxField.getText().matches("\\d+")) {
            ObservableList<Part> temp = FXCollections.observableArrayList();
            temp.add(inventory.lookupPart(Integer.parseInt(partSearchBoxField.getText())));
            partTableView.setItems(temp);
        }
        else {
            partTableView.setItems(inventory.lookupPart(partSearchBoxField.getText()));
        }
    }
    public void productSearchBoxUsed(){
        if(productSearchBoxField.getText().matches("\\d+")) {
            ObservableList<Product> temp = FXCollections.observableArrayList();
            temp.add(inventory.lookupProduct(Integer.parseInt(productSearchBoxField.getText())));
            productTableView.setItems(temp);
        }
        else{
            productTableView.setItems(inventory.lookupProduct(productSearchBoxField.getText()));
        }
    }
    public void partDeleteButtonPress(){

        errorHandlingDelPart();
        if(mainErrorBox.getChildren().size() > 0)
            return;

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "");
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.initOwner((Stage)mainAnchorPane.getScene().getWindow());
        alert.getDialogPane().setHeaderText("Delete this Part?");

        Optional<ButtonType> result = alert.showAndWait();
        if(result.get() == ButtonType.OK){
            inventory.deletePart(partTableView.getSelectionModel().getSelectedItem());
        }
    }
    public void productDeleteButtonPress(){

        errorHandlingProduct();
        if(mainErrorBox.getChildren().size() > 0)
            return;

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "");
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.initOwner((Stage)mainAnchorPane.getScene().getWindow());
        alert.getDialogPane().setHeaderText("Delete this Product?");

        Optional<ButtonType> result = alert.showAndWait();
        if(result.get() == ButtonType.OK) {
            inventory.deleteProduct(productTableView.getSelectionModel().getSelectedItem());
        }
    }
    public void exitButtonPressed(){
        ((Stage)exitButton.getScene().getWindow()).close();
    }
    public void partAddButtonPress(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("AddPartWindow.fxml"));
        Parent addPartParent = loader.load();

        Scene addPartScene = new Scene(addPartParent);

        AddPartWindow controller = loader.getController();
        controller.reciveData(inventory);

        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();

        window.setScene(addPartScene);
        window.show();
    }
    public void partModifyButtonPress(ActionEvent event) throws IOException {

        errorHandlingModPart();
        if(mainErrorBox.getChildren().size() > 0)
            return;

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("ModifyPartWindow.fxml"));
        Parent modifyPartParent = loader.load();

        Scene modifyPartScene = new Scene(modifyPartParent);

        ModifyPartWindow controller = loader.getController();

        if(partTableView.getSelectionModel().getSelectedItem() instanceof InHouse){
            controller.reciveDataInHouse(inventory, (InHouse) partTableView.getSelectionModel().getSelectedItem());
        }
        else if(partTableView.getSelectionModel().getSelectedItem() instanceof Outsourced){
            controller.reciveDataOutsourced(inventory, (Outsourced) partTableView.getSelectionModel().getSelectedItem());
        }

        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();

        window.setScene(modifyPartScene);
        window.show();
    }
    public void errorHandlingModPart(){
        mainErrorBox.getChildren().removeAll(mainErrorBox.getChildren());

        if(partTableView.getSelectionModel().getSelectedItem() == null){
            mainErrorBox.getChildren().add(new Text("Error: Please select a part to modify"));
        }
    }
    public void errorHandlingDelPart(){
        mainErrorBox.getChildren().removeAll(mainErrorBox.getChildren());

        if(partTableView.getSelectionModel().getSelectedItem() == null){
            mainErrorBox.getChildren().add(new Text("Error: Please select a part to delete"));
        }
    }
    public void errorHandlingProduct(){
        mainErrorBox.getChildren().removeAll(mainErrorBox.getChildren());

        if(productTableView.getSelectionModel().getSelectedItem() == null){
            mainErrorBox.getChildren().add(new Text("Error: Please select a Product to Delete"));
            return;
        }
        if(productTableView.getSelectionModel().getSelectedItem().getAssociatedParts().size() > 0){
            mainErrorBox.getChildren().add(new Text("Error: Product has associated parts"));
        }
    }
    public void errorHandlingModProduct(){
        mainErrorBox.getChildren().removeAll(mainErrorBox.getChildren());

        if(productTableView.getSelectionModel().getSelectedItem() == null){
            mainErrorBox.getChildren().add(new Text("Error: Please select a Product to Modify"));
        }
    }
    public void addProductButtonPress(ActionEvent event) throws IOException{
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("addProductWindow.fxml"));
        Parent addProductParent = loader.load();

        Scene addProductScene = new Scene(addProductParent);

        AddProductWindow controller = loader.getController();
        controller.receiveData(inventory);

        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();

        window.setScene(addProductScene);
        window.show();
    }
    public void modifyProductButtonPress(ActionEvent event) throws IOException{

        errorHandlingModProduct();
        if(mainErrorBox.getChildren().size() > 0)
            return;

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("ModifyProductWindow.fxml"));
        Parent modifyProductParent = loader.load();

        Scene modifyProductScene = new Scene(modifyProductParent);

        ModifyProductWindow controller = loader.getController();
        controller.receiveData(inventory, productTableView.getSelectionModel().getSelectedItem());

        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();

        window.setScene(modifyProductScene);
        window.show();
    }

}
