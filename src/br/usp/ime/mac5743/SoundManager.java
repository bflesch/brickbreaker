package br.usp.ime.mac5743;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;

public class SoundManager implements HitSoundHandler {

	private MediaPlayer bgmPlayer;
	private SoundPool sfxPool;
	private int hitSoundId;
	private int applauseSoundId;

	private final int SOUND_POOL_SIZE = 2;
	private boolean started = false;

	public SoundManager(Context context){
		bgmPlayer = MediaPlayer.create(context, R.raw.wallpaper);
		bgmPlayer.setLooping(true);

		sfxPool = new SoundPool(SOUND_POOL_SIZE, AudioManager.STREAM_MUSIC, 0);
		hitSoundId = sfxPool.load(context, R.raw.tchuc, 1);
		applauseSoundId = sfxPool.load(context,R.raw.applause, 1);
		started = false;
	}

	protected void playHit() {
		if(started){
			sfxPool.play(hitSoundId, 50.0f, 50.0f, 1, 0, 1);
		}
	}

	public void playApplause(){
		if(started){
			sfxPool.play(applauseSoundId, 50.0f, 50.0f, 1, 0, 1);
		}
	}

	public void onHit() {
		playHit();
	}

	public void start() {
		if(!started){
			started = true;
			bgmPlayer.start();
		}
	}

	public void pause(){
		if(started){
			started = false;
			bgmPlayer.pause();
		}
	}
}