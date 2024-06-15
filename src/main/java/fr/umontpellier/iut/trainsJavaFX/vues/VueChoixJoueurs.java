package fr.umontpellier.iut.trainsJavaFX.vues;

import fr.umontpellier.iut.trainsJavaFX.mecanique.plateau.Plateau;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static java.lang.Double.valueOf;

/**
 * Cette classe correspond à une nouvelle fenêtre permettant de choisir le nombre et les noms des joueurs de la partie.
 * <p>
 * Sa présentation graphique peut automatiquement être actualisée chaque fois que le nombre de joueurs change.
 * Lorsque l'utilisateur a fini de saisir les noms de joueurs, il demandera à démarrer la partie.
 */
public class VueChoixJoueurs extends Stage {

    private final ObservableList<String> nomsJoueurs;
    private Plateau plateauChoisi;

    @FXML
    private ComboBox plateau;
    @FXML
    private ComboBox nbJoueurs;
    @FXML
    private VBox vBoxJoueur;
    @FXML
    private Button valider;


    public VueChoixJoueurs() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/choixJoueurs.fxml"));
            loader.setController(this);
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        nomsJoueurs = FXCollections.observableArrayList();

    }

    @FXML
    public void initialize() {
        nbJoueurs.getItems().addAll("1", "2", "3", "4");
        plateau.getItems().addAll("Osaka", "Tokyo");
        creerBindings();
    }

    public void creerBindings(){
        nbJoueurs.valueProperty().addListener(new ChangeListener<String>() {
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                vBoxJoueur.getChildren().clear();
                for (int i = 0; i < valueOf(newValue); i++) {
                    Label l = new Label("Nom du joueur " + (i + 1) + ": ");
                    TextField tf = new TextField();
                    HBox hb = new HBox();
                    hb.getChildren().addAll(l, tf);
                    vBoxJoueur.getChildren().add(hb);

                    tf.textProperty().addListener(event -> {
                        setListeDesNomsDeJoueurs();
                    });
                }

            }
        });

        plateau.valueProperty().addListener( new ChangeListener<String>() {
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                plateauChoisi = Plateau.valueOf(newValue);
            }
        });

        BooleanProperty nonValide = new SimpleBooleanProperty();
        nonValide.bind(Bindings.isEmpty(nomsJoueurs).and(Bindings.equal(null, plateauChoisi)));
        valider.disableProperty().bind(nonValide);
        valider.disableProperty().addListener(
                (source, oldValue, newValue) -> {
                   this.hide();
                }
        );
    }




    public List<String> getNomsJoueurs() {
        return nomsJoueurs;
    }

    /**
     * Définit l'action à exécuter lorsque la liste des participants est correctement initialisée
     */
    public void setNomsDesJoueursDefinisListener(ListChangeListener<String> quandLesNomsDesJoueursSontDefinis) {

    }

    /**
     * Vérifie que tous les noms des participants sont renseignés
     * et affecte la liste définitive des participants
     */
    protected void setListeDesNomsDeJoueurs() {
        ArrayList<String> tempNamesList = new ArrayList<>();
        for (int i = 1; i <= getNombreDeJoueurs() ; i++) {
            String name = getJoueurParNumero(i);
            if (name == null || name.equals("")) {
                tempNamesList.clear();
                break;
            }
            else
                tempNamesList.add(name);
        }
        if (!tempNamesList.isEmpty()) {
            nomsJoueurs.clear();
            nomsJoueurs.addAll(tempNamesList);
        }
    }

    /**
     * Retourne le nombre de participants à la partie que l'utilisateur a renseigné
     */
    protected int getNombreDeJoueurs() {
        return Integer.parseInt(String.valueOf(nbJoueurs.getValue()));
    }

    /**
     * Retourne le nom que l'utilisateur a renseigné pour le ième participant à la partie
     * @param playerNumber : le numéro du participant
     */
    protected String getJoueurParNumero(int playerNumber) {
        return nomsJoueurs.get(playerNumber - 1);
    }

    public Plateau getPlateau() {
        return plateauChoisi;
    }
}
