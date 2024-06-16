package fr.umontpellier.iut.trainsJavaFX.vues;

import fr.umontpellier.iut.trainsJavaFX.GestionJeu;
import fr.umontpellier.iut.trainsJavaFX.mecanique.CouleurJoueur;
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
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.*;

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
    private ComboBox<String> plateau;
    @FXML
    private ComboBox<String> nbJoueurs;
    @FXML
    private VBox vBoxJoueur;
    @FXML
    private Button valider;
    @FXML
    private Button choixCarte;

    @FXML
    private HBox save;
    private VueCustomCartes vCC;
    private Scene sceneCustomCartes;

    private final static String[] defaultName = {"Benjamin","Samuel","Alice","Bernard"};

    public VueChoixJoueurs() {
        nomsJoueurs = FXCollections.observableArrayList();
        loadFXML();
    }
    private void loadFXML(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/choixJoueurs.fxml"));
            loader.setController(this);
            loader.setRoot(this);
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void initialize() {
        setTitle("Personnalisation de la partie");
        nbJoueurs.getItems().addAll("2", "3", "4");
        nbJoueurs.valueProperty().set("Combien ?");

        plateau.getItems().addAll("OSAKA", "TOKYO");
        plateau.valueProperty().set(plateau.getItems().iterator().next());
        plateauChoisi = Plateau.OSAKA;
        creerBindings();
    }

    public void creerBindings() {
        nbJoueurs.valueProperty().addListener(
                (observable, oldValue, newValue) -> {
                    vBoxJoueur.getChildren().clear();

                    for (int i = 0; i < Double.parseDouble(newValue); i++) {
                        Label l = new Label("Joueur " + (i + 1) + ": ");
                        TextField tf = new TextField();
                        ColorPicker cp = new ColorPicker(Color.web(CouleursJoueurs.getValue(i)));
                        int finalI = i;
                        cp.setOnAction(actionEvent -> {
                            modificationCouleur(finalI, cp);
                        });
                        tf.setPromptText(defaultName[i]);
                        HBox hb = new HBox();
                        hb.setAlignment(Pos.CENTER);
                        hb.setSpacing(3);
                        vBoxJoueur.setSpacing(10);
                        hb.getChildren().addAll(l, tf,cp);
                        vBoxJoueur.getChildren().add(hb);
                    }
                    Button retour = new Button("Retour");
                    retour.setOnAction(actionEvent -> {
                        vBoxJoueur.getChildren().clear();
                        vBoxJoueur.getChildren().add(save);
                    });
                    vBoxJoueur.getChildren().add(retour);

                });
        plateau.valueProperty().addListener(
                (source, oldValue, newValue) ->
                        plateauChoisi = Plateau.valueOf(newValue)
        );

        valider.setOnAction( actionEvent ->{
             setListeDesNomsDeJoueurs();
        });
        choixCarte.setOnAction(actionEvent -> {
            if(vCC == null){
                vCC = new VueCustomCartes();
                sceneCustomCartes = new Scene(vCC);
            }
            Stage stage = new Stage();
            stage.setScene(sceneCustomCartes);
            stage.show();
        });



    }
    private void modificationCouleur(int index, ColorPicker colorPicker) {
        Color color = colorPicker.getValue();
        String colorHex = String.format("#%02X%02X%02X",
                (int) (color.getRed() * 255),
                (int) (color.getGreen() * 255),
                (int) (color.getBlue() * 255));

        CouleurJoueur joueur = CouleursJoueurs.getKey(index);
        if (joueur != null) {
            CouleursJoueurs.setCouleurCustom(joueur, colorHex);
        }
    }

    public List<String> getNomsJoueurs() {
        return nomsJoueurs;
    }

    /**
     * Définit l'action à exécuter lorsque la liste des participants est correctement initialisée
     */
    public void setNomsDesJoueursDefinisListener(ListChangeListener<String> quandLesNomsDesJoueursSontDefinis) {
        nomsJoueurs.addListener(quandLesNomsDesJoueursSontDefinis);
    }
    public Set<String> cartesCustomVoulues(){
        Set<String> set = new HashSet<>();
        if(vCC != null){
            set = new HashSet<>(vCC.getListes().get(0));
        }
        return set;
    }
    public Set<String> cartesCustomEcartes(){
        Set<String> set = new HashSet<>();
        if(vCC != null){
            set = new HashSet<>(vCC.getListes().get(1));
        }
        return set;
    }

    /**
     * Vérifie que tous les noms des participants sont renseignés
     * et affecte la liste définitive des participants
     */
    protected void setListeDesNomsDeJoueurs() {
        ArrayList<String> tempNamesList = new ArrayList<>();
        boolean possible = true;
        for(Node n : vBoxJoueur.getChildren()){
            if(n instanceof HBox){
                for(Node nA : ((HBox) n).getChildren()){
                    if (nA instanceof TextField){
                        String texte = ((TextField) nA).getText().trim();
                        if(texte.isEmpty()) {
                            possible = false;
                            break;
                        } else{
                            tempNamesList.add(texte);
                        }
                    }
                }
            }

            if(!possible) break;
        }
        if(possible){
            nomsJoueurs.addAll(tempNamesList);
        }
    }

    /**
     * Retourne le nombre de participants à la partie que l'utilisateur a renseigné
     */
    protected int getNombreDeJoueurs() {
        String str = String.valueOf(nbJoueurs.getValue());
        return str.equals("null") ? 0 : Integer.parseInt(str);
    }

    /**
     * Retourne le nom que l'utilisateur a renseigné pour le ième participant à la partie
     *
     * @param playerNumber : le numéro du participant
     */
    protected String getJoueurParNumero(int playerNumber) {
        return nomsJoueurs.get(playerNumber - 1);
    }

    public Plateau getPlateau() {
        return plateauChoisi;
    }
}
