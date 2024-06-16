package fr.umontpellier.iut.trainsJavaFX.vues.vuesCartes;

import fr.umontpellier.iut.trainsJavaFX.GestionJeu;
import fr.umontpellier.iut.trainsJavaFX.ICarte;
import fr.umontpellier.iut.trainsJavaFX.mecanique.cartes.ListeDeCartes;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

public class VueListeCarte extends Pane {

    private ListeDeCartes property;
    private final int taille;

    @FXML
    private GridPane gridPane;

    public VueListeCarte(ListeDeCartes property, int taille){
        this.property.bind(property);
        this.taille = taille;
    }


    private final ListChangeListener<ICarte> changeCarteListener = change -> {
        while (change.next()) {
            if (change.wasRemoved()) {
                for (ICarte c : change.getRemoved()) {
                    gridPane.getChildren().remove(trouverVueCarte(c, gridPane));
                }
            }else if(change.wasAdded()){
                for (ICarte c : change.getAddedSubList()) {
                    VueCarte carte = new VueCarte();
                    carte.setCarte(c);
                    carte.activerExclamation(false);
                    carte.setTaille(0.30);
                    gridPane.getChildren().add(carte);

                    carte.setCarteChoisieListener(event -> {
                        GestionJeu.getJeu().joueurCourantProperty().getValue().uneCarteDeLaMainAEteChoisie(c.getNom());
                    });
                }
            }
        }
    };

    private VueCarte trouverVueCarte(ICarte carteATrouver, GridPane listeCarte){
        for(Node node : listeCarte.getChildren()){
            VueCarte vue = (VueCarte) node;
            if(carteATrouver.getNom().equals(vue.getCarte().getNom())){
                return vue;
            }
        }
        return null;
    }
}
