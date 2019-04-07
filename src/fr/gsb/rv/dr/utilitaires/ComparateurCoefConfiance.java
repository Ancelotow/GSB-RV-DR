package fr.gsb.rv.dr.utilitaires;

import fr.gsb.rv.dr.entities.Praticien;
import java.util.Comparator;

public class ComparateurCoefConfiance implements Comparator<Praticien> {

    public ComparateurCoefConfiance(){
        super();
    }

    @Override
    public int compare( Praticien p1, Praticien p2){
        if(p1.getDernierCoedConfiance() == p2.getDernierCoedConfiance()){
            return 0;
        }
        else if(p1.getDernierCoedConfiance() > p2.getDernierCoedConfiance()){
            return 1;
        }
        else{
            return -1;
        }
    }

}
