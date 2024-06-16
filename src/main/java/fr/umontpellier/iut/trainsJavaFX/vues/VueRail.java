package fr.umontpellier.iut.trainsJavaFX.vues;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;

import java.io.IOException;

public class VueRail extends Pane {

    @FXML
    private ImageView rails;
    @FXML
    private Line barre1;
    @FXML
    private Line barre2;
    @FXML
    private Line barre3;
    @FXML
    private Line barre4;
    @FXML
    private Line barre5;

    private int indice;

    public VueRail(){
        loadFXML();
    }

    private void loadFXML(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/rail.fxml"));
            loader.setRoot(this);
            loader.setController(this);
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setColor(Paint paint) {
        barre1.setStroke(paint);
        barre2.setStroke(paint);
        barre3.setStroke(paint);
        barre4.setStroke(paint);
        barre5.setStroke(paint);

    }
    public void setNum(int num){
        this.indice = num;
    }
    public int getIndice(){
        return this.indice;
    }
}
