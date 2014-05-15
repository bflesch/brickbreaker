package br.usp.ime.mac5743;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

class Block {
	
	
	private float posX = 0.0f;
    private float posY = -0.0f;

    private FloatBuffer vertexBuffer;
    
	private float height = .2f;
	private float width = .8f;
	
    
    private static final int FLOAT_SIZE_BYTES = Float.SIZE / 8;
    
    public Block(){
        
    	
        float[] vertices= {
        		0.0f, 0.0f,
        		-width/2, -height/2,
        		+width/2, -height/2,
        		+width/2, +height/2,
        		-width/2, +height/2,
        		-width/2, -height/2,
        };
        
    
    	
        
          ByteBuffer bBuff=ByteBuffer.allocateDirect(vertices.length*4);    
          bBuff.order(ByteOrder.nativeOrder());
          vertexBuffer=bBuff.asFloatBuffer();
          vertexBuffer.put(vertices);
          vertexBuffer.position(0);


    }    
    
    private void collide () {}

    
    
    public boolean gotHit(Ball ball, float[] direction) {
    	float r = ball.radius ;
    	float x = ball.posX;
    	float y = ball.posY;
    	
    	float x_max = posX + width/2;
    	float x_min = posX - width/2;
    	float y_max = posY + height/2;
    	float y_min = posY - height/2;
    	
    	//collide with the bottom
    	if (y_max > y + r && y + r > y_min)
    	    if ( x_min < x  && x < x_max) {
    		    this.collide();
    		    direction[0] = 0f; direction[1] = -1f;
    		    return true;
    	    }

    	//collide with the top
    	if (y_max > y - r && y - r > y_min)
    	    if ( x_min < x  && x < x_max) {
    		    this.collide();
    		    direction[0] = 0f; direction[1] = 1f;
    		    return true;
    	    }
    	
    	//collide with the left
    	if (x_max > x + r && x + r > x_min)
    	    if ( y_min < y  && y < y_max) {
    		    this.collide();
    		    direction[0] = -1f; direction[1] = 0f;
    		    return true;
    	    }
    	
    	//collide with the right
    	if (x_max > x - r && x - r > x_min)
    	    if ( y_min < y  && y < y_max) {
    		    this.collide();
    		    direction[0] = 1f; direction[1] = 0f;
    		    return true;
    	    }
   
    	return false;
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
        gl.glDrawArrays(GL10.GL_TRIANGLE_FAN, 0, 6);
        
        gl.glDisableClientState( GL10.GL_VERTEX_ARRAY );
        gl.glDisableClientState( GL10.GL_COLOR_ARRAY );
        
        gl.glPopMatrix();
    }
}