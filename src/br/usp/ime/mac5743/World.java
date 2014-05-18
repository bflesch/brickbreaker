package br.usp.ime.mac5743;

import javax.microedition.khronos.opengles.GL10;

import android.view.MotionEvent;

public class World {

	private Paddle paddle;
	private Ball ball;
	private BrickList bricks;
	private HitBrickHandler hitBrickHandler;
	
	//TODO ask Breno: does this deal only with sound ?
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
        float [] newSpeed = {0f,0f};
        if (bricks.gotHit(ball,newSpeed)){
        	ball.speedX = newSpeed[0];
        	ball.speedY = newSpeed[1];
        	if(hitBrickHandler != null){
        		hitBrickHandler.onHit();
        	}
        }
        if (paddle.gotHit(ball,newSpeed)) {
        	ball.speedX = newSpeed[0];
        	ball.speedY = newSpeed[1];
        }
	} 
}
