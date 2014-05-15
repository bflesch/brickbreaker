package br.usp.ime.mac5743;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

public class Paddle extends Brick {
	
	//TODO fix ball initialization
	//TODO add code for special deflection on the upper side
	
	private float speed = 0.0f;
	private float destinationX = 0.0f;
	private static float max_speed = 0;
	private Ball ball;


	public Paddle(Ball ball) {
		super(0f,-.8f);
		height = .1f;
		width = .4f;
		this.ball = ball;
		ball.setPosition(posX, posY+(height/2)+ball.radius);
		buildGlBuffer();
	}


	public void setDestination( float x ) {
		float ratio = TouchSurfaceView.getRatio();
		max_speed = ratio/150.0f;

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
		else
			posX = posX+speed;
		if (ball.stopped())
			ball.comeWithMe(posX);
	}
	
}