/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.smartteam.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import android.database.sqlite.SQLiteOpenHelper;

public class BaseDatos extends SQLiteOpenHelper {

    private Context context;
    private static final String DATABASE_NAME = "/mnt/sdcard/external_sd/tripleicorp";
    
    // Tablas de Insumos
    public static final String TABLE_AREA_INSUMOS = "Area_Insumos";
    public static final String TABLE_CONFIG_INSUMOS = "Config_Insumos";
    public static final String TABLE_RESPUESTA_INSUMOS = "Respuesta_Insumos";
    public static final String TABLE_UNIDAD_INSUMOS = "Unidad_Insumos";
    public static final String TABLE_INSUMOS = "Insumos";

    
    // Columnas
    public static final String COLUMN_ABREVIATURA = "Abreviatura";
    public static final String COLUMN_ACTIVO = "Activo";
    public static final String COLUMN_COLOR = "Color";
    public static final String COLUMN_DETERMINANTE = "Determinante";
    public static final String COLUMN_FECHA = "Fecha";
    public static final String COLUMN_ID = "_Id";
    public static final String COLUMN_IDAREAINSUMO = "IdAreaInsumo";
    public static final String COLUMN_IDCONFIG = "IdConfig";
    public static final String COLUMN_IDINSUMO = "IdInsumo";
    public static final String COLUMN_IDPROYECTO = "IdProyecto";
    public static final String COLUMN_IDUNIDADMEDIDA = "IdUnidadMedida";
    public static final String COLUMN_IDUSUARIO = "IdUsuario";
    public static final String COLUMN_NOMBRE = "Nombre";
    public static final String COLUMN_RESPUESTA = "Respuesta";
    public static final String COLUMN_VALORMAX = "ValorMax";
    public static final String COLUMN_VALORMIN = "ValorMin";
    public static final String COLUMN_SALTO = "Salto";
    public static final String COLUMN_DESCRIPCION = "Descripcion";
    public static final String COLUMN_IDVISITA = "IdVisita";
    public static final String COLUMN_ESTATUS = "Estatus";

