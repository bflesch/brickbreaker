package br.usp.ime.mac5743;

public class World {

	private Paddle paddle;
	private Ball ball;
	private Brick block;
	
	public World(Paddle paddle, Ball ball, Brick block){
		this.ball = ball;
		this.paddle = paddle;
		this.block = block;
	}
	
	public Paddle getPaddle(){
		return paddle;
	}
	
	public void step() {
        paddle.updatePosition();
        //TODO remove reference
        ball.updatePosition();
        float [] normalForceDirection = {0f,0f};
        if (block.gotHit(ball,normalForceDirection)) 
        	ball.deflect(normalForceDirection);
        if (paddle.gotHit(ball,normalForceDirection)) 
        	ball.deflect(normalForceDirection);
        
        	
	} 
}
