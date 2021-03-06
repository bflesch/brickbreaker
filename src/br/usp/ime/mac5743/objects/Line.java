package br.usp.ime.mac5743.objects;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import javax.microedition.khronos.opengles.GL10;

public class Line {
	
	    private FloatBuffer vertexBuffer;
	    private float height;
	    private float[] color = {0,0,0,0};

	    public Line (float height, float[] color, float ratio) {
	    	this.height = height;
	    	this.color = color;
	    	buildGlBuffer(ratio);
	    }
	    
    	protected void buildGlBuffer (float ratio) {
		float[] vertices= {
				-ratio, height,
				ratio, height,
				//yep, it gets drawn out of the screen
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
		gl.glDisable(GL10.GL_BLEND);
		
		gl.glEnableClientState( GL10.GL_VERTEX_ARRAY );
		gl.glVertexPointer( 2, GL10.GL_FLOAT, 0, vertexBuffer );

		gl.glDrawArrays(GL10.GL_LINES, 0, 2);

		gl.glDisableClientState( GL10.GL_VERTEX_ARRAY );
		gl.glEnable(GL10.GL_BLEND);

		gl.glPopMatrix();
	}
}