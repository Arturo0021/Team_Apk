package mx.smartteam.entities;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.io.Serializable;
import mx.triplei.R;

public class MarcaInactiva implements Serializable{

    public Integer id;
    public Integer idProyecto;
    public Integer idMarca;
    public Integer activo;
    public String idSondeo;
    public String modulo;
    public String fechaCrea;
    public String fechaModi;

  
}
