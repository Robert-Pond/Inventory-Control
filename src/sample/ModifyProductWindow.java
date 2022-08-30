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

public class ModifyProductWindow implements Initializable {

    @FXML private TextField modifyProductID;
    @FXML private TextField modifyProductName;
    @FXML private TextField modifyProductStock;
    @FXML private TextField modifyProductPrice;
    @FXML private TextField modifyProductMax;
    @FXML private TextField modifyProductMin;
    @FXML private TextField partSearchBox;

    @FXML private AnchorPane mainAnchorPane;

    @FXML private TableView<Part> fullPartTableView;
    @FXML private TableColumn<Part, Integer> fullPartID;
    @FXML private TableColumn<Part, String> fullPartName;
    @FXML private TableColumn<Part, Double> fullPartPrice;
    @FXML private TableColumn<Part, Integer> fullPartStock;

    @FXML private TableView<Part> associatedPartTableView;
    @FXML private TableColumn<Part, Integer> associatedPartID;
    @FXML private TableColumn<Part, String> associatedPartName;
    @FXML private TableColumn<Part, Double> associatedPartPrice;
    @FXML private TableColumn<Part, Integer> associatedPartStock;

    @FXML private TextFlow modifyProductErrorBox;

    private Inventory tempInventory;
    private ObservableList<Part> associatedParts = FXCollections.observableArrayList();
    private Product productToModify;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        fullPartID.setCellValueFactory(new PropertyValueFactory<Part, Integer>("Id"));
        fullPartName.setCellValueFactory(new PropertyValueFactory<Part, String>("Name"));
        fullPartPrice.setCellValueFactory(new PropertyValueFactory<Part, Double>("Price"));
        fullPartStock.setCellValueFactory(new PropertyValueFactory<Part, Integer>("Stock"));

