package fr.umontpellier.iut.trainsJavaFX.vues.vuesJoueurs;

import fr.umontpellier.iut.trainsJavaFX.GestionJeu;
import fr.umontpellier.iut.trainsJavaFX.IJeu;
import fr.umontpellier.iut.trainsJavaFX.IJoueur;
import fr.umontpellier.iut.trainsJavaFX.vues.CouleursJoueurs;
import javafx.geometry.Pos;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VueGestionAutresJoueurs extends HBox {
    private IJeu jeu;
    private List<VueAutresJoueurs> vueAutresJoueursList;

    private Map<IJoueur, VueAutresJoueurs> mapVueAutreJoueur;
    public VueGestionAutresJoueurs(){
        generer();
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

        this.setSpacing((double) 900 /vueAutresJoueursList.size());
        this.setAlignment(Pos.BASELINE_CENTER);
    }
    public void creerBindings(){
        for(VueAutresJoueurs vAJ : vueAutresJoueursList){
            vAJ.creerBindings();
        }
        jeu.joueurCourantProperty().addListener(
                (source, oldValue, newValue) -> {
                    this.getChildren().clear();
                    for(VueAutresJoueurs vAJ : vueAutresJoueursList){
                        if(!newValue.equals(vAJ.getJoueur())){
                            this.getChildren().add(vAJ);
                        }
                    }
                }
        );
        jeu.joueurCourantProperty().get().mainProperty().addListener(
                (source, oldValue, newValue) -> {
                    for(IJoueur j : this.jeu.getJoueurs()){
                        System.out.println("La main change mdr");
                        mapVueAutreJoueur.get(j).actualiserPointsVictoires();
                    }
                }
        );
        jeu.joueurCourantProperty().get().cartesRecuesProperty().addListener(
                (source, oldValue, newValue) -> {
                    for(IJoueur j : this.jeu.getJoueurs()){
                        System.out.println("Une carte a ete recue");
                        mapVueAutreJoueur.get(j).actualiserPointsVictoires();
                    }
                }
        );
        jeu.joueurCourantProperty().get().nbJetonsRailsProperty().addListener(
                (source, oldValue, newValue) -> {
                    for(IJoueur j : this.jeu.getJoueurs()){
                        System.out.println("Un rail est posé");
                        mapVueAutreJoueur.get(j).actualiserPointsVictoires();
                    }
                }
        );
    }
}
