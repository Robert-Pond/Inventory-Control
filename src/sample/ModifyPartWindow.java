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

public class ModifyPartWindow implements Initializable {

    @FXML private RadioButton inHouseRadioButton;
    @FXML private RadioButton outsourcedRadioButton;
    private ToggleGroup radioButtons;

    @FXML private TextField modifyPartID;
    @FXML private TextField modifyPartName;
    @FXML private TextField modifyPartStock;
    @FXML private TextField modifyPartCost;
    @FXML private TextField modifyPartMax;
    @FXML private TextField modifyPartMin;
    @FXML private TextField modifyPartSpecial;

    @FXML private TextFlow modifyErrorBox;

    @FXML private Label partType;

    private Inventory tempInventory;
    private InHouse partToModifyInHouse;
    private Outsourced partToModifyOutsourced;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        radioButtons = new ToggleGroup();
        inHouseRadioButton.setToggleGroup(radioButtons);
        outsourcedRadioButton.setToggleGroup(radioButtons);
    }
    private void setTextInHouse(){
        modifyPartID.setText(partToModifyInHouse.getId().toString());
        modifyPartName.setText(partToModifyInHouse.getName());
        modifyPartStock.setText(partToModifyInHouse.getStock().toString());
        modifyPartCost.setText(partToModifyInHouse.getPrice().toString());
        modifyPartMax.setText(partToModifyInHouse.getMax().toString());
        modifyPartMin.setText(partToModifyInHouse.getMin().toString());
        inHouseRadioButton.setSelected(true);
        modifyPartSpecial.setText(((Integer)(partToModifyInHouse.getMachineID())).toString());
        partType.setText("Machine ID");
    }
    private void setTextOutsourced(){
        modifyPartID.setText(partToModifyOutsourced.getId().toString());
        modifyPartName.setText(partToModifyOutsourced.getName());
        modifyPartStock.setText(partToModifyOutsourced.getStock().toString());
        modifyPartCost.setText(partToModifyOutsourced.getPrice().toString());
        modifyPartMax.setText(partToModifyOutsourced.getMax().toString());
        modifyPartMin.setText(partToModifyOutsourced.getMin().toString());
        outsourcedRadioButton.setSelected(true);
        modifyPartSpecial.setText(partToModifyOutsourced.getCompanyName());
        partType.setText("Company");
    }
    public void radioButtonChanged(){

        if(radioButtons.getSelectedToggle().equals(inHouseRadioButton)){
            partType.setText("Machine ID");
        }
        if(radioButtons.getSelectedToggle().equals(outsourcedRadioButton)){
            partType.setText("Company");
        }
    }
    public void reciveDataInHouse(Inventory mainInvetory, InHouse selectedPart){
        tempInventory = mainInvetory;
        partToModifyInHouse = selectedPart;
        setTextInHouse();
    }
    public void reciveDataOutsourced(Inventory mainInvetory, Outsourced selectedPart){
        tempInventory = mainInvetory;
        partToModifyOutsourced = selectedPart;
        setTextOutsourced();
    }
    public void modifySaveButtonPress(ActionEvent event) throws IOException {

        errorHandler();
        if(modifyErrorBox.getChildren().size() > 0)
            return;

        if(radioButtons.getSelectedToggle().equals(inHouseRadioButton)){
            tempInventory.updatePart((Integer.parseInt(modifyPartID.getText())) - 1, new InHouse(Integer.parseInt(modifyPartID.getText()) ,(modifyPartName.getText()),
                    (Double.parseDouble(modifyPartCost.getText())), (Integer.parseInt(modifyPartStock.getText())),
                    (Integer.parseInt(modifyPartMin.getText())), (Integer.parseInt(modifyPartMax.getText())), (Integer.parseInt(modifyPartSpecial.getText()))));
        }
        if(radioButtons.getSelectedToggle().equals(outsourcedRadioButton)){
            tempInventory.updatePart((Integer.parseInt(modifyPartID.getText())) - 1, new Outsourced(Integer.parseInt(modifyPartID.getText()) ,(modifyPartName.getText()),
                    (Double.parseDouble(modifyPartCost.getText())), (Integer.parseInt(modifyPartStock.getText())),
                    (Integer.parseInt(modifyPartMin.getText())), (Integer.parseInt(modifyPartMax.getText())), modifyPartSpecial.getText()));
        }

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("sample.fxml"));
        Parent modifyPartParent = loader.load();

        Scene modifyPartScene = new Scene(modifyPartParent);

        Controller controller = loader.getController();
        controller.reciveData(tempInventory);

        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();

        window.setScene(modifyPartScene);
        window.show();
    }
    public void modifyPartCancelButtonPress(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("sample.fxml"));
        Parent modifyPartParent = loader.load();

        Scene modifyPartScene = new Scene(modifyPartParent);

        Controller controller = loader.getController();
        controller.reciveData(tempInventory);

        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();

        window.setScene(modifyPartScene);
        window.show();
    }
    public void errorHandler() {

        modifyErrorBox.getChildren().removeAll(modifyErrorBox.getChildren());

        if (modifyPartName.getText() == "") {
            modifyErrorBox.getChildren().add(new Text("Error: Please enter a Name\n"));
        }
        if (!(modifyPartCost.getText().matches("(\\d+\\.\\d+)"))) {
            modifyErrorBox.getChildren().add(new Text("Error: Please enter a double for Price\n"));
        }
        if (!modifyPartStock.getText().matches("\\d+")) {
            modifyErrorBox.getChildren().add(new Text("Error: Please enter a number for Inv\n"));
        }
        if (!modifyPartMax.getText().matches("\\d+")) {
            modifyErrorBox.getChildren().add(new Text("Error: Please enter a number for Max\n"));
        }
        if (!modifyPartMin.getText().matches("\\d+")) {
            modifyErrorBox.getChildren().add(new Text("Error: Please enter a number for Min\n"));
        }
        if (radioButtons.getSelectedToggle().equals(inHouseRadioButton) && !modifyPartSpecial.getText().matches("\\d+")) {
            modifyErrorBox.getChildren().add(new Text("Error: Please enter a number for Machine ID\n"));
        } else if (modifyPartSpecial.getText() == "") {
            modifyErrorBox.getChildren().add(new Text("Error: Please enter a valid Company\n"));
        }

        if(modifyErrorBox.getChildren().size() > 0)
            return;

        if (Integer.parseInt(modifyPartStock.getText()) > Integer.parseInt(modifyPartMax.getText()) || Integer.parseInt(modifyPartStock.getText()) < Integer.parseInt(modifyPartMin.getText()) ){
            modifyErrorBox.getChildren().add(new Text("Error: Please enter a valid number for Inv\n"));
        }
        if(Integer.parseInt(modifyPartMax.getText()) <= Integer.parseInt(modifyPartMin.getText())){
            modifyErrorBox.getChildren().add(new Text("Error: Please enter valid Min/ Max values\n"));
        }
    }
}
