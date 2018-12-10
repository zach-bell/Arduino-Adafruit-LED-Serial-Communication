package main;

import processing.core.PApplet;

public class App extends PApplet {
	
	// Regular variables
	private SoundHandler soundHandler;
	private LightStrip lightStrips[];
	
	// fixed numbers
	private final int LIGHTSTRIP_COUNT = 8;
	private final int LED_PER_STRIP_COUNT = 12;
	private final int LED_SIZE = 30;
	private final int LED_SPREAD = 50;
	private final float ALARM_TOLERANCE = 150;
	private final float ALARM_THRESHOLD = 175;
	
	// variables
	private float previousBandValue = 0;
	private float loudestBandAmplitude = 0;
	private float specToleranceDifference = 0;
	private int specStartIndex = 0;
	private int specEndIndex = 0;
	private int loudestBandIndex = 0;
	private boolean drawLEDs = true;
	private boolean detectedFire = false;
	
	// Initialize everything here
	public App() {
		soundHandler = new SoundHandler(this);
		lightStrips = new LightStrip[LIGHTSTRIP_COUNT];
		specStartIndex = (int) (soundHandler.beforeSpec * soundHandler.getSpecSize());
		specEndIndex = ((int) (soundHandler.desiredSpec * soundHandler.getSpecSize())) + specStartIndex;
	}
	
	// Adjusts settings for processing
	public void settings() {
		size(1152, 648);
	}
	
	// Processing's way of initializing
	public void setup() {
		surface.setTitle("Light Simulation for Arduino");
		surface.setFrameRate(22);
		println("Frame Rate limit set to 22");
		populateShapes();
	}
	
	// Processing's draw loop which draws to the frame rate
	public void draw() {
		soundHandler.update();
		update();
		background(0);
		
		// Draws a button for the user to change views
		noStroke();
		fill(200);
		rect(width - 75, 80, 15, 15);
		
		if (drawLEDs) {
			drawLEDBoxes();
		} else {
			drawFrequencyBands();
		}
		drawSpecText();
	}
	
	// Draws text with important information to just have there
	public void drawSpecText() {
		fill(200);
		stroke(255);
		text("FrameRate: " + (int) frameRate, width - 100, 15);
		text("Change View", width - 100, 75);
		text("Loudest Band: " + loudestBandIndex, 10, (height - 3));
		text("Loudest " + (int) loudestBandAmplitude, 130, (height - 3));
		text("Tolerance DIfference: " + (int) specToleranceDifference, 220, (height - 3));
		text("Created by: Zachary Vanscoit", (width - 170), (height - 3));
	}
	
	// Will perform non-draw functions as it would be better to separate these things
	public void update() {
		// This only updates the text every 10 frames of the set 22 frame rate per second
		if ((millis() % 10) == 0) {
			loudestBandIndex = soundHandler.getStrongestBand();
			loudestBandAmplitude = soundHandler.getBand(loudestBandIndex);
			checkFireAlarmActivation();
		}
	}
	
	private void checkFireAlarmActivation() {
		float amplitudeOfSpec = 0;
		
		for (int i = specStartIndex; i <= specEndIndex; i++) {
			amplitudeOfSpec += soundHandler.getBand(i);
		}
		
		specToleranceDifference = (amplitudeOfSpec + ALARM_TOLERANCE) - 
				(soundHandler.getBandsComb() > 1 ? ALARM_TOLERANCE : soundHandler.getBandsComb());
		if (specToleranceDifference > 0)
			if (specToleranceDifference > ALARM_THRESHOLD) {
				if ((millis() % 10) == 0)
					println("Fire Detected!");
				detectedFire = true;
			} else {
				detectedFire = false;
			}
	}
	
	// checks if the mouse is over some area specified
	private boolean checkMouseOver(int x, int y, int width, int height) {
		if (mouseX >= x && mouseX <= x+width &&
				mouseY >= y && mouseY <= y+height) {
			return true;
		}
		return false;
	}
	
	// Draws a line of along the bottom for all frequencies
	private void drawFrequencyBands() {
		noFill();
		
		if (detectedFire)
			background((int) map(soundHandler.getBandsComb(), 0, 500, 0, 255), 0, 0);
		
		float adjustedHeight = height - 15;
		float bandMultiplier = 5;
		float beforeSpec = soundHandler.beforeSpec * width;
		float desiredSpec = soundHandler.desiredSpec * width;
		
		for (int i = 0; i < width; i++) {
			float bandValue = soundHandler.getBand((int)(map(i, 0, width, 0, soundHandler.getSpecSize() * 0.9f)));
			if (i > beforeSpec & i < beforeSpec + desiredSpec)
				stroke(255, 0, 0);
			else
				stroke(255);
			line(i, adjustedHeight - (previousBandValue * bandMultiplier),
					i+1, adjustedHeight - (bandValue * bandMultiplier));
			previousBandValue = bandValue;
		}
		drawActivationWindow();
	}
	
	private void drawActivationWindow() {
		fill(255, 0, 0);
		stroke(255,0,0);
		text("Activation Range", width * soundHandler.beforeSpec, height / 2 + 15);
		noFill();
		rect(width * soundHandler.beforeSpec - 2, height / 2, width * soundHandler.desiredSpec, height / 2);
	}
	
	// Draws the boxes on screen with set colors
	private void drawLEDBoxes() {
		noStroke();
		for (int y = 0; y < lightStrips.length; y++) {
			for (int x = 0; x < lightStrips[y].rects.length; x++) {
				if (detectedFire)
					lightStrips[y].rects[x].setFill(color(255,0,0));
				else
					lightStrips[y].rects[x].setFill(
				(int) map(soundHandler.getBandsComb() > 2200 ? 2200 : soundHandler.getBandsComb(), 0, 2200, 0, 255));
				shape(lightStrips[y].rects[x]);
			}
		}
	}
	
	// Populates squares to simulate LED's
	private void populateShapes() {
		println("Populating " + LIGHTSTRIP_COUNT + " strips of " + LED_PER_STRIP_COUNT + " lights per strip.");
		for (int y = 0; y < LIGHTSTRIP_COUNT; y++) {
			lightStrips[y] = new LightStrip(LED_PER_STRIP_COUNT);
			for (int x = 0; x < LED_PER_STRIP_COUNT; x++) {
				lightStrips[y].rects[x] = createShape(RECT,
						(x * LED_SPREAD) + (LED_SIZE * x), (y * LED_SPREAD) + (LED_SIZE * y), LED_SIZE, LED_SIZE);
			}
		}
	}
	
	private void toggleView() {
		println("View Toggled");
		if (drawLEDs) {
			drawLEDs = false;
		} else {
			drawLEDs = true;
		}
	}
	
	// Processing's mouseClicked function that calls whenever a mouse is clicked
	public void mousePressed() {
		if (checkMouseOver(width - 75, 80, 15, 15)) {
			toggleView();
		}
	}
	
	// Controls exit of processing
	public void exit() {
		System.out.println("\n... Closing");
		soundHandler.stop();
		super.exit();
	}
}
