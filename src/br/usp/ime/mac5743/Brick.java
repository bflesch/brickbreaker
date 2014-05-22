package br.usp.ime.mac5743;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

class Brick {

    public boolean isAlive = true;
	public boolean makesSound = true;
    
	protected float posX = 0.0f;
	protected float posY = 0.0f;

	private FloatBuffer vertexBuffer;

	protected float height = .05f;
	protected float width = .15f;
	protected float[] color = {1.0f,0.0f,0.0f, 1.0f};
	
	private float growth_rate = 1.01f; private float maxHeight = 4f;


	private static final int FLOAT_SIZE_BYTES = Float.SIZE / 8;
	protected static final int WITH_TOP = 143223411;
	protected static final int WITH_BOTTOM = 342382131;
	protected static final int WITH_UNUSED = 312120762;

	public Brick(float x, float y){
		posX = x; posY=y;
		buildGlBuffer();
	}
	
	public Brick(float x, float y, float h, float w){
		posX=x; posY=y; height=h; width=w;
		buildGlBuffer();
	}
	
	protected void buildGlBuffer () {
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

		//collide with the left
		if (x_max > x + r && x + r > x_min)
			if ( y_min < y  && y < y_max) {
				withSide[0]=WITH_UNUSED;
				normal[0] = -1f; normal[1] = 0f;
				gotHit[0] = true;
				return;
			}

		//collide with the right
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
		gl.glColor4f(1.0f,0.0f,0.0f, 1.0f);
		//gl.glScalef( 0.01f, 0.01f, 0.01f );

		gl.glEnableClientState( GL10.GL_VERTEX_ARRAY );
		gl.glColor4f(color[0],color[1],color[2],color[3]);
				

		gl.glVertexPointer( 2, GL10.GL_FLOAT, 0, vertexBuffer );

		gl.glDrawArrays(GL10.GL_TRIANGLE_FAN, 0, 6);

		gl.glDisableClientState( GL10.GL_VERTEX_ARRAY );

		gl.glPopMatrix();
	}
}