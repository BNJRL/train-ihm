package fr.umontpellier.iut.trainsJavaFX.vues;

import fr.umontpellier.iut.trainsJavaFX.ICarte;
import fr.umontpellier.iut.trainsJavaFX.IJoueur;
import fr.umontpellier.iut.trainsJavaFX.mecanique.cartes.ListeDeCartes;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.io.IOException;

/**
 * Cette classe présente les éléments appartenant au joueur courant.
 * <p>
 * On y définit les bindings sur le joueur courant, ainsi que le listener à exécuter lorsque ce joueur change
 */
public class VueJoueurCourant extends VBox {

    @FXML
    private Label nomJoueur;
    @FXML
    private Button passer;
    @FXML
    private HBox listeBoutons;
    @FXML
    private VueDuJeu vueDuJeu;
    @FXML
    private Label instruction;

    public VueJoueurCourant(VueDuJeu vueDuJeu) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/joueurCourant.fxml"));
            loader.setRoot(this);
            loader.setController(this);
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.vueDuJeu = vueDuJeu;
        passer.setText("passer");



    }

    public void creerBindings(){
        instruction.textProperty().bind(vueDuJeu.getJeu().instructionProperty());
        passer.setOnAction(event -> vueDuJeu.getJeu().passerAEteChoisi());
        vueDuJeu.getJeu().joueurCourantProperty().addListener(
                (source, oldValue, newValue) -> {
                    nomJoueur.setText(newValue.getNom() + ": ");
                    listeBoutons.getChildren().clear();
                    ListeDeCartes l = newValue.mainProperty();
                    for (ICarte c : l) {
                        Button but = new Button(c.getNom());
                        listeBoutons.getChildren().add(but);

                        but.setOnAction(event -> {
                            newValue.uneCarteDeLaMainAEteChoisie(c.getNom());
                        });

                    }
                }
        );

        for(IJoueur j : vueDuJeu.getJeu().getJoueurs()){
            j.mainProperty().addListener(changecartelistener);
        }
    }

    private final ListChangeListener<ICarte> changecartelistener = change -> {
        while (change.next()) {
            if (change.wasRemoved()) {
                for (ICarte c : change.getRemoved()) {
                    listeBoutons.getChildren().remove(trouverBoutonCarte(c));
                }
            }
        }
    };


    private Button trouverBoutonCarte(ICarte carteATrouver){
        for(Node but : listeBoutons.getChildren()){
            Button bout = (Button) but;
            if(carteATrouver.getNom().equals(bout.getText())){
                return bout;
            }
        }

        return null;
    }
}
