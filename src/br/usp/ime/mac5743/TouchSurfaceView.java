package br.usp.ime.mac5743;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.opengl.Matrix;
import android.view.MotionEvent;

class TouchSurfaceView extends GLSurfaceView {

	private Renderer renderer;

	private int screenWidth;
	private int screenHeight;

	private float[] unprojectViewMatrix = new float[16];
	private float[] unprojectProjMatrix = new float[16];

	private static float ratio = 0.0f;

	MainActivity context;
	
	long timeStamp = 0; long previousTime = 0;
	int stepsPerSecond = 60; long timeForStep = 1000/stepsPerSecond;

	//Se o jogo não estava rodando, resete o
	//timeStamp (caso contrário, ao voltar
	//rodariamos toda a fisica que "estamos devendo")
	public void onResume () {
		timeStamp = 0;
		super.onResume();
	}
	
	private class Renderer implements GLSurfaceView.Renderer {

		//World world; 
		WorldOfTwo world;
		

		private void createWorld (float ratio){
			//world = new World(ratio);
			world = new WorldOfTwo(ratio);
			world.setHitBrickHandler(context);
		}

		@Override
		public void onDrawFrame( GL10 gl ) {
			gl.glClear( GL10.GL_COLOR_BUFFER_BIT );

			previousTime = timeStamp;
			timeStamp = System.currentTimeMillis();
			if (previousTime != 0) {
				int steps = (int) ((timeStamp - previousTime)/timeForStep);
				int missing = (int) ((timeStamp - previousTime)%timeForStep);
				timeStamp -= missing;
				while (steps != 0) {
					world.step();
					steps--;
				}
			}
			world.draw(gl);
		}

		@Override
		public void onSurfaceChanged( GL10 gl, int width, int height ) {

			gl.glViewport( 0, 0, width, height );
			screenWidth = width;
			screenHeight = height;

			ratio = (float) width / height;
			gl.glMatrixMode( GL10.GL_PROJECTION );
			gl.glLoadIdentity();
			gl.glOrthof( -ratio, ratio, -1.0f, 1.0f, -1.0f, 1.0f );
			//gl.glOrthof(LEFT, RIGHT, BOTTOM, TOP, NEAR, FAR);

			Matrix.orthoM( unprojectProjMatrix, 0, -ratio, ratio, -1.0f, 1.0f, -1.0f, 1.0f );
			Matrix.setIdentityM( unprojectViewMatrix, 0 );
			
			if (world == null)
				createWorld(ratio);

		}


		@Override
		public void onSurfaceCreated( GL10 gl, EGLConfig config ) {
			gl.glDisable( GL10.GL_DITHER );
			gl.glHint( GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_FASTEST );

			gl.glClearColor( 1.0f, 1.0f, 1.0f, 1.0f );
			gl.glDisable( GL10.GL_CULL_FACE );
			gl.glShadeModel( GL10.GL_SMOOTH );
			gl.glDisable( GL10.GL_DEPTH_TEST );
		}
	}


	public TouchSurfaceView( Context context ) {
		super( context );
		renderer = new Renderer();
		setRenderer( renderer );
		this.context = (MainActivity) context;
	}


	private void handleTouch( MotionEvent e, int index) {
		final float screenX = e.getX(index);
		final float screenY = screenHeight - e.getY(index);

		final int[] viewport = {
				0, 0, screenWidth, screenHeight
		};

		float[] resultWorldPos = {
				0.0f, 0.0f, 0.0f, 0.0f
		};

		GLU.gluUnProject( screenX, screenY, 0, unprojectViewMatrix, 0, unprojectProjMatrix, 0, viewport, 0, resultWorldPos, 0 );
		resultWorldPos[0] /= resultWorldPos[3];
		resultWorldPos[1] /= resultWorldPos[3];
		resultWorldPos[2] /= resultWorldPos[3];
		resultWorldPos[3] = 1.0f;

		renderer.world.handleTouch(e ,resultWorldPos[0], resultWorldPos[1] );
	}



	@Override
	public boolean onTouchEvent( MotionEvent e ) {

		for(int i=0; i< e.getPointerCount(); i++)
			handleTouch(e,i);
		return true;
	}


	public static float getRatio() {
		return ratio;
	}
}