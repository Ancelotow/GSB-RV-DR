package fr.gsb.rv.dr;

import fr.gsb.rv.dr.entities.Praticien;
import fr.gsb.rv.dr.entities.RapportVisite;
import fr.gsb.rv.dr.entities.Visiteur;
import fr.gsb.rv.dr.modeles.ModeleGsbRv;
import fr.gsb.rv.dr.technique.Connexion;
import fr.gsb.rv.dr.technique.Session;
import fr.gsb.rv.dr.utilitaires.ComparateurCoefConfiance;
import fr.gsb.rv.dr.utilitaires.ComparateurCoefNotoriete;
import fr.gsb.rv.dr.utilitaires.ComparateurDateVisite;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Pair;


import java.util.Collections;
import java.util.List;
import java.util.Optional;


public class Appli extends Application {

    private PanneauAccueil vueAccueil = new PanneauAccueil();
    private PanneauPraticiens vuePraticiens = new PanneauPraticiens();
    private PanneauRapports vueRapports = new PanneauRapports();
    private StackPane stPane;

    @Override
    public void start(Stage primaryStage) throws Exception{
        BorderPane root = new BorderPane();
        stPane = new StackPane();
        VBox rootV = new VBox();
        stPane.getChildren().add(vueRapports);
        stPane.getChildren().add(vuePraticiens);
        stPane.getChildren().add(vueAccueil);
        rootV.getChildren().add(stPane);
        Image icon = new Image(getClass().getResourceAsStream("gsb.gif"));
        primaryStage.getIcons().add(icon);
        Scene scene = new Scene( root, 800, 500 );
        Alert alertQuitter = new Alert(Alert.AlertType.CONFIRMATION);
        alertQuitter.setTitle("Quitter");
        alertQuitter.setHeaderText("Demande de confirmation");
        alertQuitter.setContentText("Voulez-vous vraiment quitter l'application ?");
        ButtonType btnOui = new ButtonType("Oui", ButtonBar.ButtonData.OK_DONE);
        ButtonType btnNon = new ButtonType("Non", ButtonBar.ButtonData.CANCEL_CLOSE);
        alertQuitter.getButtonTypes().setAll(btnOui, btnNon);
        Dialog auth = new Dialog();
        auth.setTitle("Authentification");
        auth.setHeaderText("Connexion délégué régional");
        TextField tfLogin = new TextField();
        PasswordField pfMdp = new PasswordField();
        Label lLogin = new Label("Matricule : ");
        Label lMdp = new Label("Mot de passe : ");
        ButtonType btnConn = new ButtonType("Connexion", ButtonBar.ButtonData.OK_DONE);
        ButtonType btnAnnuler = new ButtonType("Annuler", ButtonBar.ButtonData.CANCEL_CLOSE);
        auth.getDialogPane().getButtonTypes().addAll(btnConn, btnAnnuler);
        GridPane gpDialog = new GridPane();
        gpDialog.setHgap(6);
        gpDialog.setVgap(6);
        gpDialog.setPadding(new Insets(6));
        gpDialog.add(lLogin, 0, 0);
        gpDialog.add(lMdp, 0, 1);
        gpDialog.add(tfLogin, 1, 0);
        gpDialog.add(pfMdp, 1, 1);
        auth.getDialogPane().setContent(gpDialog);
        final Node block = auth.getDialogPane().lookupButton(btnConn);
        block.setDisable(true);
        tfLogin.textProperty().addListener((observable, oldValue, newValue) -> {
            block.setDisable(newValue.trim().isEmpty());
        });
        Alert alertBadAuth = new Alert(Alert.AlertType.ERROR);
        alertBadAuth.setTitle("Erreur");
        alertBadAuth.setHeaderText("Erreur de connexion");
        alertBadAuth.setContentText("Votre identifion ou mot de passe est invalide.");
        ButtonType btnClose = new ButtonType("Fermer", ButtonBar.ButtonData.OK_DONE);
        alertBadAuth.getButtonTypes().setAll(btnClose);
        auth.setResultConverter(dialogButton -> {
            if (dialogButton == btnConn) {
                return new Pair<>(tfLogin.getText(), pfMdp.getText());
            }
            return null;
        });
        MenuBar barreMenu = new MenuBar();
        Menu menuFichier = new Menu("Fichier");
        Menu menuRapports = new Menu("Rapports");
        menuRapports.setDisable(true);
        Menu menuPraticiens = new Menu("Praticiens");
        menuPraticiens.setDisable(true);
        MenuItem itemConnecter = new MenuItem("Se connecter");
        MenuItem itemDeconnecter = new MenuItem("Se déconnecter");
        itemDeconnecter.setDisable(true);
        itemDeconnecter.setOnAction(
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        Session.fermer();
                        itemConnecter.setDisable(false);
                        itemDeconnecter.setDisable(true);
                        menuRapports.setDisable(true);
                        menuPraticiens.setDisable(true);
                        vueAccueil.toFront();
                        vuePraticiens.setCritereTri(PanneauPraticiens.CRITERE_COEF_CONFIANCE);
                    }
                }
        );
        MenuItem itemQuitter = new MenuItem("Quitter");
        itemQuitter.setAccelerator(new KeyCodeCombination(KeyCode.X, KeyCombination.CONTROL_DOWN));
        itemQuitter.setOnAction(
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        Optional<ButtonType> reponse = alertQuitter.showAndWait();
                        if(reponse.get().getButtonData() == ButtonBar.ButtonData.OK_DONE){
                            Platform.exit();
                        }
                        else{
                            alertQuitter.close();
                        }
                    }
                }
        );
        itemConnecter.setOnAction(
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        Optional<Pair<String, String>> reponse = auth.showAndWait();
                        reponse.ifPresent(usernamePassword -> {
                            boolean res = ModeleGsbRv.seConnecter(reponse.get().getKey(), reponse.get().getValue());
                            if(res == true){
                                itemConnecter.setDisable(true);
                                itemDeconnecter.setDisable(false);
                                menuRapports.setDisable(false);
                                menuPraticiens.setDisable(false);
                                vuePraticiens.setCritereTri(PanneauPraticiens.CRITERE_COEF_CONFIANCE);
                            }
                            else{
                                Optional<ButtonType> validClose = alertBadAuth.showAndWait();
                            }
                        });
                    }
                }
        );
        MenuItem itemConsulter = new MenuItem("Consulter");
        itemConsulter.setOnAction(
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        vueRapports.toFront();
                    }
                }
        );
        MenuItem itemHesitant = new MenuItem("Hésitants");
        itemHesitant.setOnAction(
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        vuePraticiens.toFront();
                        vuePraticiens.rafraichir();
                    }
                }
        );
        SeparatorMenuItem separateurMenu = new SeparatorMenuItem();
        menuFichier.getItems().addAll(itemConnecter, itemDeconnecter, separateurMenu, itemQuitter);
        menuRapports.getItems().add(itemConsulter);
        menuPraticiens.getItems().add(itemHesitant);
        barreMenu.getMenus().addAll(menuFichier, menuRapports, menuPraticiens);
        root.setTop(barreMenu);
        root.setCenter(rootV);
        primaryStage.setTitle("GSB-RV-DR");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

        public static void main(String[] args) {
        launch(args);
    }
}
