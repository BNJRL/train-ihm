package fr.umontpellier.iut.trainsJavaFX.vues;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class Mediateur {

    private static Mediateur instance;

    private EventHandler<ActionEvent> onInterrogationClicked;

    private Mediateur(){

    }
    public static Mediateur getInstance(){
        if(instance == null){
            instance = new Mediateur();
        }
        return instance;
    }
    public void setOnInterrogationClicked(EventHandler<ActionEvent> handler){
        this.onInterrogationClicked = handler;
    }
    public void triggerInterrogationClicked(ActionEvent event){
        if(onInterrogationClicked != null){
            onInterrogationClicked.handle(event);
        }
    }
}
