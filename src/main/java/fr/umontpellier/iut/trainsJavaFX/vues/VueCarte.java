package fr.umontpellier.iut.trainsJavaFX.vues;

import fr.umontpellier.iut.trainsJavaFX.ICarte;
import fr.umontpellier.iut.trainsJavaFX.mecanique.cartes.Carte;
import fr.umontpellier.iut.trainsJavaFX.mecanique.cartes.ListeDeCartes;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

import java.io.IOException;

/**
 * Cette classe représente la vue d'une carte.
 * <p>
 * On y définit le listener à exécuter lorsque cette carte a été choisie par l'utilisateur
 */
public class VueCarte extends Pane {
    private ICarte carte;
    private Label image;

    public VueCarte(ICarte carte) {
        this.carte = (Carte) carte;
        this.image = new Label();
        image.setGraphic(new ImageView("images/cartes/"+convertisseurTexte(carte.getNom())+".jpg"));
        getChildren().add(image);
    }

    public void setCarteChoisieListener(EventHandler<MouseEvent> quandCarteEstChoisie) {
        setOnMouseClicked(quandCarteEstChoisie);
    }

    private String convertisseurTexte(String str){
       return str.replaceAll(" ","_").replaceAll("ô","o").toLowerCase().replaceAll("é","e");
    }

    public Label getImage() {
        return image;
    }
    public void setCarte(ICarte c){
        this.carte = c;
    }
}
