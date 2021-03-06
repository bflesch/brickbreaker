package br.usp.ime.mac5743.activities;

import br.usp.ime.mac5743.engine.World;
import br.usp.ime.mac5743.util.SoundManager;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

public class MainActivity extends Activity  {

	public final static String EXTRA_PLAYER_NUMBER = "br.usp.ime.mac5743.players";
	public final static String VALUE_SINGLE_PLAYER = "SINGLE_PLAYER";
	public final static String VALUE_TWO_PLAYER = "TWO_PLAYER";

	private TouchSurfaceView glSurfaceView;
	private SoundManager soundManager;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate( Bundle savedInstanceState ) {
		super.onCreate( savedInstanceState );
		
		soundManager = new SoundManager(this);

		glSurfaceView = new TouchSurfaceView( this );
		setContentView( glSurfaceView );
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		String players = getIntent().getStringExtra(EXTRA_PLAYER_NUMBER);
		if (players.equals(VALUE_SINGLE_PLAYER)){
			World world = glSurfaceView.createSinglePlayerWorld(this);
			world.setHitSoundHandler(soundManager);
		}
		else {
			World world = glSurfaceView.createTwoPlayerWorld(this);
			world.setHitSoundHandler(soundManager);
		}

		glSurfaceView.requestFocus();
		glSurfaceView.setFocusableInTouchMode( true );

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
			View decorView = getWindow().getDecorView();
			int uiOptions = View.SYSTEM_UI_FLAG_LOW_PROFILE;
			decorView.setSystemUiVisibility(uiOptions);
		}
		
		soundManager.start();
	}


	@Override
	protected void onResume() {
		super.onResume();
		glSurfaceView.onResume();
		soundManager.start();
	}

	@Override
	protected void onPause() {
		super.onPause();
		glSurfaceView.onPause();
		soundManager.pause();
	}
	
	public void onBackPressed(){
		glSurfaceView.inhibitPauseOverlay = true;
		super.onBackPressed();
	}
	
	public SoundManager getSoundHandler() {
		return soundManager;
	}


}