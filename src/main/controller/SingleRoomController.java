package main.controller;

import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import main.model.Room;
import main.model.repository.DbHandler;
import main.utils.AlertHelper;

/**
 *
 * @author guru
 */
public class SingleRoomController implements Initializable {

    private final DbHandler dbh = DbHandler.getInstance();

    private Room room = null;
    @FXML
    private ImageView fullImageView;
    @FXML
    private Button gotoMainButton;
    @FXML
    private Text descriptionText;
    @FXML
    private Text costPerNightText;
    @FXML
    private CheckBox hiredCheckbox;
    @FXML
    private CheckBox underMaintenanceCheckbox;
    @FXML
    private Text roomTypeText;
    @FXML
    private Text maxGuestsText;
    @FXML
    private Text maxKidsText;
    @FXML
    private Button maintenanceButton;
    @FXML
    private Button hireButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        gotoMainButton.setOnAction(event -> {

            try {
                gotoMainButton.getScene().getWindow().hide();

                Stage stage = new Stage(StageStyle.DECORATED);
                Parent parent = FXMLLoader.load(getClass().getResource("/main/view/main.fxml"));

                stage.setTitle("City Lodge JavaFx Application");
                stage.setScene(new Scene(parent));
                stage.show();

                stage.setOnCloseRequest((WindowEvent e) -> {
                    System.out.println("Window Closed: " + e.toString());
                });

            } catch (IOException ex) {
                System.err.println("IOException: " + ex.getLocalizedMessage());
            }
        });

        hireButton.setOnAction(event -> {
            rent();
        });

    }

    public void setRoom(Room room) {
        this.room = room;
        populateFields();
        System.out.println(room.getFilename());
    }

    private void populateFields() {
        fullImageView.setImage(new Image(room.getFilename()));

        descriptionText.setText(room.getDescription());
        costPerNightText.setText("KES " + new DecimalFormat("00.00").format(room.getCostPerNight()));
        hiredCheckbox.setSelected(!room.isAvailable());
        underMaintenanceCheckbox.setSelected(room.isUnderMaintenance());
        roomTypeText.setText(roomTypeText.getText() + " " + room.getType());
        maxGuestsText.setText("max Guests: " + room.getMaxGuests());
        maxKidsText.setText("max Kids: " + room.getMaxKids());

        if (room.isUnderMaintenance()) {
            maintenanceButton.setText("Complete Maintenamce");
        }

        if (!room.isAvailable()) {
            hireButton.setText("Return Room");
        }
    }

    private void rent() {

        if (room != null) {

            if (room.isAvailable()) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/view/hire_room.fxml"));
                openNewWindow(loader);
                HireController hireController = loader.getController();
                hireController.setRoom(room);

            } else {
                returnRoom();
            }

        }

    }

    private void returnRoom() {
        //Check hiring for the room
        //Check whether all the money is paid for the room
        //Return the room
        if (room != null) {
            if (dbh.executeAction("UPDATE rooms SET available = true WHERE id = " + room.getId())) {
                AlertHelper.success("Return Room", "Maintenance Started on the room");
            }
        }
    }

    @FXML
    private void performMaintenance(ActionEvent event) {
        if (room != null) {
            if (room.isAvailable()) {
                if (dbh.executeAction("UPDATE rooms SET under_maintenance = true WHERE id = " + room.getId())) {
                    AlertHelper.success("Perfoming Maintenance", "Maintenance Started on the room");
                }
            } else {

            }
        }
    }

    @FXML
    private void completeMaintenance(ActionEvent event) {

        if (room != null) {
            if (room.isUnderMaintenance()) {
                if (dbh.executeAction("UPDATE rooms SET under_maintenance = false WHERE id = " + room.getId())) {
                    AlertHelper.success("Perfoming Maintenance", "Maintenance Completed on the room");
                }
            } else {

            }
        }

    }

    /**
     * Open a new Stage
     *
     * @param loader FMLLoader that has the fxml that should be loaded
     */
    public void openNewWindow(FXMLLoader loader) {
        try {

            Parent parent = loader.load();

            Stage stage = new Stage(StageStyle.DECORATED);

            stage.setScene(new Scene(parent));

            stage.show();

        } catch (IOException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
