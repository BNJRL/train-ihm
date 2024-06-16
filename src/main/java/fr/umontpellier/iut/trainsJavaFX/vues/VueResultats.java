package fr.umontpellier.iut.trainsJavaFX.vues;

import fr.umontpellier.iut.trainsJavaFX.IJeu;
import fr.umontpellier.iut.trainsJavaFX.IJoueur;
import fr.umontpellier.iut.trainsJavaFX.TrainsIHM;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VueResultats extends BorderPane {

    private TrainsIHM ihm;

    @FXML
    private Button oui;
    @FXML
    private Button non;
    @FXML
    private GridPane gridpane;

    public VueResultats(TrainsIHM ihm) {
        this.ihm = ihm;
        loadFXML();
        creerBindings();
        genererImageFond();
        attributionDesPrix();
    }

    private void loadFXML(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/resultats.fxml"));
            loader.setController(this);
            loader.setRoot(this);
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void attributionDesPrix(){
        ArrayList<IJoueur> liste = genererLesPrix(recupererResultats());
        for(int i = 1; i<=liste.size();i++){
            ImageView image;
            if(i == liste.size()){
                image = new ImageView("images/icons/rigole.png");
            }else{
                image = new ImageView("images/icons/medaille_"+i+".png");
            }
            Label nomJoueur = new Label(liste.get(i-1).getNom());
            Label imageGraphique = new Label();
            imageGraphique.setGraphic(image);
            gridpane.add(nomJoueur,0,i-1);
            gridpane.add(imageGraphique,1,i-1);
        }
    }

    private ArrayList<IJoueur> genererLesPrix(Map<IJoueur, Integer> res){
        HashMap<IJoueur, Integer> map = new HashMap<>(res);
        ArrayList<IJoueur> ordreJoueur = new ArrayList<>();
        int scoreMax = -1;
        IJoueur courant =  null;
        while (!map.isEmpty()){
            for(IJoueur j : map.keySet()){
                if(map.get(j)>scoreMax){
                    courant = j;
                    scoreMax = map.get(j);
                }
            }
            ordreJoueur.add(courant);
            scoreMax = -1;
            map.remove(courant);
        }
        return ordreJoueur;
    }

    private Map<IJoueur, Integer> recupererResultats(){
        Map<IJoueur,Integer> res = new HashMap<>();
        for(IJoueur j : ihm.getJeu().getJoueurs()){
            res.put(j, j.getScoreTotal());
        }
        return res;
    }

    private void creerBindings(){
        oui.setOnAction(actionEvent -> {
            ihm.getPrimaryStage().close();
            ihm.start(ihm.getPrimaryStage());
        });
        non.setOnAction(actionEvent -> {
            ihm.getPrimaryStage().close();
        });
    }
    private void genererImageFond(){
        ImageView fond = new ImageView("images/background.png");

        BackgroundImage backgroundImage = new BackgroundImage(
                fond.getImage(),
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT
        );

        setBackground(new Background(backgroundImage));
    }
}
