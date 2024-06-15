package fr.umontpellier.iut.trainsJavaFX.vues;

import fr.umontpellier.iut.trainsJavaFX.GestionJeu;
import fr.umontpellier.iut.trainsJavaFX.ICarte;
import fr.umontpellier.iut.trainsJavaFX.IJeu;
import fr.umontpellier.iut.trainsJavaFX.IJoueur;
import fr.umontpellier.iut.trainsJavaFX.mecanique.cartes.ListeDeCartes;
import fr.umontpellier.iut.trainsJavaFX.vues.vuesCartes.VueCarte;
import fr.umontpellier.iut.trainsJavaFX.vues.vuesCartes.VueCarteReserve;
import fr.umontpellier.iut.trainsJavaFX.vues.vuesCartes.VueReserve;
import fr.umontpellier.iut.trainsJavaFX.vues.vuesJoueurs.VueAutresJoueurs;
import fr.umontpellier.iut.trainsJavaFX.vues.vuesJoueurs.VueGestionAutresJoueurs;
import fr.umontpellier.iut.trainsJavaFX.vues.vuesJoueurs.VueJoueurCourant;
import javafx.collections.ListChangeListener;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.io.IOException;
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
public class VueDuJeu extends BorderPane implements IVues{

    @FXML
    private VueGestionAutresJoueurs autresJoueurs;
    @FXML
    private VBox bas;
    @FXML
    private Pane top;
    @FXML
    private StackPane centre;
    @FXML
    private Pane right;
    @FXML
    private Pane left;


    private FlowPane choix;

    private Label instruction;

    private final IJeu jeu;

    private VueJoueurCourant joueurCourant;

    private Label nomJoueur;

    private Button passer;
    @FXML
    private VuePlateau plateau;
    @FXML
    private VueReserve reserve;
    @FXML
    private HBox zoneAction;


    public VueDuJeu(IJeu jeu) {
        loadFXML();
        genererVues();

        this.jeu = jeu;
        gestionBas();

        genererImageFond();
    }

    private void loadFXML(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/jeu.fxml"));
            loader.setController(this);
            loader.setRoot(this);
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void gestionBas(){
        passer = new Button("Passer");
        passer.setPrefSize(100, 500);
        passer.setMinWidth(50);
        nomJoueur = new Label();
        instruction = new Label();
        nomJoueur.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        instruction.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        choix = new FlowPane();

        HBox ligneInstruction = new HBox();
        zoneAction = new HBox();
        zoneAction.getChildren().addAll(passer, joueurCourant);



        ligneInstruction.getChildren().addAll(nomJoueur, instruction, choix);
        ligneInstruction.setAlignment(Pos.CENTER);

        bas.getChildren().addAll(ligneInstruction, zoneAction);
        bas.setAlignment(Pos.CENTER);
    }

    private void genererVues(){
        plateau = new VuePlateau();
        reserve = new VueReserve();
        autresJoueurs = new VueGestionAutresJoueurs();
        joueurCourant = new VueJoueurCourant();

        right.getChildren().add(reserve);
        top.getChildren().add(autresJoueurs);
        centre.getChildren().add(plateau);
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
                    String couleurHex = CouleursJoueurs.couleursBackgroundJoueur.get(newValue.getCouleur());
                    bas.setBackground(Background.fill(Color.web(couleurHex,0.6)));
                    nomJoueur.setText(newValue.getNom() + ": ");
                    System.out.println("bindings");
                    newValue.cartesAChoisir().addListener(choixcartelistener);
                }
        );

        autresJoueurs.creerBindings();
        reserve.creerBindings();
        reserve.prefHeightProperty().bind(plateau.prefHeightProperty());


        /**
        this.heightProperty().addListener(
                (source, oldValue, newValue) -> {
                    autresJoueurs.setPrefHeight((Double) newValue/9);
                    centre.setPrefHeight(((Double) newValue/9)*5);
                    bas.setPrefHeight(((Double) newValue/9)*3);
                    zoneAction.setPrefHeight((bas.getHeight()/10)*9);
                    passer.setPrefHeight(zoneAction.getHeight());
                    joueurCourant.setPrefHeight(zoneAction.getHeight());
                }
        );
         */
        initialiserExclamation();

    }


    private final ListChangeListener<ICarte> choixcartelistener = change -> {
        while (change.next()) {
            if (change.wasRemoved()) {
                for (ICarte c : change.getRemoved()) {
                    choix.getChildren().remove(trouverBouton(c));
                }
            }else if(change.wasAdded()){
                System.out.println(change.getAddedSubList().size());
                choix.getChildren().clear();
                for (ICarte c : change.getAddedSubList()) {
                    Button button = new Button(c.getNom());
                    button.setOnAction(event -> {
                        GestionJeu.getJeu().uneCarteAChoisirChoisie(c.getNom());
                    });
                    choix.getChildren().add(button);
                }
            }
        }
    };

    private Button trouverBouton(ICarte carteATrouver){
        for(Node node : choix.getChildren()){
            Button bouton = (Button) node;
            if(carteATrouver.getNom().equals(bouton.getText())){
                return bouton;
            }
        }

        return null;
    }


    public IJeu getJeu() {
        return jeu;
    }
    private void genererImageFond(){
        ImageView fond = new ImageView("images/background.png");

        BackgroundImage backgroundImage = new BackgroundImage(
                fond.getImage(),
                BackgroundRepeat.NO_REPEAT, // Répétition de l'image (aucune répétition)
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT, // Position de l'image (par défaut)
                BackgroundSize.DEFAULT // Taille de l'image (par défaut)
        );

        setBackground(new Background(backgroundImage));
    }

    EventHandler<? super MouseEvent> actionPasserParDefaut = (mouseEvent -> System.out.println("Vous avez choisi Passer"));

    private void initialiserExclamation(){
        for(VueCarteReserve vue : this.reserve.getVueCarteReserve()){
            VueCarte vueCarte = vue.getVueCarte();
            vueCarte.setOnExclamationEntered(event -> onExclamationEntered(vueCarte));
            vueCarte.setOnExclamationExited(event -> onExclamationExited(vueCarte));
        }
    }
    @Override
    public void onExclamationEntered(VueCarte vueCarte) {
        System.out.println("test"+vueCarte.getCarte().getNom());
        VueCarte nouvelle = new VueCarte();
        nouvelle.setCarte(vueCarte.getCarte());
        nouvelle.setTaille(this.getWidth()/1920);
        centre.getChildren().add(nouvelle);
    }

    @Override
    public void onExclamationExited(VueCarte vueCarte) {
        System.out.println("je suis parti");
        centre.getChildren().remove(1);
    }
}
