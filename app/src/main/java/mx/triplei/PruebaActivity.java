package mx.triplei;

import android.app.ActionBar;
import android.os.Bundle;
import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.view.Menu;
import mx.triplei.R;

public class PruebaActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_prueba);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.prueba, menu);
                        Sucursal sucursal =new Sucursal();
        Drawable d=sucursal.setIconMenu(this);
        ActionBar actionBar = getActionBar();
        actionBar.setIcon(d);
		return true;
	}

}
