package main;

import ddf.minim.AudioInput;
import ddf.minim.Minim;
import ddf.minim.analysis.FFT;

public class SoundHandler {
	
	private Minim minim;
	private FFT fft;
	private AudioInput lineIn;
	private float desiredSpec = 0.02f;
	
	public SoundHandler() {
		minim = new Minim(this);
		lineIn = minim.getLineIn();
		lineIn.mute();
		fft = new FFT(lineIn.bufferSize(), lineIn.sampleRate());
	}
	
	public float getFFT() {
		fft.forward(lineIn.mix);
		float comb = 0;
		for (int i=0; i < fft.specSize() * desiredSpec; i++) {
			comb += fft.getBand(i);
		}
		return comb;
	}
	
	
}
