package br.usp.ime.mac5743;

import javax.microedition.khronos.opengles.GL10;

import android.view.MotionEvent;

public class World extends WorldInterface {

	private Paddle paddle;
	private Ball ball;
	private BrickList bricks;
	private HitSoundHandler hitBrickHandler;//TODO caçar esse nome
	private int level = 1;
	
	private static float[] paddleColor = {0.0f,1.0f,1.0f,1.0f}; 

	public void setHitSoundHandler(HitSoundHandler hitBrickHandler) {
		this.hitBrickHandler = hitBrickHandler;
	}
	
	
	private void restart() {
		start();
	}
	
	protected void start (){
		ball = new Ball();
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
	
	private boolean lost(){
		return (ball.posY < -1.2f);
	}

	public void step() {
		paddle.updatePosition();
		ball.updatePosition();

		if (bricks.checkHitAndDeflect(ball)){
			if(bricks.playHitSound() && hitBrickHandler != null)
				hitBrickHandler.onHit();
		}
		if (paddle.gotHit(ball)) 
			paddle.changeSpeed(ball);
		
		if (lost())
			restart();
		
		if (bricks.allBricksAreDead()){
			level +=1;
			if (level > BrickList.maxLevels)
				level = 1;
			restart();
		}
	} 
}
