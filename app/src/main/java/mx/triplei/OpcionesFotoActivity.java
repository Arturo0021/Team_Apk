package mx.triplei;

import android.app.ActionBar;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import mx.smartteam.entities.Categoria;
import mx.smartteam.entities.CategoriaCollection;
import mx.smartteam.entities.EnumFormulario;
import mx.smartteam.entities.Foto;
import mx.smartteam.entities.Marca;
import mx.smartteam.entities.MarcaCollection;
import mx.smartteam.entities.Opcion;
import mx.smartteam.entities.OpcionCollection;
import mx.smartteam.entities.Producto;
import mx.smartteam.entities.ProductoCollection;
import mx.smartteam.settings.Ajustes;
import mx.smartteam.settings.ServiceClient;

import android.opengl.Visibility;
import android.os.AsyncTask;
import android.os.Bundle;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Toast;
import mx.triplei.R;

public class OpcionesFotoActivity extends Activity {

	private GlobalSettings settings;
	private Foto objFoto;
	private Categoria currenCategoria;
	private Dialog dialog;
	private OpcionCollection itemsSelected;
	private Context context;
	private ProgressDialog _progressDialog;
	private int _progress = 0;
	private byte[] byteImage;
       Sucursal sucursal =new Sucursal();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;
		setContentView(R.layout.activity_opciones_foto);

		settings = ((GlobalSettings) getApplicationContext());

		ImageButton imgButton = (ImageButton) findViewById(R.id.btnOpciones);

		LinearLayout LayoutCategoria = (LinearLayout) findViewById(R.id.LayoutCategoria);
		LinearLayout LayoutComentario = (LinearLayout) findViewById(R.id.LayoutComentario);
		ImageButton btnOpciones = (ImageButton) findViewById(R.id.btnOpciones);

