package br.usp.ime.mac5743.engine;

import javax.microedition.khronos.opengles.GL10;

import br.usp.ime.mac5743.objects.Ball;
import br.usp.ime.mac5743.objects.BrickList;
import br.usp.ime.mac5743.objects.Paddle;
import br.usp.ime.mac5743.util.SoundManager;
import android.view.MotionEvent;

public class WorldOfOne extends World {

	private Paddle paddle;
	private Ball ball;
	private BrickList bricks;
	private SoundManager soundManager;
	private int level = 1;
	
	private static float[] paddleColor = {0.0f,0.7f,0.7f,1.0f}; 

	public void setHitSoundHandler(SoundManager hitSoundHandler) {
		this.soundManager = hitSoundHandler;
	}
	
	private void restart() {
		start();
	}
	
	protected void start (){
		ball = new Ball();
		ball.increaseSpeed();
		bricks = new BrickList(1,level,screenRatio);
		paddle = new Paddle(ball);
		paddle.setColor(paddleColor);
	}

	public void startBallIfNotStarted() {
		if (ball.stopped())
			ball.launch();
	}

	public void handleTouch(MotionEvent e, float x, float y) {
		switch ( e.getAction() ) {
		case MotionEvent.ACTION_MOVE:
			updatePaddleSpeed( x, y );
			break;
		case MotionEvent.ACTION_UP:
			startBallIfNotStarted();
			break;
		}
	}

	public void updatePaddleSpeed( float x, float y ) {
		paddle.setDestination( x );
	}

	public void draw(GL10 gl) {
		paddle.draw(gl);
		ball.draw(gl);
		bricks.draw(gl);
	}
	
	public boolean isFinished() {
		return (level > BrickList.maxLevels);
	}
	
	private boolean lost(){
		return (ball.posY < -1.2f);
	}

	public void step() {
		paddle.updatePosition();
		ball.updatePosition();

		if (bricks.checkHitAndDeflect(ball)){
			if(bricks.playHitSound() && soundManager != null)
				soundManager.playHit();
		}
		if (paddle.gotHit(ball)) 
			paddle.changeSpeed(ball);
		
		if (lost()){
			soundManager.playTeleport();
			restart();
		}
		
		if (bricks.allBricksAreDead()){
			soundManager.playApplause();
			level +=1;
			if (level <= BrickList.maxLevels)
			    restart();
		}
	} 
}
