package br.usp.ime.mac5743;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

class Line {
	
	    private FloatBuffer vertexBuffer;
	    private float height;
	    private float[] color = {0,0,0,0};

	    public Line (float height, float[] color) {
	    	this.height = height;
	    	this.color = color;
	    	buildGlBuffer();
	    }
	    
    	protected void buildGlBuffer () {
		float[] vertices= {
				-1.0f, height,
				1.0f, height,
				//yep, it gets drawn out of the screen
				//TODO may it get too short ?
		};

		ByteBuffer bBuff=ByteBuffer.allocateDirect(vertices.length*4);    
		bBuff.order(ByteOrder.nativeOrder());
		vertexBuffer=bBuff.asFloatBuffer();
		vertexBuffer.put(vertices);
		vertexBuffer.position(0);
	}    
	
		public void draw( GL10 gl ) {
		gl.glMatrixMode( GL10.GL_MODELVIEW );
		gl.glPushMatrix();
		gl.glLoadIdentity();
		//gl.glTranslatef( posX, posY, 0.0f );
		gl.glColor4f(color[0],color[1],color[2],color[3]);
		
		gl.glEnableClientState( GL10.GL_VERTEX_ARRAY );
		gl.glVertexPointer( 2, GL10.GL_FLOAT, 0, vertexBuffer );

		gl.glDrawArrays(GL10.GL_LINES, 0, 2);

		gl.glDisableClientState( GL10.GL_VERTEX_ARRAY );

		gl.glPopMatrix();
	}
}