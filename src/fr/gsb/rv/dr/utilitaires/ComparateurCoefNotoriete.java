package fr.gsb.rv.dr.utilitaires;

import fr.gsb.rv.dr.entities.Praticien;
import java.util.Comparator;

public class ComparateurCoefNotoriete implements Comparator<Praticien> {

    public ComparateurCoefNotoriete(){
        super();
    }

    public int compare(Praticien p1, Praticien p2){
        if(p1.getCoefNotoriete() == p2.getCoefNotoriete()){
            return 0;
        }
        else if(p1.getCoefNotoriete() > p2.getCoefNotoriete()){
            return 1;
        }
        else{
            return -1;
        }
    }

}
