package main;

import processing.core.PApplet;

public class LightSimulationLauncher {
	
	/*	This class is basically just to separate the main
	 * 	from the rest of the classes
	 */
	
	public static void main(String[] args) {
		System.out.println("Launching applet...\n");
		PApplet.main("main.App");	// Needed to launch processing correctly
	}
}
