package bbc.juniperus.games.minesweeper.gui.swing;

import java.io.IOException;

import javax.media.Manager;
import javax.media.NoPlayerException;
import javax.media.Player;
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
	private String sound = "explosion.wav";
	private Player player;

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
		
		try {
			player = Manager.createPlayer(this.getClass().getResource(sound));
			player.realize();
		} catch (NoPlayerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
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
	/*

		System.out.println(clip.getFrameLength());
		clip.start();	
		*/
		player.start();
	}
	
	public static void main(String[] args) throws InterruptedException{
		
		SoundPlayer sp = new SoundPlayer();
		//Thread.sleep(1000); //Let open the file
		sp.playSound();
		//Thread.sleep(10000); //Wait for the playback to finish
	}
	
	/*
	FloatControl gainControl = (FloatControl) clip
	        .getControl(FloatControl.Type.MASTER_GAIN);
	
    double gain = 0.1D; // number between 0 and 1 (loudest)
    float dB = (float) (Math.log(gain) / Math.log(10.0) * 20.0);
    gainControl.setValue(dB);
    */
}
