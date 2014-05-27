package br.usp.ime.mac5743.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import br.usp.ime.mac5743.R;

public class MainMenu extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_menu);
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
