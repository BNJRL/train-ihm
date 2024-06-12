package fr.umontpellier.iut.trainsJavaFX.vues;

import javafx.scene.Node;
import javafx.scene.layout.FlowPane;

import java.util.ArrayList;
import java.util.List;

public class VueReserve extends FlowPane {

    List<VueCarteReserve> cartes;

    public VueReserve(){
        this.cartes = new ArrayList<>();

    }
    public void ajouterEnfant(VueCarteReserve vueCarte){
        getChildren().add(vueCarte);
    }
}
