package br.usp.ime.mac5743;

import javax.microedition.khronos.opengles.GL10;

import android.view.MotionEvent;

public interface WorldInterface {
	
	abstract public void step();
	abstract public void setHitBrickHandler(HitBrickHandler hitBrickHandler);
	abstract public void draw(GL10 gl);
	abstract public void handleTouch(MotionEvent e, float x, float y);

}
