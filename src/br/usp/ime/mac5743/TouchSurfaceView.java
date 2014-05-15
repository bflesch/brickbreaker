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

        private Paddle paddle;
        private Ball ball;
        private BrickList bricks;
        private World world;


        public Renderer() {
            ball = new Ball();
            bricks = new BrickList();
            paddle = new Paddle(ball);
            world = new World(paddle,ball,bricks);
        }


        @Override
        public void onDrawFrame( GL10 gl ) {
            gl.glClear( GL10.GL_COLOR_BUFFER_BIT );
            world.step();
        	paddle.draw(gl);
            ball.draw(gl);
            bricks.draw(gl);
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

            gl.glClearColor( 1.0f, 1.0f, 1.0f, 1.0f );
            gl.glDisable( GL10.GL_CULL_FACE );
            gl.glShadeModel( GL10.GL_SMOOTH );
            gl.glDisable( GL10.GL_DEPTH_TEST );
        }


        public void updatePaddleSpeed( final float x, final float y ) {
            queueEvent( new Runnable() {
                @Override
                public void run() {
                    paddle.setDestination( x );
                    paddle.updatePosition();
                }
            } );
        }


		public void startBallIfNotStarted() {
			if (ball.stopped())
			     ball.launch();
		}
    }


    public TouchSurfaceView( Context context ) {
        super( context );
        renderer = new Renderer();
        setRenderer( renderer );
    }


    @Override
    public boolean onTouchEvent( MotionEvent e ) {
        switch ( e.getAction() ) {
            case MotionEvent.ACTION_MOVE:
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

                renderer.updatePaddleSpeed( resultWorldPos[0], resultWorldPos[1] );
                break;
            case MotionEvent.ACTION_UP:   
                renderer.startBallIfNotStarted();
                break;
                
        }

        return true;
    }


	public static float getRatio() {
		return ratio;
	}
}