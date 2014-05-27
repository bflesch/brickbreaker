package br.usp.ime.mac5743.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import br.usp.ime.mac5743.R;

public class MainMenu extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_menu);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void startSinglePlayerGame(View v){
		Intent intent = new Intent(this, MainActivity.class);
		intent.putExtra(MainActivity.EXTRA_PLAYER_NUMBER, MainActivity.VALUE_SINGLE_PLAYER);
		startActivity(intent);
	}
	
	public void startTwoPlayerGame(View v){
		Intent intent = new Intent(this, MainActivity.class);
		intent.putExtra(MainActivity.EXTRA_PLAYER_NUMBER, MainActivity.VALUE_TWO_PLAYER);
		startActivity(intent);
	}
	
	public void startCredits(View v){
		Intent intent = new Intent(this, Credits.class);
		startActivity(intent);
	}

}
