package mx.smartteam.business;

import android.content.Context;

public class Sod {
    
    public static mx.smartteam.entities.Sod GetOne(Context context, mx.smartteam.entities.ExhibicionConfig econfig, mx.smartteam.entities.Pop pop) throws Exception {
        mx.smartteam.entities.Sod sod = new mx.smartteam.entities.Sod();
        if(econfig == null){
            throw new Exception("Objeto Exhibicion no referenciado");
        }
        if(pop == null){
            throw new Exception("Objeto visita no referenciado");
        }
        
        sod = mx.smartteam.data.Sod.getOne(context, econfig, pop);
        return sod;
    }
    
    public static String Insert(Context context, mx.smartteam.entities.Sod sod, mx.smartteam.entities.Pop pop, mx.smartteam.entities.ExhibicionConfig econfig) throws Exception{
        String result = null;
        if(sod == null){
            throw new Exception("Objeto sod no referenciado ");
        }
        if(pop == null){
            throw new Exception("Objeto visita no referenciado");
        }
        if(econfig == null){
            throw new Exception("Objeto exh no referenciado");
        }
        result = mx.smartteam.data.Sod.Insert(context, sod, pop, econfig);
        return result;
    }
    
    public static String Update(Context context, mx.smartteam.entities.Sod sod) throws Exception {
        String result = null;
        if(sod == null){
            throw new Exception("Objeto sod no referenciado");
        }
        result = mx.smartteam.data.Sod.Update(context, sod);
        return result;
    }

    public static int existe_contestacion(Context context, mx.smartteam.entities.Pop pop) throws Exception {
        
        if(pop == null) {
            throw new Exception("Objeto Pop No Referenciado - existe_contestacion");
        }
        
        int existe = mx.smartteam.data.Sod.existe_contestacion(context, pop);
        
        return existe;
    }
    
    public static mx.smartteam.entities.SodCollection getAllnotUpload(Context context, mx.smartteam.entities.PopVisita popvisita) throws Exception {
        
        mx.smartteam.entities.SodCollection sodCollection = new mx.smartteam.entities.SodCollection();
        if(popvisita == null){
            throw new Exception("Objeto Visita no referenciado");
        }
        sodCollection = mx.smartteam.data.Sod.getAllnotUpload(context, popvisita);    
        return sodCollection;
    }
    public static void UpateSodByVisita(Context context , mx.smartteam.entities.PopVisita visita) throws Exception{
        if(visita == null){
            throw new Exception("");
        }
        mx.smartteam.data.Sod.UpdateSodByVisita(context, visita);
    }
    
    public static String SodInsert(mx.smartteam.entities.PopVisita visita, mx.smartteam.entities.SodCollection sodCollection) throws Exception {
        String result = "";
    
        result = mx.smartteam.data.Sod.Upload.Insert(visita, sodCollection);
        
        return result;
    }
    
    
}
