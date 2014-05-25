package br.usp.ime.mac5743;

class IronBrick extends Brick {

	
	public IronBrick(float x, float y, float h, float w){
		super(x,y);
		posX=x; posY=y; height=h; width=w;
		makesSound = false;
		isKillable = false;
		color[0] = 101/256; color[1] = 110/256; color[2] = 117/256; color[3] = 1;
		buildGlBuffer();
	}
	
	
	public void collide (int side, Ball ball) {
	}


}