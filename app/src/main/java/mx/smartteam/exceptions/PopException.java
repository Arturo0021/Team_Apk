package mx.smartteam.exceptions;

public class PopException {

	public static class NoExiste extends Exception {
		public NoExiste() {
			super("El folio no existe, verifique por favor");
		}
	}
	
	
	public static class FolioInvalido extends Exception {
		public FolioInvalido() {
			super("Folio no valido, verifique por favor");
		}
	}
	
        
        public static class TiendaAbierta extends Exception {
		public TiendaAbierta() {
			super("No puede abrir mas de una vez la tienda, verifique por favor");
		}
	}
	
        
        public static class NoAbrir extends Exception {
		public NoAbrir() {
			super("Tiene una tienda abierta, verifique por favor");
		}
	}
        
        
        public static class DebesSincronizar extends Exception{
                public DebesSincronizar(){
                    super("Tienes sucursales por sincronizar, verifica por favor");
                }
        }       
        
        
        public static class NoVisitasPendientesPorSync extends Exception {
		public NoVisitasPendientesPorSync() {
			super("No hay visitas pedientes por sincronizar");
		}
	}
        public static class NoSeguirHastaSincronizar extends Exception {
		public NoSeguirHastaSincronizar() {
			super("Para poder continuar necesita Sincronizar, verifique por favor");
		}
        }
        
        public static class NoSincronizarporTiendas extends Exception{
                public NoSincronizarporTiendas(){
                        super("Se requiere que las tiendas esten cerradas, verifique por favor");
                }
        }
}
