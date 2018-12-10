package main;

import ddf.minim.AudioInput;
import ddf.minim.Minim;
import ddf.minim.analysis.FFT;
import processing.core.PApplet;

public class SoundHandler {
	
	private Minim minim;
	
	private FFT fft;
	private AudioInput lineIn;
	
	/* Bit rate 44,800 bits / 2 channels = 22,400Hz per channel
	 * Highest frequency 22,400Hz
	 * Fire alarm activation range 2,500Hz to 3,500Hz
	 * Size of range is 1,000Hz
	 * Percentage size desired is 1,000Hz / 22,400Hz = desiredSpec
	 * Percentage before desired is 2,500Hz / 22,400Hz = beforeSpec
	 * Desired isn't exactly desired so multiplying by 2
	 *	*	*	*	*	*	*	*	*	*	*	*	*	*	*	*/
	public final float desiredSpec = 0.0892857142857142f;
	public final float beforeSpec = 0.1116071428571429f;
	
	public SoundHandler(PApplet app) {
		minim = new Minim(app);
		lineIn = minim.getLineIn();
		lineIn.mute();
		lineIn.enableMonitoring();
		fft = new FFT(lineIn.bufferSize(), lineIn.sampleRate());
	}
	
	/**<strong>update()</strong>
	 * <p>Will update the sound class to grab new FFT data.</p>
	 */
	public void update() {
		fft.forward(lineIn.mix);
	}
	
	/**<strong>getBandsComb()</strong>
	 * <p>Returns a combination of all the frequency bands amplitude.</p>
	 * @return float
	 */
	public float getBandsComb() {
		fft.forward(lineIn.mix);
		float comb = 0;
		for (int i=0; i < fft.specSize(); i++) {
			comb += fft.getBand(i);
		}
		return comb;
	}
	
	/**<strong>getStrongestBand()</strong>
	 * <p>Returns the strongest amplitude band index.</p>
	 * @return
	 */
	public int getStrongestBand() {
		float max = 0;
		int maxIndex = 0;
		for (int i = 0; i < fft.specSize(); i++) {
			if (fft.getBand(i) > max) {
				max = fft.getBand(i);
				maxIndex = i;
			}
		}
		return maxIndex;
	}
	
	/**<strong>getBand()</strong>
	 * <p>Gets the amplitude of the band at index i out of 513 bands.</p>
	 * @param <h3>i</h3> Some number between 0 and 513.
	 * @return float
	 */
	public float getBand(int i) {
		if (i >= 0 & i <= 513) {
			return fft.getBand(i);
		}
		return 0;
	}
	
	/**<strong>getFrequencyActivation()</strong>
	 * <p>Allows you to get the amplitude of the frequencies from the starting band to the ending band inclusive.</p>
	 * <p>Try to make sure this falls somewhere between 0 and 513 or else it will just return 0 amplitude.</p>
	 * @param <h3>startingBand</h3> Some number greater than 0 but less than 513.
	 * @param <h3>endingBand</h3> Some number less than 513 but greater than the startingBand number.
	 * @return float
	 */
	public float getFrequencyActivation(int startingBand, int endingBand) {
		// Checks to make sure the user is not a dumbass
		if (startingBand < 0 | startingBand > 513 |
		endingBand < 0 | endingBand > 513 | endingBand < startingBand)
			return 0;
		
		float comb = 0;
		for (int i = startingBand; i < endingBand; i++) {
			comb += fft.getBand(i);
		}
		return comb;
	}
	
	public int getSpecSize() {
		return fft.specSize();
	}
	
	/**<strong>stop()</strong>
	 * <p>Stops minim and closes the lineIn cleanly.</p>
	 * You could just close it and not give a dam though.
	 */
	public void stop() {
		minim.stop();
		lineIn.close();
	}
}
