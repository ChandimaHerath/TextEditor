package controller;

import com.sun.javafx.print.PrinterJobImpl;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.print.PrinterJob;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.DragEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.prefs.Preferences;
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
    String text;




    public void initialize() {
        //clear the properties file
        pneSearch.setVisible(false);
        pneReplace.setVisible(false);
        this.printerJob = PrinterJob.createPrinterJob();
        this.text = txtEditor.getText();


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

          saveFile();

//        FileChooser fileChooser = new FileChooser();
//        File file = fileChooser.showSaveDialog(txtEditor.getScene().getWindow());
//        File appSettings = new File(System.getProperty("user.dir") ,"TextEditor.properties" );
//        prop.setProperty("Saved File", file.toString());
//
//        System.out.println(prop.getProperty("Saved File"));
//
//        if (file == null) return;
//
//        try (FileWriter fw = new FileWriter(file); BufferedWriter br = new BufferedWriter(fw)){
//            br.write(txtEditor.getText());
//
//        } catch (IOException e) {
//           new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
//        }
//
//        try(FileOutputStream outputStream = new FileOutputStream(appSettings);
//            BufferedOutputStream bos = new BufferedOutputStream(outputStream)){
//
//            prop.store(bos,"Property Updated!");
//        }
//        catch (IOException e) {
//            e.printStackTrace();
//        }

    }

    public void mnuitemSave_OnAction(ActionEvent actionEvent) {

        if(!text.equals(txtEditor.getText())){
            text = txtEditor.getText();
            saveFile();
        }

        // System.out.println(prop.getProperty("Saved File"));
//        File saveFile = new File(prop.getProperty("Saved File"));
//        try(FileWriter fileWriter = new FileWriter(saveFile);BufferedWriter br = new BufferedWriter(fileWriter)){
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

        Stage primaryStage = new Stage();
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/view/AboutView.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        primaryStage.setTitle("About");
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();

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

    private void saveFile(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save File");
        File file = fileChooser.showSaveDialog(txtEditor.getScene().getWindow());

        if (file == null) return;

        try (FileWriter fileWriter = new FileWriter(file);
             BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
            bufferedWriter.write(txtEditor.getText());
        } catch (IOException e) {
            new Alert(Alert.AlertType.ERROR, "Can't save the file", ButtonType.CLOSE).show();
        }
    }

    public void txtEditorDragDropped_OnAction(DragEvent dragEvent) {
        if (dragEvent.getDragboard().hasFiles()) {
            File file = dragEvent.getDragboard().getFiles().get(0);
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
    }

    public void txtEditorDragOver_OnAction(DragEvent dragEvent) {
        if (dragEvent.getDragboard().hasFiles()) {
            dragEvent.acceptTransferModes(TransferMode.ANY);
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