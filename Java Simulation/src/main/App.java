package main;

import processing.core.PApplet;

public class App extends PApplet {
	
	// Regular variables
	private SoundHandler soundHandler;
	private LightStrip lightStrips[];
	
	// fixed numbers
	private final int LIGHTSTRIP_COUNT = 8;
	private final int LED_PER_STRIP_COUNT = 12;
	private final int LED_SPACING = 10, LED_SIZE = 10;
	
	// variables
	private float previousBandValue = 0;
	
	// Initialize everything here
	public App() {
		soundHandler = new SoundHandler(this);
		lightStrips = new LightStrip[LIGHTSTRIP_COUNT];
	}
	
	// Adjusts settings for processing
	public void settings() {
		size(1152, 648);
	}
	
	// Processing's way of initializing
	public void setup() {
		surface.setTitle("Light Simulation for Arduino");
		surface.setFrameRate(22);
		populateShapes();
	}
	
	// Processing's draw loop which draws to the frame rate
	public void draw() {
		soundHandler.update();
		update();
		background(0);
		
		stroke(255);
		noFill();
		drawFrequencyBands();
		drawSpecText();
	}
	
	// Draws text with important information to just have there
	public void drawSpecText() {
		text("Loudest Band: " + soundHandler.getStrongestBand(), 10, (height - 3));
		text("Loudest ", 120, (height - 3));
	}
	
	public void update() {
		
	}
	
	// Draws a line of along the bottom for all frequencies
	public void drawFrequencyBands() {
		for (int i = 0; i < width; i++) {
			float adjustedHeight = height - 15;
			float bandMultiplier = 5;
			float bandValue = soundHandler.getBand((int)(map(i, 0, width, 0, soundHandler.getSpecSize())));
			line(i, adjustedHeight - (previousBandValue * bandMultiplier),
					i+1, adjustedHeight - (bandValue * bandMultiplier));
			previousBandValue = bandValue;
		}
	}
	
	// Populates squares to simulate LED's
	private void populateShapes() {
		println("Populating " + LIGHTSTRIP_COUNT + " strips of " + LED_PER_STRIP_COUNT + " lights per strip.");
		for (int y = 0; y < LIGHTSTRIP_COUNT; y++) {
			lightStrips[y] = new LightStrip(LED_PER_STRIP_COUNT);
			for (int x = 0; x < LED_PER_STRIP_COUNT; x++) {
				lightStrips[y].rects[x] = createShape(RECT, (x * LED_SPACING) + LED_SPACING,
								(y * LED_SPACING) + LED_SPACING, (x * LED_SIZE), (y * LED_SIZE));
			}
		}
	}
	
	// Controls exit of processing
	public void exit() {
		System.out.println("\n... Closing");
		soundHandler.stop();
		super.exit();
	}
}
