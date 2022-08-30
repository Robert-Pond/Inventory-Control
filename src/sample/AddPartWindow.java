package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AddPartWindow implements Initializable {

    @FXML private RadioButton inHouseRadioButton;
    @FXML private RadioButton outsourcedRadioButton;
    private ToggleGroup radioButtons;

    @FXML private TextField newPartName;
    @FXML private TextField newPartStock;
    @FXML private TextField newPartCost;
    @FXML private TextField newPartMax;
    @FXML private TextField newPartMin;
    @FXML private TextField newPartSpecial;

    @FXML private Label partType;
    @FXML private TextFlow addErrorBox;

    private Inventory tempInventory;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        radioButtons = new ToggleGroup();
        inHouseRadioButton.setToggleGroup(radioButtons);
        outsourcedRadioButton.setToggleGroup(radioButtons);
    }
    public void radioButtonChanged(){
        if(radioButtons.getSelectedToggle().equals(inHouseRadioButton)){
            partType.setText("Machine ID");
        }
        if(radioButtons.getSelectedToggle().equals(outsourcedRadioButton)){
            partType.setText("Company");
        }
    }
    public void reciveData(Inventory mainInvetory){
        tempInventory = mainInvetory;
    }
    public void addPartButtonPressed(ActionEvent event) throws IOException{

        errorHandler();
        if(addErrorBox.getChildren().size() > 0)
            return;

        if(radioButtons.getSelectedToggle().equals(inHouseRadioButton)){
            tempInventory.addPart(new InHouse((tempInventory.getAllParts().size() + 1) ,(newPartName.getText()),
                    (Double.parseDouble(newPartCost.getText())), (Integer.parseInt(newPartStock.getText())),
                    (Integer.parseInt(newPartMin.getText())), (Integer.parseInt(newPartMax.getText())), (Integer.parseInt(newPartSpecial.getText()))));
        }
        if(radioButtons.getSelectedToggle().equals(outsourcedRadioButton)){
            tempInventory.addPart(new Outsourced((tempInventory.getAllParts().size() + 1) ,(newPartName.getText()),
                    (Double.parseDouble(newPartCost.getText())), (Integer.parseInt(newPartStock.getText())),
                    (Integer.parseInt(newPartMin.getText())), (Integer.parseInt(newPartMax.getText())), newPartSpecial.getText()));
        }

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("sample.fxml"));
        Parent addPartParent = loader.load();

        Scene addPartScene = new Scene(addPartParent);

        Controller controller = loader.getController();
        controller.reciveData(tempInventory);

        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();

        window.setScene(addPartScene);
        window.show();
    }
    public void addPartCancelButtonPress(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("sample.fxml"));
        Parent addPartParent = loader.load();

        Scene addPartScene = new Scene(addPartParent);

        Controller controller = loader.getController();
        controller.reciveData(tempInventory);

        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();

        window.setScene(addPartScene);
        window.show();
    }
    private void errorHandler() {
        addErrorBox.getChildren().removeAll(addErrorBox.getChildren());

        if (newPartName.getText() == "") {
            addErrorBox.getChildren().add(new Text("Error: Please enter a Name\n"));
        }
        if (!(newPartCost.getText().matches("(\\d+\\.\\d+)"))) {
            addErrorBox.getChildren().add(new Text("Error: Please enter a double for Price\n"));
        }
        if (!newPartStock.getText().matches("\\d+")) {
            addErrorBox.getChildren().add(new Text("Error: Please enter a number for Inv\n"));
        }
        if (!newPartMax.getText().matches("\\d+")) {
            addErrorBox.getChildren().add(new Text("Error: Please enter a number for Max\n"));
        }
        if (!newPartMin.getText().matches("\\d+")) {
            addErrorBox.getChildren().add(new Text("Error: Please enter a number for Min\n"));
        }
        if (radioButtons.getSelectedToggle().equals(inHouseRadioButton) && !newPartSpecial.getText().matches("\\d+")) {
            addErrorBox.getChildren().add(new Text("Error: Please enter a number for Machine ID\n"));
        } else if (newPartSpecial.getText() == "") {
            addErrorBox.getChildren().add(new Text("Error: Please enter a valid Company\n"));
        }

        if(addErrorBox.getChildren().size() > 0)
            return;

        if (Integer.parseInt(newPartStock.getText()) > Integer.parseInt(newPartMax.getText()) || Integer.parseInt(newPartStock.getText()) < Integer.parseInt(newPartMin.getText()) ){
            addErrorBox.getChildren().add(new Text("Error: Please enter a valid number for Inv\n"));
        }
        if(Integer.parseInt(newPartMax.getText()) <= Integer.parseInt(newPartMin.getText())){
            addErrorBox.getChildren().add(new Text("Error: Please enter valid Min/ Max values\n"));
        }
    }
}
