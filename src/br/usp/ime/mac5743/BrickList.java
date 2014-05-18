package br.usp.ime.mac5743;

import javax.microedition.khronos.opengles.GL10;

class BrickList {

	int bricks = 120;
    Brick brickV[] = new Brick[bricks];


	public BrickList(){
		float x = -.6f;
		float y = -.6f;
		for(int i=0; i<bricks; i++) {
			Brick brick = new Brick (x,y);
			brickV[i]=brick;
			y += .06;
			if (y > .7) {
				y = -.6f;
				x += .4f;
			}
				
		}
	}


	public boolean finished() {
		for(int i=0;i<bricks;i++)
        	if (brickV[i].isAlive)
        		return false;
		return true;
	}
	
	public boolean gotHit(Ball ball, float[] direction) {
        for(int i=0;i<bricks;i++)
        	if (brickV[i].isAlive && brickV[i].gotHit(ball,direction))
        		return true;
        return false;			
	}

	public void draw( GL10 gl ) {
        for(int i=0;i<bricks;i++)
        	if (brickV[i].isAlive)
        	    brickV[i].draw(gl);
	}
}