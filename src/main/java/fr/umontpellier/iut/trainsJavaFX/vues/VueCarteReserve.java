package fr.umontpellier.iut.trainsJavaFX.vues;

import fr.umontpellier.iut.trainsJavaFX.ICarte;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class VueCarteReserve {

    @FXML
    private Label nbReserve;
    @FXML
    private VueCarte vueCarte;

    public VueCarteReserve(ICarte carte, int n) {

        loadFXML();
        initialize(n); // Initialisez `nbReserve` avec la valeur `n` après le chargement du FXML
    }

    private void loadFXML() {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/carteReserve.fxml"));
        loader.setController(this);
        try {
            loader.setRoot(this);
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initialize(int n) {
        // `nbReserve` est déjà injecté par le FXML, il suffit de mettre à jour son texte.
        nbReserve.setText(String.valueOf(n));
    }
}
