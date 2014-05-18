package br.usp.ime.mac5743;

import javax.microedition.khronos.opengles.GL10;

import android.view.MotionEvent;

public class World {

	private Paddle paddle;
	private Ball ball;
	private BrickList bricks;
	private HitBrickHandler hitBrickHandler;
	
	public void setHitBrickHandler(HitBrickHandler hitBrickHandler) {
		this.hitBrickHandler = hitBrickHandler;
	}

	public World(){
		ball = new Ball();
		bricks = new BrickList();
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
	
	public void step() {
        paddle.updatePosition();
        //TODO remove reference
        ball.updatePosition();
        float [] normalForceDirection = {0f,0f};
        if (bricks.gotHit(ball,normalForceDirection)){
        	ball.deflect(normalForceDirection);
        	if(hitBrickHandler != null){
        		hitBrickHandler.onHit();
        	}
        }
        if (paddle.gotHit(ball,normalForceDirection)) 
        	ball.deflect(normalForceDirection);
	} 
}
