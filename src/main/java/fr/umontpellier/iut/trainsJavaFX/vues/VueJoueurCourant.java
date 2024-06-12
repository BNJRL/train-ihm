package fr.umontpellier.iut.trainsJavaFX.vues;

import fr.umontpellier.iut.trainsJavaFX.GestionJeu;
import fr.umontpellier.iut.trainsJavaFX.ICarte;
import fr.umontpellier.iut.trainsJavaFX.IJeu;
import fr.umontpellier.iut.trainsJavaFX.IJoueur;
import fr.umontpellier.iut.trainsJavaFX.mecanique.cartes.ListeDeCartes;
import javafx.beans.property.IntegerProperty;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.io.IOException;

/**
 * Cette classe présente les éléments appartenant au joueur courant.
 * <p>
 * On y définit les bindings sur le joueur courant, ainsi que le listener à exécuter lorsque ce joueur change
 */
public class VueJoueurCourant extends HBox{

    @FXML
    private HBox listeBoutons;
    @FXML
    private IJoueur joueur;
    @FXML
    private Label nbPointsVictoire;
    @FXML
    private Label nbJetonsRails;
    @FXML
    private Label nbJetonsGare;
    @FXML
    private Label argent;
    @FXML
    private Label nbPointsRails;
    @FXML
    private Label nbCartesPioche;

    public VueJoueurCourant(IJoueur joueur) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/joueurCourant.fxml"));
            loader.setRoot(this);
            loader.setController(this);
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.joueur = joueur ;
        nbCartesPioche.setText(String.valueOf(joueur.piocheProperty().getSize()));
        nbJetonsRails.setText(String.valueOf(joueur.nbJetonsRailsProperty().get()));
        nbPointsVictoire.setText(String.valueOf(joueur.scoreProperty().get()));
        nbPointsRails.setText(String.valueOf(joueur.pointsRailsProperty().get()));
        argent.setText(String.valueOf(joueur.argentProperty().get()));
    }

    public IJoueur getJoueur() {
        return joueur;
    }

    public void creerBindings(){
        GestionJeu.getJeu().joueurCourantProperty().addListener(
                (source, oldValue, newValue) -> {
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
        for(IJoueur j : GestionJeu.getJeu().getJoueurs()){
            j.mainProperty().addListener(changecartelistener);
        }
        joueur.nbJetonsRailsProperty().addListener(
                (source, oldValue, newValue) -> {
                    nbJetonsRails.setText(String.valueOf(newValue));
                }
        );
        joueur.argentProperty().addListener(
                (source, oldValue, newValue) -> {
                    argent.setText(String.valueOf(newValue));
                }
        );
        joueur.piocheProperty().addListener(
                (source, oldValue, newValue) -> {
                    nbCartesPioche.setText(String.valueOf(newValue.size()));
                }
        );
        joueur.pointsRailsProperty().addListener(
                (source, oldValue, newValue) -> {
                    nbPointsRails.setText(String.valueOf(newValue));
                }
        );
        joueur.scoreProperty().addListener(
                (source, oldValue, newValue) -> {
                    nbPointsVictoire.setText(String.valueOf(newValue));
                }
        );
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
