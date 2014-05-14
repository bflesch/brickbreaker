package br.usp.ime.mac5743;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

class Ball {
	
	private int points = 70;
	private float radius=.05f;
	
    private float posX = 0.0f;
    private float posY = -0.89f;

    private FloatBuffer vertexBuffer;
    
	private float speedX = 0.0f;
	private float speedY = 0.0f;
    
    
    
    private static final int FLOAT_SIZE_BYTES = Float.SIZE / 8;
    
    public Ball(){

        float[] vertices=new float[(points+2)*2];
        vertices[0]=vertices[1] = 0;
        for(int i=1;i<=(points+1);i+=1){
          double rad=(i*1.0/points)*(3.14*2);
          vertices[2*i]=radius * (float)Math.cos(rad);
          vertices[2*i+1]=radius *(float) Math.sin(rad);
        }
    	
        
          ByteBuffer bBuff=ByteBuffer.allocateDirect(vertices.length*4);    
          bBuff.order(ByteOrder.nativeOrder());
          vertexBuffer=bBuff.asFloatBuffer();
          vertexBuffer.put(vertices);
          vertexBuffer.position(0);


    }    
    
    public void launch(){
    	speedX = 1.0f/150.0f;
    	speedY = speedX;
    }
    
    public void updatePosition(World world){
    	float futurePosX = posX + speedX;
    	float futurePosY = posY + speedY;
    	if(isOutOfBoundsX(futurePosX)){
    		speedX = -speedX;
    	}
    	if(isOutOfBoundsY(futurePosY)){
    		speedY = -speedY;
    	}
    	if(world.getPaddle().contains(futurePosX,futurePosY)){
    		if(!world.getPaddle().contains(posX, posY)){
    			speedY = -speedY;
    		}
    	}
    	posX = posX+speedX;
    	posY = posY+speedY;
    }

    private boolean isOutOfBoundsY(float futurePosY) {
    	return ((futurePosY > 1.0f) || (futurePosY < -1.0f));
	}

	private boolean isOutOfBoundsX(float futurePosX) {
		return ((futurePosX > TouchSurfaceView.getRatio()) || (futurePosX < -TouchSurfaceView.getRatio()));
	}

	public void draw( GL10 gl ) {
        gl.glMatrixMode( GL10.GL_MODELVIEW );
        gl.glPushMatrix();
        gl.glLoadIdentity();
        gl.glTranslatef( posX, posY, 0.0f );
        gl.glColor4f(1.0f,0.0f,0.0f, 1.0f);
        //gl.glScalef( 0.01f, 0.01f, 0.01f );

        gl.glEnableClientState( GL10.GL_VERTEX_ARRAY );
        gl.glColor4f(1.0f,0.0f,0.0f, 1.0f);
        
        gl.glVertexPointer( 2, GL10.GL_FLOAT, 0, vertexBuffer );
        //cor, draw, declaracao, radius
        gl.glDrawArrays(GL10.GL_TRIANGLE_FAN, 0, points+2);
        
        gl.glDisableClientState( GL10.GL_VERTEX_ARRAY );
        gl.glDisableClientState( GL10.GL_COLOR_ARRAY );
        
        gl.glPopMatrix();
    }
}