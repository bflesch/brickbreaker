package br.usp.ime.mac5743.util;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import br.usp.ime.mac5743.R;

public class SoundManager {

	private MediaPlayer bgmPlayer;
	private SoundPool sfxPool;
	private int hitSoundId;
	private int applauseSoundId;
	private int teleportSoundId;

	private final int SOUND_POOL_SIZE = 3;
	private boolean started = false;
	

	public SoundManager(Context context){
		bgmPlayer = MediaPlayer.create(context, R.raw.wallpaper);
		bgmPlayer.setLooping(true);

		sfxPool = new SoundPool(SOUND_POOL_SIZE, AudioManager.STREAM_MUSIC, 0);
		hitSoundId = sfxPool.load(context, R.raw.tchuc, 1);
		applauseSoundId = sfxPool.load(context,R.raw.applause, 1);
		teleportSoundId = sfxPool.load(context,R.raw.teleport, 1);
		started = false;
	}

	public void playHit() {
		if(started){
			sfxPool.play(hitSoundId, 0.8f, 0.8f, 1, 0, 1);
		}
	}

	public void playApplause(){
		if(started){
			sfxPool.play(applauseSoundId, 1.0f, 1.0f, 2, 0, 1);
		}
	}
	
	public void playTeleport(){
		if(started){
			sfxPool.play(teleportSoundId, 0.8f, 0.8f, 2, 0, 1);
		}
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