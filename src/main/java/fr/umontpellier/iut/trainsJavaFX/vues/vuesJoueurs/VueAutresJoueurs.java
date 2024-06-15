package fr.umontpellier.iut.trainsJavaFX.vues.vuesJoueurs;

import fr.umontpellier.iut.trainsJavaFX.IJoueur;
import fr.umontpellier.iut.trainsJavaFX.mecanique.CouleurJoueur;
import fr.umontpellier.iut.trainsJavaFX.vues.CouleursJoueurs;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Cette classe présente les éléments des joueurs autres que le joueur courant,
 * en cachant ceux que le joueur courant n'a pas à connaitre.
 * <p>
 * On y définit les bindings sur le joueur courant, ainsi que le listener à exécuter lorsque ce joueur change
 */
public class VueAutresJoueurs extends Pane {

    private IJoueur joueur;
    @FXML
    private Rectangle rectangleJoueur;
    @FXML
    private Label nomJoueur;
    @FXML
    private Label ptsVictoiresJoueur;
    @FXML
    private Label nbRailsJoueur;
    private String couleurJoueur;

    public VueAutresJoueurs(IJoueur joueur){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/autresJoueurs.fxml"));
            loader.setRoot(this);
            loader.setController(this);
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //this.nomJoueur = new Label(joueur.getNom());

        this.joueur = joueur;

        String couleurHex = CouleursJoueurs.couleursBackgroundJoueur.get(joueur.getCouleur());
        this.rectangleJoueur.setFill(Color.web(couleurHex,0.6));
        this.rectangleJoueur.setStroke(Color.web(couleurHex));

        this.nomJoueur.setText(String.valueOf(joueur.getNom()));
        this.setScaleX(0.72);
        this.setScaleY(0.72);

    }

    public void creerBindings(){
        this.joueur.nbJetonsRailsProperty().addListener(
                (source,oldValue,newValue) ->{
                    this.nbRailsJoueur.setText(newValue.toString());
                    actualiserPointsVictoires();
                }
        );
    }
    public IJoueur getJoueur(){
        return this.joueur;
    }
    public void actualiserPointsVictoires(){
        this.ptsVictoiresJoueur.setText(String.valueOf(this.joueur.getScoreTotal()));
    }
}
