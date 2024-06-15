package fr.umontpellier.iut.trainsJavaFX.vues;

import fr.umontpellier.iut.trainsJavaFX.GestionJeu;
import fr.umontpellier.iut.trainsJavaFX.ICarte;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class VueCarteReserve extends Pane {

    @FXML
    private Label nbReserve;
    /**
     * On ne peut pas gérer vueCarte de cette manière avec un fx:include parce qu'on a besoin
     * dans le code que vueCarte soit de type VueCarte (alors que cette manière implique que
     * vueCarte soit de type Pane)
     @FXML
     private Pane vueCarte;
     */
    private VueCarte vue;
    private ICarte carte;

    public VueCarteReserve(ICarte carte, int n) {
        initializeCarte(carte);
        loadFXML();
        initialize(carte, n);
    }

    private void loadFXML() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/carteReserve.fxml"));
            loader.setController(this);
            loader.setRoot(this);
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void initializeCarte(ICarte carte){
        this.carte = carte;
        vue = new VueCarte();
        vue.setCarte(carte);
        getChildren().add(vue);
    }

    private void initialize(ICarte carte, int n) {
        nbReserve.setText(String.valueOf(n));

        double v = 0.30;
        this.setPrefWidth(this.getPrefWidth() * v);
        this.setPrefHeight(this.getPrefHeight() * v);
        this.setScaleX(v);
        this.setScaleY(v);

    }
    public Label getImage(){
        return this.vue.getImage();
    }

    public ICarte getCarte(){
        return carte;
    }

    public void decrementerCompteur(){
        if(this.nbReserve.getText().equals("0")){

        } else if(this.nbReserve.getText().equals("1")){
            this.nbReserve.setText(String.valueOf(Integer.parseInt(nbReserve.getText())-1));
            vue.noirEtBlanc();
        } else{
            this.nbReserve.setText(String.valueOf(Integer.parseInt(nbReserve.getText())-1));
        }
    }
    public void incrementerCompteur(){
        this.nbReserve.setText(String.valueOf(Integer.parseInt(nbReserve.getText())+1));
        if(Integer.parseInt(nbReserve.getText()) > 0){
            vue.imageParDefaut();
        }
    }

}