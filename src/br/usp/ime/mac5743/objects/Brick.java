package br.usp.ime.mac5743.objects;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

import br.usp.ime.mac5743.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;

public class Brick {

    public boolean isAlive = true;
    public boolean isKillable = true;
	public boolean makesSound = true;
    
	protected float posX = 0.0f;
	protected float posY = 0.0f;

	private FloatBuffer vertexBuffer;
	protected static FloatBuffer textureBuffer;
	protected static FloatBuffer colorBuffer;
	
	private static int[] textures;


	//Note: não faça com que twoPlayerBrick.maxSpeed 
	//                        + ball.maxSpeed
	//seja maior que a menor dimensão do bloquinho.
	//caso contrário, poderá haver teleportes
	// (i.e., o bloquinho e a bola deviam bater, mas
	// passam reto um pelo outro)
	public static float defaultHeight = .05f;
	protected float height = defaultHeight;
	public static float widthBeforeRatio = .20f;
	public static float ratio;
	protected float width;
	
	private float[] color = {1.0f,0.0f,0.0f, 1.0f};
	
	private float growth_rate = 1.01f;
	private float maxHeight = 4f;

	private static final int FLOAT_SIZE_BYTES = Float.SIZE / 8;
	
	protected static final int WITH_TOP = 143223411;
	protected static final int WITH_BOTTOM = 342382131;
	protected static final int WITH_UNUSED = 312120762;

	public Brick(float x, float y){
		posX = x; posY=y;
		width = widthBeforeRatio*ratio;
		buildGlBuffer();
	}
	
	public Brick(float x, float y, float h, float w){
		posX=x; posY=y; height=h; width=w;
		width = widthBeforeRatio*ratio;
		buildGlBuffer();
	}
	
	protected void buildGlBuffer () {	
		if(textureBuffer == null || colorBuffer == null ){		
			final float texture[] = {    		
		    		0.0f, 0.0f,
		    		1.0f, 0.0f,
		    		1.0f, 1.0f,
		    		0.0f, 1.0f
			};
			
			final float bg_color[] = {    		
		    		1.0f, 1.0f,
		    		1.0f, 0.05f
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
	
	public void step() {
	}
	//TODO return speed ?
	//Sets speed of the ball according to simple reflection
	public static void reflect(Ball ball, float[] normalForceDirection) {
		
		float normalX = normalForceDirection[0];
		float normalY = normalForceDirection[1];
		float dotproduct = ball.speedX*normalX +
				            ball.speedY*normalY;
		if (dotproduct < 0) {
            ball.speedX -= 2*dotproduct*normalX;
            ball.speedY -= 2*dotproduct*normalY;
		}
	}
	
	public void collide (int side, Ball ball) {
		isAlive = false;
	}

	private float distance(float[] a, float[] b) {
		float d = (a[0]-b[0])*(a[0]-b[0]) + (a[1]-b[1])*(a[1]-b[1]);
		return (float) Math.sqrt(d);
	}

	static public void normalize(float[] vec) {
		float size = vec[0]*vec[0]+vec[1]*vec[1];
		size = (float) Math.sqrt(size);
		vec[0] /= size; vec[1] /= size;
	}
	
	public void grow() {
		if (width < 4)
		   this.width *= growth_rate;
		if (height < maxHeight)
		   this.height *= growth_rate;
		buildGlBuffer();
	}
	
	public void goHalfway () {
		maxHeight = 2*Math.abs(posY);
	}

	private boolean gotHitOnCorner(Ball ball, int[] withSide,float[] normal) {
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
				normal[0] = center[0]-corner[0];
				normal[1] = center[1]-corner[1];
				normalize(normal);
				
				if(i < 2) {
					withSide[0] = WITH_TOP;
				}
				else {
					withSide[0] = WITH_BOTTOM;
				}
				return true;
			}
		}
		return false;
	}

	public boolean gotHit(Ball ball) {
		float[] normal = {0,0};
		boolean[] gotHit = {false};
		int [] withSide = {0};
		checkHit(ball, normal,gotHit, withSide);
		if (gotHit[0])
		   collide(withSide[0],ball);
		return gotHit[0];
	}

	public float[] normal(Ball ball) {
		float[] normal = {0,0};
		boolean[] gotHit = {false};
		int [] withSide = {0};
		checkHit(ball, normal,gotHit, withSide);
		return normal;
	}
	
	public void changeSpeed(Ball ball) {
		float[] normal = {0,0};
		boolean[] gotHit = {false};
		int [] withSide = {0};
		checkHit(ball, normal,gotHit, withSide);
		reflect(ball, normal);
	}
	
	public void checkHit(Ball ball, float[] normal, boolean[] gotHit, int[] withSide) {
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
				withSide[0]=WITH_BOTTOM;
				normal[0] = 0f; normal[1] = -1f;
				gotHit[0] = true;
				return;
			}

		//collide with the top
		if (y_max > y - r && y - r > y_min)
			if ( x_min < x  && x < x_max) {
				withSide[0]=WITH_TOP;
				normal[0] = 0f; normal[1] = 1f;
				gotHit[0] = true;
				return;
			}

		//collide with the left side
		if (x_max > x + r && x + r > x_min)
			if ( y_min < y  && y < y_max) {
				withSide[0]=WITH_UNUSED;
				normal[0] = -1f; normal[1] = 0f;
				gotHit[0] = true;
				return;
			}

		//collide with the right side
		if (x_max > x - r && x - r > x_min)
			if ( y_min < y  && y < y_max) {
				withSide[0]=WITH_UNUSED;
				normal[0] = 1f; normal[1] = 0f;
				gotHit[0] = true;
				return;
			}

		if(gotHitOnCorner(ball,withSide,normal)) {
			//WITH SIDE IS SET
			//NORMAL IS SET
			gotHit[0] = true;
			return;
		}
		gotHit[0] = false;
		return;
	}

	public void draw( GL10 gl ) {
		gl.glMatrixMode( GL10.GL_MODELVIEW );
		gl.glPushMatrix();
		gl.glLoadIdentity();
		gl.glTranslatef( posX, posY, 0.0f );
		//gl.glColor4f(1.0f,0.0f,0.0f, 0.9f);
		gl.glColor4f(getColor()[0],getColor()[1],getColor()[2],getColor()[3]);
		
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
			textures = new int[2];
		}
		
		Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.raw.blox2);
		
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

	public float[] getColor() {
		return color;
	}

	public void setColor(float[] color) {
		this.color = color;
	}
}