package fr.gsb.rv.dr.utilitaires;

import fr.gsb.rv.dr.entities.Praticien;
import java.util.Comparator;

public class ComparateurDateVisite implements Comparator<Praticien> {

    public ComparateurDateVisite(){
        super();
    }

    public int compare(Praticien p1, Praticien p2){
        if(p1.getDateDerniereVisite().isEqual(p2.getDateDerniereVisite())){
            return 0;
        }
        else if(p1.getDateDerniereVisite().isAfter(p2.getDateDerniereVisite())){
            return 1;
        }
        else{
            return -1;
        }
    }

}
