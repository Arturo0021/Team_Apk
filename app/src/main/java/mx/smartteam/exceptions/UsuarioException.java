package mx.smartteam.exceptions;

public class UsuarioException {

	@SuppressWarnings("serial")
	public static class UserEmpty extends Exception {
		public UserEmpty() {
			// TODO Auto-generated constructor stub
			super("El usuario no puede ser NUll, verifique por favor");
		}

	}

	@SuppressWarnings("serial")
	public static class PassEmpty extends Exception {
		public PassEmpty() {
			// TODO Auto-generated constructor stub
			super("El password no puede ser NULL, verifique por favor");
		}
	}

	@SuppressWarnings("serial")
	public static class ErrorAutenticar extends Exception {
		public ErrorAutenticar() {
			// TODO Auto-generated constructor stub
			super("No es posible autenticar , verifique por favor");
		}
	}

	@SuppressWarnings("serial")
	public static class ErrorImei extends Exception {
		public ErrorImei() {
			// TODO Auto-generated constructor stub
			super(
					"El equipo no ha sido registrado, consulte con el administrador");
		}
	}
	
	@SuppressWarnings("serial")
	public static class UsuarioSinProyecto extends Exception {
		public UsuarioSinProyecto() {
			// TODO Auto-generated constructor stub
			super(
					"El usuario no cuenta con permisos");
		}
	}
        
                  @SuppressWarnings("serial")
                  public static class JobException extends Exception{
                                    public JobException (){
                                                    // TODO Auto-generated constructor stub
                                                    super("Problem Job");
                                    }
                  }
                  
                  
                    @SuppressWarnings("serial")
                  public static class LogException extends Exception{
                                    public LogException (){
                                                    // TODO Auto-generated constructor stub
                                                    super("Problem Log");
                                    }
                  }
        

}