        associatedPartID.setCellValueFactory(new PropertyValueFactory<Part, Integer>("Id"));
        associatedPartName.setCellValueFactory(new PropertyValueFactory<Part, String>("Name"));
        associatedPartPrice.setCellValueFactory(new PropertyValueFactory<Part, Double>("Price"));
        associatedPartStock.setCellValueFactory(new PropertyValueFactory<Part, Integer>("Stock"));

    }
    public void addPartToModifiedProductButtonPress() {

        errorHandlingAdd();
        if(modifyProductErrorBox.getChildren().size() > 0)
            return;

        associatedParts.add(fullPartTableView.getSelectionModel().getSelectedItem());
        associatedPartTableView.setItems(associatedParts);
    }
    public void removeAssociatedPartButtonPress() {

        errorHandlingRemove();
        if(modifyProductErrorBox.getChildren().size() > 0)
            return;

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "");
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.initOwner((Stage)mainAnchorPane.getScene().getWindow());
        alert.getDialogPane().setHeaderText("Remove this Part?");

        Optional<ButtonType> result = alert.showAndWait();
        if(result.get() == ButtonType.OK){
            associatedParts.remove(associatedPartTableView.getSelectionModel().getSelectedItem());
        }
    }
    public void partSearchBoxUsed(){
        if(partSearchBox.getText().matches("\\d+")) {
            ObservableList<Part> temp = FXCollections.observableArrayList();
            temp.add(tempInventory.lookupPart(Integer.parseInt(partSearchBox.getText())));
            fullPartTableView.setItems(temp);
        }
        else {
            fullPartTableView.setItems(tempInventory.lookupPart(partSearchBox.getText()));
        }
    }
    public void modifyProductSaveButtonPress(ActionEvent event) throws IOException {

        errorHandling();
        if(modifyProductErrorBox.getChildren().size() > 0)
            return;

        Product tempProduct = new Product(Integer.parseInt(modifyProductID.getText()), modifyProductName.getText(), Double.parseDouble(modifyProductPrice.getText()),
                Integer.parseInt(modifyProductStock.getText()), Integer.parseInt(modifyProductMin.getText()) , Integer.parseInt(modifyProductMax.getText()));

        for (int i = 0; i < associatedParts.size(); i++) {
            tempProduct.addAssociatedPart(associatedParts.get(i));
        }

        tempInventory.updateProduct(Integer.parseInt(modifyProductID.getText()) - 1, tempProduct);

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("sample.fxml"));
        Parent addProductParent = loader.load();

        Scene addProductScene = new Scene(addProductParent);

        Controller controller = loader.getController();
        controller.reciveData(tempInventory);

        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();

        window.setScene(addProductScene);
        window.show();
    }
    public void cancelProductButtonPress(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("sample.fxml"));
        Parent addProductParent = loader.load();

        Scene addProductScene = new Scene(addProductParent);

        Controller controller = loader.getController();
        controller.reciveData(tempInventory);

        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();

        window.setScene(addProductScene);
        window.show();
    }
    public void receiveData(Inventory mainInventory, Product selectedProduct){
        tempInventory = mainInventory;
        productToModify = selectedProduct;
        associatedParts = productToModify.getAssociatedParts();
        fullPartTableView.setItems(tempInventory.getAllParts());
        setTextAndTable();
    }
    public void setTextAndTable(){
        modifyProductID.setText(productToModify.getId().toString());
        modifyProductName.setText(productToModify.getName());
        modifyProductStock.setText(productToModify.getStock().toString());
        modifyProductPrice.setText(productToModify.getPrice().toString());
        modifyProductMax.setText(productToModify.getMax().toString());
        modifyProductMin.setText(productToModify.getMin().toString());

        associatedPartTableView.setItems(associatedParts);
    }
    public void errorHandling(){
        modifyProductErrorBox.getChildren().removeAll(modifyProductErrorBox.getChildren());

        if (modifyProductName.getText() == "") {
            modifyProductErrorBox.getChildren().add(new Text("Error: Please enter a Name\n"));
        }
        if (!(modifyProductPrice.getText().matches("(\\d+\\.\\d+)"))) {
            modifyProductErrorBox.getChildren().add(new Text("Error: Please enter a double for Price\n"));
        }
        if (!modifyProductStock.getText().matches("\\d+")) {
            modifyProductErrorBox.getChildren().add(new Text("Error: Please enter a number for Inv\n"));
        }
        if (!modifyProductMax.getText().matches("\\d+")) {
            modifyProductErrorBox.getChildren().add(new Text("Error: Please enter a number for Max\n"));
        }
        if (!modifyProductMin.getText().matches("\\d+")) {
            modifyProductErrorBox.getChildren().add(new Text("Error: Please enter a number for Min\n"));
        }

        if(modifyProductErrorBox.getChildren().size() > 0)
            return;

        if (Integer.parseInt(modifyProductStock.getText()) > Integer.parseInt(modifyProductMax.getText()) || Integer.parseInt(modifyProductStock.getText()) < Integer.parseInt(modifyProductMin.getText()) ){
            modifyProductErrorBox.getChildren().add(new Text("Error: Please enter a valid number for Inv\n"));
        }
        if(Integer.parseInt(modifyProductMax.getText()) <= Integer.parseInt(modifyProductMin.getText())) {
            modifyProductErrorBox.getChildren().add(new Text("Error: Please enter valid Min/ Max values\n"));
        }
    }
    public void errorHandlingAdd(){
        modifyProductErrorBox.getChildren().removeAll(modifyProductErrorBox.getChildren());

        if(fullPartTableView.getSelectionModel().getSelectedItem() == null){
            modifyProductErrorBox.getChildren().add(new Text("Error: Please select a part to add\n"));
        }
    }
    public void errorHandlingRemove(){
        modifyProductErrorBox.getChildren().removeAll(modifyProductErrorBox.getChildren());

        if(associatedPartTableView.getSelectionModel().getSelectedItem() == null){
            modifyProductErrorBox.getChildren().add(new Text("Error: Please select a part to remove\n"));
        }
    }
}
