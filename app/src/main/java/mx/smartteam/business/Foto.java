package mx.smartteam.business;

import android.content.Context;
import mx.smartteam.entities.Foto.Type;

public class Foto {

    public static void Insert(Context context, mx.smartteam.entities.Foto foto) throws Exception {
        if (foto == null) {
            throw new Exception("Object [foto] no referenciado");
        }
        mx.smartteam.data.Foto.Insert(context, foto);
    }

    public static void UpdateStatusSync(Context context, mx.smartteam.entities.Foto foto) throws Exception {

        if (foto == null) {
            throw new Exception("Object foto no referenciado");
        }

        mx.smartteam.data.Foto.UpdateStatusSync(context, foto);
    }
    
    public static int existe_foto(Context context, mx.smartteam.entities.Pop pop) throws Exception {
        
       if(pop == null) {
           throw new Exception("Objeto Pop No Referenciado - existe_foto");
       } 
        
       int existe = mx.smartteam.data.Foto.existe_foto(context, pop);
       
       return existe;
    }

    public static mx.smartteam.entities.FotoCollection GetByVisita(Context context, mx.smartteam.entities.PopVisita visita) throws Exception {
        if (visita == null) {
            throw new Exception("Object [visita] no referenciado");
        }
        mx.smartteam.entities.FotoCollection fotoCollection = mx.smartteam.data.Foto.GetByVisita(context, visita);
        return fotoCollection;
    }
    public static mx.smartteam.entities.FotoCollection GetByVisita2(Context context, mx.smartteam.entities.PopVisita visita, mx.smartteam.entities.Sondeo sondeo) throws Exception {
        if (visita == null) {
            throw new Exception("Object [visita] no referenciado");
        }
        mx.smartteam.entities.FotoCollection fotoCollection = mx.smartteam.data.Foto.GetByVisita2(context, visita, sondeo);
        return fotoCollection;
    }
    
    public static int countFotos(Context context, mx.smartteam.entities.PopVisita visita) throws Exception{
        int count = 0;

        if (visita == null) {
                throw new Exception("object visita");
        }
        count = mx.smartteam.data.Foto.GetByFotoCount(context, visita);
        
        return count;
    }
    
    public static mx.smartteam.entities.FotoCollection FotosVisitas(Context context, mx.smartteam.entities.PopVisita visita) throws Exception {
        if (visita == null) {
            throw new Exception("Object [visita] no referenciado");
        }
        mx.smartteam.entities.FotoCollection fotoCollection = mx.smartteam.data.Foto.FotosVisitas(context, visita);
        return fotoCollection;
    }
    
    public static int BuscarFotoEntrada(Context context, mx.smartteam.entities.Pop pop) throws Exception {
        int fotos = 0;
        if(pop == null){
            throw new Exception("Objeto Tienda no referenciado");
        }
            
        fotos = mx.smartteam.data.Foto.BuscarFotoEntrada(context, pop);
        return fotos;
    }
     public static int BuscarFotoSalida(Context context, mx.smartteam.entities.Pop pop) throws Exception {
        int fotos = 0;
        if(pop == null){
            throw new Exception("Objeto Tienda no referenciado");
        }
            
        fotos = mx.smartteam.data.Foto.BuscarFotoSalida(context, pop);
        return fotos;
    }
    
     public static int countFotos2(Context context, mx.smartteam.entities.PopVisita visita, mx.smartteam.entities.Sondeo sondeo) throws Exception{
        int count = 0;

        if (visita == null) {
                throw new Exception("object visita");
        }
        count = mx.smartteam.data.Foto.GetByFotoCount2(context, visita, sondeo);
        
        return count;
    }

    public static class Upload {

