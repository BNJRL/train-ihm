package fr.umontpellier.iut.trainsJavaFX.vues;

import javafx.scene.Node;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

import java.util.ArrayList;
import java.util.List;

public class VueReserve extends GridPane {

    int compteur;
    int val;

    public VueReserve(){
        this.compteur = 0;
        this.val = 6;
        //setMaxWidth(400);

    }
    public void ajouterEnfant(VueCarteReserve vueCarte){
        add(vueCarte, compteur%val,compteur/val);
        compteur++;
    }
}
