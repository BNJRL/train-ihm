package fr.umontpellier.iut.trainsJavaFX;

import fr.umontpellier.iut.trainsJavaFX.mecanique.Jeu;
import fr.umontpellier.iut.trainsJavaFX.mecanique.cartes.FabriqueListeDeCartes;
import fr.umontpellier.iut.trainsJavaFX.mecanique.plateau.Plateau;
import fr.umontpellier.iut.trainsJavaFX.vues.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.util.*;

public class TrainsIHM extends Application {
    private VueChoixJoueurs vueChoixJoueurs;
    private Stage primaryStage;
    private Jeu jeu;
    private Scene scene;
    private VueDuJeu vueDuJeu;

    private final boolean avecVueChoixJoueurs = true;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        debuterJeu();
    }

    private void debuterJeu() {
        if (avecVueChoixJoueurs) {
            vueChoixJoueurs = new VueChoixJoueurs();
            vueChoixJoueurs.setNomsDesJoueursDefinisListener(quandLesNomsJoueursSontDefinis);
            vueChoixJoueurs.show();
        } else {
            demarrerPartie();
        }
    }

    public void demarrerPartie() {
        String[] nomsJoueurs;
        Plateau plateau = Plateau.OSAKA;

        Set<String> cartesVoulues = new HashSet<>();
        Set<String> cartesPasVoulues = new HashSet<>();

        if (avecVueChoixJoueurs) {
            nomsJoueurs = vueChoixJoueurs.getNomsJoueurs().toArray(new String[0]);
            plateau = vueChoixJoueurs.getPlateau();
            vueChoixJoueurs.close();
            cartesVoulues = vueChoixJoueurs.cartesCustomVoulues();
            cartesPasVoulues = vueChoixJoueurs.cartesCustomEcartes();
        } else {
            nomsJoueurs = new String[]{"John", "Paul","George","Ringo"};
        }

        String[] nomsCartes = cartesAChoisir(cartesVoulues, cartesPasVoulues);

        jeu = new Jeu(nomsJoueurs, nomsCartes, plateau);

        jeu.finDePartieProperty().addListener(quandLaPartieEstFinie);

        GestionJeu.setJeu(jeu);
        vueDuJeu = new VueDuJeu(jeu);
        vueDuJeu.setIhm(this);

        scene = new Scene(vueDuJeu, Screen.getPrimary().getBounds().getWidth() * DonneesGraphiques.pourcentageEcran, Screen.getPrimary().getBounds().getHeight() * DonneesGraphiques.pourcentageEcran); // la scene doit être créée avant de mettre en place les bindings
        vueDuJeu.creerBindings();
        jeu.run(); // le jeu doit être démarré après que les bindings ont été mis en place


        primaryStage.setMinWidth(Screen.getPrimary().getBounds().getWidth() / 2);
        primaryStage.setMinHeight(Screen.getPrimary().getBounds().getHeight() / 2);
        /**
        primaryStage.setMaxWidth(Screen.getPrimary().getBounds().getWidth());
        primaryStage.setMaxHeight(Screen.getPrimary().getBounds().getHeight());
         */

        primaryStage.setScene(scene);
        primaryStage.setTitle("Trains");
        primaryStage.centerOnScreen();
        primaryStage.setOnCloseRequest(event -> {
            this.arreterJeu();
            event.consume();
        });
        primaryStage.show();
    }

    private String[] cartesAChoisir(Set<String> cartesVoulues, Set<String> cartesPasVoulues){

        List<String> cartesPreparation = new ArrayList<>(FabriqueListeDeCartes.getNomsCartesPreparation());
        cartesPreparation.removeAll(cartesPasVoulues);
        Collections.shuffle(cartesPreparation);


        ArrayList<String> nomsCartesFinales = new ArrayList<>();
        int c = 0;
        while(!cartesVoulues.isEmpty() && c<8){
            String str = cartesVoulues.iterator().next();
            nomsCartesFinales.add(str);
            cartesVoulues.remove(str);
            c++;
        }
        cartesPreparation.removeAll(nomsCartesFinales);
        Collections.shuffle(cartesPreparation);
        for(int i = c; i<8;i++){
            nomsCartesFinales.add(cartesPreparation.get(i-c));
        }
        return nomsCartesFinales.toArray(new String[8]);
    }

    private final ListChangeListener<String> quandLesNomsJoueursSontDefinis = change -> {
        if (!vueChoixJoueurs.getNomsJoueurs().isEmpty())
            demarrerPartie();
    };

    private final ChangeListener<Boolean> quandLaPartieEstFinie = (source, oldValue, newValue) -> {
        if(jeu.finDePartieProperty().get()){
            VueResultats vueResultats = new VueResultats(this);
            Scene scene = new Scene(vueResultats);
            primaryStage.setScene(scene);
            primaryStage.show();
        }
    };

    public void chargerVueInterrogation() {
        VueInterrogation vueInt = new VueInterrogation(vueDuJeu);
        vueInt.setIhm(this);
        primaryStage.setScene(new Scene(vueInt,1280,1280));
        primaryStage.show();
    }
    public void chargerVueDuJeu() {
        scene.setRoot(vueDuJeu);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void arreterJeu() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setContentText("On arrête de jouer ?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
        Platform.exit();
        }
    }

    public Jeu getJeu() {
        return jeu;
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void main(String[] args) {
        launch(args);
    }

}