package br.usp.ime.mac5743;

import javax.microedition.khronos.opengles.GL10;

import android.view.MotionEvent;

public class WorldOfTwo {

	private static final int DRAW = 123324;
	private static final int LOW_WINS = 3423;
	private static final int HIGH_WINS = 007;
	private static final int NO_WINNER_YET = 31232;
	
	private Paddle paddleHighSide; private Paddle paddleLowSide;
	private Ball ball;
	private Line lineHighSide; private Line lineLowSide;
	
	private Brick highInvader; private Brick lowInvader;
	private boolean game_over; private int gameOverTimer;
	
	public static float[] colorPlayerInTheHighSide = {1f,0f,0f,0f};
	public static float[] colorPlayerInTheLowSide = {0f,0f,1f,0f};
	public static float[] colorNeutral ={201f/256f,192f/256f,187f/256f,1};
	private float linePos = 0.6f;
	private float paddlePos = 0.8f;
	
	private BrickList brickList;
	
	public WorldOfTwo(){
		this.start();
	}

	private void restart() {
		this.start();
	}
	
	private void start() {
		game_over = false;highInvader = null; lowInvader = null;
		ball = new Ball();
		paddleHighSide = new twoPlayerPaddle(0f,paddlePos,colorPlayerInTheHighSide);
		lineHighSide = new Line(linePos,colorPlayerInTheHighSide);
		paddleLowSide = new twoPlayerPaddle(0f,-paddlePos,colorPlayerInTheLowSide);
		lineLowSide = new Line(-linePos,colorPlayerInTheLowSide);
		brickList = new BrickList(2);
	}
	
	public void setHitBrickHandler(HitBrickHandler hitBrickHandler) {
	}

	private int whoWon() {
		lowInvader = brickList.brickBellow(-linePos);
		highInvader = brickList.brickAbove(linePos);
		if (highInvader != null && lowInvader != null)
			return DRAW;
		if (highInvader != null)
			return LOW_WINS;
		if (lowInvader != null)
			return HIGH_WINS;
		return NO_WINNER_YET;
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
		if (game_over) {
			//Eles já foram desenhados na BrickList
			//mas precisamos garantir que eles fiquem por cima
			if (highInvader != null)
				highInvader.draw(gl);
			if (lowInvader != null)
			    lowInvader.draw(gl);
		}
	}

	public void step() {

		if (!game_over) {
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
			verifyGameOver();
		}
		
		if (game_over)
			stepGameOver();
	}

	private void stepGameOver() {
		if(highInvader != null)
			highInvader.grow();
		if(lowInvader != null)
			lowInvader.grow();
		if (gameOverTimer-- == 0)
			restart();
	}
	
	private void verifyGameOver() {
		int victory = whoWon();

        if (victory == DRAW) {	
			highInvader.goHalfway();
			lowInvader.goHalfway();
        }
        
		if (victory != NO_WINNER_YET) {
			game_over = true;
			//TODO colocar esse timer nas constantes
			gameOverTimer = 500;
		}
		
			
		
	} 
}
