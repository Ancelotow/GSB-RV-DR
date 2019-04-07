package fr.gsb.rv.dr;

import fr.gsb.rv.dr.entities.RapportVisite;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class VueRapport extends Dialog {

    private RapportVisite leRapport;

    public VueRapport(RapportVisite rapport){
        VBox root = new VBox();
        this.leRapport = rapport;
        this.setTitle("Rapport visite");
        this.setHeaderText("Rapport Visite : n°" + leRapport.getNumero() + "\nVisiteur : " +
                leRapport.getLeVisiteur().getPrenom() + " " + leRapport.getLeVisiteur().getNom().toUpperCase());
        Label visite = new Label("Date de visite : " + leRapport.getDateVisite());
        Label redaction = new Label("Date de rédaction : " + leRapport.getDateRedaction());
        Label bilan = new Label("Bilan : " + leRapport.getBilan() + "  Motif : " + leRapport.getMotif());
        Label praticien = new Label("Praticien : " + leRapport.getLePraticien().getPrenom() + " "
                                     + leRapport.getLePraticien().getNom().toUpperCase());
        ButtonType btnQuit = new ButtonType("Fermer", ButtonBar.ButtonData.OK_DONE);
        root.getChildren().addAll(praticien, bilan, visite, redaction);
        this.getDialogPane().setContent(root);
        this.getDialogPane().getButtonTypes().add(btnQuit);
    }

}
