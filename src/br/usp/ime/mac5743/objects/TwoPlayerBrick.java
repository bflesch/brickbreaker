package br.usp.ime.mac5743.objects;

import br.usp.ime.mac5743.engine.WorldOfTwo;


class TwoPlayerBrick extends Brick {

	float speed;
	//Note: não faça com que max_speed + ball.maxspeed
	//seja maior que a menor dimensão do bloquinho.
	//caso contrário, poderá haver teleportes
	// (i.e., o bloquinho e a bola deviam bater, mas
	// passam reto um pelo outro)
	static float max_speed = .1f/150.0f;
	static float UP = max_speed;
	static float DOWN = -max_speed;

	public TwoPlayerBrick(float x, float y) {
		super(x,y);
		setColor(WorldOfTwo.colorNeutral);
		makesSound = false;
        isKillable = false;
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
			this.setColor(ball.color);
		}

		if (side == WITH_BOTTOM && //
				equal_colors(ball.color,WorldOfTwo.colorPlayerInTheLowSide)){
			speed = UP;
			this.setColor(ball.color);
		}
	}
}
