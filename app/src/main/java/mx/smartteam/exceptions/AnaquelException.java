package mx.smartteam.exceptions;

public class AnaquelException {
	
	public static class CantidaEmpty extends Exception {
		public CantidaEmpty() {
			super("La cantidad es requerido, verifique por favor");
		}
	}
	
	public static class PrecioEmpty extends Exception {
		public PrecioEmpty() {
			super("El precio es requerido, verifique por favor");
		}
	}
	
	public static class TramoEmpty extends Exception {
		public TramoEmpty() {
			super("El Tramo es requerido, verifique por favor");
		}
	}
	
	
	public static class SueloEmpty extends Exception {
		public SueloEmpty() {
			super("Frentes a la altura de Suelo es requerido, verifique por favor");
		}
	}
	
	public static class TechoEmpty extends Exception {
		public TechoEmpty() {
			super("Frentes a la altura del Techo es requerido, verifique por favor");
		}
	}

	public static class OjosEmpty extends Exception {
		public OjosEmpty() {
			super("Frentes a la altura de los Ojos es requerido, verifique por favor");
		}
	}

	public static class ManosEmpty extends Exception {
		public ManosEmpty() {
			super("Frentes a la altura de las Manos es requerido, verifique por favor");
		}
	}
}
