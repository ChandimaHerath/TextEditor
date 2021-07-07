package controller;

import com.sun.javafx.print.PrinterJobImpl;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.print.PrinterJob;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
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
    public TextField txtReplace;
    public AnchorPane pneReplace;
    private final List<Index> searchIndexes = new ArrayList<>();
    private int findoffset = -1;
    private PrinterJob printerJob;


    public void initialize() {
        pneSearch.setVisible(false);
        pneReplace.setVisible(false);
        this.printerJob = PrinterJob.createPrinterJob();

        ChangeListener textListener = (ChangeListener<String>) (observable, oldValue, newValue) -> {
            search(newValue);
        };
        txtSearch.textProperty().addListener(textListener);
    }

    private void search(String query){
        try {
            Pattern regExp = Pattern.compile(query);
            Matcher matcher = regExp.matcher(txtEditor.getText());

            searchIndexes.clear();

            while (matcher.find()) {

                searchIndexes.add(new Index(matcher.start(), matcher.end()));
            }
        } catch (PatternSyntaxException e){

        }
    }

    public void mnuitemNew_OnAction(ActionEvent actionEvent) {
        txtEditor.clear();
        txtEditor.requestFocus();
    }

    public void mnuitemSaveAs_OnAction(ActionEvent actionEvent)  {

        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showSaveDialog(txtEditor.getScene().getWindow());

        if (file == null) return;

        try (FileWriter fw = new FileWriter(file); BufferedWriter br = new BufferedWriter(fw)){
            br.write(txtEditor.getText());

        } catch (IOException e) {
           new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }


    }

    public void mnuitemSave_OnAction(ActionEvent actionEvent) {
//         File saveFile = new File(System.getProperty("user.dir"));
//
//         try(FileWriter fileWriter = new FileWriter(saveFile);BufferedWriter br = new BufferedWriter(fileWriter)){
//           br.write(txtEditor.getText());
//         } catch (IOException e)
//         {
//             e.printStackTrace();
//         }

    }

    public void mnuitemOpen_OnAction(ActionEvent actionEvent) throws FileNotFoundException {
        FileChooser filechooser = new FileChooser();
        filechooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("All Text Files","*.txt","*.html"));
        filechooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("All Files","*.*"));
        File file = filechooser.showOpenDialog(txtEditor.getScene().getWindow());

        if(file == null) return;

        txtEditor.clear();

        try (FileReader fileReader = new FileReader(file); BufferedReader bufferedReader = new BufferedReader(fileReader);)
        {

            String line = null;
            while((line = bufferedReader.readLine())!=null){
            txtEditor.appendText(line+ '\n');

            }

        }
        catch (IOException e) {
            e.printStackTrace();
        }


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


    public void btnReplace_OnAction(ActionEvent actionEvent) {
        if (findoffset == -1) return;
        txtEditor.replaceText(searchIndexes.get(findoffset).startingIndex, searchIndexes.get(findoffset).endIndex, txtReplace.getText());
        search(txtSearch.getText());
    }


    public void btnReplaceAll_OnAction(ActionEvent actionEvent) {
        while (!searchIndexes.isEmpty()) {
            txtEditor.replaceText(searchIndexes.get(0).startingIndex, searchIndexes.get(0).endIndex, txtReplace.getText());
            search(txtSearch.getText());
        }

    }

    public void mnuitemPrint_OnAction(ActionEvent actionEvent) {

        printerJob.showPrintDialog(txtEditor.getScene().getWindow());
        printerJob.printPage(txtEditor.lookup("Text"));
    }

    public void mnuitemPageSetup_OnAction(ActionEvent actionEvent) {

        printerJob.showPageSetupDialog(txtEditor.getScene().getWindow());

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