package controller;

import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class EditorFormController {
    public TextArea txtEditor;
    public AnchorPane pneSearch;
    public Button btnNext;
    public Button btnPrevious;
    public TextField txtSearch;
    public AnchorPane pneReplace;
    private final List<Index> searchIndexes = new ArrayList<>();
    private int findoffset = -1;


    public void initialize() {
        pneSearch.setVisible(false);
        pneReplace.setVisible(false);


        txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                Pattern regExp = Pattern.compile(newValue);
                Matcher matcher = regExp.matcher(txtEditor.getText());

                searchIndexes.clear();

                while (matcher.find()) {

                    searchIndexes.add(new Index(matcher.start(), matcher.end()));
                }
            } catch (PatternSyntaxException e){

            }
        });




    }

    public void mnuitemNew_OnAction(ActionEvent actionEvent) {
        txtEditor.clear();
        txtEditor.requestFocus();
    }

    public void mnuitemSaveAs_OnAction(ActionEvent actionEvent) {
    }

    public void mnuitemOpen_OnAction(ActionEvent actionEvent) {
    }

    public void mnuitemSave_OnAction(ActionEvent actionEvent) {
    }

    public void mnuitemExit_OnAction(ActionEvent actionEvent) {
        Stage stage = (Stage) txtEditor.getScene().getWindow();
        stage.close();

    }


    public void mnuItemPaste_OnAction(ActionEvent actionEvent) {
        txtEditor.paste();
    }

    public void mnuitemFind_OnAction(ActionEvent actionEvent) {
        pneSearch.setVisible(true);
        txtSearch.requestFocus();

    }

    public void mnuitemReplace_OnAction(ActionEvent actionEvent) {
        pneSearch.setVisible(true);
        pneReplace.setVisible(true);
    }

    public void mnuitemAbout_OnAction(ActionEvent actionEvent) {
    }

    public void mnuitemSelectAll_OnAction(ActionEvent actionEvent) {

        txtEditor.selectAll();
    }


    public void mnuitemCopy_OnAction(ActionEvent actionEvent) {
        txtEditor.copy();

    }

    public void mnuitemCut_OnAction(ActionEvent actionEvent) {

        txtEditor.cut();
    }

    public void btnNext_OnAction(ActionEvent actionEvent) {
        if (!searchIndexes.isEmpty()){
            if(findoffset== -1){
                findoffset = 0;
            }
             txtEditor.selectRange(searchIndexes.get(findoffset).startingIndex, searchIndexes.get(findoffset).endIndex);
            findoffset++;
            if (findoffset >= searchIndexes.size()){
                findoffset=0;
            }


        }
        }


    public void btnPrevious_OnAction(ActionEvent actionEvent) {

        if (!searchIndexes.isEmpty()){
            if(findoffset==-1){
                findoffset= searchIndexes.size()-1;
            }
            txtEditor.selectRange(searchIndexes.get(findoffset).startingIndex, searchIndexes.get(findoffset).endIndex);
            findoffset--;
            if(findoffset<0){
                findoffset = searchIndexes.size()-1;
            }

        }
    }


}


class Index {
    int startingIndex;
    int endIndex;

    public Index(int startingIndex, int endIndex) {
        this.startingIndex = startingIndex;
        this.endIndex = endIndex;
    }

}