		this.objFoto = (Foto) this.settings.CurreMenu.Tag;
		if (this.objFoto != null) {

			// Verificamos que permita seleccionar categoria
			if (this.objFoto.Categoria != null
					&& this.objFoto.Categoria.size() > 0) {
				LayoutCategoria.setVisibility(View.VISIBLE);
				SetCategoria();

			}

			// Verificamos k permita seleccionar opciones
			if (this.objFoto.Opcion != null && this.objFoto.Opcion.size() > 0) {

				btnOpciones.setVisibility(View.VISIBLE);

				dialog = SetOpciones();
				imgButton.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub

						dialog.show();
					}
				});

			}

			// Verificamos que permita capturar comentarios
			if (this.objFoto.Opcion != null
					&& this.objFoto.PermiteComentario.equals(true)) {

				LayoutComentario.setVisibility(View.VISIBLE);
			}

			VistaPrevia();

		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.foto, menu);
                Sucursal sucursal =new Sucursal();
                Drawable d=sucursal.setIconMenu(context);
                ActionBar actionBar = getActionBar();
                actionBar.setIcon(d);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case R.id.action_save:
			EnviarFoto();
			return true;
		case R.id.action_cancelar:
			finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void EnviarFoto() {

		EditText editComentario = (EditText) findViewById(R.id.editComentario);
		String opcion = "";

		// Bitmap bmp = settings.Image;
		// ByteArrayOutputStream stream = new ByteArrayOutputStream();
		// bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
		// byte[] byteArray = stream.toByteArray();

		StringBuilder strOpcion = new StringBuilder();
		strOpcion.append("");
		if (itemsSelected != null) {
			for (Opcion item : itemsSelected) {

				strOpcion.append("," + item.Id);

			}
			opcion = strOpcion.toString().substring(1,
					strOpcion.toString().length());

		}

		new SendFoto().execute(this.byteImage, settings.EntityProyecto.Id
				.toString(), settings.EntityTienda.Id.toString(),
				settings.EntityUsuario.Id.toString(), currenCategoria.Id
						.toString(), editComentario.getText().toString(),
				opcion);

	}

	private void SetCategoria() {
		if (this.objFoto != null) {

			Spinner spinCategoria = (Spinner) findViewById(R.id.spinCategoria);

			spinCategoria.setAdapter(new Categoria.Adapter(getBaseContext(),
					android.R.layout.simple_spinner_dropdown_item,
					this.objFoto.Categoria));

			// evento para selecionar un item
			spinCategoria
					.setOnItemSelectedListener(new OnItemSelectedListener() {
						@Override
						public void onItemSelected(AdapterView<?> adapterView,
								View view, int position, long id) {

							MarcaCollection marcaCollectionCategoria = null;
							Spinner spinMarca = (Spinner) findViewById(R.id.spinMarca);

							Categoria.Adapter adapterCategoria = (Categoria.Adapter) adapterView
									.getAdapter();

							currenCategoria = adapterCategoria
									.getItem(position);

						}

						@Override
						public void onNothingSelected(AdapterView<?> adapter) {
						}
					});

		}
	}

	private Dialog SetOpciones() {

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
                Drawable d=sucursal.setIconAlert(context);
                builder.setIcon(d);
		final Opcion.AdapterMultiple adapter = new Opcion.AdapterMultiple(this,
				this.objFoto.Opcion);
                builder.setTitle( Html.fromHtml("<font color='#FFFFF'>"+(this.objFoto.Opcion.get(0).Nombre)+"</font>"));   
		//builder.setTitle(this.objFoto.Opcion.get(0).Nombre);

		builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
			}
		});

		builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				itemsSelected = adapter.GetItemsSelected();

			}
		});

		AlertDialog alertDialog = builder.create();
		// alertDialog.getListView().setAdapter(adapter);
                alertDialog.setIcon(d);
		alertDialog.getListView().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		// alertDialog.getListView().setOnItemClickListener(
		// new AdapterView.OnItemClickListener() {

		// @Override
		// public void onItemClick(AdapterView<?> parent, View view,
		// int position, long id) { // TODO Auto-generated
		// method stub

		// }

		// });

		return alertDialog;
	}

	private void VistaPrevia() {

		/*
		 * Bitmap bitmap = settings.Image;
		 * 
		 * BitmapDrawable bitmapDrawable = new BitmapDrawable(bitmap);
		 * 
		 * ImageView preview = (ImageView) findViewById(R.id.imageViewPreview);
		 * preview.setAdjustViewBounds(true);
		 * 
		 * preview.setImageDrawable(bitmapDrawable);
		 */
		byte[] image = this.getIntent().getByteArrayExtra("image");
		if (image != null) {
			byteImage = image;

			Bitmap bitmapPicture = BitmapFactory.decodeByteArray(image, 0,
					image.length);

			ImageView preview = (ImageView) findViewById(R.id.imageViewPreview);
			preview.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
			preview.setImageBitmap(bitmapPicture);
		}

	}

	class SendFoto extends AsyncTask<Object, Integer, String> {

		private Object[] parameters;
		private ProgressDialog pd;

		@Override
		protected void onPreExecute() {

			pd = new ProgressDialog(context);
			pd.setTitle("Enviando imagen");
			pd.setMessage("Espere por favor.");
			pd.setCancelable(false);
			pd.setIndeterminate(true);
			pd.show();

		}

		@Override
		protected String doInBackground(Object... params) {
			// TODO Auto-generated method stub

			this.parameters = params;

			ServiceClient.Foto.Sondeo objFoto = new ServiceClient.Foto.Sondeo(
					params[0], params[1], params[2], params[3], params[4],
					params[5], params[6]);

			/*
			 * ServiceClient.Fotografia objFoto = new ServiceClient.Fotografia(
			 * params[0], params[1], params[2], params[3], params[4], params[5],
			 * params[6]);
			 */

			return objFoto.GetResult();

		}

		protected void onPostExecute(String resultado) {
			pd.dismiss();
			if (resultado != null && resultado.equals("OK")) {
				Toast.makeText(context, "Fotografia enviada correctamente",
						Toast.LENGTH_LONG).show();
				finish();
			} else {

				Toast.makeText(context, "Error al enviar la foto",
						Toast.LENGTH_LONG).show();
			}

		}
	}
}
