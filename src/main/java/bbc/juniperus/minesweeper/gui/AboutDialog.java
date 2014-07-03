package bbc.juniperus.minesweeper.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URL;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import bbc.juniperus.minesweeper.gui.ResourceLoader.SoundResource;

/**
 * A modal dialog which shows the information about the application and the
 * author.
 */
@SuppressWarnings("serial")
public class AboutDialog extends JDialog {

	private final static int HEIGHT = 120;
	private final static int WIDTH = 250;

	public AboutDialog(Frame frame) {
		super(frame, true);
		setup();
		setLocationRelativeTo(frame);
		setTitle("About");
		this.setSize(WIDTH, HEIGHT);

	}

	/**
	 * Creates and adds all the dialog components. 
	 */
	private void setup() {
		JLabel label = new JLabel();
		label.setText("<html>This is a Java Swing <br>"
				+ "implementation of Microsoft's <br>classic Windows XP Minesweeper."
				+ "<br<i>Version 1.0<i/>" + "</html>");
		JButton button = new JButton("Ok");
		button.setPreferredSize(new Dimension(50, 20));
		button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				AboutDialog.this.dispose();
			}

		});

		JPanel panel = new JPanel();

		JPanel buttonPanel = new JPanel();
		buttonPanel.add(button);

		panel.add(label);

		add(panel);
		add(buttonPanel, BorderLayout.SOUTH);
	}
	
	public static void main(String[] args)
			throws UnsupportedAudioFileException, IOException, LineUnavailableException, InterruptedException {
		System.out.println("Starting");
		URL url = ResourceLoader.getInstance().getSoundResourceUrl(
				SoundResource.WIN);
		AudioInputStream audioStream = AudioSystem.getAudioInputStream(url);

		AudioFormat format = audioStream.getFormat();
		DataLine.Info info = new DataLine.Info(Clip.class, format);
		Clip clip = (Clip) AudioSystem.getLine(info);
		clip.open(audioStream);
		clip.start();
		
		Thread.sleep(4000);
		System.out.println("finished");
	}
}
