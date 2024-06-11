package fr.umontpellier.iut.trainsJavaFX.vues;

import fr.umontpellier.iut.trainsJavaFX.ICarte;
import fr.umontpellier.iut.trainsJavaFX.IJeu;
import fr.umontpellier.iut.trainsJavaFX.IJoueur;
import fr.umontpellier.iut.trainsJavaFX.mecanique.cartes.Carte;
import fr.umontpellier.iut.trainsJavaFX.mecanique.cartes.ListeDeCartes;
import javafx.collections.ListChangeListener;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.ArrayList;
import java.util.List;

/**
 * Cette classe correspond à la fenêtre principale de l'application.
 *
 * Elle est initialisée avec une référence sur la partie en cours (Jeu).
 * <p>
 * On y définit les bindings sur les éléments internes qui peuvent changer
 * (le joueur courant, ses cartes en main, son score, ...)
 * ainsi que les listeners à exécuter lorsque ces éléments changent
 */
public class VueDuJeu extends VBox {

    @FXML
    private final IJeu jeu;
    @FXML
    private VuePlateau plateau;
    @FXML
    private VueJoueurCourant joueurCourant;
    @FXML
    private HBox autresJoueurs;
    @FXML
    private GridPane reserve;

    private List<VueAutresJoueurs> vueAutresJoueursList;

    public VueDuJeu(IJeu jeu) {
        this.jeu = jeu;
        plateau = new VuePlateau();
        reserve = new GridPane();
        joueurCourant = new VueJoueurCourant(this);
        autresJoueurs = new HBox();

        vueAutresJoueursList = new ArrayList<>();
        for(IJoueur j : jeu.getJoueurs()){
            vueAutresJoueursList.add(new VueAutresJoueurs(j));
        }

        HBox centre = new HBox();
        getChildren().add(autresJoueurs);
        centre.getChildren().addAll(plateau, reserve);
        getChildren().addAll(centre, joueurCourant);
    }

    public void creerBindings() {
        plateau.prefWidthProperty().bind(getScene().widthProperty());
        plateau.prefHeightProperty().bind(getScene().heightProperty());
        plateau.creerBindings();
        joueurCourant.creerBindings();
        this.getJeu().joueurCourantProperty().addListener(
                (source, oldValue, newValue) -> {
                    autresJoueurs.getChildren().clear();
                    for(VueAutresJoueurs vAJ : vueAutresJoueursList){
                        if(!newValue.equals(vAJ.getJoueur())){
                            autresJoueurs.getChildren().add(vAJ);
                        }
                    }
                }
        );
    }

    public IJeu getJeu() {
        return jeu;
    }

    EventHandler<? super MouseEvent> actionPasserParDefaut = (mouseEvent -> System.out.println("Vous avez choisi Passer"));
}
