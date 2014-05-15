package br.usp.ime.mac5743;

public class World {

	private Paddle paddle;
	private Ball ball;
	private Block block;
	
	public World(Paddle paddle, Ball ball, Block block){
		this.paddle = paddle;
		this.ball = ball;
		this.block = block;
	}
	
	public Paddle getPaddle(){
		return paddle;
	}
	
	public void step() {
        paddle.updatePosition();
        //TODO remove reference
        ball.updatePosition(this);
        float [] normalForceDirection = {0f,0f};
        if (block.gotHit(ball,normalForceDirection)) 
        	ball.deflect(normalForceDirection);
        
        	
	} 
}
