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
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
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

        autresJoueurs.setSpacing((double) 900 /vueAutresJoueursList.size());
        autresJoueurs.setAlignment(Pos.CENTER);

        HBox centre = new HBox();


        HBox ligneInstruction = new HBox();
        basGauche = new HBox();
        basGauche.getChildren().addAll(passer, joueurCourant);

        ligneInstruction.getChildren().addAll(nomJoueur, instruction);
        centre.getChildren().addAll(plateau, reserve);

        getChildren().addAll(autresJoueurs, centre, ligneInstruction, basGauche);
        setMargin(centre,new Insets(150,0,0,0));

        this.setSpacing(20);

        ImageView fond = new ImageView("images/background.png");

        BackgroundImage backgroundImage = new BackgroundImage(
                fond.getImage(),
                BackgroundRepeat.NO_REPEAT, // Répétition de l'image (aucune répétition)
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT, // Position de l'image (par défaut)
                BackgroundSize.DEFAULT // Taille de l'image (par défaut)
        );

// Application de la BackgroundImage à la VBox
        setBackground(new Background(backgroundImage));
    }

    public void creerBindings() {
        joueurCourant.creerBindings();
        passer.setOnAction(event  -> jeu.passerAEteChoisi());
        instruction.textProperty().bind(jeu.instructionProperty());
        plateau.prefWidthProperty().bind(getScene().widthProperty());
        plateau.prefHeightProperty().bind(getScene().heightProperty());
        plateau.creerBindings();
        joueurCourant.creerBindings();
        for(VueAutresJoueurs vAJ : vueAutresJoueursList){
            vAJ.creerBindings();
        }
        this.getJeu().joueurCourantProperty().addListener(
                (source, oldValue, newValue) -> {
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
