package mx.smartteam.entities;

import java.util.ArrayList;

public class PopCollection extends ArrayList<Pop> {

    public Pop Find(int folio) {
        Pop pop = null;

        for (Pop myPop : this) {
            if (myPop.Id.equals(folio)) {
                return myPop;
            }
        }

        return pop;

    }

    public Boolean FindOpen() {


        for (Pop myPop : this) {
            if (myPop.Estatus.equals(1)) {
                return true;
            }
        }

        return false;

    }
}
