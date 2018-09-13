package mx.triplei;

//import android.R;
import android.app.ActionBar;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

//import mx.smartteam.Sucursal.DescargaSondeos;
import mx.smartteam.settings.Ajustes;
import mx.smartteam.settings.ServiceClient;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import mx.triplei.R;

public class Asistencia extends Activity {

	public static final int ONE_ID = 1;
	public static final int TWO_ID = 2;
	public static final int THREE_ID = 3;
	public static final int FOUR_ID = 4;

	private int optionSel = 0;
	private String[] items = { "Enviar", "Cancelar" };
	private Uri imageUri;
	private String strPhoto;
	private GlobalSettings appConfiguration;
	private byte[] byteImage;
	private Context context;
        Sucursal sucursal =new Sucursal();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_asistencia);
		this.context = this;

		appConfiguration = ((GlobalSettings) getApplicationContext());

		if (savedInstanceState == null) {
			this.AsistenciaDialog();
		} else {
			this.optionSel = savedInstanceState.getInt("option");
		}

		Log.d("Evento", "OnCreate");
	}

	
	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		super.onSaveInstanceState(savedInstanceState);
		savedInstanceState.putInt("option", this.optionSel);
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.asistencia, menu);
                Sucursal sucursal =new Sucursal();
                Drawable d=sucursal.setIconMenu(context);
                ActionBar actionBar = getActionBar();
                actionBar.setIcon(d);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case R.id.action_enviar:
			AccionEnviar();
			break;
		case R.id.action_foto:
			CapturarImagen();
			break;

		case R.id.action_cancelar:

			break;
		default:
			break;
		}
		return true;
	}

        private void AsistenciaDialog() {
		final CharSequence[] items = { "Entrada", "Salida" };

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle( Html.fromHtml("<font color='#FFFFF'>Asistencia</font>"));
		//builder.setTitle("Asistencia");
                Drawable d=sucursal.setIconAlert(context);
                builder.setIcon(d);
		builder.setSingleChoiceItems(items, 0,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int item) {
						if (items[item].equals("Entrada")) {

							dialog.cancel();
							optionSel = 0;
							CapturarImagen();
						}
						if (items[item].equals("Salida")) {
                                                    dialog.cancel();
							optionSel = 1;
							CapturarImagen();

						}

					}
				});
		AlertDialog alert = builder.create();
		alert.show();

	}

	private void CapturarImagen() {
		Intent i = new Intent("mx.smartteam.fotoasistencia");
		i.putExtra("opcion", optionSel);
		startActivity(i);
                finish();

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode,
			final Intent data) {
		if (resultCode == Activity.RESULT_OK && requestCode == 0) {

			byte[] image = data.getByteArrayExtra("image");
			if (image != null) {
				 byteImage = image;

                                
                            
				Bitmap bitmapPicture = BitmapFactory.decodeByteArray(image, 0,
						image.length);
                                
                                
                                Drawable dr=new BitmapDrawable(bitmapPicture);
                                
                                LinearLayout LayoutAsistencia=(LinearLayout)findViewById(R.id.LayoutAsistencia);
                                            LayoutAsistencia.setBackgroundDrawable(dr);
                                
				//ImageView preview = (ImageView) findViewById(R.id.imgPreview);
				//preview.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
				//preview.setImageBitmap(bitmapPicture);
                                
                                
                                
			}
		}
	}

	private void AccionEnviar() {

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setTitle( Html.fromHtml("<font color='#FFFFF'>"+R.string.title_dialog+"</font>"));   
		//alertDialogBuilder.setTitle(R.string.title_dialog);
                Drawable d=sucursal.setIconAlert(context);
                alertDialogBuilder.setIcon(d);
		alertDialogBuilder
				.setMessage("Dese utilizar la Imagen?")
				.setCancelable(false)
				.setPositiveButton("Si", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {

						new EnviarFoto().execute(byteImage,
								appConfiguration.EntityProyecto.Id.toString(),
								appConfiguration.EntityTienda.Id
										.toString(),
								appConfiguration.EntityUsuario.Id.toString());

					}
				})
				.setNegativeButton("No", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						CapturarImagen();
						dialog.cancel();
					}
				});

		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();

		// show it
		alertDialog.show();

	}

	class EnviarFoto extends AsyncTask<Object, Void, String> {

		private ProgressDialog pd;

		@Override
		protected void onPreExecute() {

			pd = new ProgressDialog(context);
			pd.setTitle("Enviando foto");
			pd.setMessage("Espere por favor.");
			pd.setCancelable(false);
			pd.setIndeterminate(true);
			pd.show();
		}

		@Override
		protected String doInBackground(Object... params) {

			if (optionSel == 0) {
				ServiceClient.Foto.Entrada sendFotoEntrada = new ServiceClient.Foto.Entrada(
						params);
				return sendFotoEntrada.GetResult();
			} else {
				ServiceClient.Foto.Salida sendFotoSalida = new ServiceClient.Foto.Salida(
						params);
				return sendFotoSalida.GetResult();
			}

		}

		protected void onPostExecute(String resultado) {
			pd.dismiss();
			if (resultado.trim().equals("OK")) {
				finish();

			} else {
				Toast.makeText(context, "Error al enviar la foto",
						Toast.LENGTH_LONG).show();
			}
		}

	}

}
