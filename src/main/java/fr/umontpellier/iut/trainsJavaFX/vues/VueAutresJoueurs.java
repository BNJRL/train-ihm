package fr.umontpellier.iut.trainsJavaFX.vues;

import fr.umontpellier.iut.trainsJavaFX.IJoueur;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

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

    private VueDuJeu vueDuJeu;


    public VueAutresJoueurs(IJoueur joueur){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/autresJoueurs.fxml"));
            loader.setRoot(this);
            loader.setController(this);
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.joueur = joueur;
        this.vueDuJeu = vueDuJeu;
        this.getChildren().add(new Label(joueur.getNom()));

    }

    public void creerBindings(){
    }
    public IJoueur getJoueur(){
        return this.joueur;
    }
}
