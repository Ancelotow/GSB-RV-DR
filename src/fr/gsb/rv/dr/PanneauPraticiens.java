package fr.gsb.rv.dr;

import fr.gsb.rv.dr.entities.Praticien;
import fr.gsb.rv.dr.modeles.ModeleGsbRv;
import fr.gsb.rv.dr.utilitaires.ComparateurCoefConfiance;
import fr.gsb.rv.dr.utilitaires.ComparateurCoefNotoriete;
import fr.gsb.rv.dr.utilitaires.ComparateurDateVisite;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.collections.ObservableList;
import java.util.List;

class PanneauPraticiens extends Pane {

    public final static int CRITERE_COEF_CONFIANCE = 1;
    public final static int CRITERE_COEF_NOTORIETE = 2;
    public final static int CRITERE_DATE_VISITE = 3;
    private int critereTri = CRITERE_COEF_CONFIANCE;
    private RadioButton rbCoefConfiance = new RadioButton("Confiance");
    private RadioButton rbCoefNotoriete = new RadioButton("Notoriété");
    private RadioButton rbDateVisite = new RadioButton("Date Visite");
    private TableView<Praticien> tbPraticien = new TableView<Praticien>();

    public PanneauPraticiens(){
        VBox root = new VBox();
        root.setSpacing(5);
        root.setPadding(new Insets(10,20, 10,10));
        GridPane radios = new GridPane();
        radios.setHgap(10);
        radios.setVgap(10);
        Label lbConsigne = new Label("Sélectionner un critère de tri : ");
        lbConsigne.setStyle("-fx-font-weight: bold");
        root.getChildren().add(lbConsigne);
        ToggleGroup tgTri = new ToggleGroup();
        rbCoefConfiance.setToggleGroup(tgTri);
        rbCoefConfiance.setOnAction(
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        setCritereTri(CRITERE_COEF_CONFIANCE);
                        rafraichir();
                    }
                }
        );
        rbCoefNotoriete.setToggleGroup(tgTri);
        rbCoefNotoriete.setOnAction(
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        setCritereTri(CRITERE_COEF_NOTORIETE);
                        rafraichir();
                    }
                }
        );
        rbDateVisite.setToggleGroup(tgTri);
        rbDateVisite.setOnAction(
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        setCritereTri(CRITERE_DATE_VISITE);
                        rafraichir();
                    }
                }
        );
        rbCoefConfiance.setSelected(true);
        radios.add(rbCoefConfiance, 0, 0);
        radios.add(rbCoefNotoriete, 1, 0);
        radios.add(rbDateVisite, 2, 0);
        root.getChildren().add(radios);
        TableColumn<Praticien, Integer> colNum = new TableColumn<Praticien, Integer>("Numéro");
        TableColumn<Praticien, String> colNom = new TableColumn<Praticien, String>("Nom");
        TableColumn<Praticien, String> colVille = new TableColumn<Praticien, String>("Ville");
        colNum.setCellValueFactory(new PropertyValueFactory<>("numero"));
        colNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colVille.setCellValueFactory(new PropertyValueFactory<>("ville"));
        tbPraticien.getColumns().addAll(colNum, colNom, colVille);
        tbPraticien.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        root.getChildren().add(tbPraticien);
        rafraichir();
        this.setStyle("-fx-background-color: white");
        this.getChildren().add(root);
    }

    public void rafraichir(){
        if(critereTri == CRITERE_COEF_CONFIANCE){
            List<Praticien> lesPraticiens = ModeleGsbRv.getPraticienHesitants();
            ObservableList<Praticien> list = FXCollections.observableArrayList(lesPraticiens);
            list.sort(new ComparateurCoefConfiance());
            tbPraticien.setItems(list);
        }
        else if(critereTri == CRITERE_COEF_NOTORIETE){
            List<Praticien> lesPraticiens = ModeleGsbRv.getPraticienHesitants();
            ObservableList<Praticien> list = FXCollections.observableArrayList(lesPraticiens);
            list.sort(new ComparateurCoefNotoriete().reversed());
            tbPraticien.setItems(list);
        }
        else{
            List<Praticien> lesPraticiens = ModeleGsbRv.getPraticienHesitants();
            ObservableList<Praticien> list = FXCollections.observableArrayList(lesPraticiens);
            list.sort(new ComparateurDateVisite().reversed());
            tbPraticien.setItems(list);
        }
    }

    public int getCritereTri(){
        return critereTri;
    }

    public void setCritereTri(int critereTri){
        this.critereTri = critereTri;
        if(critereTri == CRITERE_COEF_CONFIANCE){
            rbCoefConfiance.setSelected(true);
        }
        else if(critereTri == CRITERE_COEF_NOTORIETE){
            rbCoefNotoriete.setSelected(true);
        }
        else{
            rbDateVisite.setSelected(true);
        }
    }

}
