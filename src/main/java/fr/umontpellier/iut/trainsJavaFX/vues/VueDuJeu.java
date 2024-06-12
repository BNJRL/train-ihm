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
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private HBox zoneAction;
    @FXML
    private VBox bas;
    @FXML
    private HBox autresJoueurs;
    @FXML
    private FlowPane reserve;
    @FXML
    private Label instruction;
    @FXML
    private Button passer;
    @FXML
    private Label nomJoueur;

    private List<VueJoueurCourant> vueJoueursCourantList;
    private List<VueAutresJoueurs> vueAutresJoueursList;

    private Map<IJoueur, VueAutresJoueurs> mapVueAutreJoueur;
    private Map<ICarte, VueCarte> mapVueCarte;

    public VueDuJeu(IJeu jeu) {
        this.jeu = jeu;
        plateau = new VuePlateau();
        reserve = new FlowPane();
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
        mapVueAutreJoueur = new HashMap<>();
        for(IJoueur j : jeu.getJoueurs()){
            mapVueAutreJoueur.put(j, new VueAutresJoueurs(j));
            vueAutresJoueursList.add(mapVueAutreJoueur.get(j));

        }
        /**
        mapVueCarte = new HashMap<>();
        for(ICarte c : jeu.getReserve()){
            //mapVueCarte.put(c,new VueCarteReserve(c, jeu.getTaillesPilesReserveProperties().get(c.getNom()).intValue()));
            reserve.getChildren().add(mapVueCarte.get(c));
        }
         */

        autresJoueurs.setSpacing((double) 900 /vueAutresJoueursList.size());
        autresJoueurs.setAlignment(Pos.CENTER);

        HBox centre = new HBox();


        HBox ligneInstruction = new HBox();
        zoneAction = new HBox();
        zoneAction.getChildren().addAll(passer, joueurCourant);

        ligneInstruction.getChildren().addAll(nomJoueur, instruction);
        centre.getChildren().addAll(plateau);

        bas = new VBox();
        bas.getChildren().addAll(ligneInstruction, zoneAction);

        getChildren().addAll(autresJoueurs, centre, bas);
        setMargin(centre,new Insets(120,0,0,0));
        /**
        reserve.setVgap(1);
        reserve.setHgap(1);
        /**
        reserve.setScaleX(0.3);
        reserve.setScaleY(0.3);
         */
        //setMargin(reserve, new Insets(-520, 0, 0, 0));

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
                    String couleurHex = CouleursJoueurs.couleursBackgroundJoueur.get(newValue.getCouleur());
                    bas.setBackground(Background.fill(Color.web(couleurHex,0.6)));
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

        this.joueurCourant.getJoueur().mainProperty().addListener(
                (source, oldValue, newValue) -> {
                    for(IJoueur j : this.jeu.getJoueurs()){
                        System.out.println("La main change mdr");
                        mapVueAutreJoueur.get(j).actualiserPointsVictoires();
                    }

                }
        );
        this.joueurCourant.getJoueur().cartesRecuesProperty().addListener(
                (source, oldValue, newValue) -> {
                    for(IJoueur j : this.jeu.getJoueurs()){
                        System.out.println("Une carte a ete recue");
                        mapVueAutreJoueur.get(j).actualiserPointsVictoires();
                    }
                }
        );
        this.joueurCourant.getJoueur().nbJetonsRailsProperty().addListener(
                (source, oldValue, newValue) -> {
                    for(IJoueur j : this.jeu.getJoueurs()){
                        System.out.println("Un rail est posé");
                        mapVueAutreJoueur.get(j).actualiserPointsVictoires();
                    }
                }
        );
    }

    public IJeu getJeu() {
        return jeu;
    }

    EventHandler<? super MouseEvent> actionPasserParDefaut = (mouseEvent -> System.out.println("Vous avez choisi Passer"));
}
