package fr.umontpellier.iut.trainsJavaFX.vues;

import fr.umontpellier.iut.trainsJavaFX.TrainsIHM;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class VueInterrogation extends Pane {

    private VueDuJeu vueDuJeu;
    private TrainsIHM ihm;

    @FXML
    private Button retour;
    @FXML
    private ImageView imageview;
    @FXML
    private ScrollPane scrollPane;

    public VueInterrogation(VueDuJeu vueDuJeu){
        this.vueDuJeu = vueDuJeu;
        loadFXML();
        actionRetour();
        //configurationImage();
    }

    private void loadFXML(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/vueinterrogation.fxml"));
            loader.setController(this);
            loader.setRoot(this);
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    public void initialize() {
        Image image = new Image(getClass().getResourceAsStream("/images/regles/complet.png"));

        imageview.setImage(image);

        imageview.fitWidthProperty().bind(scrollPane.widthProperty());

        imageview.setFitHeight(image.getHeight());

        imageview.setPreserveRatio(true);

        imageview.setSmooth(true);
    }

    public void setIhm(TrainsIHM ihm){
        this.ihm = ihm;
    }

    private void actionRetour(){
        retour.setOnAction(actionEvent -> {
            ihm.chargerVueDuJeu();
        });
    }
}
