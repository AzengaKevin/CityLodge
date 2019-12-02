/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.model.factory;

import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;
import main.controller.RoomCell;
import main.model.Room;

/**
 *
 * @author guru
 */
public class RoomCellFactory implements Callback<ListView<Room>, ListCell<Room>>{

    @Override
    public ListCell<Room> call(ListView<Room> param) {
        return new RoomCell();
    }
    
}
