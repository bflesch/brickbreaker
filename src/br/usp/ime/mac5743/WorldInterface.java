package br.usp.ime.mac5743;

import javax.microedition.khronos.opengles.GL10;

import android.view.MotionEvent;

public abstract class WorldInterface {
	
	float screenRatio;
	
	abstract public void step();
	abstract protected void start();
	abstract public void setHitSoundHandler(HitSoundHandler hitBrickHandler);
	abstract public void draw(GL10 gl);
	abstract public void handleTouch(MotionEvent e, float x, float y);
	abstract public boolean isFinished(); 
	
	public void generate(float ratio){
		this.screenRatio = ratio;
		Brick.ratio = ratio;
		start();
	}

}
