package fr.gsb.rv.dr;

import fr.gsb.rv.dr.entities.Praticien;
import fr.gsb.rv.dr.entities.RapportVisite;
import fr.gsb.rv.dr.entities.Visiteur;
import fr.gsb.rv.dr.modeles.ModeleGsbRv;
import fr.gsb.rv.dr.technique.Mois;
import fr.gsb.rv.dr.utilitaires.ComparateurDateVisite;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.util.Callback;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

class PanneauRapports extends Pane {

    private ComboBox<Visiteur> cbVisiteur = new ComboBox<Visiteur>();
    private ComboBox<Mois> cbMois = new ComboBox<Mois>();
    private ComboBox<Integer> cbAnnee = new ComboBox<Integer>();
    private TableView<RapportVisite> tabRv = new TableView<RapportVisite>();

    public PanneauRapports(){
        VBox root = new VBox();
        root.setSpacing(5);
        root.setPadding(new Insets(10,20, 10,10));
        GridPane form = new GridPane();
        Button btnValid = new Button("Valider");
        btnValid.setOnAction(
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        rafraichir();
                    }
                }
        );
        cbVisiteur.getItems().setAll(ModeleGsbRv.getVisiteurs());
        cbVisiteur.setPromptText("Visiteur");
        cbMois.getItems().setAll(Mois.values());
        cbMois.setPromptText("Mois");
        int anneeCourant = LocalDate.now().getYear();
        int anneePast = anneeCourant - 6;
        while(anneeCourant >  anneePast){
            cbAnnee.getItems().add(anneeCourant);
            anneeCourant = anneeCourant - 1;
        }
        cbAnnee.setPromptText("Année");
        form.setHgap(10);
        form.setVgap(10);
        form.add(cbVisiteur,0,0);
        form.add(cbMois,1,0);
        form.add(cbAnnee,2,0);
        TableColumn<RapportVisite, Integer> colNum = new TableColumn<RapportVisite, Integer>("Numéro");
        TableColumn<RapportVisite, String> colPrat = new TableColumn<RapportVisite, String>("Praticien");
        TableColumn<RapportVisite, String> colVille = new TableColumn<RapportVisite, String>("Ville");
        TableColumn<RapportVisite, LocalDate> colVisite = new TableColumn<RapportVisite, LocalDate>("Date de visite");
        TableColumn<RapportVisite, LocalDate> colRedac = new TableColumn<RapportVisite, LocalDate>("Date de rédaction");
        colNum.setCellValueFactory(new PropertyValueFactory<>("numero"));
        colVisite.setCellValueFactory(new PropertyValueFactory<>("dateVisite"));
        colRedac.setCellValueFactory(new PropertyValueFactory<>("dateRedaction"));
        colPrat.setCellValueFactory(
                new Callback<TableColumn.CellDataFeatures<RapportVisite, String>, ObservableValue<String>>() {
                    @Override
                    public ObservableValue<String> call(TableColumn.CellDataFeatures<RapportVisite, String> param) {
                        String nom = param.getValue().getLePraticien().getNom();
                        return new SimpleStringProperty(nom);
                    }
                }
        );
        colVille.setCellValueFactory(
                new Callback<TableColumn.CellDataFeatures<RapportVisite, String>, ObservableValue<String>>() {
                    @Override
                    public ObservableValue<String> call(TableColumn.CellDataFeatures<RapportVisite, String> param) {
                        String ville = param.getValue().getLePraticien().getVille();
                        return new SimpleStringProperty(ville);
                    }
                }
        );
        colVisite.setCellFactory(
                colonne -> {
                    return new TableCell<RapportVisite, LocalDate>(){
                        @Override
                        protected void updateItem(LocalDate item, boolean empty){
                            super.updateItem(item, empty);
                            if(empty){
                                setText("");
                            }
                            else{
                                DateTimeFormatter formateur = DateTimeFormatter.ofPattern("dd/MM/uuuu");
                                setText(item.format(formateur));
                            }
                        }
                    };
                }
        );
        colRedac.setCellFactory(
                colonne -> {
                    return new TableCell<RapportVisite, LocalDate>(){
                        @Override
                        protected void updateItem(LocalDate item, boolean empty){
                            super.updateItem(item, empty);
                            if(empty){
                                setText("");
                            }
                            else{
                                DateTimeFormatter formateur = DateTimeFormatter.ofPattern("dd/MM/uuuu");
                                setText(item.format(formateur));
                            }
                        }
                    };
                }
        );
        tabRv.setRowFactory(
                ligne -> {
                    return new TableRow<RapportVisite>(){
                        @Override
                        protected void updateItem(RapportVisite item, boolean empty){
                            super.updateItem(item, empty);
                            if(item != null ){
                                if( item.isLu() ){
                                    setStyle("-fx-background-color: gold");
                                }
                                else{
                                    setStyle("-fx-background-color: cyan");
                                }
                            }
                        }
                    };
                }
        );
        tabRv.setOnMouseClicked(
                (MouseEvent event) -> {
                    if(event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2){
                        int numero = tabRv.getSelectionModel().getSelectedItem().getNumero();
                        VueRapport vr = new VueRapport(tabRv.getSelectionModel().getSelectedItem());
                        Optional<ButtonType> reponse = vr.showAndWait();
                        if(reponse.get().getButtonData() == ButtonBar.ButtonData.OK_DONE){
                            vr.close();
                        }
                        String matricule = tabRv.getSelectionModel().getSelectedItem().getLeVisiteur().getMatricule();
                        ModeleGsbRv.setRapportsVisiteLu(matricule, numero);
                        rafraichir();
                    }
                }
        );
        tabRv.getColumns().addAll(colNum, colPrat, colVille, colVisite, colRedac);
        tabRv.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        root.getChildren().add(form);
        root.getChildren().add(btnValid);
        root.getChildren().add(tabRv);
        this.getChildren().add(root);
        this.setStyle("-fx-background-color: white");
    }

    public void rafraichir(){
        Visiteur leVisiteur = cbVisiteur.getSelectionModel().getSelectedItem();
        Mois leMois = cbMois.getSelectionModel().getSelectedItem();
        int lannee = cbAnnee.getSelectionModel().getSelectedItem();
        List<RapportVisite> lesRvs = ModeleGsbRv.getRapportsVisite(leVisiteur.getMatricule(), leMois.ordinal() + 1, lannee);
        ObservableList<RapportVisite> list = FXCollections.observableArrayList(lesRvs);
        tabRv.setItems(list);
    }

}
