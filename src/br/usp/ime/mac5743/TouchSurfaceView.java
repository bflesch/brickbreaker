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


	private class Renderer implements GLSurfaceView.Renderer {

		World world;

		public Renderer() {
			world = new World();
		}


		@Override
		public void onDrawFrame( GL10 gl ) {
			gl.glClear( GL10.GL_COLOR_BUFFER_BIT );
			world.step();
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
		}


		@Override
		public void onSurfaceCreated( GL10 gl, EGLConfig config ) { 
			gl.glDisable( GL10.GL_DITHER );
			gl.glHint( GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_FASTEST );
			gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
			gl.glEnable(GL10.GL_TEXTURE_2D);
			gl.glEnable(GL10.GL_BLEND);

			gl.glTexEnvf(GL10.GL_TEXTURE_ENV, GL10.GL_TEXTURE_ENV_MODE, GL10.GL_BLEND);
			gl.glClearColor( 1.0f, 1.0f, 1.0f, 1.0f );
			gl.glDisable( GL10.GL_CULL_FACE );
			gl.glShadeModel( GL10.GL_SMOOTH );
			gl.glDisable( GL10.GL_DEPTH_TEST );
			Brick.loadGLTexture(gl, getContext().getApplicationContext());
		}
	}


	public TouchSurfaceView( Context context ) {
		super( context );
		renderer = new Renderer();
		setRenderer( renderer );
		renderer.world.setHitBrickHandler((MainActivity) context);
	}


	@Override
	public boolean onTouchEvent( MotionEvent e ) {

		final float screenX = e.getX();
		final float screenY = screenHeight - e.getY();

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
		
		switch ( e.getAction() ) {
		case MotionEvent.ACTION_MOVE:
			renderer.world.updatePaddleSpeed( resultWorldPos[0], resultWorldPos[1] );
			break;
		case MotionEvent.ACTION_UP:
			renderer.world.startBallIfNotStarted(resultWorldPos[1]);
			break;
		}
		return true;
	}


	public static float getRatio() {
		return ratio;
	}
}