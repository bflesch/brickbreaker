package br.usp.ime.mac5743.engine;


public class Engine {
	
	long timeStamp = 0;
	long previousTime = 0;
	final static int STEPS_PER_SECOND = 60;
	long timeForStep = 1000/STEPS_PER_SECOND;
	
	public void pause(){
		timeStamp = 0;
	}
	
	public void runUpdates(World world){
		previousTime = timeStamp;
		timeStamp = System.currentTimeMillis();
		if (previousTime != 0) {
			int stepsToProcess = (int) ((timeStamp - previousTime)/timeForStep);
			long missingMiliseconds = (timeStamp - previousTime)%timeForStep;
			timeStamp -= missingMiliseconds;
			while (stepsToProcess != 0) {
				world.step();
				stepsToProcess--;
			}
		}
	}

}
