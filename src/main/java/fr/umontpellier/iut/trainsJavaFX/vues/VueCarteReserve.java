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
    @FXML
    private Pane vueCarte;  // Déclarez vueCarte comme une instance de VueCarte
    private VueCarte vueCarteVue;
    private ICarte carte;

    public VueCarteReserve(ICarte carte, int n) {
        loadFXML();
        initialize(carte, n);  // Initialisez avec les valeurs après le chargement du FXML

        double v = 0.30;
        this.setPrefWidth(this.getPrefWidth() * v);
        this.setPrefHeight(this.getPrefHeight() * v);
        this.setScaleX(v);
        this.setScaleY(v);

    }

    private void loadFXML() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/carteReserve.fxml"));
            loader.setController(this);
            loader.setRoot(this);  // Assurez-vous d'utiliser l'instance actuelle comme racine
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initialize(ICarte carte, int n) {

        // Vérifiez si `vueCarte` est bien initialisé après le chargement du FXML
        if (vueCarte != null) {
            // Créez et ajoutez une instance de VueCarte à vueCarte
            this.carte = carte;
            vueCarteVue = new VueCarte(carte);
            vueCarte.getChildren().add(vueCarteVue);
        } else {
            System.err.println("Erreur : vueCarte n'est pas initialisé !");
        }

        // Initialisez nbReserve
        if (nbReserve != null) {
            nbReserve.setText(String.valueOf(n));
        } else {
            System.err.println("Erreur : nbReserve n'est pas initialisé !");
        }

    }
    public ICarte getCarte(){
        return carte;
    }

    public void decrementerCompteur(){

        if(this.nbReserve.getText().equals("0")){

        } else if(this.nbReserve.getText().equals("1")){
            this.nbReserve.setText(String.valueOf(Integer.parseInt(nbReserve.getText())-1));
            vueCarteVue.noirEtBlanc();
        } else{
            this.nbReserve.setText(String.valueOf(Integer.parseInt(nbReserve.getText())-1));
        }
    }
    public void incrementerCompteur(){
        this.nbReserve.setText(String.valueOf(Integer.parseInt(nbReserve.getText())+1));
        if(Integer.parseInt(nbReserve.getText()) > 0){
            vueCarteVue.imageParDefaut();
        }
    }

}
