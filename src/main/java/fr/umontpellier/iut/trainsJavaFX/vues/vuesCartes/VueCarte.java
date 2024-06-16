package fr.umontpellier.iut.trainsJavaFX.vues.vuesCartes;

import fr.umontpellier.iut.trainsJavaFX.GestionJeu;
import fr.umontpellier.iut.trainsJavaFX.ICarte;
import fr.umontpellier.iut.trainsJavaFX.mecanique.cartes.Carte;
import fr.umontpellier.iut.trainsJavaFX.mecanique.cartes.ListeDeCartes;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
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
    @FXML
    private Label image;
    private ImageView imageView;
    @FXML
    private ImageView exclamation;
    private ICarte carte;
    private boolean etatExclamation;
    public VueCarte(){
        loadFXML();
        setupExclamationClickHandler();
        this.etatExclamation = true;
    }
    private EventHandler<ActionEvent> onExclamationEntered;
    private EventHandler<ActionEvent> onExclamationExited;
    private void loadFXML() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/carte.fxml"));
            loader.setController(this);
            loader.setRoot(this);
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public Label getImage(){
        return this.image;
    }
    public ICarte getCarte(){
        return carte;
    }
    public void setCarte(ICarte carte){
        this.carte = carte;
        imageView = new ImageView("images/cartes/"+convertisseurTexte(carte.getNom())+".png");
        image.setGraphic(imageView);
    }
    public void creerBindingsCartes(double rang) {

        sceneProperty().addListener((observable, oldScene, newScene) -> {
            if (newScene != null) {
                imageView.fitWidthProperty().bind(Bindings.createDoubleBinding(() -> {
                    int nbImage = 14;
                    double nouvelTaille = newScene.getWidth();
                    return (nouvelTaille / nbImage) / rang;
                }, newScene.widthProperty()));
                imageView.fitHeightProperty().bind(imageView.fitWidthProperty().multiply(1.4));

                image.prefWidthProperty().bind(Bindings.createDoubleBinding(() -> {
                    int nbImage = 14;
                    double nouvelTaille = newScene.getWidth();
                    return (nouvelTaille / nbImage) / rang;
                }, newScene.widthProperty()));
                image.prefHeightProperty().bind(image.prefWidthProperty().multiply(1.4));

                this.prefWidthProperty().bind(Bindings.createDoubleBinding(() -> {
                    int nbImage = 14;
                    double nouvelTaille = newScene.getWidth();
                    return (nouvelTaille / nbImage) / rang;
                }, newScene.widthProperty()));
                this.prefHeightProperty().bind(this.prefWidthProperty().multiply(1.4));
            }
        });


    }
    public void setTaille(double v){
        this.setPrefWidth(this.getPrefWidth() * v);
        this.setPrefHeight(this.getPrefHeight() * v);
        this.setScaleX(v);
        this.setScaleY(v);
    }

    private String convertisseurTexte(String str){
        return str.replaceAll(" ","_").replaceAll("ô","o").toLowerCase().replaceAll("é","e");
    }

    public void setCarteChoisieListener(EventHandler<MouseEvent> quandCarteEstChoisie) {
        setOnMouseClicked(quandCarteEstChoisie);
    }
    public void noirEtBlanc() {
        image.setGraphic(new ImageView("images/cartes/" + convertisseurTexte(carte.getNom()) + ".jpg"));

        ColorAdjust colorAdjust = new ColorAdjust();
        colorAdjust.setSaturation(-1.0);
        image.setEffect(colorAdjust);
    }
    public void imageParDefaut(){
        image.setGraphic(new ImageView("images/cartes/" + convertisseurTexte(carte.getNom()) + ".jpg"));
    }

    public void activerExclamation(boolean b){
        if(!b){
            exclamation.setImage(null);
            this.etatExclamation = false;
        } else{
            exclamation.setImage(new Image("images/icons/point_exclamation.png"));
            this.etatExclamation = true;
        }
    }

    private void setupExclamationClickHandler() {
        exclamation.setOnMouseEntered(event -> {
            if(etatExclamation){
                if (onExclamationEntered != null) {
                    onExclamationEntered.handle(new ActionEvent(this, null));
                }
                this.exclamation.setImage(new Image("images/icons/point_exclamation_active.png"));
            }
        });
        exclamation.setOnMouseExited(event -> {
            if(etatExclamation){
                if (onExclamationExited != null) {
                    onExclamationExited.handle(new ActionEvent(this, null));

                }
                this.exclamation.setImage(new Image("images/icons/point_exclamation.png"));
            }
        });
    }
    public void setOnExclamationEntered(EventHandler<ActionEvent> onExclamationEntered) {

        this.onExclamationEntered = onExclamationEntered;

    }
    public void setOnExclamationExited(EventHandler<ActionEvent> onExclamationExited) {
        this.onExclamationExited = onExclamationExited;

    }
}