package fr.umontpellier.iut.trainsJavaFX.vues;

import fr.umontpellier.iut.trainsJavaFX.GestionJeu;
import fr.umontpellier.iut.trainsJavaFX.ICarte;
import fr.umontpellier.iut.trainsJavaFX.IJeu;
import fr.umontpellier.iut.trainsJavaFX.IJoueur;
import fr.umontpellier.iut.trainsJavaFX.mecanique.cartes.ListeDeCartes;
import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Cette classe présente les éléments appartenant au joueur courant.
 * <p>
 * On y définit les bindings sur le joueur courant, ainsi que le listener à exécuter lorsque ce joueur change
 */
public class VueJoueurCourant extends HBox{

    @FXML
    private FlowPane cartesEnMain;
    @FXML
    private FlowPane cartesEnJeu;
    @FXML
    private FlowPane cartesRecues;
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
    @FXML
    private Label nbCartesDefausse;

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

    }

    public void initialize(URL url, ResourceBundle resourceBundle) {
        creerBindings();
    }

    public IJoueur getJoueur() {
        return joueur;
    }

    public void creerBindings(){
        GestionJeu.getJeu().joueurCourantProperty().addListener(
                (source, oldValue, newValue) -> {
                    cartesEnMain.getChildren().clear();
                    ListeDeCartes l = newValue.mainProperty();
                    for (ICarte c : l) {
                        VueCarte carte = new VueCarte();
                        carte.setCarte(c);
                        carte.setTaille(0.30);
                        //carte.creerBindingsCartes(cartesEnMain, 1);
                        cartesEnMain.getChildren().add(carte);

                        carte.setCarteChoisieListener(event -> {
                            newValue.uneCarteDeLaMainAEteChoisie(c.getNom());
                        });
                    }
                    argent.textProperty().bind(newValue.argentProperty().asString());
                    nbPointsVictoire.textProperty().bind(newValue.scoreProperty().asString());
                    nbPointsRails.textProperty().bind(newValue.pointsRailsProperty().asString());
                    nbJetonsRails.textProperty().bind(newValue.nbJetonsRailsProperty().asString());
                    nbCartesPioche.textProperty().bind(newValue.piocheProperty().sizeProperty().asString());
                    nbCartesDefausse.textProperty().bind(newValue.defausseProperty().sizeProperty().asString());
                    nbJetonsGare.textProperty().bind(GestionJeu.getJeu().gareProperty().asString());
                }
        );

        for(IJoueur j : GestionJeu.getJeu().getJoueurs()){
            j.mainProperty().addListener(changecartelistener);
            j.cartesEnJeuProperty().addListener(jouecartelistener);
            j.cartesRecuesProperty().addListener(recoitcartelistener);
        }

    }

    private final ListChangeListener<ICarte> changecartelistener = change -> {
        while (change.next()) {
            if (change.wasRemoved()) {
                for (ICarte c : change.getRemoved()) {
                    cartesEnMain.getChildren().remove(trouverVueCarte(c, cartesEnMain));
                }
            }else if(change.wasAdded()){
                for (ICarte c : change.getAddedSubList()) {
                    VueCarte carte = new VueCarte();
                    carte.setCarte(c);
                    carte.setTaille(0.30);
                   // carte.creerBindingsCartes(cartesEnMain, 1);
                    cartesEnMain.getChildren().add(carte);

                    carte.setCarteChoisieListener(event -> {
                        GestionJeu.getJeu().joueurCourantProperty().getValue().uneCarteDeLaMainAEteChoisie(c.getNom());
                    });
                }
            }
        }
    };

    private final ListChangeListener<ICarte> jouecartelistener = change -> {
        while (change.next()) {
            if (change.wasRemoved()) {
                for (ICarte c : change.getRemoved()) {
                    cartesEnJeu.getChildren().remove(trouverVueCarte(c, cartesEnJeu));
                }
            }else if(change.wasAdded()){
                for (ICarte c : change.getAddedSubList()) {
                    VueCarte carte = new VueCarte();
                    carte.setCarte(c);
                    carte.setTaille(0.15);
                   // carte.creerBindingsCartes(cartesEnJeu, 2);
                    cartesEnJeu.getChildren().add(carte);

                    carte.setCarteChoisieListener(event -> {
                        GestionJeu.getJeu().joueurCourantProperty().getValue().uneCarteEnJeuAEteChoisie(c.getNom());
                    });
                }
            }
        }
    };

    private final ListChangeListener<ICarte> recoitcartelistener = change -> {
        while (change.next()) {
            if (change.wasRemoved()) {
                for (ICarte c : change.getRemoved()) {
                    cartesRecues.getChildren().remove(trouverVueCarte(c, cartesRecues));
                }
            }else if(change.wasAdded()){
                for (ICarte c : change.getAddedSubList()) {
                    VueCarte carte = new VueCarte();
                    carte.setCarte(c);
                    carte.setTaille(0.15);
                   // carte.creerBindingsCartes(cartesRecues, 2);
                    cartesRecues.getChildren().add(carte);

                }
            }
        }
    };




    private VueCarte trouverVueCarte(ICarte carteATrouver, FlowPane listeCarte){
        for(Node node : listeCarte.getChildren()){
            VueCarte vue = (VueCarte) node;
            if(carteATrouver.getNom().equals(vue.getCarte().getNom())){
                return vue;
            }
        }

        return null;
    }

}
