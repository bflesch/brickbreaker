package br.usp.ime.mac5743;

public class World {

	private Paddle paddle;
	private Ball ball;
	
	public World(Paddle paddle, Ball ball){
		this.paddle = paddle;
		this.ball = ball;
	}
	
	public Paddle getPaddle(){
		return paddle;
	}
}
