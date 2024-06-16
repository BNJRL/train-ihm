package fr.umontpellier.iut.trainsJavaFX.vues;

import fr.umontpellier.iut.trainsJavaFX.GestionJeu;
import fr.umontpellier.iut.trainsJavaFX.ICarte;
import fr.umontpellier.iut.trainsJavaFX.IJeu;
import fr.umontpellier.iut.trainsJavaFX.mecanique.cartes.*;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.*;

public class VueCustomCartes extends VBox {

    private Set<String> listeCarte;
    private Map<CheckBox, String> map;

    private Button valider;
    private ObservableList<CheckBox> indeterminateCoches;

    public VueCustomCartes(){
        this.listeCarte = genererCartes();
        this.map = new HashMap<>();
        this.indeterminateCoches = FXCollections.observableArrayList();
        genererBoutons();
        creerBindings();
    }

    private Set<String> genererCartes(){
        return FabriqueListeDeCartes.getNomsCartesPreparation();
    }

    private void genererBoutons(){
        for(String s : listeCarte){
            HBox hb = new HBox();
            CheckBox check = new CheckBox();
            check.setAllowIndeterminate(true);
            Label label = new Label(s);

            check.indeterminateProperty().addListener(
                    (source, oldValue, newValue) -> {
                        if (newValue) {
                            indeterminateCoches.add(check);
                        } else {
                            indeterminateCoches.remove(check);
                        }
                    });


            map.put(check, s);
            hb.getChildren().addAll(check, label);
            getChildren().add(hb);
        }
        HBox finale = new HBox();
        valider = new Button("Valider");
        valider.setOnAction(actionEvent -> {
            Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
            stage.close();
        });
        Button reset = new Button("Reset");
        reset.setOnAction(actionEvent -> {
            for(Node n : getChildren()){
                if(n instanceof HBox){
                    for(Node a : ((HBox) n).getChildren()){
                        if(a instanceof CheckBox) ((CheckBox) a).setSelected(false);
                        if(a instanceof CheckBox) ((CheckBox) a).setIndeterminate(false);
                    }
                }
            }
        });
        finale.getChildren().add(valider);
        finale.getChildren().add(reset);
        finale.setSpacing(5);
        finale.setAlignment(Pos.CENTER);
        getChildren().add(finale);
    }
    private void creerBindings(){
        setAlignment(Pos.CENTER);
        setSpacing(2);

        valider.disableProperty().bind(
                Bindings.size(indeterminateCoches).greaterThan(22)
        );
    }

    public List<Set<String>> getListes(){
        Set<String> voulu = new HashSet<>();
        Set<String> exclu = new HashSet<>();
        for(Node n : getChildren()){
            if(n instanceof HBox){
                for(Node ch : ((HBox) n).getChildren()){
                    if(ch instanceof CheckBox){
                        if(((CheckBox) ch).isSelected()) voulu.add(map.get(ch));
                        else if(((CheckBox) ch).isIndeterminate()) exclu.add(map.get(ch));
                    }
                }
            }
        }
        List<Set<String>> listes = new ArrayList<>();
        listes.add(voulu);
        listes.add(exclu);
        return listes;
    }
}
