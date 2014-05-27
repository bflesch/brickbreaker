package br.usp.ime.mac5743.engine;

import javax.microedition.khronos.opengles.GL10;

import br.usp.ime.mac5743.objects.Brick;
import br.usp.ime.mac5743.util.SoundManager;
import android.view.MotionEvent;

public abstract class World {
	
	float screenRatio;
	boolean generated = false;
	
	abstract public void step();
	abstract protected void start();
	abstract public void setHitSoundHandler(SoundManager hitBrickHandler);
	abstract public void draw(GL10 gl);
	abstract public void handleTouch(MotionEvent e, float x, float y);
	abstract public boolean isFinished(); 
	
	public void generate(float ratio){
		this.generated = true;
		this.screenRatio = ratio;
		Brick.ratio = ratio;
		start();
	}
	public boolean isGenerated() {
		return generated;
	}

}
