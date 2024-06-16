package fr.umontpellier.iut.trainsJavaFX.vues;

import fr.umontpellier.iut.trainsJavaFX.TrainsIHM;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class VueInterrogation extends Pane {

    private VueDuJeu vueDuJeu;
    private TrainsIHM ihm;

    @FXML
    private Button retour;

    public VueInterrogation(VueDuJeu vueDuJeu){
        this.vueDuJeu = vueDuJeu;
        loadFXML();
        actionRetour();
    }

    private void loadFXML(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/vueinterrogation.fxml"));
            loader.setController(this);
            loader.setRoot(this);
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setIhm(TrainsIHM ihm){
        this.ihm = ihm;
    }

    private void actionRetour(){
        retour.setOnAction(actionEvent -> {
            ihm.chargerVueDuJeu();
        });
    }
}
