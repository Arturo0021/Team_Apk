/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.smartteam.exceptions;

/**
 *
 * @author edgarin.lara
 */
public class GpsException {
    public static class Inactivo extends Exception {
		public Inactivo() {
			super("El GPS esta desactivado, verifique por favor");
		}
	}
}
