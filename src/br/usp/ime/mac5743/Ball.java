package br.usp.ime.mac5743;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

class Ball {

	private int points = 70;
	public float radius=.05f;
	//TODO unificar a cor cinza em um lugar
	public float[] color = {201f/256f, 192f/256f, 187f/256f,1};

	//Note: não faça com que twoPlayerBrick.maxSpeed 
	//                        + ball.maxSpeed
	//seja maior que a menor dimensão do bloquinho.
	//caso contrário, poderá haver teleportes
	// (i.e., o bloquinho e a bola deviam bater, mas
	// passam reto um pelo outro)
	private float speedSize = 2f*1.0f/150.0f;

	public float posX;
	public float posY;

	private FloatBuffer vertexBuffer;

	public float speedX = 0.0f;
	public float speedY = 0.0f;

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
	
	public Ball(float x,float y) {
		this();
		posX = x; posY = y;
	}

	public void setDirection(float [] direction) {
		speedX = direction[0]*speedSize;
		speedY = direction[1]*speedSize;
	}

	public void launch(){
		speedX = speedSize;
		float [] direction = {1,1};
		Brick.normalize(direction);
		setDirection(direction);
	}

	public void comeWithMe(float posXNew) {
		posX = posXNew;
	}

	public void setPosition(float posXNew, float posYNew) {
		posY = posYNew;
		posX = posXNew;
	}

	public boolean stopped() {
		return ( -0.00001f < speedX && speedX < 0.00001f && // 
				-0.00001f < speedY && speedY < 0.00001f);
	}



	public void updatePosition(){
		posX = posX+speedX;
		posY = posY+speedY;
	}

	public void draw( GL10 gl ) {
        gl.glMatrixMode( GL10.GL_MODELVIEW );
        gl.glPushMatrix();
        gl.glLoadIdentity();
        gl.glTranslatef( posX, posY, 0.0f );

        gl.glEnableClientState( GL10.GL_VERTEX_ARRAY );
        gl.glColor4f(color[0],color[1],color[2],color[3]);
        
        gl.glVertexPointer( 2, GL10.GL_FLOAT, 0, vertexBuffer );
        gl.glDisable(GL10.GL_BLEND);

        gl.glDrawArrays(GL10.GL_TRIANGLE_FAN, 0, points+2);
        
        gl.glDisableClientState( GL10.GL_VERTEX_ARRAY );
        gl.glEnable(GL10.GL_BLEND);
        
        gl.glPopMatrix();
    }
}