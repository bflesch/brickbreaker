package br.usp.ime.mac5743;

import javax.microedition.khronos.opengles.GL10;

class BrickList {

	int bricks;
	int movableBricks;
	Brick brickV[]; 
	boolean playSound;

	
	public BrickList(int players, float ratio) {
		if(players == 1)
			buildOnePlayerGame(ratio);
		if(players == 2)
			buildTwoPlayerGame(ratio);
	}
	
	private void buildOnePlayerGame (float ratio){
		bricks = 95;
		movableBricks = 0;
		brickV = new Brick[bricks];
		float xStart = -ratio + Brick.defaultWidth/2;
		float x = xStart;
		float xStep = ((2*ratio - 4*Brick.defaultWidth)/3)
				        + Brick.defaultWidth;
		float y = -.6f;
		for(int i=0; i<bricks-3; i++) {
			Brick brick = new Brick (x,y);
			brickV[i]=brick;
			x += xStep;
			if (x > ratio) {
				x = xStart;
				y += .06f;
			}
		brickV[bricks-1] = new IronBrick(-ratio -0.1f, 0, 2.5f, 0.2f);
		brickV[bricks-2] = new IronBrick(+ratio +0.1f, 0, 2.5f, 0.2f);
		brickV[bricks-3] = new IronBrick(0 , 1+0.1f, 0.2f, 2.5f);
		brickV[bricks-4] = new IronBrick(0 , -1-0.1f, 0.2f, 2.5f);
		}
	}

	public void buildTwoPlayerGame(float screenRatio){
		bricks = 7;
		movableBricks = 3;
		brickV = new Brick[bricks];
		brickV[0] = new TwoPlayerBrick (-.4f,0);
		brickV[1] = new TwoPlayerBrick (.4f,0);
		brickV[2] = new TwoPlayerBrick (0f,0f);
		
		brickV[3] = new IronBrick(-screenRatio -0.1f, 0, 2.5f, 0.2f);
		brickV[4] = new IronBrick(+screenRatio +0.1f, 0, 2.5f, 0.2f);
		brickV[5] = new IronBrick(0 , 1+0.1f, 0.2f, 2.5f);
		brickV[6] = new IronBrick(0 , -1-0.1f, 0.2f, 2.5f);
	}

	public boolean allBricksAreDead() {
		for(int i=0;i<bricks;i++)
			if (brickV[i].isAlive && brickV[i].isKillable)
				return false;
		return true;
	}
	
	public boolean playHitSound (){
		return playSound;
	}
	
	public boolean checkHitAndDeflect(Ball ball) {
		boolean gotHit = false; 
		playSound = false;
		float[] normal = {0,0};
		float[] normalTemp; 
		for(int i=0;i<bricks;i++)
			if (brickV[i].isAlive && 
					brickV[i].gotHit(ball)) {
				normalTemp = brickV[i].normal(ball);
				normal[0] += normalTemp[0];normal[1] += normalTemp[1];
				gotHit = true;
				if (brickV[i].makesSound)
				    playSound = true;
			}
		if(gotHit == true) {
			Brick.normalize(normal);
			Brick.reflect(ball,normal);
		}
		return gotHit;			
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

	public Brick brickAbove(float linePos) {
		for(int i=0;i<movableBricks;i++)
			if (brickV[i].isAlive)
			    if (brickV[i].posY - brickV[i].height > linePos)
			    	return brickV[i];
		return null;
	}
	
	public Brick brickBellow(float linePos) {
		for(int i=0;i<movableBricks;i++)
			if (brickV[i].isAlive)
				if (brickV[i].posY + brickV[i].height < linePos)
			    	return brickV[i];
		return null;
	}
	
}