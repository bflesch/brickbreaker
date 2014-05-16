package br.usp.ime.mac5743;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

public class Paddle extends Brick {
	
	//TODO add code for special deflection on the upper side
	//TODO corrigir paddle que invade a bola
	
	private float speed = 0.0f;
	private float destinationX = 0.0f;
	private static float max_speed = 0;
	private Ball ball = null;

    //on a one player game. Needs a ball, knows where to start
	public Paddle(Ball ball) {
		super(0f,-.8f);
		height = .1f;
		width = .4f;
		this.ball = ball;
		ball.setPosition(posX, posY+(height/2)+ball.radius);
		buildGlBuffer();
	}
	
	//     on a two player game. Lacks a ball, 
	//              does not know where to start
	public Paddle(float x, float y) {
		super(x,y);
		height = .1f;
		width = .4f;
		buildGlBuffer();
	}

	@Override
	public void collide (int unused,Ball unused2) {
	}

	public void setDestination( float x ) {
		float ratio = TouchSurfaceView.getRatio();
		max_speed = 3*ratio/150.0f;

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
		//ball might be null for other types of paddle
		if (ball != null && ball.stopped())
			ball.comeWithMe(posX);
	}
	
}