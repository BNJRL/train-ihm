package fr.umontpellier.iut.trainsJavaFX.vues;

import fr.umontpellier.iut.trainsJavaFX.mecanique.CouleurJoueur;

import java.util.HashMap;
import java.util.Map;

import static fr.umontpellier.iut.trainsJavaFX.mecanique.CouleurJoueur.*;

public class CouleursJoueurs {
    public static Map<CouleurJoueur, String> couleursBackgroundJoueur = new HashMap<>(Map.of(
            CouleurJoueur.JAUNE, "#FED440",
            CouleurJoueur.ROUGE, "#795593",
            CouleurJoueur.BLEU, "#4093B6",
            CouleurJoueur.VERT, "#2CCDB4"
    ));

    public static void setCouleurCustom(CouleurJoueur cj, String val){
        couleursBackgroundJoueur.replace(cj,val);
    }
    public static String getValue(int index){
        return switch (index) {
            case 0 -> couleursBackgroundJoueur.get(JAUNE);
            case 1 -> couleursBackgroundJoueur.get(ROUGE);
            case 2 -> couleursBackgroundJoueur.get(BLEU);
            case 3 -> couleursBackgroundJoueur.get(VERT);
            default -> null;
        };
    }
    public static CouleurJoueur getKey(int index){
        return switch (index) {
            case 0 -> JAUNE;
            case 1 -> ROUGE;
            case 2 -> BLEU;
            case 3 -> VERT;
            default -> null;
        };
    }
}