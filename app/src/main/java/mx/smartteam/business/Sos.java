package mx.smartteam.business;

import android.content.Context;

public class Sos {

    public static void Insert(Context context, mx.smartteam.entities.Sos sos) throws Exception {

        if (sos == null) {
            throw new Exception("Oject Sos no referenciado");
        }

        mx.smartteam.data.Sos.Insert(context, sos);

    }

    public static void Update(Context context, mx.smartteam.entities.Sos sos) throws Exception {

        if (sos == null) {
            throw new Exception("Oject Sos no referenciado");
        }

        mx.smartteam.data.Sos.Update(context, sos);
    }

    public static mx.smartteam.entities.Sos getByVisita(Context context, mx.smartteam.entities.Pop pop, mx.smartteam.entities.Marca marca,
            mx.smartteam.entities.Categoria categoria) throws Exception
    {
        mx.smartteam.entities.Sos Sos = new mx.smartteam.entities.Sos();
        
        if (pop == null) {
            throw new Exception("Object pop no referenciado");
        }
        
         if (marca == null) {
            throw new Exception("Object pop no referenciado");
        }
         
        if (categoria == null) {
            throw new Exception("Object pop no referenciado");
        }
        
        Sos = mx.smartteam.data.Sos.GetByVisita(context, pop, marca, categoria);
        
        return Sos;
    }
    
      public static mx.smartteam.entities.SosCollection getSosCollection(Context context, mx.smartteam.entities.PopVisita popVisita) throws Exception
    {
        mx.smartteam.entities.SosCollection sosCollection = new mx.smartteam.entities.SosCollection();
        
        if (popVisita == null) {
            throw new Exception("Object pop no referenciado");
        }
        
       sosCollection = mx.smartteam.data.Sos.getSosCollection(context, popVisita);
        
        return sosCollection;
    }
    
    
    
    
    public static void UpdateStatusSync(Context context, mx.smartteam.entities.PopVisita popVisita) throws Exception {

        if (popVisita == null) {
            throw new Exception("Object Sos no referenciado");
        }

        mx.smartteam.data.Sos.UpdateStatusSync(context, popVisita);
    }

 
    public static class Upload {

        public static String Insert(mx.smartteam.entities.PopVisita visita, mx.smartteam.entities.SosCollection sosCollection) throws Exception {
                String strResult= null;
            if (visita == null) {
                throw new Exception("Object visita no referencido");

            }
            if (sosCollection == null) {
                throw new Exception("Object Sos no referencido");

            }
            strResult = mx.smartteam.data.Sos.Upload.Insert(visita, sosCollection);

            return strResult;
        }
    }
    

}