    //private static final String COLUMN_
    
    
    public BaseDatos(Context context) {
        super(context, DATABASE_NAME, null, 5);//version de la modifcacion de la base de datos
        /* TEAM 37 V1 */
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Fue Ordenado para Tener un Mejor Control
        TableAnaquel(db);
        TableBodega(db);
        TableCategoria(db);
        TableCategoriasConfig(db);
        TableComSincronizacion(db);
        TableConfig(db);
        TableContestacionPregunta(db);
        TableContestacionPreguntaOpcion(db);
        TableExhibicion(db);
        TableExhibicionAdicional(db);
        TableExhibicionConfig(db);
        TableFormulario(db);//
        TableFoto(db);
        TableFotoOpcion(db);
        TableGetMsjMetaCadenas(db);
        TableJob(db);
        TableLog(db);
        TableMarca(db);//
        TableMarcaConfig(db);
        TableMarcaInactiva(db);
        TableMaterialPromocional(db);
        TableNotificaciones(db);//
        TableOpciones(db);//
        TablePonderacion(db);
        TablePop(db);//
        TablePopVisita(db);
        TablePregunta(db);//
        TablePreguntaOpcion(db);
        TableProducto(db);
        TableProductoCadena(db);
        TableProyecto(db);
        TableRespuestaOpcion(db);
        TableRespuestaSondeo(db);
        TableRutas(db);//
        TableScoredCard(db);
        TableSeguimientoGps(db);
        TableSod(db);
        TableSondeo(db);//
        TableSos(db);
        TableUsuario(db);
        TableUsuarioProyecto(db);
        TableVersiones(db);
        TableContestacion(db);
        Table_Area_Insumos(db);
        TableVersionUsuario(db);
        Table_Insumos(db);
        Table_Unidad_Medida(db);
        Table_Config_Insumos(db);
        Table_Respuestas_Insumos(db);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
        /*
         * Cuando se genere un cambio hacer el cambio respecto a la BD 
         * en esta seccion aparte de que se tiene que modificar el metodo de 
         * creacion.
         * Ordenado
         */
        db.execSQL("DROP TABLE IF EXISTS Anaquel");
        db.execSQL("DROP TABLE IF EXISTS Bodega");
        db.execSQL("DROP TABLE IF EXISTS Categoria");
        db.execSQL("DROP TABLE IF EXISTS ConfigGPS");
        db.execSQL("DROP TABLE IF EXISTS Contestacion");
        db.execSQL("DROP TABLE IF EXISTS ContestacionPregunta");
        db.execSQL("DROP TABLE IF EXISTS ContestacionPreguntaOpcion");
        db.execSQL("DROP TABLE IF EXISTS Exhibicion");
        db.execSQL("DROP TABLE IF EXISTS ExhibicionAdicional");
        db.execSQL("DROP TABLE IF EXISTS ExhibicionConfig");
        db.execSQL("DROP TABLE IF EXISTS Formulario");
        db.execSQL("DROP TABLE IF EXISTS Foto");
        db.execSQL("DROP TABLE IF EXISTS FotoOpcion");
        db.execSQL("DROP TABLE IF EXISTS Job");
        db.execSQL("DROP TABLE IF EXISTS Log");
        db.execSQL("DROP TABLE IF EXISTS Marca");
        db.execSQL("DROP TABLE IF EXISTS MaterialPromocional");
        db.execSQL("DROP TABLE IF EXISTS Notificaciones");
        db.execSQL("DROP TABLE IF EXISTS Opcion");
        db.execSQL("DROP TABLE IF EXISTS Pop");
        db.execSQL("DROP TABLE IF EXISTS PopVisita");
        db.execSQL("DROP TABLE IF EXISTS Pregunta");
        db.execSQL("DROP TABLE IF EXISTS PreguntaOpcion");
        db.execSQL("DROP TABLE IF EXISTS Producto");
        db.execSQL("DROP TABLE IF EXISTS ProductoCadena");
        db.execSQL("DROP TABLE IF EXISTS Proyecto");
        db.execSQL("DROP TABLE IF EXISTS Regsincro");
        db.execSQL("DROP TABLE IF EXISTS RespuestaOpcion");
        db.execSQL("DROP TABLE IF EXISTS RespuestaSondeo");
        db.execSQL("DROP TABLE IF EXISTS Rutas");
        db.execSQL("DROP TABLE IF EXISTS SeguimientoGps");
        db.execSQL("DROP TABLE IF EXISTS Sod");
        db.execSQL("DROP TABLE IF EXISTS Sondeo");
        db.execSQL("DROP TABLE IF EXISTS Sos");
        db.execSQL("DROP TABLE IF EXISTS Usuario");
        db.execSQL("DROP TABLE IF EXISTS UsuarioProyecto");
        db.execSQL("DROP TABLE IF EXISTS Versiones");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_AREA_INSUMOS + ";");
        db.execSQL("DROP TABLE IF EXISTS TableVersionUsuario");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_INSUMOS + ";");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_UNIDAD_INSUMOS + ";");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONFIG_INSUMOS + ";");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RESPUESTA_INSUMOS + ";");
        
        onCreate(db);
    }

    public void onOpen(final SQLiteDatabase db) {
        super.onOpen(db);
    }

    protected void TableProyecto(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS Proyecto("
                + "Id INTEGER"
                + ", Nombre VARCHAR"
                + ", Ficha"
                + ");");
    }

    protected void Table_Area_Insumos(SQLiteDatabase db) {
        String area_insumos = 
                "CREATE TABLE IF NOT EXISTS " + TABLE_AREA_INSUMOS
                        + "("
                            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                            + COLUMN_NOMBRE + " VARCHAR, "
                            + COLUMN_COLOR + " VARCHAR, "
                            + COLUMN_ACTIVO + " INTEGER, "
                            + COLUMN_DESCRIPCION + " VARCHAR"
                        + ");";
        
        db.execSQL(area_insumos);
    }
    
    protected void Table_Insumos(SQLiteDatabase db) {
        String insumos = 
                "CREATE TABLE IF NOT EXISTS " + TABLE_INSUMOS
                        + "("
                            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                            + COLUMN_NOMBRE + " VARCHAR, "
                            + COLUMN_ACTIVO + " INTEGER"
                        + ");";
        
        db.execSQL(insumos);
    }
    
    protected void Table_Unidad_Medida(SQLiteDatabase db) {
        String unidad_medida = 
                "CREATE TABLE IF NOT EXISTS " + TABLE_UNIDAD_INSUMOS
                        + "("
                            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                            + COLUMN_NOMBRE + " VARCHAR, "
                            + COLUMN_ABREVIATURA + " VARCHAR"
                        + ");";
        db.execSQL(unidad_medida);
    }
    
    protected void Table_Config_Insumos(SQLiteDatabase db) {
        String insumos_config = 
                "CREATE TABLE IF NOT EXISTS " + TABLE_CONFIG_INSUMOS
                        + "("
                            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                            + COLUMN_IDAREAINSUMO + " INTEGER, "
                            + COLUMN_IDINSUMO + " INTEGER, "
                            + COLUMN_ACTIVO + " INTEGER, "
                            + COLUMN_IDPROYECTO + " INTEGER, "
                            + COLUMN_IDUNIDADMEDIDA + " INTEGER, "
                            + COLUMN_VALORMIN + " DOUBLE, "
                            + COLUMN_VALORMAX + " DOUBLE, "
                            + COLUMN_SALTO + " DOUBLE"
                        + ");";
        db.execSQL(insumos_config);
    }
    
    protected void Table_Respuestas_Insumos(SQLiteDatabase db) {
        String respuestas_insumos = 
                "CREATE TABLE IF NOT EXISTS " + TABLE_RESPUESTA_INSUMOS
                        + " ("
                            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                            + COLUMN_IDCONFIG + " INTEGER, "
                            + COLUMN_IDUSUARIO + " INTEGER, "
                            + COLUMN_RESPUESTA + " VARCHAR, "
                            + COLUMN_FECHA + " INTEGER DEFAULT(strftime('%s','now')), "
                            + COLUMN_DETERMINANTE + " INTEGER, "
                            + COLUMN_IDVISITA + " INTEGER, "
                            + COLUMN_ESTATUS + " INTEGER DEFAULT 0"
                        + ");";
        
        db.execSQL(respuestas_insumos);
    }
    
    protected void TablePop(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS Pop("
                + "Id INTEGER"
                + ",DeterminanteGSP INTEGER"
                + ",DeterminanteTienda INTEGER"
                + ",Facturacion INTEGER"
                + ",IdCanal INTEGER"
                + ",IdGrupo INTEGER"
                + ",IdCadena INTEGER"
                + ",IdFormato INTEGER"
                + ",Sucursal VARCHAR"
                + ",Direccion TEXT"
                + ",CP INTEGER"
                + ",IdPais INTEGER"
                + ",IdEstado INTEGER"
                + ",IdMunicipio INTEGER"
                + ",IdCiudad INTEGER"
                + ",Telefonos TEXT"
                + ",Latitud DOUBLE"
                + ",Longitud DOUBLE"
                + ",Altitud INTEGER"
                + ",Activo INTEGER"
                + ",Calle VARCHAR"
                + ",Numero VARCHAR"
                + ",Colonia VARCHAR"
                + ",NuevoPunto INTEGER"
                + ",RangoGPS DOUBLE"
                + ",Nielsen VARCHAR"
                + ",Cadena VARCHAR"
                + ",IdProyecto INTEGER"
                + ",IdUsuario INTEGER"
                + ",StatusSync INTEGER"
                + ", Fechasync NUMERIC"
                + ");");
    }

    protected void TableUsuario(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS Usuario("
                + " Id INTEGER "
                + ", Usuario VARCHAR "
                + ", Password VARCHAR "
                + ", Nombre VARCHAR "
                + ", Imei VARCHAR "
                + ", Sim VARCHAR "
                + ", Telefono VARCHAR "
                + ", Tipo INTEGER"
                + ");");
    }

    protected void TablePopVisita(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS Popvisita("
                + " Id INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL "
                + ",IdProyecto INTEGER"
                + ",DeterminanteGSP INTEGER"
                + ",IdUsuario INTEGER"
                + ",Latitud DOUBLE"
                + ",Longitud DOUBLE"
                + ",IdStatus INTEGER DEFAULT 1"
                + ",Abierta INTEGER DEFAULT 1"
                + ",FotoEntrada BLOB"
                + ",FechaEntrada INTEGER"
                + ",FotoSalida BLOB"
                + ",FechaSalida INTEGER"
                + ",FechaCrea INTEGER  DEFAULT(strftime('%s','now')) "
                + ",StatusSync INTEGER DEFAULT 0 "
                + ",FechaSync INTEGER"
                + ",FechaCierre INTEGER"
                + ");");

    }

    protected void TableUsuarioProyecto(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS UsuarioProyecto("
                + "IdUsuario INTEGER"
                + ",IdProyecto INTEGER"
                + ");");

    }

    protected void TableFormulario(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS Formulario("
                + "Id INTEGER"
                + ", IdProyecto INTEGER"
                + ", Nombre VARCHAR"
                + ", Activo INTEGER"
                + ", Orden INTEGER"
                + ", Tipo VARCHAR"
                + ", StatusSync INTEGER"
                + ", Fechasync NUMERIC"
                + ", Titulo VARCHAR"
                + ", Requerido INTEGER"
                + ");");

    }

    protected void TableSondeo(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS Sondeo("
                + "  Id INTEGER"
                + ", IdProyecto INTEGER"
                + ", Nombre VARCHAR"
                + ", Activo INTEGER"
                + ", Orden INTEGER"
                + ", Tipo VARCHAR"
                + ", StatusSync INTEGER"
                + ", Fechasync NUMERIC"
                + ", IdentificadorVentas INTEGER"
                + ", IdGrupoSondeo INTEGER "
                + ", NombreSondeo VARCHAR "
                + ", Requerido INTEGER "
                + ");");

    }

    protected void TableCategoria(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS Categoria("
                + "Id INTEGER"
                + ",Nombre VARCHAR"
                + ",Tipo VARCHAR"
                + ",IdProyecto INTEGER"
                + ", StatusSync INTEGER"
                + ", Fechasync NUMERIC"
                + ", Config INTEGER"
                + ", Activo INTEGER"
                + ");");

    }

    protected void TableMarca(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS Marca("
                + "Id INTEGER"
                + ",Nombre VARCHAR"
                + ",IdCategoria INTEGER"
                + ",IdProyecto INTEGER"
                + ",StatusSync INTEGER "
                + ",Fechasync NUMERIC"
                + ");");

    }

    protected void TableProducto(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS Producto("
                + "Id INTEGER"
                + ",IdCategoria INTEGER"
                + ",IdMarca INTEGER"
                + ",SKU NUMERIC"
                + ",Nombre VARCHAR"
                + ",Descripcion VARCHAR"
                + ",Barcode VARCHAR"
                + ",Maximo DECIMAL"
                + ",Minimo DECIMAL"
                + ",Precio DECIMAL"
                + ",IdProyecto INTEGER"
                + ", StatusSync INTEGER"
                + ", Fechasync NUMERIC"
                + ", activo INTEGER"
                + ", orden INTEGER"
                + ");");

    }

    protected void TableProductoCadena(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS ProductoCadena("
                + "IdProyecto INTEGER"
                + ",IdCadena INTEGER"
                + ",SKU NUMERIC"
                + ", StatusSync INTEGER"
                + ", Fechasync NUMERIC"
                + ",activo INTEGER"
                + ",psugerido DECIMAL"
                + ",ppromocion DECIMAL"
                + ");");
    }

    protected void TableAnaquel(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS Anaquel ("
                + "Id INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL , "
                + "IdProyecto INTEGER, "
                + "IdUsuario INTEGER, "
                + "DeterminanteGSP  INTEGER, "
                + "Sku NUMERIC, "
                + "IdVisita INTEGER, "
                + "Cantidad INTEGER, "
                + "Precio DOUBLE, "
                + "Comentario VARCHAR, "
                + "Suelo INTEGER, "
                + "Manos INTEGER, "
                + "Ojos INTEGER, "
                + "Techo INTEGER, "
                + "TIPO  VARCHAR, "
                + "FechaCrea INTEGER  DEFAULT(strftime('%s','now')),"
                + "StatusSync INTEGER NOT NULL  DEFAULT 0,"
                + "FechaSync INTEGER,"
                + "FAnaquel INTEGER NOT NULL  DEFAULT 0,"
                + "FPrecio INTEGER NOT NULL  DEFAULT 0,"
                + "IdFoto INTEGER "
                + ");");
    }

    protected void TableBodega(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS Bodega ("
                + "Id INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL , "
                + "IdProyecto INTEGER, "
                + "IdUsuario INTEGER, "
                + "DeterminanteGSP INTEGER, "
                + "Sku NUMERIC, "
                + "IdVisita INTEGER, "
                + "Cantidad INTEGER, "
                + "Tarima INTEGER, "
                + "Comentario VARCHAR, "
                + "IdFoto INTEGER DEFAULT null, "
                + "FechaCrea INTEGER  DEFAULT(strftime('%s','now')), "
                + "StatusSync INTEGER DEFAULT 0, "
                + "FechaSync INTEGER);");
    }

    protected void TableMaterialPromocional(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS MaterialPromocional ("
                + "Id INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL , "
                + "IdProyecto INTEGER, "
                + "IdUsuario INTEGER, "
                + "DeterminanteGSP INTEGER, "
                + "Sku NUMERIC, "
                + "IdVisita INTEGER, "
                + "Cenafas INTEGER, "
                + "Dangles INTEGER, "
                + "Stoppers INTEGER, "
                + "Colgantes INTEGER, "
                + "Cartulinas INTEGER, "
                + "Corbatas INTEGER, "
                + "Flash INTEGER, "
                + "Tiras INTEGER, "
                + "Preciadores INTEGER, "
                + "Folletos INTEGER, "
                + "Tapetes INTEGER, "
                + "Faldones INTEGER, "
                + "Otros INTEGER, "
                + "Comentario VARCHAR, "
                + "IdFoto INTEGER DEFAULT 0 , "
                + "FechaCrea INTEGER  DEFAULT(strftime('%s','now')), "
                + "StatusSync INTEGER DEFAULT 0, "
                + "FechaSync INTEGER);");
    }

    protected void TableExhibicionAdicional(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS ExhibicionAdicional ("
                + "Id INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL , "
                + "IdProyecto INTEGER, "
                + "IdUsuario INTEGER, "
                + "DeterminanteGSP INTEGER, "
                + "Sku NUMERIC, "
                + "IdVisita INTEGER, "
                + "Cabecera INTEGER, "
                + "CabeceraFrente INTEGER, "
                + "Isla INTEGER, "
                + "IslaFrente INTEGER, "
                + "Exhibidor INTEGER, "
                + "ExhibidorFrente INTEGER, "
                + "Bunker INTEGER, "
                + "BunkerFrente INTEGER, "
                + "Area INTEGER, "
                + "AreaFrente INTEGER, "
                + "Tira INTEGER, "
                + "TiraFrente INTEGER, "
                + "Caja INTEGER, "
                + "CajaFrente INTEGER, "
                + "Arete INTEGER, "
                + "AreteFrente INTEGER, "
                + "Forway INTEGER, "
                + "ForwayFrente INTEGER, "
                + "Otros INTEGER, "
                + "OtrosFrente INTEGER, "
                + "Comentario VARCHAR, "
                + "IdFoto INTEGER, "
                + "FechaCrea INTEGER  DEFAULT(strftime('%s','now')), "
                + "StatusSync INTEGER DEFAULT 0, "
                + "FechaSync INTEGER);");
    }

    protected void TablePregunta(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS Pregunta ("
                + "Id INTEGER, "
                + "IdSondeo INTEGER, "
                + "Nombre VARCHAR, "
                + "Tipo VARCHAR, "
                + "Longitud INTEGER, "
                + "Requerido INTEGER, "
                + "StatusSync INTEGER, "
                + "Fechasync NUMERIC,"
                + "clave VARCHAR, "
                + "Activo INTEGER"
                + ");");
    }

    protected void TablePreguntaOpcion(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS PreguntaOpcion ("
                + "Id INTEGER, "
                + "IdPregunta INTEGER, "
                + "Nombre VARCHAR, "
                + "StatusSync INTEGER,"
                + "FechaSync NUMERIC, "
                + "Activo INTEGER"
                + ");");
    }

    protected void TableRespuestaSondeo(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS RespuestaSondeo("
                + "Id INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL "
                + ", IdPregunta INTEGER"
                + ", IdSondeo INTEGER"
                + ", IdVisita INTEGER"
                + ", Respuesta VARCHAR"
                + ", FechaCrea INTEGER  DEFAULT(strftime('%s','now'))"
                + ", StatusSync INTEGER"
                + ", FechaSync INTEGER);");
    }

    protected void TableRespuestaOpcion(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS RespuestaOpcion ("
                + "IdRespuesta INTEGER"
                + ", IdOpcion INTEGER);");
    }

    protected void TableFoto(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS Foto("
                + "Id INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL "
                + ", IdVisita INTEGER"
                + ", Foto BLOB"
                + ", Tipo VARCHAR"
                + ", IdCategoria INTEGER"
                + ", Comentario VARCHAR"
                + ", FechaCrea INTEGER  DEFAULT(strftime('%s','now'))"
                + ", StatusSync INTEGER"
                + ", IdSondeo INTEGER"
                + ", nOpcion INTEGER"
                + ", FechaSync INTEGER"
                + ", Categoria INTEGER"
                + ", IdMarca INTEGER"
                + ",SKU NUMERIC"
                + ",IdExhibicionConfig INTEGER "
                + ");");
    }

    protected void TableFotoOpcion(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS FotoOpcion("
                + "Id INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL "
                + ", IdFoto INTEGER"
                + ", IdOpcion INTEGER"
                + ", FechaCrea INTEGER  DEFAULT(strftime('%s','now'))"
                + ", StatusSync INTEGER"
                + ", FechaSync INTEGER);");

    }

    protected void TableOpciones(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS Opcion("
                + "Id INTEGER "
                + ", Nombre VARCHAR"
                + ", Titulo VARCHAR"
                + ", IdProyecto INTEGER"
                + ", StatusSync INTEGER"
                + ", Fechasync NUMERIC"
                + ");");
    }

    protected void TableRutas(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS Rutas("
                + "Id INTEGER PRIMARY KEY "
                + ", IdProyecto INTEGER "
                + ", IdUsuario INTEGER "
                + ", DeterminanteGSP INTEGER "
                + ", Dia INTEGER "
                + ", Orden INTEGER "
                + ", Fecha NUMERIC "
                + ", StatusSync INTEGER "
                + ", Fechasync NUMERIC"
                + ", activo INTEGER "
                + ");");

    }

    protected void TableComSincronizacion(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS Regsincro("
                + "  Id INTEGER Primary key AUTOINCREMENT"
                + ", Fechaactual TIMESTAMP NOT NULL DEFAULT current_timestamp "
                + ", Upload NUMERIC"
                + ", Download NUMERIC"
                + ", idProyecto INTEGER"
                + ", IdUsuario INTEGER"
                + ");");
    }

    protected void TableNotificaciones(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS Notificaciones("
                + "  Id INTEGER PRIMARY KEY"
                + ", Tipo VARCHAR"
                + ", Cuerpo VARCHAR"
                + ", CapturaFecha NUMERIC"
                + ", FechaFin NUMERIC "
                + ", FechaEnvio NUMERIC"
                + ", IdProyecto INTEGER"
                + ", StatusSync INTEGER"
                + ", Fechasync NUMERIC"
                + ");");

    }

    protected void TableVersiones(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS Versiones("
                + "  Id INTEGER PRIMARY KEY"
                + ", nombreapk VARCHAR"
                + ", version INTEGER"
                + ", URL VARCHAR"
                + ");");

    }
    
    protected void TableVersionUsuario(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS VersionUsuario("
                + "  Id INTEGER PRIMARY KEY"
                + ", Actual INTEGER "
                + ", Nueva INTEGER "
                + ", Status INTEGER "
                + ", FechaCrea VARCHAR "
                + ", FechaMod VARCHAR "
                + ", UsuarioInsert INTEGER "
                + ", IdUsuario INTEGER "
                + ");");

    }
    
    
    

    protected void TableConfig(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS ConfigGPS("
                + "  IdUsuario INTEGER Primary Key"
                + ", configgps INTEGER"
                + ", Tipo VARCHAR "
                + ", Conexion VARCHAR "
                + ");");

    }

    protected void TableContestacion(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS Contestacion("
                + "Id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL"
                + ",IdSondeo INTEGER"
                + ",IdVisita INTEGER"
                + ",IdUsuario INTEGER"
                + ",IdFoto INTEGER"
                + ",Sku NUMERIC default null"
                + ",Fecha NUMERIC default null"
                + ",FechaSync NUMERIC"
                + ",StatusSync INTEGER"
                + ",IdOpcion INTEGER"
                + ");");

    }

    protected void TableContestacionPregunta(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS ContestacionPregunta ("
                + "Id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL"
                + ",IdContestacion INTEGER"
                + ",IdPregunta INTEGER"
                + ",Fecha NUMERIC"
                + ",Respuesta VARCHAR"
                + ",FechaSync NUMERIC"
                + ",StatusSync NUMERIC"
                //+ ",Sku NUMERIC"
                + ",IdCategoria INTEGER "
                + ",IdMarca INTEGER "
                + ");");
    }

    protected void TableContestacionPreguntaOpcion(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS ContestacionPreguntaOpcion ("
                + "Id INTEGER PRIMARY KEY AUTOINCREMENT"
                + ",IdContestacionPregunta INTEGER"
                + ",IdOpcion INTEGER"
                + ",NOpcion INTEGER"
                + ");");
    }

    protected void TableJob(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS  Job("
                + "ID INTEGER PRIMARY KEY AUTOINCREMENT"
                + ", Fecha NUMERIC"
                + ");");

    } // END

    protected void TableLog(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS  Log("
                + "ID INTEGER PRIMARY KEY AUTOINCREMENT"
                + ", UsuarioId INTEGER"
                + ", ProyectoId INTEGER"
                + ", Fecha NUMERIC DEFAULT(strftime('%s','now'))"
                + ");");

    } // END

    protected void TableSeguimientoGps(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS  SeguimientoGps("
                + "ID INTEGER PRIMARY KEY AUTOINCREMENT"
                + ", UsuarioId INTEGER"
                + ", ProyectoId INTEGER"
                + ", Fecha NUMERIC DEFAULT(strftime('%s','now'))"
                + ", Latitud DOUBLE"
                + ", Longitud DOUBLE"
                + ", StatusSync INTEGER DEFAULT 0"
                + ", FechaSync NUMERIC "
                + ", determinanteGsp INTEGER"
                + ");");

    } // END

    protected void TableSos(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS Sos("
                + "Id INTEGER PRIMARY KEY AUTOINCREMENT"
                + ",idVisita    VARCHAR"
                + ",IdMarca     INTEGER"
                + ",IdCategoria     INTEGER"
                + ",Valor     INTEGER"
                + ",IdFoto     INTEGER"
                + ",Fecha NUMERIC DEFAULT(strftime('%s','now'))"
                + ", StatusSync INTEGER DEFAULT 0"
                + ", FechaSync NUMERIC DEFAULT(strftime('%s','now'))"
                + ");");
    }
    
    protected void TableMarcaInactiva(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS MarcaInactiva("
                + "id INTEGER"
                + ",idProyecto INTEGER"
                + ",idMarca INTEGER"
                + ",activo INTEGER"
                + ",idSondeo INTEGER"
                + ",modulo VARCHAR"
                + ",fechaCrea NUMERIC DEFAULT(strftime('%s','now'))"
                + ",fechaModi NUMERIC DEFAULT(strftime('%s','now'))"
                + ");");
    }
    
    protected void TableCategoriasConfig(SQLiteDatabase db){
        db.execSQL("CREATE TABLE IF NOT EXISTS CategoriasConfig("
                + "Id INTEGER"
                + ",IdFormulario INTEGER"
                + ",IdCategoria INTEGER"
                + ",Activo INTEGER"
                + ",IdSondeo INTEGER"
                + ",Modulo VARCHAR"
                + ",FechaCrea NUMERIC DEFAULT(strftime('%s','now'))"
                + ",FechaModi NUMERIC DEFAULT(strftime('%s','now'))"
                + ",Orden INTEGER"
                + ");");
    }

    protected void TableMarcaConfig(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS MarcaConfig("
                + "Id INTEGER"
                + ",IdFormulario INTEGER"
                + ",IdCategoria INTEGER"
                + ",idMarca INTEGER"
                + ",Activo INTEGER"
                + ",IdSondeo INTEGER"
                + ",Modulo VARCHAR"
                + ",FechaCrea NUMERIC DEFAULT(strftime('%s','now'))"
                + ",FechaModi NUMERIC DEFAULT(strftime('%s','now'))"
                + ",Orden INTEGER"
                + ",Max INTEGER DEFAULT 0"
                + ");");
    }
    
    protected void TableSod(SQLiteDatabase db){
        db.execSQL("CREATE TABLE IF NOT EXISTS Sod("
                + "Id INTEGER PRIMARY KEY AUTOINCREMENT"
                + ",IdExhibicionConfig INTEGER"
                + ",Valor INTEGER"
                + ",IdVisita INTEGER"
                + ",Comentario VARCHAR"
                + ",FechaCrea NUMERIC DEFAULT(strftime('%s','now'))"
                + ",StatusSync INTEGER DEFAULT 0 "
                + ");");
    
    }
    
    protected void TableExhibicionConfig(SQLiteDatabase db){
    db.execSQL("CREATE TABLE IF NOT EXISTS ExhibicionConfig("
                + "Id INTEGER"
                + ",IdSondeoOrden INTEGER"
                + ",IdCategoria INTEGER"
                + ",IdMarca INTEGER"
                + ",IdExhibicion INTEGER"
                + ",Ponderacion DOUBLE"
                + ",Activo BYTE"
                + ",FechaCrea NUMERIC DEFAULT(strftime('%s','now'))"
                + ",FechaMod NUMERIC DEFAULT(strftime('%s','now'))"
                + ");");
    }
    
    protected void TableExhibicion(SQLiteDatabase db){
        db.execSQL("CREATE TABLE IF NOT EXISTS Exhibicion("
                + "Id INTEGER"
                + ",Nombre VARCHAR"
                + ");"); 
    }  
    
    protected void TablePonderacion(SQLiteDatabase db){
        db.execSQL("CREATE TABLE IF NOT EXISTS Ponderaciones("
                + " Id INTEGER"
                + ",IdProyecto INTEGER"
                + ",IdCadena INTEGER"
                + ",Ponderacion INTEGER"
                + ",Activo INTEGER"
                + ");"
                );
    }
    
    protected void TableScoredCard(SQLiteDatabase db){
        db.execSQL("CREATE TABLE IF NOT EXISTS ScoredCard ("
                + " Id INTEGER PRIMARY KEY AUTOINCREMENT "
                + ", IdProyecto INTEGER "
                + ", IdUsuario INTEGER "
                + ", IdCadena INTEGER "
                + ", Registros INTEGER "
                + ", Determinantegsp INTEGER "
                + ", Fecha LONG"
                + ");"
                );
        
    }
    
    protected void TableGetMsjMetaCadenas(SQLiteDatabase db){
        db.execSQL(" CREATE TABLE IF NOT EXISTS MetaMensaje ("
            + " Id INTEGER "
            + ", IdProyecto INTEGER "
            + ", mensaje VARCHAR "
            + ", tipo VARCHAR "
            + ", diaCaptura INTEGER"
            + ");"
    
        );
    
    }
    
    
}
