package br.usp.ime.mac5743;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

class IronBrick extends Brick {

	
	public IronBrick(float x, float y, float h, float w){
		super(x,y);
		posX=x; posY=y; height=h; width=w;
		makesSound = false;
		buildGlBuffer();
	}
	
	
	public void collide (int side, Ball ball) {
	}


}