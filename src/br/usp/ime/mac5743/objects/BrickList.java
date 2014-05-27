package br.usp.ime.mac5743.objects;

import javax.microedition.khronos.opengles.GL10;

public class BrickList {

	int bricks;
	int movableBricks;
	Brick brickV[]; 
	boolean playSound;
    public static final int maxLevels = 3; 

	public BrickList(int players, int level,float ratio) {
		if(players == 1 && level == 1)
			buildClassicalOnePlayerGame(ratio);
		if(players == 1 && level == 2)
			buildStairsOnePlayerGame(ratio);
		if(players == 1 && level == 3)
			buildGolfOnePlayerGame(ratio);
		if(players == 2)
			buildTwoPlayerGame(ratio);
	}

	private void buildGolfOnePlayerGame(float ratio) {
		bricks = 9;
		brickV = new Brick[bricks];
		float side = ratio/12; //side of the ironbricks
		float positionY = 0.4f; //position of the center
		float width = ratio*1.2f; // side of the box
		float height = .5f; //height of the box
		brickV[0] = new IronBrick(-width/2+side/2, positionY-side/2, 
				height-side, side);
		brickV[1] = new IronBrick(0, height/2-side/2 +positionY
				, side,  width);
		brickV[2] = new IronBrick(+width/2-side/2, positionY, 
				height-2*side, side);
		brickV[3] = new IronBrick(width/4-side/4,-height/2+side/2 + positionY, 
				side, width/2+side/2);
		brickV[4] = new IronBrick(0,  -height/4+side + positionY,  
				height/2,  side);
		brickV[5] = new Brick(width/4,-height*.3f+positionY);

		brickV[6] = new IronBrick(-ratio -0.1f, 0, 2.5f, 0.2f);
		brickV[7] = new IronBrick(+ratio +0.1f, 0, 2.5f, 0.2f);
		brickV[8] = new IronBrick(0 , 1+0.1f, 0.2f, 2.5f);
	}

	private void buildClassicalOnePlayerGame (float ratio){
		bricks = 95;
		brickV = new Brick[bricks];
		float brickWidth = Brick.widthBeforeRatio* Brick.ratio;
		float xStart = -ratio + brickWidth/2;
		float x = xStart;
		float xStep = ((2*ratio - 4*brickWidth)/3)
				+ brickWidth;
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
		}
	}

	private void buildStairsOnePlayerGame (float ratio){
		bricks = 52;
		brickV = new Brick[bricks];
		float brickWidth = Brick.widthBeforeRatio* Brick.ratio;
		float xStart = -ratio + brickWidth/2;
		int xInBricks = 0;
		int xSizeInBricks = (int) Math.floor(2*ratio/brickWidth);
		int yInBricks = 0;
		float yStart = -.6f;
		float yHeightOfBrick = Brick.defaultHeight + 0.01f;
		for(int i=bricks-4; i>-1 ; ) {
			if ((xInBricks - yInBricks) % 4 == 0) {
				float x = xStart + brickWidth*xInBricks;
				float y = yStart + yHeightOfBrick*yInBricks;
				Brick brick = new Brick (x,y);
				brickV[i]=brick;
				i -= 1;
			}
			xInBricks += 1;
			if (xInBricks > xSizeInBricks-1) {
				xInBricks = 0;
				yInBricks += 1;
			}
		}
		brickV[bricks-1] = new IronBrick(-ratio -0.1f, 0, 2.5f, 0.2f);
		brickV[bricks-2] = new IronBrick(+ratio +0.1f, 0, 2.5f, 0.2f);
		brickV[bricks-3] = new IronBrick(0 , 1+0.1f, 0.2f, 2.5f);
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