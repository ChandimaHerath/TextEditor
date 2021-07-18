package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class AboutViewController {

    @FXML
    private AnchorPane pneAbout;

    public void initializer(){

    }

    public void OnClick_btnOK(ActionEvent actionEvent) {
        Stage stage = (Stage) pneAbout.getScene().getWindow();
        stage.close();

    }
}
