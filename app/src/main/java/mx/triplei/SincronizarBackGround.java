package mx.triplei;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;
import mx.smartteam.entities.Anaquel;
import mx.smartteam.entities.AnaquelCollection;
import mx.smartteam.entities.Bodega;
import mx.smartteam.entities.BodegaCollection;
import mx.smartteam.entities.Contestacion;
import mx.smartteam.entities.ExhibicionAdicional;
import mx.smartteam.entities.ExhibicionAdicionalCollection;
import mx.smartteam.entities.Foto;
import mx.smartteam.entities.FotoCollection;
import mx.smartteam.entities.MaterialPromocional;
import mx.smartteam.entities.MaterialPromocionalCollection;
import mx.smartteam.entities.PopVisita;
import mx.smartteam.entities.RespuestaSondeoCollection;
import mx.smartteam.entities.Sondeo;


public class SincronizarBackGround extends Service {
   private mx.smartteam.entities.Usuario currentUsuario = null;
   private mx.smartteam.entities.Proyecto currentProyecto = null;
   String time = null;   
   public Context context = null;
    public SincronizarBackGround()
    {
    }

    @Override
    public IBinder onBind(Intent intent) 
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    @Override
    public void onCreate() 
    {
     context = this;
   }

    @Override
    public void onStart(Intent intent, int startId) 
    {
           currentProyecto = (mx.smartteam.entities.Proyecto) intent.getSerializableExtra("proyecto");
           currentUsuario = (mx.smartteam.entities.Usuario) intent.getSerializableExtra("usuario");
              Upload();
    }


    @Override
    public void onDestroy() 
    {
        Toast.makeText(this, "Service Team :)", Toast.LENGTH_LONG).show();
    }

    
    
    
    public void Upload(){
    
        new Thread(new Runnable(){

            public void run() {
                mx.smartteam.entities.PopVisitaCollection visitas;
                mx.smartteam.entities.SondeoCollection sondeoCollection;
                 try 
                 {
                visitas = mx.smartteam.business.PopVisita.GetPendings(context, currentUsuario, currentProyecto);
                 
                 for (final PopVisita popVisita : visitas) {
                    
                    //Creamos la visita
                    mx.smartteam.business.PopVisita.Upload.Insert(popVisita);

                    //Fotos     
                    int count = 0;
                    
                    count = mx.smartteam.business.Foto.countFotos(context, popVisita);
                    if (count>0){
                      for( int ii =0 ; ii<count; ii++ ){  
                    FotoCollection fotoCollection = mx.smartteam.business.Foto.GetByVisita(context, popVisita);
                    for (Foto foto : fotoCollection) {
                        mx.smartteam.business.Foto.Upload.Insert(context,popVisita, foto);
                        foto.StatusSync = 1;
                        mx.smartteam.business.Foto.UpdateStatusSync(context, foto);
                            }
                        }
                    }
                    //Anaquel
                    AnaquelCollection anaquelCollection = mx.smartteam.business.Anaquel.GetByVisita(context, popVisita);
                    for (final Anaquel anaquel : anaquelCollection) {
                        if (anaquel.StatusSync.equals(0)) {
                            mx.smartteam.business.Anaquel.Upload.Insert(popVisita, anaquel);
                            anaquel.StatusSync = 1;
                            mx.smartteam.business.Anaquel.UpdateStatusSync(context, anaquel);
                        }
                    }

                    //Bodega
                    BodegaCollection bodegaCollection = mx.smartteam.business.Bodega.GetByVisita(context, popVisita);
                    for (final Bodega bodega : bodegaCollection) {
                        if (bodega.StatusSync.equals(0)) {
                            mx.smartteam.business.Bodega.Upload.Insert(popVisita, bodega);
                            bodega.StatusSync = 1;
                            mx.smartteam.business.Bodega.UpdateStatusSync(context, bodega);
                        }
                    }

                    //Material promocional
                    MaterialPromocionalCollection materialPromocionalCollection = mx.smartteam.business.MaterialPromocional.GetByVisita(context, popVisita);
                    for (final MaterialPromocional materialPromocional : materialPromocionalCollection) {
                        if (materialPromocional.StatusSync.equals(0)) {
                            mx.smartteam.business.MaterialPromocional.Upload.Insert(popVisita, materialPromocional);
                            materialPromocional.StatusSync = 1;
                            mx.smartteam.business.MaterialPromocional.UpdateStatusSync(context, materialPromocional);
                        }
                    }


                  //Exhibicion adicional
                    ExhibicionAdicionalCollection exhibicionAdicionalCollection = mx.smartteam.business.ExhibicionAdicional.GetByVisita(context, popVisita);
                    for (final ExhibicionAdicional exhibicionAdicional : exhibicionAdicionalCollection) {
                        if (exhibicionAdicional.StatusSync.equals(0)) {
                            mx.smartteam.business.ExhibicionAdicional.Upload.Insert(popVisita, exhibicionAdicional);
                            exhibicionAdicional.StatusSync = 1;
                            mx.smartteam.business.ExhibicionAdicional.UpdateStatusSync(context, exhibicionAdicional);
                        }
                    }

                    //Sondeos
                    /*sondeoCollection = mx.smartteam.business.Sondeo.GetByProyecto(context, currentProyecto);
                    if(sondeoCollection.size() > 0){
                    for (final Sondeo sondeo : sondeoCollection) {
                        mx.smartteam.entities.ContestacionCollection contestacionCollection;
                        contestacionCollection = mx.smartteam.business.Contestacion.ContestacionGetByVisita(context, popVisita, sondeo);
                        for (final Contestacion contestacion : contestacionCollection) {
                            final RespuestaSondeoCollection respuestaSondeoCollection = mx.smartteam.business.Contestacion.GetByVisitaSondeop(context, popVisita, sondeo, contestacion);//data.RespuestaSondeo.GetByVisitaSondeop(context, popVisita, sondeo, contestacion);
                            mx.smartteam.business.Sondeo.Upload.Insert(popVisita, sondeo, respuestaSondeoCollection);
                            //Actualizar Status y Fecha de Sincronizacion 
                                
                                contestacion.StatusSync = 1;
                                mx.smartteam.business.Contestacion.UpdateStatusSync(context, contestacion);//data.RespuestaSondeo.UpdateStatusSync(context, contestacion);

                        }
                    }
                    }*/
              
                    
                    //Enviamos el seguimiento
                mx.smartteam.entities.SeguimientoGpsCollection seguimientoGpsCollection = null;
                    seguimientoGpsCollection = mx.smartteam.business.SeguimientoGps.GetSeguimientoGps(context, currentProyecto, currentUsuario);
                    if(seguimientoGpsCollection.size()>0)
                    {
                        String strResult= null;
                        strResult = mx.smartteam.business.SeguimientoGps.Upload.Insert(currentProyecto, currentUsuario, seguimientoGpsCollection);
                        if(strResult.equals("OK"))
                        {
                                mx.smartteam.business.SeguimientoGps.ActualizarStatus(context, currentProyecto, currentUsuario);
                        }
                        
                    }// END
                  
                    
                    //Una ves enviada toda la informacion cambiamos el status de la visita
                    //     popVisita.StatusSync = 1;
                    //mx.smartteam.business.PopVisita.UpdateStatusSync(context, popVisita);
               
                }
                 
                 } catch (Throwable ex) {//END
                ex.getMessage();
                }
                
            }
        
        
        }).start();
    
    }
    
    
}
