package fr.umontpellier.iut.trainsJavaFX.vues;

import fr.umontpellier.iut.trainsJavaFX.GestionJeu;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
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
        setHgap(2);
        //setMaxWidth(400);

    }
    public void ajouterEnfant(VueCarteReserve vueCarte){
        add(vueCarte, compteur%val,compteur/val);
        //getChildren()
        compteur++;
    }

    public void creerBindings(){
        for(Node n : getChildren()){
            VueCarteReserve vue = (VueCarteReserve) n;
            n.addEventHandler(MouseEvent.MOUSE_CLICKED, action -> {
                GestionJeu.getJeu().uneCarteDeLaReserveEstAchetee(vue.getCarte().getNom());
            });
            GestionJeu.getJeu().getTaillesPilesReserveProperties().get(vue.getCarte().getNom()).addListener(
                    (source, oldValue, newValue) -> {
                        if(newValue.intValue()<oldValue.intValue()){
                            vue.decrementerCompteur();
                        }else{
                            vue.incrementerCompteur();
                        }
                    }
            );
        }
    }
}
