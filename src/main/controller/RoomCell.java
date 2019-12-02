package main.controller;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import main.model.Room;

/**
 *
 * @author guru
 */
public class RoomCell extends ListCell<Room> {

    @FXML
    private ImageView avatarImageView;

    @FXML
    private Text roomNameText;

    @FXML
    private Text maxGuestsText;

    @FXML
    private Text maxKidsText;

    @FXML
    private Text descriptionText;

    @FXML
    private Label costPerNightLabel;

    @FXML
    private Button selectButton;

    public RoomCell() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/view/room_cell.fxml"));
            loader.setController(this);
            loader.setRoot(this);
            loader.load();

        } catch (IOException ex) {
            throw new RuntimeException(ex.getLocalizedMessage());
        }
    }

    @Override
    protected void updateItem(Room item, boolean empty) {
        super.updateItem(item, empty);

        if (empty) {
            setText(null);
            setContentDisplay(ContentDisplay.TEXT_ONLY);
        } else {
            Image image = new Image(item.getFilename());
            avatarImageView.setImage(image);
            avatarImageView.setFitHeight(128);
            avatarImageView.setFitWidth(160);
            roomNameText.setText(item.getName());
            maxGuestsText.setText("max Guests: " + String.valueOf(item.getMaxGuests()));
            maxKidsText.setText("max Kids: " + String.valueOf(item.getMaxKids()));
            descriptionText.setText(item.getDescription());
            costPerNightLabel.setText("KES " + new DecimalFormat("00.00").format(item.getCostPerNight()));

            selectButton.setOnAction(event -> {
                
                this.getScene().getWindow().hide();

                FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/view/single_room.fxml"));
                openNewWindow(loader);

                SingleRoomController singleRoomController = loader.getController();
                
                singleRoomController.setRoom(item);

            });

            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
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
