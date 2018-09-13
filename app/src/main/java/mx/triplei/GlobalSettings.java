package mx.triplei;

import java.util.ArrayList;
import java.util.List;

import mx.smartteam.entities.CategoriaCollection;
import mx.smartteam.entities.MarcaCollection;
import mx.smartteam.entities.Pop;
import mx.smartteam.entities.PopCollection;
import mx.smartteam.entities.Producto;
import mx.smartteam.entities.ProductoCollection;
import mx.smartteam.entities.Proyecto;
import mx.smartteam.entities.Usuario;

import android.app.Application;
import android.graphics.Bitmap;

public class GlobalSettings  extends Application{
	
	Usuario EntityUsuario;
	Proyecto EntityProyecto;
	Pop EntityTienda;
	mx.smartteam.entities.MenuCollection Menu;
	Producto CurrenProducto;
	ProductoCollection ProductoCollection;
	MarcaCollection MarcaCollection;
	CategoriaCollection CategoriaCollection;
	mx.smartteam.entities.Menu CurreMenu;
	PopCollection Tiendas;
	Bitmap Image;
	////////Manejo del estado de las tiendas
public int statenotificaciones = 1;
public void verNotificaciones()
{statenotificaciones=1;}
public void comp()
{statenotificaciones=2;}
public int valorconseguirNotificacion() 
{return statenotificaciones;}
	
}
