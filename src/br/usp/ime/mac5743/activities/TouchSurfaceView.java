package br.usp.ime.mac5743.activities;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import br.usp.ime.mac5743.engine.Engine;
import br.usp.ime.mac5743.engine.World;
import br.usp.ime.mac5743.engine.WorldInterface;
import br.usp.ime.mac5743.engine.WorldOfTwo;
import br.usp.ime.mac5743.objects.Brick;
import br.usp.ime.mac5743.objects.Overlay;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.opengl.Matrix;
import android.view.MotionEvent;

public class TouchSurfaceView extends GLSurfaceView {

	private Renderer renderer;

	private int screenWidth;
	private int screenHeight;

	private float[] unprojectViewMatrix = new float[16];
	private float[] unprojectProjMatrix = new float[16];

	private static float ratio = 0.0f;

	MainActivity mainActivity;
	Engine engine;
	WorldInterface world;
	
	boolean isPaused = false;
	
	public void onPause() {
		isPaused = true;
		engine.pause();
		super.onPause();
	}
	
	private class Renderer implements GLSurfaceView.Renderer {
		
		private Overlay pauseOverlay;

		@Override
		public void onDrawFrame( GL10 gl ) {
			if(isPaused)
				gl.glClearColor(209f/256f,209f/256f,220f/256f,1f);
			else
				gl.glClearColor(1, 1, 1, 1);
			if (world.isFinished())
				mainActivity.finish();
			gl.glClear( GL10.GL_COLOR_BUFFER_BIT );
			world.draw(gl);			
			if(isPaused){
				if(!world.isFinished())
					pauseOverlay.draw(gl);
			}
			else {
				engine.runUpdates(world);
			}
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
			
			if (!world.isGenerated()){
				world.generate(ratio);
				engine = new Engine();
			}
			
			if (pauseOverlay == null){
				pauseOverlay = new Overlay(ratio);
			}
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
			gl.glShadeModel( GL10.GL_FLAT );
			gl.glDisable( GL10.GL_DEPTH_TEST );
			System.err.println("VERSION: " + gl.glGetString(GL10.GL_VERSION));
			Context applicationContex = getContext().getApplicationContext();
			Brick.loadGLTexture(gl, applicationContex);
			Overlay.loadGLTexture(gl, applicationContex);
		}
	}


	public TouchSurfaceView( Context context ) {
		super( context );
		renderer = new Renderer();
		setRenderer( renderer );
		this.mainActivity = (MainActivity) context;
	}
	
	private void handleTouch( MotionEvent e, int index) {
		
		isPaused = false;
		
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

		world.handleTouch(e ,resultWorldPos[0], resultWorldPos[1] );
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

	public WorldInterface createSinglePlayerWorld(Context activity) {
		world = new World();
		return world;
	}
	
	public WorldInterface createTwoPlayerWorld(Context activity) {
		world = new WorldOfTwo();
		return world;
	}
}