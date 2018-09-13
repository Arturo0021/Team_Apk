package mx.smartteam.entities;

import java.util.ArrayList;

public class MarcaCollection extends ArrayList<Marca> {
	
	public Marca GetByMarcaId(int Id)
	{
		
		for (Marca  item : this) {
			
			if (item.Id.equals(Id)) {
				return item;
			}
		}
		return null;
	}
	
	//Obtiene las marcas por categoria de la lista de productos
	public MarcaCollection GetByCategoria(ProductoCollection productoCollection,Categoria categoria)
	{
		MarcaCollection marcaCollection=new MarcaCollection();
		
		if (categoria!=null) {
			
			ProductoCollection prodCollectionCateg=productoCollection.GetByCategoria(categoria);
			if(prodCollectionCateg.size()>0)
			{
				for (Producto producto : prodCollectionCateg) {
					
					Marca marcarFind=GetByMarcaId(producto.IdMarca);
					if (marcarFind!=null) {
						if (!marcaCollection.contains(marcarFind)) {
							marcaCollection.add(marcarFind);
                                                        
						}
						
					}
				}
			}
			
		}
		
		
		return marcaCollection;
		
		
	}

}
