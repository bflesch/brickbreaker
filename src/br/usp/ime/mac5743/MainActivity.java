package br.usp.ime.mac5743;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.opengl.GLSurfaceView;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity implements HitSoundHandler {
	
	public final static String EXTRA_PLAYER_NUMBER = "br.usp.ime.mac5743.players";
	public final static String VALUE_SINGLE_PLAYER = "SINGLE_PLAYER";
	public final static String VALUE_TWO_PLAYER = "TWO_PLAYER";

    private GLSurfaceView glSurfaceView;
    private MediaPlayer bgmPlayer;
    private SoundPool sfxPool;
    private int hitSoundId;
    
    private final int SOUND_POOL_SIZE = 2;
    

	@SuppressLint("NewApi")
	@Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        

        glSurfaceView = new TouchSurfaceView( this );
        setContentView( glSurfaceView );
        
        String players = getIntent().getStringExtra(EXTRA_PLAYER_NUMBER);
        if (players.equals(VALUE_SINGLE_PLAYER)){
        	((TouchSurfaceView) glSurfaceView).createSinglePlayerWorld(this); 
        }
        else {
        	((TouchSurfaceView) glSurfaceView).createTwoPlayerWorld(this);
        }

        
        glSurfaceView.requestFocus();
        glSurfaceView.setFocusableInTouchMode( true );
        
        bgmPlayer = MediaPlayer.create(this, R.raw.wallpaper);
        bgmPlayer.setLooping(true);
        
        sfxPool = new SoundPool(SOUND_POOL_SIZE, AudioManager.STREAM_MUSIC, 0);
        hitSoundId = sfxPool.load(this, R.raw.tchuc, 1);
        
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
        	View decorView = getWindow().getDecorView();
        	int uiOptions = View.SYSTEM_UI_FLAG_LOW_PROFILE;
        	decorView.setSystemUiVisibility(uiOptions);
        }
        
        bgmPlayer.start();
    }


    @Override
    protected void onResume() {
        super.onResume();
        glSurfaceView.onResume();
        bgmPlayer.start();
    }


    @Override
    protected void onPause() {
        super.onPause();
        glSurfaceView.onPause();
        bgmPlayer.pause();
    }
    
    protected void playHit() {
    	sfxPool.play(hitSoundId, 50.0f, 50.0f, 1, 0, 1);
    }

	@Override
	public void onHit() {
		playHit();
	}
}