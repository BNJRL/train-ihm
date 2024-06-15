package fr.umontpellier.iut.trainsJavaFX.vues.vuesCartes;

import fr.umontpellier.iut.trainsJavaFX.GestionJeu;
import fr.umontpellier.iut.trainsJavaFX.ICarte;
import fr.umontpellier.iut.trainsJavaFX.IJeu;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VueReserve extends Pane {

    private int compteur;
    private int val;
    private Map<ICarte, VueCarteReserve> mapVueCarte;

    @FXML
    private GridPane gridPane;

    public VueReserve(){
        initialize();
        genererReserve();
    }
    public void initialize(){
        this.compteur = 0;
        this.val = 6;
        gridPane = new GridPane();
        //gridPane.setHgap(1);
        getChildren().add(gridPane);
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
}
