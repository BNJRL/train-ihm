package fr.umontpellier.iut.trainsJavaFX.vues;

import fr.umontpellier.iut.trainsJavaFX.ICarte;
import fr.umontpellier.iut.trainsJavaFX.IJeu;
import fr.umontpellier.iut.trainsJavaFX.IJoueur;
import fr.umontpellier.iut.trainsJavaFX.mecanique.cartes.Carte;
import fr.umontpellier.iut.trainsJavaFX.mecanique.cartes.ListeDeCartes;
import javafx.collections.ListChangeListener;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

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

    private final IJeu jeu;
    private VuePlateau plateau;
    private Label instruction;
    private Label nomJoueur;
    private Button passer;
    private HBox listeBoutons;

    public VueDuJeu(IJeu jeu) {
        this.jeu = jeu;
        this.instruction = new Label();
        this.nomJoueur = new Label();
        this.instruction.setFont(Font.font("Arial", FontWeight.BOLD,30));
        this.passer = new Button("passer");
        this.listeBoutons = new HBox();

        plateau = new VuePlateau();
        getChildren().addAll(instruction, nomJoueur,passer,listeBoutons, plateau);
    }

    public void creerBindings() {
        plateau.prefWidthProperty().bind(getScene().widthProperty());
        plateau.prefHeightProperty().bind(getScene().heightProperty());
        instruction.textProperty().bind(this.jeu.instructionProperty());
        plateau.creerBindings();
        passer.setOnAction(event -> jeu.passerAEteChoisi());

        jeu.joueurCourantProperty().addListener(
                (source, oldValue, newValue) -> {
                    nomJoueur.setText(newValue.getNom());
                    listeBoutons.getChildren().clear();
                    ListeDeCartes l = newValue.mainProperty();
                    for (Carte c : l) {
                        Button but = new Button(c.getNom());
                        listeBoutons.getChildren().add(but);

                        but.setOnAction(event -> {
                            newValue.uneCarteDeLaMainAEteChoisie(c.getNom());
                            //listeBoutons.getChildren().remove(but);
                        });

                    }
                }
        );

        for(IJoueur j : getJeu().getJoueurs()){
            j.mainProperty().addListener(changecartelistener);
        }
        //   joueurCourantProperty().mainProperty().addListener(changecartelistener);
    }

    public IJeu getJeu() {
        return jeu;
    }
    private final ListChangeListener<ICarte> changecartelistener = change -> {
        while (change.next()) {
            if (change.wasRemoved()) {
                for (ICarte c : change.getRemoved()) {
                    listeBoutons.getChildren().remove(trouverBoutonCarte(c));
                }
            }
        }
    };


    private Button trouverBoutonCarte(ICarte carteATrouver){
        for(Node but : listeBoutons.getChildren()){
            Button bout = (Button) but;
            if(carteATrouver.getNom().equals(bout.getText())){
                return bout;
            }
        }

        return null;
    }

    EventHandler<? super MouseEvent> actionPasserParDefaut = (mouseEvent -> System.out.println("Vous avez choisi Passer"));
}
