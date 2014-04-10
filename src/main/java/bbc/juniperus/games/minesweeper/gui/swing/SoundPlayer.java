package bbc.juniperus.games.minesweeper.gui.swing;

import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class SoundPlayer {
	private Clip clip;
	private String sound = "bomb.wav";

	public SoundPlayer() {
		try {
			clip = loadClip();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		} catch (UnsupportedAudioFileException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	private Clip loadClip() throws LineUnavailableException, UnsupportedAudioFileException, IOException{
		AudioInputStream audio = AudioSystem.getAudioInputStream(this.getClass().getResource(sound));
		AudioFormat format = audio.getFormat();
		DataLine.Info info = new DataLine.Info(Clip.class, format);
		final Clip clip =  (Clip) AudioSystem.getLine(info);
		clip.open(audio);
		
		clip.addLineListener(new LineListener() {
			
			@Override
			public void update(LineEvent event) {
				System.out.println(event);
				System.out.println(clip.getFramePosition());
			}
		});
		
		
		return clip;
	}
	
	public void playSound(){
	

		System.out.println(clip.getFrameLength());
		clip.start();	
	}
	
	public static void main(String[] args) throws InterruptedException{
		
		SoundPlayer sp = new SoundPlayer();
		Thread.sleep(2000); //Let open the file
		sp.playSound();
		Thread.sleep(10000); //Wait for the playback to finish
	}
	
	/*
	FloatControl gainControl = (FloatControl) clip
	        .getControl(FloatControl.Type.MASTER_GAIN);
	
    double gain = 0.1D; // number between 0 and 1 (loudest)
    float dB = (float) (Math.log(gain) / Math.log(10.0) * 20.0);
    gainControl.setValue(dB);
    */
}