        public static void Insert(Context context, mx.smartteam.entities.PopVisita visita, mx.smartteam.entities.Foto foto) throws Exception {

            if (foto == null) {
                throw new Exception("Object foto no referenciado");
            }
            mx.smartteam.entities.Anaquel anaquel = new mx.smartteam.entities.Anaquel();
            mx.smartteam.entities.Bodega bodega = new mx.smartteam.entities.Bodega();
            mx.smartteam.entities.ExhibicionAdicional exhibicion = new mx.smartteam.entities.ExhibicionAdicional();
            mx.smartteam.entities.Contestacion contestacion = null;//= new mx.smartteam.entities.Contestacion();
            mx.smartteam.entities.Sos sos = new mx.smartteam.entities.Sos();
            mx.smartteam.entities.MaterialPromocional mpromocional = new mx.smartteam.entities.MaterialPromocional();
            foto.Comentario = (foto.Comentario == null ? "" : foto.Comentario);
            switch(foto.Tipo) {
                case Entrada:
                    mx.smartteam.data.Foto.Upload.FotoEntrada(visita, foto);
                break;

                case Salida:
                    mx.smartteam.data.Foto.Upload.FotoSalida(visita, foto);
                break;
                
                case Foto:
                    mx.smartteam.data.Foto.Upload.Foto(visita, foto);
                break;
                    
                case Anaquel:    
                        mx.smartteam.data.Foto.Upload.FotoInsertSkuSondeo(visita, foto, 0, foto.SKU,0,0);
                break;    
                    
                case Precio: 
                        mx.smartteam.data.Foto.Upload.FotoInsertSkuSondeo(visita, foto, 0, foto.SKU ,0,0);
                break;                    
                
                case Bodega: 
                        mx.smartteam.data.Foto.Upload.FotoInsertSkuSondeo(visita, foto, 0, foto.SKU ,0,0);
                break;
                    
                case Exhibicion:    
                        mx.smartteam.data.Foto.Upload.FotoInsertSkuSondeo(visita, foto, 0, foto.SKU,0,0);
                break;
                    
                case Sondeo:
                    int cat = 0 , mar = 0;
                    if(foto.Categoriaid >0 || foto.Categoriaid != null){ cat = foto.Categoriaid; }
                    if(foto.Marcaid >0 || foto.Marcaid != null){ mar = foto.Marcaid; }
                    if(foto.nOpcion != null || foto.nOpcion !=  0 ){
                        cat = foto.nOpcion;
                    }
                    
                        mx.smartteam.data.Foto.Upload.FotoInsertSkuSondeo(visita, foto, foto.IdSondeo , foto.SKU,cat,mar);
                break;
                    
                case Sos: 
                        mx.smartteam.data.Foto.Upload.FotoInsertSkuSondeo(visita, foto, 0, 0L, foto.Categoriaid , foto.Marcaid);
                break;                    
                    
                case MaterialPromocional:
                        mx.smartteam.data.Foto.Upload.FotoInsertSkuSondeo(visita, foto, 0, foto.SKU,0,0);
                break;
                    
                case foto_anaquel:
                    
                    mx.smartteam.data.Foto.Upload.FotoAnaquel(visita, foto);
                break;
                    
                case foto_adicional:
                    mx.smartteam.data.Foto.Upload.FotoAnaquel(visita, foto);
                break;
                    
                case foto_producto:
                    mx.smartteam.data.Foto.Upload.FotoProducto(visita, foto);
                break;
                    
                case Sod:
                    //debemos de identificar que tipo de foto estamos subiendo
                    mx.smartteam.data.Foto.Upload.FotoSod(visita, foto);
                    break;
                    
                default:
                throw new AssertionError();
            }
            
        }
        
        
        public static String InsertV2(Context context, mx.smartteam.entities.PopVisita visita, mx.smartteam.entities.Foto foto) throws Exception {
            String result = ""; 
            if (foto == null) {
                throw new Exception("Object foto no referenciado");
            }
            mx.smartteam.entities.Anaquel anaquel = new mx.smartteam.entities.Anaquel();
            mx.smartteam.entities.Bodega bodega = new mx.smartteam.entities.Bodega();
            mx.smartteam.entities.ExhibicionAdicional exhibicion = new mx.smartteam.entities.ExhibicionAdicional();
            mx.smartteam.entities.Contestacion contestacion = null;//= new mx.smartteam.entities.Contestacion();
            mx.smartteam.entities.Sos sos = new mx.smartteam.entities.Sos();
            mx.smartteam.entities.MaterialPromocional mpromocional = new mx.smartteam.entities.MaterialPromocional();
            foto.Comentario = (foto.Comentario == null ? "" : foto.Comentario);
            switch(foto.Tipo) {
                case Entrada:
                    result = mx.smartteam.data.Foto.Upload.FotoEntrada(visita, foto);
                break;

                case Salida:
                    result = mx.smartteam.data.Foto.Upload.FotoSalida(visita, foto);
                break;
                
                case Foto:
                    result = mx.smartteam.data.Foto.Upload.Foto(visita, foto);
                break;
                    
                case Anaquel:    
                    result = mx.smartteam.data.Foto.Upload.FotoInsertSkuSondeo(visita, foto, 0, foto.SKU,0,0);
                break;    
                    
                case Precio: 
                    result = mx.smartteam.data.Foto.Upload.FotoInsertSkuSondeo(visita, foto, 0, foto.SKU ,0,0);
                break;                    
                
                case Bodega: 
                    result = mx.smartteam.data.Foto.Upload.FotoInsertSkuSondeo(visita, foto, 0, foto.SKU ,0,0);
                break;
                    
                case Exhibicion:    
                    result = mx.smartteam.data.Foto.Upload.FotoInsertSkuSondeo(visita, foto, 0, foto.SKU,0,0);
                break;
                    
                case Sondeo:
                    int cat = 0 , mar = 0;
                    if(foto.Categoriaid >0 || foto.Categoriaid != null){ cat = foto.Categoriaid; }
                    if(foto.Marcaid >0 || foto.Marcaid != null){ mar = foto.Marcaid; }
                    if(foto.nOpcion != null || foto.nOpcion !=  0 ){
                        cat = foto.nOpcion;
                    }
                    
                    result = mx.smartteam.data.Foto.Upload.FotoInsertSkuSondeo(visita, foto, foto.IdSondeo , foto.SKU,cat,mar);
                break;
                    
                case Sos: 
                    result = mx.smartteam.data.Foto.Upload.FotoInsertSkuSondeo(visita, foto, 0, 0L, foto.Categoriaid , foto.Marcaid);
                break;                    
                    
                case MaterialPromocional:
                    result = mx.smartteam.data.Foto.Upload.FotoInsertSkuSondeo(visita, foto, 0, foto.SKU,0,0);
                break;
                    
                case foto_anaquel:
                    
                    result = mx.smartteam.data.Foto.Upload.FotoAnaquel(visita, foto);
                break;
                    
                case foto_adicional:
                    result = mx.smartteam.data.Foto.Upload.FotoAnaquel(visita, foto);
                break;
                    
                case foto_producto:
                    result = mx.smartteam.data.Foto.Upload.FotoProducto(visita, foto);
                break;
                    
                case Sod:
                    //debemos de identificar que tipo de foto estamos subiendo
                    mx.smartteam.data.Foto.Upload.FotoSod(visita, foto);
                    break;
                    
                default:
                throw new AssertionError();
            }
            return result;
        }
        
        
        
