package br.usp.ime.mac5743;

import javax.microedition.khronos.opengles.GL10;

import android.view.MotionEvent;

public class World {

	private Paddle paddle;
	private Ball ball;
	private BrickList bricks;
	private HitBrickHandler hitBrickHandler;
	
	private float ratio;

	public void setHitBrickHandler(HitBrickHandler hitBrickHandler) {
		this.hitBrickHandler = hitBrickHandler;
	}

	public World(float ratio){
		this.ratio = ratio;
        reset();
	}
	
	private void reset (){
		ball = new Ball();
		bricks = new BrickList(1,ratio);
		paddle = new Paddle(ball);
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
			reset();
	} 
}
