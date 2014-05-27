package br.usp.ime.mac5743.objects;


class IronBrick extends Brick {

	
	public IronBrick(float x, float y, float h, float w){
		super(x,y);
		posX=x; posY=y; height=h; width=w;
		makesSound = false;
		isKillable = false;
		getColor()[0] = 101/256; getColor()[1] = 110/256; getColor()[2] = 117/256; getColor()[3] = 1;
		buildGlBuffer();
	}
	
	
	public void collide (int side, Ball ball) {
	}


}