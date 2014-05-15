package br.usp.ime.mac5743;

public class World {

	private Paddle paddle;
	private Ball ball;
	private BrickList bricks;
	
	public World(Paddle paddle, Ball ball, BrickList bricks){
		this.ball = ball;
		this.paddle = paddle;
		this.bricks = bricks;
	}
	
	public Paddle getPaddle(){
		return paddle;
	}
	
	public void step() {
        paddle.updatePosition();
        //TODO remove reference
        ball.updatePosition();
        float [] normalForceDirection = {0f,0f};
        if (bricks.gotHit(ball,normalForceDirection)) 
        	ball.deflect(normalForceDirection);
        if (paddle.gotHit(ball,normalForceDirection)) 
        	ball.deflect(normalForceDirection);
        
        	
	} 
}
