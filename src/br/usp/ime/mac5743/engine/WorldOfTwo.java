package br.usp.ime.mac5743.engine;

import javax.microedition.khronos.opengles.GL10;

import br.usp.ime.mac5743.objects.Ball;
import br.usp.ime.mac5743.objects.Brick;
import br.usp.ime.mac5743.objects.BrickList;
import br.usp.ime.mac5743.objects.Line;
import br.usp.ime.mac5743.objects.Paddle;
import br.usp.ime.mac5743.util.SoundManager;
import android.view.MotionEvent;

public class WorldOfTwo extends WorldInterface{

	private static final int DRAW = 123324;
	private static final int LOW_WINS = 3423;
	private static final int HIGH_WINS = 007;
	private static final int NO_WINNER_YET = 31232;
	
	private static final int GAME_OVER_ANIMATION_STEPS = 500;
	
	private Paddle paddleHighSide;
	private Paddle paddleLowSide;
	
	private Line lineHighSide;
	private Line lineLowSide;
	
	private Ball[] balls = {null,null,null,null};
	
	private Brick highInvader; private Brick lowInvader;
	private boolean game_over; private int gameOverTimer;
	
	public static float[] colorPlayerInTheHighSide = {1f,0f,0f,1f};
	public static float[] colorPlayerInTheLowSide = {0f,0f,1f,1f};
	public static float[] colorNeutral ={201f/256f,192f/256f,187f/256f,1};
	private float linePos = 0.6f;
	private float paddlePos = 0.8f;
	
	private BrickList brickList;

	
	private void restart() {
		this.start();
	}
	
	protected void start() {
		game_over = false;highInvader = null; lowInvader = null;
		
		balls[0] = new Ball(.2f,.2f);
		balls[1] = new Ball(-.2f,.2f);
		balls[2] = new Ball(.2f,-.2f);
		balls[3] = new Ball(-.2f,-.2f);
		
		paddleHighSide = Paddle.createTwoPlayerPaddle(0f, paddlePos);
		paddleHighSide.setColor(colorPlayerInTheHighSide);
		lineHighSide = new Line(linePos,colorPlayerInTheHighSide);
		paddleLowSide = Paddle.createTwoPlayerPaddle(0f, -paddlePos);
		paddleLowSide.setColor(colorPlayerInTheLowSide);
		lineLowSide = new Line(-linePos,colorPlayerInTheLowSide);
		int unused = 1984;
		brickList = new BrickList(2,unused,screenRatio);
		

	}
	
	public void setHitSoundHandler(SoundManager hitBrickHandler) {
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
		if (balls[0].stopped()) {
			float[] direction0 = {1.0f,1.0f};
			balls[0].setDirection(direction0);
			float[] direction1 = {-1.0f,1.0f};
			balls[1].setDirection(direction1);
			float[] direction2 = {1.0f,-1.0f};
			balls[2].setDirection(direction2);
			float[] direction3 = {-1.0f,-1.0f};
			balls[3].setDirection(direction3);
		}
	}
	
	public boolean isFinished() {
		return false;
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
		for(int i=0; i < balls.length; i++)
		        balls[i].draw(gl);
		brickList.draw(gl);
		if (game_over) {
			//precisamos garantir que eles fiquem por cima
			if (highInvader != null)
				highInvader.draw(gl);
			if (lowInvader != null)
			    lowInvader.draw(gl);
		}
	}

	private void checkCollision(Ball ball) {
		Paddle paddle1 = paddleHighSide;
		Paddle paddle2 = paddleLowSide;
		
		if (paddle1.gotHit(ball)){
			paddle1.changeSpeed(ball);
			ball.color = paddle1.getColor();
		}
		if (paddle2.gotHit(ball)){ 
			paddle2.changeSpeed(ball);
			ball.color = paddle2.getColor();
		}
		if (brickList.checkHitAndDeflect(ball)){ 
		}
	}
	
	public void step() {
        
		if (!game_over) {
			Paddle paddle1 = paddleHighSide;
			Paddle paddle2 = paddleLowSide;
			paddle1.updatePosition();
			paddle2.updatePosition();

			brickList.step();

			for(int i=0; i < balls.length; i++)
				balls[i].updatePosition();

			for(int i=0; i < balls.length; i++)
				checkCollision(balls[i]);
            
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
			gameOverTimer = GAME_OVER_ANIMATION_STEPS;
		}
		
	} 
}
