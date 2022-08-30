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

public class AddProductWindow implements Initializable {

    @FXML private TextField newProductName;
    @FXML private TextField newProductStock;
    @FXML private TextField newProductPrice;
    @FXML private TextField newProductMax;
    @FXML private TextField newProductMin;
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

    @FXML private TextFlow addProductErrorBox;

    private Inventory tempInventory;
    private ObservableList<Part> associatedParts = FXCollections.observableArrayList();

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
    public void addPartToNewProductButtonPress() {

        errorHandlingAdd();
        if(addProductErrorBox.getChildren().size() > 0)
            return;

        associatedParts.add(fullPartTableView.getSelectionModel().getSelectedItem());
        associatedPartTableView.setItems(associatedParts);
    }
    public void removeAssociatedPartButtonPress() {

        errorHandlingRemove();
        if(addProductErrorBox.getChildren().size() > 0)
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
    public void addProductSaveButtonPress(ActionEvent event) throws IOException {

        errorHandling();
        if(addProductErrorBox.getChildren().size() > 0)
            return;

        tempInventory.addProduct(new Product(tempInventory.getAllProducts().size() + 1, newProductName.getText(), Double.parseDouble(newProductPrice.getText()),
                Integer.parseInt(newProductStock.getText()), Integer.parseInt(newProductMin.getText()) , Integer.parseInt(newProductMax.getText())));

        for (int i = 0; i < associatedParts.size(); i++) {
            tempInventory.lookupProduct(tempInventory.getAllProducts().size()).addAssociatedPart(associatedParts.get(i));
        }

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
    public void receiveData(Inventory mainInventory){
        tempInventory = mainInventory;
        fullPartTableView.setItems(tempInventory.getAllParts());
    }
    public void errorHandling(){
        addProductErrorBox.getChildren().removeAll(addProductErrorBox.getChildren());

        if (newProductName.getText() == "") {
            addProductErrorBox.getChildren().add(new Text("Error: Please enter a Name\n"));
        }
        if (!(newProductPrice.getText().matches("(\\d+\\.\\d+)"))) {
            addProductErrorBox.getChildren().add(new Text("Error: Please enter a double for Price\n"));
        }
        if (!newProductStock.getText().matches("\\d+")) {
            addProductErrorBox.getChildren().add(new Text("Error: Please enter a number for Inv\n"));
        }
        if (!newProductMax.getText().matches("\\d+")) {
            addProductErrorBox.getChildren().add(new Text("Error: Please enter a number for Max\n"));
        }
        if (!newProductMin.getText().matches("\\d+")) {
            addProductErrorBox.getChildren().add(new Text("Error: Please enter a number for Min\n"));
        }

        if(addProductErrorBox.getChildren().size() > 0)
            return;

        if (Integer.parseInt(newProductStock.getText()) > Integer.parseInt(newProductMax.getText()) || Integer.parseInt(newProductStock.getText()) < Integer.parseInt(newProductMin.getText()) ){
            addProductErrorBox.getChildren().add(new Text("Error: Please enter a valid number for Inv\n"));
        }
        if(Integer.parseInt(newProductMax.getText()) <= Integer.parseInt(newProductMin.getText())) {
            addProductErrorBox.getChildren().add(new Text("Error: Please enter valid Min/ Max values\n"));
        }
    }
    public void errorHandlingAdd(){
        addProductErrorBox.getChildren().removeAll(addProductErrorBox.getChildren());

        if(fullPartTableView.getSelectionModel().getSelectedItem() == null){
            addProductErrorBox.getChildren().add(new Text("Error: Please select a part to add\n"));
        }
    }
    public void errorHandlingRemove(){
        addProductErrorBox.getChildren().removeAll(addProductErrorBox.getChildren());

        if(associatedPartTableView.getSelectionModel().getSelectedItem() == null){
            addProductErrorBox.getChildren().add(new Text("Error: Please select a part to remove\n"));
        }
    }
}
