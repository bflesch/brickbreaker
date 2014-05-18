package br.usp.ime.mac5743;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;

class Brick {

    public boolean isAlive = true;
	
	protected float posX = 0.0f;
	protected float posY = 0.0f;

	private FloatBuffer vertexBuffer;
	private FloatBuffer textureBuffer;

	protected float height = .05f;
	protected float width = .15f;
	
	private static int[] textures;

	private static final int FLOAT_SIZE_BYTES = Float.SIZE / 8;

	public Brick(float x, float y){
		posX = x; posY=y;
		buildGlBuffer();
	}
	protected void buildGlBuffer () {
		float[] vertices= {
				-width/2, -height/2,
				+width/2, -height/2,
				+width/2, +height/2,
				-width/2, +height/2
		};
		
		final float texture[] = {    		
	    		0.0f, 0.0f,
	    		1.0f, 0.0f,
	    		1.0f, 1.0f,
	    		0.0f, 1.0f
		};

		ByteBuffer bBuff=ByteBuffer.allocateDirect(vertices.length * FLOAT_SIZE_BYTES);    
		bBuff.order(ByteOrder.nativeOrder());
		vertexBuffer=bBuff.asFloatBuffer();
		vertexBuffer.put(vertices);
		vertexBuffer.position(0);
		
		ByteBuffer byteBuf = ByteBuffer.allocateDirect(texture.length * FLOAT_SIZE_BYTES);
		byteBuf.order(ByteOrder.nativeOrder());
		textureBuffer = byteBuf.asFloatBuffer();
		textureBuffer.put(texture);
		textureBuffer.position(0);
	}    
	
	public void collide () {
		isAlive = false;
	}

	private float distance(float[] a, float[] b) {
		float d = (a[0]-b[0])*(a[0]-b[0]) + (a[1]-b[1])*(a[1]-b[1]);
		return (float) Math.sqrt(d);
	}

	private void normalize(float[] vec) {
		float size = vec[0]*vec[0]+vec[1]*vec[1];
		size = (float) Math.sqrt(size);
		vec[0] /= size; vec[1] /= size;
	}

	private boolean gotHitOnCorner(Ball ball, float[] direction) {
		float radius = ball.radius ;
		float[] center =  {ball.posX,ball.posY};

		float[] corner1 = {posX + width/2,posY + height/2};
		float[] corner2 = {posX - width/2,posY + height/2};
		float[] corner3 = {posX + width/2,posY - height/2};
		float[] corner4 = {posX - width/2,posY - height/2};

		ArrayList<float[]> corners = new ArrayList<float[]> ();
		corners.add(corner1);corners.add(corner2);
		corners.add(corner3);corners.add(corner4);

		for (int i=0; i < 4; i++) {
			float[] corner = corners.get(i);
			if (distance(corner,center) < radius) {
				direction[0] = center[0]-corner[0];
				direction[1] = center[1]-corner[1];
				normalize(direction);
				return true;
			}
		}

		direction[0] = direction[1] = 0;
		return false;
	}

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

		if(gotHitOnCorner(ball,direction)) {
			this.collide();
			return true;
		}

		direction[0] = direction[1] = 0;
		return false;
	}

	public void draw( GL10 gl ) {
		gl.glMatrixMode( GL10.GL_MODELVIEW );
		gl.glPushMatrix();
		gl.glLoadIdentity();
		gl.glTranslatef( posX, posY, 0.0f );
		gl.glColor4f(1.0f,1.0f,1.0f, 1.0f);
		//gl.glScalef( 0.01f, 0.01f, 0.01f );
		
		gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[0]);

		gl.glEnableClientState( GL10.GL_VERTEX_ARRAY );
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);

		//gl.glColor4f(1.0f,0.0f,0.0f, 1.0f);

		gl.glVertexPointer( 2, GL10.GL_FLOAT, 0, vertexBuffer );
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBuffer);

		
		gl.glDrawArrays(GL10.GL_TRIANGLE_FAN, 0, 4);

		gl.glDisableClientState( GL10.GL_VERTEX_ARRAY );
		gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);

		//gl.glDisableClientState( GL10.GL_COLOR_ARRAY );

		gl.glPopMatrix();
	}
	
	public static void loadGLTexture(GL10 gl, Context context) {
		//Get the texture from the Android resource directory
		Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.blox);
		
		if(textures == null){
			textures = new int[2];
		}

		//Generate one texture pointer...
		gl.glGenTextures(1, textures, 0);
		//...and bind it to our array
		gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[0]);
		
		//Create Nearest Filtered Texture
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);

		//Different possible texture parameters, e.g. GL10.GL_CLAMP_TO_EDGE
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S, GL10.GL_REPEAT);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T, GL10.GL_REPEAT);
		
		//Use the Android GLUtils to specify a two-dimensional texture image from our bitmap
		GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
		
		//Clean up
		bitmap.recycle();
	}
}