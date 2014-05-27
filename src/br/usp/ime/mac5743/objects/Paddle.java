package br.usp.ime.mac5743.objects;

import br.usp.ime.mac5743.activities.TouchSurfaceView;

public class Paddle extends Brick {
	
	//TODO corrigir paddle que invade a bola
	
	private float speed = 0.0f;
	private float destinationX = 0.0f;
	private static float max_speed = 0;
	private Ball ball = null;
	int lastCollisionSide;
	float deflectorPosition = .4f;

	public static Paddle createTwoPlayerPaddle(float x, float y){
		return new Paddle(x,y);
	}
	
	static Paddle createSinglePlayerPaddle(Ball ball){
		Paddle toReturn = new Paddle(0f,-.8f);
		toReturn.ball = ball;
		ball.setPosition(toReturn.posX, toReturn.posY+(toReturn.height/2)+ball.radius);
		return toReturn;
	}
	
    //on a one player game. Needs a ball, knows where to start
	public Paddle(Ball ball) {
		super(0f,-.8f);
		height = .1f;
		width = .6f*ratio;
		this.ball = ball;
		ball.setPosition(posX, posY+(height/2)+ball.radius);
		buildGlBuffer();
	}
	
	//     on a two player game. Lacks a ball, 
	//              does not know where to start
	private Paddle(float x, float y) {
		super(x,y);
		height = .1f;
		width = .4f;
		buildGlBuffer();
	}

	public void changeSpeed(Ball ball) {
		float[] normal = {0,0};
		boolean[] gotHit = {false};
		int [] withSide = {0};
		checkHit(ball, normal,gotHit, withSide);
		
		
		if (withSide[0] == WITH_TOP) {
			float [] direction = {0,0};
			direction[0] = ball.posX - this.posX; 
			direction[1] = ball.posY - (this.posY-deflectorPosition);
			normalize(direction);
			ball.setDirection(direction);
		}
		if (withSide[0] == WITH_BOTTOM) {
			float [] direction = {0,0};
			direction[0] = ball.posX - this.posX; 
			direction[1] = ball.posY - (this.posY+deflectorPosition);
			normalize(direction);
			ball.setDirection(direction);
		}
	}
	
	public void setDestination( float x ) {
		float ratio = TouchSurfaceView.getRatio();
		max_speed = 5*ratio/150.0f;

		destinationX = x;

		if (posX > destinationX) {
			speed = -max_speed;
		}
		else{
			speed =  max_speed;
		}
	}

	public boolean reachesOrPassesDestination(){
		return (Math.abs(posX - destinationX) < Math.abs(speed));
	}

	public void updatePosition(){
		if (reachesOrPassesDestination()){
			posX = destinationX;
		}
		else {
			posX = posX+speed;
		}
		if (ball != null && ball.stopped())
			ball.comeWithMe(posX);
	}
	
}