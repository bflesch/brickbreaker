package br.usp.ime.mac5743;

import javax.microedition.khronos.opengles.GL10;

public class World {

	private Paddle paddle;
	private Ball ball;
	private BrickList bricks;
	
	public World(){
		ball = new Ball();
		bricks = new BrickList();
		paddle = new Paddle(ball);
	}
	
	public void startBallIfNotStarted(float y) {
         startBallIfNotStarted();
	}
	
	private void startBallIfNotStarted() {
		if (ball.stopped())
		     ball.launch();
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
        if (bricks.gotHit(ball,normalForceDirection)) 
        	ball.deflect(normalForceDirection);
        if (paddle.gotHit(ball,normalForceDirection)) 
        	ball.deflect(normalForceDirection);
	} 
}
