package fr.gsb.rv.dr.modeles;

import fr.gsb.rv.dr.entities.Praticien;
import fr.gsb.rv.dr.entities.RapportVisite;
import fr.gsb.rv.dr.entities.Visiteur;
import fr.gsb.rv.dr.technique.Connexion;
import fr.gsb.rv.dr.technique.Session;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ModeleGsbRv {

    public static boolean seConnecter(String login, String mdp){
        boolean valid = false;
        String req = "SELECT vis_nom, vis_prenom\n" +
                "FROM travailler t1\n" +
                "INNER JOIN visiteur v ON t1.vis_matricule = v.vis_matricule\n" +
                "WHERE v.vis_matricule = ?\n" +
                "AND v.vis_mdp = ?\n" +
                "AND t1.jjmmaa = (\n" +
                "  SELECT MAX(t2.jjmmaa)\n" +
                "  FROM travailler t2\n" +
                "  WHERE t1.vis_matricule = t2.vis_matricule\n" +
                ")\n" +
                "AND t1.tra_role like 'D%';";
        Connection conn = Connexion.getConnexion();
        try {
            PreparedStatement stmt = conn.prepareStatement(req);
            stmt.setString(1, login);
            stmt.setString(2, mdp);
            ResultSet res = stmt.executeQuery();
            while(res.next()){
                Visiteur vis = new Visiteur(login, res.getString("vis_nom"), res.getString("vis_prenom"));
                Session.ouvrir(vis);
                valid = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return valid;
    }

    public static List<Praticien> getPraticienHesitants(){
        List<Praticien> lesPraticiens = new ArrayList<Praticien>();
        String req = "SELECT pra_nom, p.pra_num, pra_ville, pra_coefnotoriete, rv.rap_date_visite, rv.rap_confiance, pra_adresse, pra_prenom, pra_cp\n" +
                "FROM praticien p\n" +
                "INNER JOIN  rapportvisite rv ON p.pra_num = rv.pra_num\n" +
                "WHERE rap_confiance < 5\n" +
                "AND rv.rap_date_visite = (\n" +
                "  SELECT MAX(rv2.rap_date_visite)\n" +
                "  FROM rapportvisite rv2\n" +
                "  WHERE rv.pra_num = rv2.pra_num\n" +
                ")\n" +
                "GROUP BY pra_prenom, pra_nom\n" +
                "ORDER BY pra_nom";
        Connection conn = Connexion.getConnexion();
        try {
            Statement stmt  = conn.createStatement();
            ResultSet res = stmt.executeQuery(req);
            while(res.next()){
                Praticien unPraticien = new Praticien();
                unPraticien.setNom(res.getString("pra_nom"));
                unPraticien.setNumero(res.getString("pra_num"));
                unPraticien.setVille(res.getString("pra_ville"));
                unPraticien.setCoefNotoriete(res.getDouble("pra_coefnotoriete"));
                unPraticien.setDateDerniereVisite(res.getDate("rap_date_visite").toLocalDate());
                unPraticien.setDernierCoedConfiance(res.getByte("rap_confiance"));
                unPraticien.setAdresse(res.getString("pra_adresse"));
                unPraticien.setPrenom(res.getString("pra_prenom"));
                unPraticien.setCodePostale(res.getString("pra_cp"));
                lesPraticiens.add(unPraticien);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lesPraticiens;
    }

    public static List<Visiteur> getVisiteurs(){
        List<Visiteur> lesVisiteurs = new ArrayList<Visiteur>();
        String req = "SELECT vis_matricule, vis_nom, vis_prenom\n" +
                "FROM visiteur ORDER BY vis_nom";
        Connection conn = Connexion.getConnexion();
        try {
            Statement stmt = conn.createStatement();
            ResultSet res = stmt.executeQuery(req);
            while(res.next()){
                Visiteur unVisiteur = new Visiteur();
                unVisiteur.setMatricule(res.getString("vis_matricule"));
                unVisiteur.setNom(res.getString("vis_nom"));
                unVisiteur.setPrenom(res.getString("vis_prenom"));
                lesVisiteurs.add(unVisiteur);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lesVisiteurs;
    }

    public static List<RapportVisite> getRapportsVisite(String matricule, int mois, int annee){
        List<RapportVisite> lesRvs = new ArrayList<RapportVisite>();
        String req = "SELECT vis_matricule, rap_num, rap_date_visite, rap_bilan, rap_date_redaction, rap_confiance, pra_num, rap_lu, m.mo_libelle\n" +
                "FROM rapportvisite\n" +
                "INNER JOIN motif m on rapportvisite.mo_code = m.mo_code\n" +
                "WHERE vis_matricule = ?\n" +
                "AND MONTH(rap_date_visite) = ?\n" +
                "AND YEAR(rap_date_visite) = ?\n" +
                "ORDER BY rap_date_redaction DESC;";
        String reqV = "SELECT vis_matricule, vis_nom, vis_prenom\n" +
                "FROM visiteur\n" +
                "WHERE vis_matricule = ?";
        String reqP = "SELECT pra_nom, p.pra_num, pra_ville, pra_coefnotoriete, rv.rap_date_visite, rv.rap_confiance, pra_adresse, pra_prenom, pra_cp\n" +
                "FROM praticien p\n" +
                "INNER JOIN  rapportvisite rv ON p.pra_num = rv.pra_num\n" +
                "WHERE p.pra_num = ?\n" +
                "AND rv.rap_date_visite = (\n" +
                "  SELECT MAX(rv2.rap_date_visite)\n" +
                "  FROM rapportvisite rv2\n" +
                "  WHERE rv.pra_num = rv2.pra_num\n" +
                ")\n" +
                "GROUP BY pra_prenom, pra_nom\n" +
                "ORDER BY rap_date_redaction;";
        Connection conn = Connexion.getConnexion();
        Connection connV = Connexion.getConnexion();
        Connection connP = Connexion.getConnexion();
        try {
            PreparedStatement stmt = conn.prepareStatement(req);
            stmt.setString(1, matricule);
            stmt.setInt(2, mois);
            stmt.setInt(3, annee);
            ResultSet res = stmt.executeQuery();
            while(res.next()){
                RapportVisite rv = new RapportVisite();
                Visiteur unVisiteur = new Visiteur();
                Praticien unPraticien = new Praticien();
                rv.setNumero(res.getByte("rap_num"));
                rv.setDateVisite(res.getDate("rap_date_visite").toLocalDate());
                rv.setDateRedaction(res.getDate("rap_date_redaction").toLocalDate());
                rv.setBilan(res.getString("rap_bilan"));
                rv.setMotif(res.getString("mo_libelle"));
                rv.setCoefConfiance(res.getByte("rap_confiance"));
                rv.setLu(res.getBoolean("rap_lu"));
                PreparedStatement stmtV = connV.prepareStatement(reqV);
                stmtV.setString(1, res.getString("vis_matricule"));
                ResultSet resV = stmtV.executeQuery();
                while(resV.next()){
                    unVisiteur.setMatricule(resV.getString("vis_matricule"));
                    unVisiteur.setNom(resV.getString("vis_nom"));
                    unVisiteur.setPrenom(resV.getString("vis_prenom"));
                }
                rv.setLeVisiteur(unVisiteur);
                PreparedStatement stmtP = connP.prepareStatement(reqP);
                stmtP.setInt(1, res.getByte("pra_num"));
                ResultSet resP = stmtP.executeQuery();
                while(resP.next()){
                    unPraticien.setNom(resP.getString("pra_nom"));
                    unPraticien.setNumero(resP.getString("pra_num"));
                    unPraticien.setVille(resP.getString("pra_ville"));
                    unPraticien.setCoefNotoriete(resP.getDouble("pra_coefnotoriete"));
                    unPraticien.setDateDerniereVisite(resP.getDate("rap_date_visite").toLocalDate());
                    unPraticien.setDernierCoedConfiance(resP.getByte("rap_confiance"));
                    unPraticien.setAdresse(resP.getString("pra_adresse"));
                    unPraticien.setPrenom(resP.getString("pra_prenom"));
                    unPraticien.setCodePostale(resP.getString("pra_cp"));
                }
                rv.setLePraticien(unPraticien);
                lesRvs.add(rv);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lesRvs;
    }

    public static void setRapportsVisiteLu(String matricule, int numRapport){
        String req = "UPDATE rapportvisite\n" +
                "SET rap_lu = true\n" +
                "WHERE rap_num = ?\n" +
                "AND vis_matricule = ?";
        Connection conn = Connexion.getConnexion();
        try {
            PreparedStatement stmt = conn.prepareStatement(req);
            stmt.setInt(1, numRapport);
            stmt.setString(2, matricule);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
