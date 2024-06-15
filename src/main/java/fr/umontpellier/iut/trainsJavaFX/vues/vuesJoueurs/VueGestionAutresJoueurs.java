package fr.umontpellier.iut.trainsJavaFX.vues.vuesJoueurs;

import fr.umontpellier.iut.trainsJavaFX.GestionJeu;
import fr.umontpellier.iut.trainsJavaFX.IJeu;
import fr.umontpellier.iut.trainsJavaFX.IJoueur;
import fr.umontpellier.iut.trainsJavaFX.vues.CouleursJoueurs;
import javafx.beans.binding.DoubleBinding;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VueGestionAutresJoueurs extends Pane {
    private IJeu jeu;
    private List<VueAutresJoueurs> vueAutresJoueursList;

    @FXML
    private HBox box;

    private Map<IJoueur, VueAutresJoueurs> mapVueAutreJoueur;
    public VueGestionAutresJoueurs(){
        loadFXML();
        generer();
    }

    private void loadFXML(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/gestionsautresjoueurs.fxml"));
            loader.setController(this);
            loader.setRoot(this);
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void generer(){
        jeu = GestionJeu.getJeu();

        vueAutresJoueursList = new ArrayList<>();
        mapVueAutreJoueur = new HashMap<>();
        for(IJoueur j : jeu.getJoueurs()){
            VueAutresJoueurs vAU = new VueAutresJoueurs(j);
            vAU.setTaille(0.60);
            mapVueAutreJoueur.put(j, vAU);
            vueAutresJoueursList.add(mapVueAutreJoueur.get(j));
        }

        box.setAlignment(Pos.CENTER);

    }
    public void creerBindings(){
        for(VueAutresJoueurs vAJ : vueAutresJoueursList){
            vAJ.creerBindings();
        }

        attributionEnfants();
        bindingsTaille();
    }

    private void attributionEnfants(){
        jeu.joueurCourantProperty().addListener(
                (source, oldValue, newValue) -> {
                    box.getChildren().clear();
                    for(VueAutresJoueurs vAJ : vueAutresJoueursList){
                        if(!newValue.equals(vAJ.getJoueur())){
                            box.getChildren().add(vAJ);
                        }
                    }
                }
        );
    }

    private void bindingsTaille() {
        int nbJoueurs = this.vueAutresJoueursList.size();
        Region conteneurParent = (Region) getParent();
        conteneurParent.widthProperty().addListener(
                (source, oldValue, newValue) ->
                {
                    box.setSpacing(conteneurParent.getWidth()/(nbJoueurs+2));
                }
        );
        box.layoutXProperty().bind(conteneurParent.widthProperty().subtract(box.widthProperty()).divide(2).subtract(30));
    }
}
