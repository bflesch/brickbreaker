package br.usp.ime.mac5743;

import javax.microedition.khronos.opengles.GL10;

import android.view.MotionEvent;

public class WorldOfTwo {

	private Paddle paddleHighSide; private Paddle paddleLowSide;
	private Ball ball;
	private Line lineHighSide; private Line lineLowSide;
	public static float[] colorPlayerInTheHighSide = {1f,0f,0f,0f};
	public static float[] colorPlayerInTheLowSide = {0f,0f,1f,0f};
	public static float[] colorNeutral ={201f/256f,192f/256f,187f/256f,1};
	private float linePos = 0.6f;
	private float paddlePos = 0.8f;
	
	private BrickList brickList;
	
	public WorldOfTwo(){
		ball = new Ball();
		paddleHighSide = new twoPlayerPaddle(0f,paddlePos,colorPlayerInTheHighSide);
		lineHighSide = new Line(linePos,colorPlayerInTheHighSide);
		paddleLowSide = new twoPlayerPaddle(0f,-paddlePos,colorPlayerInTheLowSide);
		lineLowSide = new Line(-linePos,colorPlayerInTheLowSide);
		brickList = new BrickList(2);
	}
	
	
	public void setHitBrickHandler(HitBrickHandler hitBrickHandler) {
	}

	
	public void startBallIfNotStarted() {
		if (ball.stopped())
		     ball.launch();
	}
	
	public void handleTouch(MotionEvent unused, float x, float y ) {
		if (y > 0)
		   paddleHighSide.setDestination( x );
		if (y < 0)
		   paddleLowSide.setDestination(x);
		startBallIfNotStarted();
	}
	
	public void draw(GL10 gl) {
		lineHighSide.draw(gl);
		lineLowSide.draw(gl);
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
        
        brickList.step();
        
        ball.updatePosition();
        
        if (paddle1.gotHit(ball)){
        	paddle1.changeSpeed(ball);
        	ball.color = paddle1.color;
        }
        if (paddle2.gotHit(ball)){ 
        	paddle2.changeSpeed(ball);
        	ball.color = paddle2.color;
        }
        if (brickList.checkHitAndDeflect(ball)){ 
        }
	} 
}
