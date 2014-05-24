package br.usp.ime.mac5743;

class IronBrick extends Brick {

	
	public IronBrick(float x, float y, float h, float w){
		super(x,y);
		posX=x; posY=y; height=h; width=w;
		makesSound = false;
		isKillable = false;
		//TODO set color
		color[0] = 0; color[1] = 1; color[2] = 0; color[3] = 1;
		buildGlBuffer();
	}
	
	
	public void collide (int side, Ball ball) {
	}


}