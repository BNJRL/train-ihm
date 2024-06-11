package fr.umontpellier.iut.trainsJavaFX.vues;

import fr.umontpellier.iut.trainsJavaFX.ICarte;
import fr.umontpellier.iut.trainsJavaFX.IJeu;
import fr.umontpellier.iut.trainsJavaFX.IJoueur;
import fr.umontpellier.iut.trainsJavaFX.mecanique.Joueur;
import fr.umontpellier.iut.trainsJavaFX.mecanique.cartes.Carte;
import fr.umontpellier.iut.trainsJavaFX.mecanique.cartes.ListeDeCartes;
import javafx.beans.binding.Bindings;
import javafx.collections.ListChangeListener;
import javafx.css.Size;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
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
    private HBox basGauche;
    @FXML
    private HBox autresJoueurs;
    @FXML
    private GridPane reserve;
    @FXML
    private Label instruction;
    @FXML
    private Button passer;
    @FXML
    private Label nomJoueur;

    private List<VueJoueurCourant> vueJoueursCourantList;
    private List<VueAutresJoueurs> vueAutresJoueursList;

    public VueDuJeu(IJeu jeu) {
        this.jeu = jeu;
        plateau = new VuePlateau();
        reserve = new GridPane();
        passer = new Button("Passer");
        passer.setPrefSize(100, 500);
        nomJoueur = new Label();
        instruction = new Label();
        nomJoueur.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        instruction.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        autresJoueurs = new HBox();


        vueJoueursCourantList = new ArrayList<>();
        for(IJoueur j : jeu.getJoueurs()){
            VueJoueurCourant temp = new VueJoueurCourant(j);
            vueJoueursCourantList.add(temp);
            if(jeu.joueurCourantProperty().get() == j){
                joueurCourant = temp;
            }
        }

        vueAutresJoueursList = new ArrayList<>();
        for(IJoueur j : jeu.getJoueurs()){
            vueAutresJoueursList.add(new VueAutresJoueurs(j));
        }

        HBox centre = new HBox();
        HBox ligneInstruction = new HBox();
        ligneInstruction.setBackground(Background.fill(Paint.valueOf("#C45")));
        basGauche = new HBox();
        basGauche.getChildren().addAll(passer, joueurCourant);
        basGauche.setAlignment(Pos.CENTER_LEFT);
        basGauche.setSpacing(10);
        ligneInstruction.getChildren().addAll(nomJoueur, instruction);
        getChildren().add(autresJoueurs);
        centre.getChildren().addAll(plateau, reserve);
        getChildren().addAll(centre,ligneInstruction, basGauche);
    }

    public void creerBindings() {
        joueurCourant.creerBindings();
        passer.setOnAction(event  -> jeu.passerAEteChoisi());
        instruction.textProperty().bind(jeu.instructionProperty());
        plateau.prefWidthProperty().bind(getScene().widthProperty());
        plateau.prefHeightProperty().bind(getScene().heightProperty());
        plateau.creerBindings();
        this.getJeu().joueurCourantProperty().addListener(
                (source, oldValue, newValue) -> {
                    basGauche.setBackground(Background.fill(Paint.valueOf(CouleursJoueurs.couleursBackgroundJoueur.get(joueurCourant.getJoueur().getCouleur()))));
                    nomJoueur.setText(newValue.getNom() + ": ");
                    autresJoueurs.getChildren().clear();
                    for(VueAutresJoueurs vAJ : vueAutresJoueursList){
                        if(!newValue.equals(vAJ.getJoueur())){
                            autresJoueurs.getChildren().add(vAJ);
                        }else{
                            for(VueJoueurCourant vJo : vueJoueursCourantList){
                                if(vJo.getJoueur().equals(vAJ.getJoueur())){
                                    joueurCourant = vJo;
                                }
                            }
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
