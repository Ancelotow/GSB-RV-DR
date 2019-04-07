package fr.gsb.rv.dr;

import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

class PanneauAccueil extends Pane {

    public PanneauAccueil(){
        AnchorPane root = new AnchorPane();
        ImageView logo = new ImageView();
        Image img = new Image(getClass().getResourceAsStream("gsb.gif"));
        logo.setImage(img);
        logo.setFitWidth(500);
        logo.setFitHeight(400);
        root.setTopAnchor(logo, 50.00);
        root.setLeftAnchor(logo, 150.00);
        root.getChildren().add(logo);
        this.getChildren().add(root);
        this.setStyle("-fx-background-color: white");
    }

}
