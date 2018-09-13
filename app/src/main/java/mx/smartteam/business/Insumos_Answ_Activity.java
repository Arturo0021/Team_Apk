package mx.smartteam.business;

import android.content.Context;

/**
 *
 * @author ivan.guerra
 */
public class Insumos_Answ_Activity {

    public static void InsertRespuestas(Context context, mx.smartteam.entities.Insumos insumos, mx.smartteam.entities.Usuario usuario,mx.smartteam.entities.Pop pop, String Respuesta) throws Exception{
        
        if(insumos.getIdConfig() == null) {
            throw new Exception("Objeto Insumos No Referenciado - InsertRespuestas");
        }
        
        if(Respuesta.equalsIgnoreCase("")) {
            throw new Exception("Variable Respuesta No Referenciado - InsertRespuestas");
        }
        
        mx.smartteam.data.Insumos_Answ_Activity.InsertRespuestas(context, insumos, usuario,pop, Respuesta);
        
    }
    
    public static int existe_contestacion(Context context, mx.smartteam.entities.Pop pop) throws Exception{
        
        if(pop == null) {
            throw new Exception("Objeto Pop No Referenciado - existe_contestacion");
        }
        
        int existe = mx.smartteam.data.Insumos_Answ_Activity.existe_contestacion(context, pop);
        
        return existe;
    }
    
}
