package main.controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javax.imageio.ImageIO;
import main.model.Room;
import main.model.repository.DbHandler;
import main.utils.AlertHelper;

/**
 *
 * @author guru
 */
public class AddRoomController implements Initializable {

    public static final String DEFAULT_SMALL_IMAGE = "images/noimage-small.png";
    private String imageToSave = null;

    @FXML
    private ImageView avatarImageView;
    @FXML
    private TextField roomNameField;
    @FXML
    private Button chooseImageButton;
    @FXML
    private Label savedFileNameLabel;
    @FXML
    private ComboBox<String> roomTypeCombobox;
    @FXML
    private CheckBox underMaintenanceCheckBox;
    @FXML
    private CheckBox availableCheckBox;
    @FXML
    private TextField costPerNightField;
    @FXML
    private TextField maxGuestsField;
    @FXML
    private TextArea descriptionTextArea;
    @FXML
    private Button addRoomButton;
    @FXML
    private AnchorPane rooPane;
    @FXML
    private Button saveImageButton;
    @FXML
    private TextField maxKidsField;

    private final DbHandler dbh = DbHandler.getInstance();

    /**
     * Initializes the Controller
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        File file = new File(DEFAULT_SMALL_IMAGE);

        Image image = new Image(file.toURI().toString());
        avatarImageView.setImage(image);

        chooseImageButton.setOnAction(event -> {

            FileChooser chooseAvatarFileChooser = new FileChooser();
            chooseAvatarFileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
            FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("image/*", "jpg", "jpeg");
            chooseAvatarFileChooser.setSelectedExtensionFilter(filter);
            chooseAvatarFileChooser.setTitle("Choose Room Image");

            File choosedFile = chooseAvatarFileChooser.showOpenDialog(rooPane.getScene().getWindow());
            if (choosedFile != null) {
                Image choosedImage = new Image(choosedFile.toURI().toString());
                avatarImageView.setImage(choosedImage);

            } else {
                AlertHelper.error("Chossing Image", "Please Choose an Image");
            }

        });

        underMaintenanceCheckBox.setSelected(false);
        availableCheckBox.setSelected(true);

        roomTypeCombobox.setItems(FXCollections.observableArrayList("Standard Room", "Suite"));

        saveImageButton.setOnAction(event -> {
            handSaveImageButtonClick(event);
        });

        addRoomButton.setOnAction(event -> {
            handleAddRoomButtonClick(event);
        });

    }

    private void handSaveImageButtonClick(ActionEvent event) {
        FileChooser fileCHooser = new FileChooser();
        fileCHooser.setTitle("Save Image");

        File newFile = fileCHooser.showSaveDialog(saveImageButton.getScene().getWindow());

        if (newFile != null) {
            try {

                ImageIO.write(SwingFXUtils.fromFXImage(avatarImageView.getImage(), null), "jpg", newFile);
                savedFileNameLabel.setText(newFile.toURI().toString());

                imageToSave = savedFileNameLabel.getText();

            } catch (IOException ex) {
                Logger.getLogger(
                        MainController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void handleAddRoomButtonClick(ActionEvent event) {
        double costPerNight;
        int maxGuests, maxKids;

        String name = roomNameField.getText();
        if (name.length() < 3) {
            AlertHelper.error("Add Room", "Name should be atleast 3 characters");
            return;
        }

        String type = roomTypeCombobox.getSelectionModel().getSelectedItem();
        if (type == null) {
            AlertHelper.error("Add Room", "Select Type PLease");
            return;
        }

        try {
            costPerNight = Double.parseDouble(costPerNightField.getText());
        } catch (NumberFormatException e) {
            System.err.println("NumberFormatException: " + e.getLocalizedMessage());
            AlertHelper.error("Add Room", "Cost Per Night Must be a Number");
            return;
        }

        try {
            maxGuests = Integer.parseInt(maxGuestsField.getText());
        } catch (NumberFormatException e) {
            System.err.println("NumberFormatException: " + e.getLocalizedMessage());
            AlertHelper.error("Add Room", "Maximum Guests Must be a Number");
            return;
        }

        try {
            maxKids = Integer.parseInt(maxKidsField.getText());
        } catch (NumberFormatException e) {
            System.err.println("NumberFormatException: " + e.getLocalizedMessage());
            AlertHelper.error("Add Room", "Maximum Kids Must be a Number");
            return;
        }

        String description = descriptionTextArea.getText();

        if (description.isEmpty()) {
            AlertHelper.error("Add Room", "Description is required");
            return;
        }

        if (imageToSave == null) {
            if (!AlertHelper.confirm("Add Room", "Save Room Without Image")) {
                return;
            }

            imageToSave = "file:/home/azenga/NetBeansProjects/CityLodge/images/noimage-small.png";
        }

        Room room = new Room(name, imageToSave, maxGuests, maxKids, costPerNight, underMaintenanceCheckBox.isSelected(), description, availableCheckBox.isSelected(), type);

        int result = dbh.addRoom(room);

        if (result != -1) {
            System.out.println("Result: " + result);
            AlertHelper.success("Add Room", "Operation Successfull");

        }

    }

}
