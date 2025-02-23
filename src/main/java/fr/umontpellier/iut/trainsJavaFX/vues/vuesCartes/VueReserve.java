package fr.umontpellier.iut.trainsJavaFX.vues.vuesCartes;

import fr.umontpellier.iut.trainsJavaFX.GestionJeu;
import fr.umontpellier.iut.trainsJavaFX.ICarte;
import fr.umontpellier.iut.trainsJavaFX.IJeu;
import fr.umontpellier.iut.trainsJavaFX.vues.IVues;
import fr.umontpellier.iut.trainsJavaFX.vues.Mediateur;
import javafx.beans.binding.DoubleBinding;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VueReserve extends Pane {

    private int compteur;
    private int val;
    private Map<ICarte, VueCarteReserve> mapVueCarte;
    private IVues ivue;

    @FXML
    private ImageView interrogation;

    @FXML
    private GridPane gridPane;

    public VueReserve(){
        loadFXML();
        initialize();
        genererReserve();
        setupInterrogationHandler();

    }
    private void loadFXML(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/reserve.fxml"));
            loader.setController(this);
            loader.setRoot(this);
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void initialize(){
        this.compteur = 0;
        this.val = 6;
        //gridPane = new GridPane();
        //gridPane.setHgap(1);
        //getChildren().add(gridPane);
    }

    private void genererReserve(){
        IJeu jeu = GestionJeu.getJeu();
        mapVueCarte = new HashMap<>();
        for(ICarte c : jeu.getReserve()){
            mapVueCarte.put(c,new VueCarteReserve(c, jeu.getTaillesPilesReserveProperties().get(c.getNom()).intValue()));
            ajouterEnfant(mapVueCarte.get(c));
        }
    }
    public Collection<VueCarteReserve> getVueCarteReserve(){
        return mapVueCarte.values();
    }
    private void ajouterEnfant(VueCarteReserve vueCarte){
        gridPane.add(vueCarte, compteur%val,compteur/val);
        //getChildren()
        compteur++;
    }

    public void creerBindings(){
        for(Node n : gridPane.getChildren()){
            VueCarteReserve vue = (VueCarteReserve) n;
            n.addEventHandler(MouseEvent.MOUSE_CLICKED, action -> {
                GestionJeu.getJeu().uneCarteDeLaReserveEstAchetee(vue.getCarte().getNom());
            });
            GestionJeu.getJeu().getTaillesPilesReserveProperties().get(vue.getCarte().getNom()).addListener(
                    (source, oldValue, newValue) -> {
                        if(newValue.intValue()<oldValue.intValue()){
                            vue.decrementerCompteur();
                        }else{
                            vue.incrementerCompteur();
                        }
                    }
            );
        }
    }

    private void setupInterrogationHandler() {

        interrogation.setOnMouseClicked(event -> {
            Mediateur.getInstance().triggerInterrogationClicked(new ActionEvent());
        });

        interrogation.setOnMouseEntered(event -> {
            this.interrogation.setImage(new Image("images/icons/point_interrogation_active.png"));

        });
        interrogation.setOnMouseExited(event -> {
            this.interrogation.setImage(new Image("images/icons/point_interrogation.png"));
        });
    }
}
