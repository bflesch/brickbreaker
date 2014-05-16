package br.usp.ime.mac5743;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

class TwoPlayerBrick extends Brick {

    public TwoPlayerBrick(float x, float y) {
    	super(x,y);
    	//TODO unificar a cor cinza em um lugar
    	color[0] = 201f/256f;
    	color[1] = 192f/256f; 
       	color[2] = 187f/256f;
    	color[3] = 1;
    }
	
	public void collide (int side, Ball ball) {
		this.color = ball.color;
	}
}
