/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.utils;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

/**
 *
 * @author guru
 */
public class AlertHelper {
    
    
    /**
     * Alert to show Error 
     * @param title of the alert
     * @param body content of the alert
     */
    public static void error(String title, String body){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(title);
        alert.setContentText(body);
        alert.showAndWait();
    }

    /**
     * Alert to show Success / Information 
     * @param title of the alert
     * @param body content of the alert
     */    
    public static void success(String title, String body){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(title);
        alert.setContentText(body);
        alert.showAndWait();
    }
    
    public static boolean confirm(String title, String body){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText(title);
        alert.setContentText(body);
        alert.showAndWait();
        
        return alert.getResult() == ButtonType.OK;
    }
    
}
