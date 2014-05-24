package br.usp.ime.mac5743;

class IronBrick extends Brick {

	
	public IronBrick(float x, float y, float h, float w){
		super(x,y);
		posX=x; posY=y; height=h; width=w;
		makesSound = false;
		isKillable = false;
		buildGlBuffer();
	}
	
	
	public void collide (int side, Ball ball) {
	}


}