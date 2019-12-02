package main.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import main.model.Hire;
import main.model.Room;
import main.model.repository.DbHandler;
import main.utils.AlertHelper;

/**
 *
 * @author guru
 */
public class HireController implements Initializable {

    private Room room = null;

    @FXML
    private Text roomIdText;
    @FXML
    private Text roomNameText;
    @FXML
    private Text roomTypeText;
    @FXML
    private Text maxGuestsText;
    @FXML
    private Text maxKidsText;
    @FXML
    private Text costPerNightText;
    @FXML
    private Text underMaintenanceText;
    @FXML
    private Text availableText;
    @FXML
    private TextField cusNameField;
    @FXML
    private TextField numOfGuestsField;
    @FXML
    private TextField numOfKidsField;
    @FXML
    private TextField depositField;
    @FXML
    private Button completeHigherButton;

    private final DbHandler dbh = DbHandler.getInstance();

    /**
     * Initializes the UI view
     *
     * @param location
     * @param resources
     *
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        completeHigherButton.setOnAction(event -> {
            handleCompleteHireButtonClicked();
        });

    }

    public void setRoom(Room room) {
        this.room = room;

        updateUI();
    }

    private void updateUI() {
        roomIdText.setText(roomIdText.getText() + room.getId());
        roomNameText.setText(roomNameText.getText() + room.getName());
        roomTypeText.setText(roomTypeText.getText() + room.getType());
        maxGuestsText.setText(maxGuestsText.getText() + room.getMaxGuests());
        maxKidsText.setText(maxKidsText.getText() + room.getMaxKids());
        costPerNightText.setText(costPerNightText.getText() + room.getCostPerNight());
        underMaintenanceText.setText(underMaintenanceText.getText() + (room.isUnderMaintenance() ? "Yes" : "No"));
        availableText.setText(availableText.getText() + (room.isAvailable() ? "Yes" : "No"));
    }

    private void handleCompleteHireButtonClicked() {
        String customer = cusNameField.getText();
        int numOfGuests, numOfKids;
        double deposit;

        if (customer.isEmpty()) {
            AlertHelper.error("Hiring", "Customer name is required");
            return;
        }

        try {
            numOfGuests = Integer.parseInt(numOfGuestsField.getText());
        } catch (NumberFormatException ex) {
            System.err.println("NumberFormatException: " + ex.getLocalizedMessage());
            AlertHelper.error("Hiring", "Number of guests is required\nIt must be a number");
            return;
        }

        if (numOfGuests > room.getMaxGuests()) {
            AlertHelper.error("Hiring", "Number of guests limit exceeded");
            return;
        }

        try {
            deposit = Double.parseDouble(depositField.getText());
        } catch (NumberFormatException ex) {
            System.err.println("NumberFormatException: " + ex.getLocalizedMessage());
            AlertHelper.error("Hiring", "Deposit is required\nIt must be a number");
            return;
        }

        try {
            numOfKids = Integer.parseInt(numOfKidsField.getText());
        } catch (NumberFormatException ex) {
            System.err.println("NumberFormatException: " + ex.getLocalizedMessage());
            AlertHelper.error("Hiring", "Number of kids is required\nIt must be a number");
            return;
        }

        if (numOfKids > room.getMaxKids()) {
            AlertHelper.error("Hiring", "Number of kids limit exceeded");
            return;
        }

        if (deposit > room.getCostPerNight()) {
            if (!AlertHelper.confirm("Hiring", "Are you sure you want pay this amount?\nIt's more than cost per night.")) {
                return;
            }
        }

        Hire hire = new Hire(0, room.getId(), customer, deposit, null, null, numOfGuests, numOfKids);

        if (dbh.addNewHire(hire)) {

            if (dbh.executeAction("UPDATE rooms SET available = false, hires = hires + 1 WHERE id = " + room.getId())) {

                AlertHelper.success("Hiring", room.getName() + " successfully hired");
                roomNameText.getScene().getWindow().hide();
            }
        }
    }

}