        public static void InsertSondeo(Context context, mx.smartteam.entities.PopVisita visita, mx.smartteam.entities.Foto foto,mx.smartteam.entities.Sondeo sondeo ) throws Exception {

            if (foto == null) {
                throw new Exception("Object foto no referenciado");
            }
          
            mx.smartteam.entities.Contestacion contestacion = null;//= new mx.smartteam.entities.Contestacion();
            mx.smartteam.entities.Sos sos = new mx.smartteam.entities.Sos();
            mx.smartteam.entities.MaterialPromocional mpromocional = new mx.smartteam.entities.MaterialPromocional();
            switch(foto.Tipo) {
                
                
                case Sondeo:    
                    //contestacion = mx.smartteam.data.RespuestaSondeo.Contestacion(context, visita, foto);
                    if(sondeo.Id > 0){
                    // El quinto parametro es el mejor 
                    mx.smartteam.data.Foto.Upload.FotoInsertSkuSondeo(visita, foto, sondeo.Id , 0L,foto.nOpcion,0);
                    
                        }
                break;
                    
                    
                default:
                throw new AssertionError();
            
            }
            



            
            
            

            
            if (foto.Tipo == Type.Foto) {
                
            }
            


        }
        
        
        
    }

     public static class Opcion {

        public static Integer Insert(Context context, Integer IdFoto, mx.smartteam.entities.Opcion opcion) throws Exception{

            Integer index=-1;
            if (IdFoto!=null && opcion!=null) {
                index=mx.smartteam.data.Foto.Opcion.Insert(context, IdFoto, opcion);
            }
            return index;
        }
    }


}
