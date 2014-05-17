package br.usp.ime.mac5743;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

class BrickList {

	int bricks;
    Brick brickV[]; 

	public BrickList(){
		bricks = 120;
		brickV = new Brick[bricks];
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

	//TODO meu deus, que gambiarra!
	// temos dois construtores diferentes
	public BrickList(int unused){
		bricks = 3;
		brickV = new Brick[bricks];
		brickV[0] = new TwoPlayerBrick (-.4f,0);
		brickV[1] = new TwoPlayerBrick (.4f,0);
		brickV[2] = new TwoPlayerBrick (0f,0f);

	}
    //never been used. Don't trust me
	public boolean finished() {
		for(int i=0;i<bricks;i++)
        	if (brickV[i].isAlive)
        		return false;
		return true;
	}
	//TODO maybe combine many collision directions
	//TODO maybe allow the ball to hit many targets
	public boolean gotHit(Ball ball, float[] direction) {
        for(int i=0;i<bricks;i++)
        	if (brickV[i].isAlive && brickV[i].gotHit(ball,direction))
        		return true;
        return false;			
	}
	
	public void step() {
        for(int i=0;i<bricks;i++)
        	brickV[i].step();	
	}

	public void draw( GL10 gl ) {
        for(int i=0;i<bricks;i++)
        	if (brickV[i].isAlive)
        	    brickV[i].draw(gl);
	}
}