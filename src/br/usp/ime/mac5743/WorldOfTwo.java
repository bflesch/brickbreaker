package br.usp.ime.mac5743;

import javax.microedition.khronos.opengles.GL10;

public class WorldOfTwo {

	private Paddle paddleHighSide;
	private Paddle paddleLowSide;
	private Ball ball;
	public static float[] colorPlayerInTheHighSide = {1f,0f,0f,0f};
	public static float[] colorPlayerInTheLowSide = {0f,0f,1f,0f};
	
	private BrickList brickList;
	
	public WorldOfTwo(){
		ball = new Ball();
		paddleHighSide = new twoPlayerPaddle(0f,0.8f,colorPlayerInTheHighSide);
		paddleLowSide = new twoPlayerPaddle(0f,-0.8f,colorPlayerInTheLowSide);
	    brickList = new BrickList(2);
	}

	
	public void startBallIfNotStarted() {
		if (ball.stopped())
		     ball.launch();
	}
	
	public void updatePaddleSpeed( float x, float y ) {
		if (y > 0)
		   paddleHighSide.setDestination( x );
		if (y < 0)
		   paddleLowSide.setDestination(x);
		startBallIfNotStarted();
	}
	
	public void draw(GL10 gl) {
		paddleHighSide.draw(gl);
		paddleLowSide.draw(gl);
		ball.draw(gl);
		brickList.draw(gl);
	}
	
	public void step() {
		Paddle paddle1 = paddleHighSide;
		Paddle paddle2 = paddleLowSide;
        paddle1.updatePosition();
        paddle2.updatePosition();
        //TODO remove reference
        ball.updatePosition();
        float [] normalForceDirection = {0f,0f};
        if (paddle1.gotHit(ball,normalForceDirection)){ 
        	ball.deflect(normalForceDirection);
        	ball.color = paddle1.color;
        }
        if (paddle2.gotHit(ball,normalForceDirection)){ 
        	ball.deflect(normalForceDirection);
        	ball.color = paddle2.color;
        }
        if (brickList.gotHit(ball,normalForceDirection)){ 
        	ball.deflect(normalForceDirection);
        }
	} 
}
