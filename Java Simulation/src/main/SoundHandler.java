package main;

import ddf.minim.AudioInput;
import ddf.minim.Minim;
import ddf.minim.analysis.FFT;

public class SoundHandler {
	
	private Minim minim;
	private FFT fft;
	private AudioInput lineIn;
	
	public SoundHandler() {
		minim = new Minim(this);
		lineIn = minim.getLineIn();
		lineIn.mute();
		fft = new FFT(lineIn.bufferSize(), lineIn.sampleRate());
	}

}
