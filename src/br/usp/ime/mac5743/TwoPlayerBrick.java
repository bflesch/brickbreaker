package br.usp.ime.mac5743;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

class TwoPlayerBrick extends Brick {

	float speed;
	static float max_speed = .1f/150.0f;
	static float UP = max_speed;
	static float DOWN = -max_speed;

	public TwoPlayerBrick(float x, float y) {
		super(x,y);
		color = WorldOfTwo.colorNeutral;
		makesSound = false;
	}

	private boolean equal_colors(float[] a, float[] b) {
		for(int i=0; i<4; i++)
			if (a[i] != b[i])
				return false;
		return true;
	}

	public void step() {
		this.posY += speed;
	}

	public void collide (int side, Ball ball) {
		
		if (side == WITH_TOP && //
				equal_colors(ball.color,WorldOfTwo.colorPlayerInTheHighSide)){
			speed = DOWN;
			this.color = ball.color;
		}

		if (side == WITH_BOTTOM && //
				equal_colors(ball.color,WorldOfTwo.colorPlayerInTheLowSide)){
			speed = UP;
			this.color = ball.color;
		}
	}
}
