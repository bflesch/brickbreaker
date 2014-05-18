package br.usp.ime.mac5743;

import android.app.Activity;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.opengl.GLSurfaceView;
import android.os.Bundle;

public class MainActivity extends Activity implements HitBrickHandler {

    private GLSurfaceView glSurfaceView;
    private MediaPlayer bgmPlayer;
    private SoundPool sfxPool;
    private int hitSoundId;
    
    private final int SOUND_POOL_SIZE = 2;
    
    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );

        glSurfaceView = new TouchSurfaceView( this );
        setContentView( glSurfaceView );
        
        glSurfaceView.requestFocus();
        glSurfaceView.setFocusableInTouchMode( true );
        
        bgmPlayer = MediaPlayer.create(this, R.raw.wallpaper);
        bgmPlayer.setLooping(true);
        
        sfxPool = new SoundPool(SOUND_POOL_SIZE, AudioManager.STREAM_MUSIC, 0);
        hitSoundId = sfxPool.load(this, R.raw.tchuc, 1);
        
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