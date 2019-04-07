package fr.gsb.rv.dr.technique;

import fr.gsb.rv.dr.entities.Visiteur;

public class Session {

    private static Session session = null;
    private Visiteur leVisiteur;

    private Session(Visiteur leVisiteur){
        this.leVisiteur = leVisiteur;
    }

    public static void ouvrir(Visiteur leVisiteur){
        if(session == null){
            session = new Session(leVisiteur);
        }
    }

    public static void fermer(){
        session = null;
    }

    public static Session getSession(){
        return session;
    }

    public Visiteur getLeVisiteur(){
        return leVisiteur;
    }

    public static boolean estOuverte(){
        if(session == null){
            return true;
        }
        else{
            return false;
        }
    }

}
