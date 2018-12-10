package main;

import processing.core.PShape;

public class LightStrip {
	
	PShape rects[];
	int ledCount = 12;
	
	public LightStrip(int ledCount) {
		this.ledCount = ledCount;
		rects = new PShape[ledCount];
	}
}
