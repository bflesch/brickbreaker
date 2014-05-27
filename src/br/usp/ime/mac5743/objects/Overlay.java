package br.usp.ime.mac5743.objects;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import javax.microedition.khronos.opengles.GL10;

import br.usp.ime.mac5743.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;

public class Overlay {
  
	protected float posX = 0.0f;
	protected float posY = 0.0f;

	private FloatBuffer vertexBuffer;
	protected static FloatBuffer textureBuffer;
	protected static FloatBuffer colorBuffer;
	
	private static int[] textures;

	protected float height;
	protected float width;

	private static final int FLOAT_SIZE_BYTES = Float.SIZE / 8;
	
	protected static final int WITH_TOP = 143223411;
	protected static final int WITH_BOTTOM = 342382131;
	protected static final int WITH_UNUSED = 312120762;

	public Overlay(float ratio){
		posX = 0f;
		posY = 0f;
		width = (ratio < 1.0f) ? (ratio*2.0f) : 2.0f;
		height = (ratio < 1.0f) ? (ratio*2.0f) : 2.0f;
		buildGlBuffer();
	}
	
	protected void buildGlBuffer() {	
		if(textureBuffer == null || colorBuffer == null ){		
			final float texture[] = {    		
		    		0.0f, 1.0f,
		    		1.0f, 1.0f,
		    		1.0f, 0.0f,
		    		0.0f, 0.0f
			};
			
			final float bg_color[] = {    		
		    		1.0f, 1.0f,
		    		1.0f, 1.0f
			};
			
			ByteBuffer byteBuf = ByteBuffer.allocateDirect(texture.length * FLOAT_SIZE_BYTES);
			byteBuf.order(ByteOrder.nativeOrder());
			textureBuffer = byteBuf.asFloatBuffer();
			textureBuffer.put(texture);
			textureBuffer.position(0);
			
			byteBuf = ByteBuffer.allocateDirect(bg_color.length * FLOAT_SIZE_BYTES);
			byteBuf.order(ByteOrder.nativeOrder());
			colorBuffer = byteBuf.asFloatBuffer();
			colorBuffer.put(bg_color);
			colorBuffer.position(0);	
		}
		float[] vertices= {
				-width/2, -height/2,
				+width/2, -height/2,
				+width/2, +height/2,
				-width/2, +height/2
		};

		ByteBuffer bBuff=ByteBuffer.allocateDirect(vertices.length * FLOAT_SIZE_BYTES);    
		bBuff.order(ByteOrder.nativeOrder());
		vertexBuffer=bBuff.asFloatBuffer();
		vertexBuffer.put(vertices);
		vertexBuffer.position(0);
	}    
	
	public void draw( GL10 gl ) {
		gl.glMatrixMode( GL10.GL_MODELVIEW );
		gl.glPushMatrix();
		gl.glLoadIdentity();
		gl.glTranslatef( posX, posY, 0.0f );
		
		gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[0]);

		gl.glEnableClientState( GL10.GL_VERTEX_ARRAY );
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);

		gl.glVertexPointer( 2, GL10.GL_FLOAT, 0, vertexBuffer );
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBuffer);
		gl.glTexEnvfv(GL10.GL_TEXTURE_ENV, GL10.GL_TEXTURE_ENV_COLOR, colorBuffer);

		gl.glDrawArrays(GL10.GL_TRIANGLE_FAN, 0, 4);

		gl.glDisableClientState( GL10.GL_VERTEX_ARRAY );

		gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);

		gl.glPopMatrix();
	}
	
	public static void loadGLTexture(GL10 gl, Context context) {
		
		if(textures == null){
			textures = new int[1];
		}
		
		Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.raw.pause_overlay);
		
		gl.glGenTextures(1, textures, 0);
		gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[0]);
		
		//Create Nearest Filtered Texture
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);

		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S, GL10.GL_CLAMP_TO_EDGE);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T, GL10.GL_CLAMP_TO_EDGE);
		
		GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, GL10.GL_RGBA, bitmap, 0);
		
		bitmap.recycle();
	}

}