package br.usp.ime.mac5743;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

class Ball {
    private float posX = 0.0f;
    private float posY = -0.89f;

    private FloatBuffer vertexBuffer;
    private FloatBuffer colorBuffer;
    
	private float speedX = 0.0f;
	private float speedY = 0.0f;
    
    private static final float[] vertices = {
        -1.0f,  -1.0f,
        -1.0f,  1.0f,
         1.0f,  -1.0f,
         1.0f,  1.0f,
     //   X      Y 
    };
    
    private static final float[] colors = {
        0.7f,  0.7f,  0.7f,  1.0f,
        0.7f,  0.7f,  0.7f,  1.0f,
        0.7f,  0.7f,  0.7f,  1.0f,
        0.7f,  0.7f,  0.7f,  1.0f,
     // R       G      B      A 
    };
    
    private static final int FLOAT_SIZE_BYTES = Float.SIZE / 8;
    
    
    public Ball() {
        ByteBuffer vbb = ByteBuffer.allocateDirect( vertices.length * FLOAT_SIZE_BYTES );
        vbb.order( ByteOrder.nativeOrder() );
        vertexBuffer = vbb.asFloatBuffer();
        vertexBuffer.put( vertices );
        vertexBuffer.position( 0 );

        ByteBuffer cbb = ByteBuffer.allocateDirect( colors.length * FLOAT_SIZE_BYTES );
        cbb.order( ByteOrder.nativeOrder() );
        colorBuffer = cbb.asFloatBuffer();
        colorBuffer.put( colors );
        colorBuffer.position( 0 );
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
        gl.glScalef( 0.01f, 0.01f, 0.01f );

        gl.glEnableClientState( GL10.GL_VERTEX_ARRAY );
        gl.glEnableClientState( GL10.GL_COLOR_ARRAY );
        
        gl.glVertexPointer( 2, GL10.GL_FLOAT, 0, vertexBuffer );
        gl.glColorPointer( 4, GL10.GL_FLOAT, 0, colorBuffer );
        
        gl.glDrawArrays( GL10.GL_TRIANGLE_STRIP, 0, 4 );
        
        gl.glDisableClientState( GL10.GL_VERTEX_ARRAY );
        gl.glDisableClientState( GL10.GL_COLOR_ARRAY );
        
        gl.glPopMatrix();
    }
}