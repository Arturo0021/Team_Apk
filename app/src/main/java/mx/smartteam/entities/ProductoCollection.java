package mx.smartteam.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;




public  class ProductoCollection extends ArrayList<Producto> implements Serializable{


	public ProductoCollection GetByCategoria(Categoria categoria)
	{
		ProductoCollection prodColl=new ProductoCollection(); 		
		for (Producto prod : this) {
			
			if (prod.IdCategoria!=null && prod.IdCategoria.equals(categoria.Id)
					) {
				prodColl.add(prod);
                                
			}
		}
		
		return prodColl;
	}
	
	
	public ProductoCollection GetByCategoriaMarca(Categoria categoria,Marca marca)
	{
		ProductoCollection prodColl=new ProductoCollection(); 
				
		
		for (Producto prod : this) {
			if (prod.IdCategoria!=null && prod.IdMarca!=null && prod.IdCategoria.equals(categoria.Id) && prod.IdMarca.equals(marca.Id) 
					) {
				prodColl.add(prod);
			}
		}
		
		return prodColl;
	}
	

	
}
