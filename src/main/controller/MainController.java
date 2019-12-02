package main.controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import main.model.Hire;
import main.model.Room;
import main.model.exception.InvalidObjectStringFormatException;
import main.model.factory.RoomCellFactory;
import main.model.repository.DbHandler;
import main.model.repository.FileHandler;
import main.utils.AlertHelper;

/**
 *
 * @author guru
 */
public class MainController implements Initializable {

    @FXML
    AnchorPane mainAnchorPane;
    @FXML
    private ListView<Room> roomsListView;

    private final DbHandler dbh = DbHandler.getInstance();
    private final FileHandler fh = new FileHandler();

    /**
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        roomsListView.setCellFactory(new RoomCellFactory());

        updateRooms();

        dbh.getAllHires().forEach(System.out::println);

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

            stage.showAndWait();

            stage.setOnCloseRequest(event -> {
                updateRooms();
            });

        } catch (IOException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @FXML
    public void closeWindow(Event event) {
        Stage stage = (Stage) mainAnchorPane.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void loadAddRoom(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/view/add_room.fxml"));
        openNewWindow(loader);
    }

    @FXML
    private void refreshRoomsList(ActionEvent event) {
        updateRooms();
    }

    @FXML
    private void truncateDatabaseTables(ActionEvent event) {
        dbh.deleteRoomsTable();
        dbh.deleteHiresTable();

        updateRooms();
    }

    private void updateRooms() {
        ObservableList<Room> rooms = dbh.getAllRooms();

        if (rooms.isEmpty()) {
            AlertHelper.success("Getting Rooms", "No Rooms Avaiables");
            roomsListView.setItems(rooms);
        } else {
            roomsListView.setItems(rooms);
        }
    }

    @FXML
    private void exportData(ActionEvent event) {

        StringBuilder dataToExport = new StringBuilder();

        dbh.getAllRooms().stream().map((room) -> {
            dataToExport.append(room);
            return room;
        }).forEachOrdered((room) -> {
            dataToExport.append("\n");
            dataToExport.append(getRoomHires(room));
        });

        DirectoryChooser dc = new DirectoryChooser();

        File file = dc.showDialog(roomsListView.getScene().getWindow());

        System.out.println("Directory: " + file.getAbsolutePath());

        if (fh.writeToFile(file.getAbsolutePath(), dataToExport.toString().trim())) {
            AlertHelper.success("Exporting Data", "Data Exported Successfully");
        }
    }

    @FXML
    private void importData(ActionEvent event) {
        FileChooser importDataFileChooser = new FileChooser();
        importDataFileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("text/*", "txt");
        importDataFileChooser.setSelectedExtensionFilter(filter);
        importDataFileChooser.setTitle("Choose Data File");

        File choosedFile = importDataFileChooser.showOpenDialog(roomsListView.getScene().getWindow());
        if (choosedFile != null) {

            String data = fh.readFronFile(choosedFile.getAbsolutePath());

            parseDataAndInsert(data);

        } else {
            AlertHelper.error("Importing Data", "Choose File to Import data");
        }
    }

    private String getRoomHires(Room room) {

        StringBuilder dataToExport = new StringBuilder();

        dbh.getAllHiresByRoom(room.getId()).stream().map((hire) -> {
            dataToExport.append(hire);
            return hire;
        }).forEachOrdered((_item) -> {
            dataToExport.append('\n');
        });

        return dataToExport.toString();
    }

    private void parseDataAndInsert(String data) {

        int roomId = 1;

        try {
            for (String line : data.split("\n")) {
                System.out.println("Line: " + line);
                if (line.startsWith("Room")) {
                    if (dbh.addRoom(Room.fromString(line)) != -1) {
                        roomId++;
                    }
                }
                if (line.startsWith("Hire")) {
                    Hire hire = Hire.fromString(line);
                    hire.setRoomId(roomId);

                } else {
                    System.out.println("Faulty Line: " + line);
                }
            }

        } catch (InvalidObjectStringFormatException e) {
            e.printStackTrace();
        }

    }

}
