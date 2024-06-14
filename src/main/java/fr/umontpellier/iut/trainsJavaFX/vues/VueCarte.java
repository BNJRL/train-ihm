package fr.umontpellier.iut.trainsJavaFX.vues;

import fr.umontpellier.iut.trainsJavaFX.ICarte;
import fr.umontpellier.iut.trainsJavaFX.mecanique.cartes.Carte;
import fr.umontpellier.iut.trainsJavaFX.mecanique.cartes.ListeDeCartes;
import javafx.beans.binding.Bindings;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
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
    private ImageView imageView;

    public VueCarte(ICarte carte) {
        this.carte = (Carte) carte;
        this.image = new Label();

        imageView = new ImageView("images/cartes/"+convertisseurTexte(carte.getNom())+".jpg");
        image = new Label("",imageView);
        getChildren().add(image);

    }

    public void creerBindingsCartes(FlowPane parents, int rang) {
        sceneProperty().addListener((observable, oldScene, newScene) -> {
            if (newScene != null) {
                imageView.fitWidthProperty().bind(Bindings.createDoubleBinding(() -> {
                    double padding = parents.getPadding().getLeft() + parents.getPadding().getRight();
                    int nbImage = 16;
                    double nouvelTaille = newScene.getWidth();
                    return (nouvelTaille / nbImage) / rang;
                }, newScene.widthProperty()));
                imageView.fitHeightProperty().bind(imageView.fitWidthProperty().multiply(1.4));
            }
        });
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
    public ICarte getCarte(){
        return carte;
    }
    public void noirEtBlanc() {
        ImageView imageView = new ImageView("images/cartes/" + convertisseurTexte(carte.getNom()) + ".jpg");

        ColorAdjust colorAdjust = new ColorAdjust();
        colorAdjust.setSaturation(-1.0);
        imageView.setEffect(colorAdjust);

        image.setGraphic(imageView);
    }
    public void imageParDefaut(){
        ImageView imageView = new ImageView("images/cartes/" + convertisseurTexte(carte.getNom()) + ".jpg");

        image.setGraphic(imageView);
    }
}
