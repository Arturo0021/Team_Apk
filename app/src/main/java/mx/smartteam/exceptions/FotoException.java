package mx.smartteam.exceptions;

public class FotoException {

    public static void BuscarFotoEntrada() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public static class BuscarFotoEntrada extends Exception{
        public BuscarFotoEntrada(){
            super("Error al buscar Fotos de entrada");
        }
    }
    
}
