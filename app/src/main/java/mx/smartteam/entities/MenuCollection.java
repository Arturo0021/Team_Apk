package mx.smartteam.entities;

import java.util.ArrayList;

public class MenuCollection extends ArrayList<Menu> {

    public Menu Find(int id) {
        Menu menu = null;

        for (Menu menu1 : this) {
            if (menu1.Id != null) {
                if (menu1.Id.equals(id)) {
                    return menu1;
                }

            }
        }

        return menu;

    }
}
