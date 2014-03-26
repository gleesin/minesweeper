package bbc.juniperus.games.minesweeper.gui.swing;

import java.awt.Image;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class IconSettingProblem {

	
	public static void main(String[] args) throws InterruptedException, IOException{
		

		
		
		JPanel panel = new JPanel();
		final JLabel[] labels = new JLabel[20];
		
		for (int i= 0; i < labels.length; i++){
			labels[i] = new JLabel("---|");
			panel.add(labels[i]);
		}
		
		
		JFrame frame = new JFrame();
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(panel);
		frame.pack();
		frame.setVisible(true);
		
		Thread.sleep(2000);
		
		URL url = new URL("http://www.quarktet.com/Icon-small.jpg");
		
		System.out.println(url);
		
		Image image = ImageIO.read(url);
		final ImageIcon icon = new ImageIcon(image);
		
		SwingUtilities.invokeLater(new Runnable(){

			@Override
			public void run() {
				for (int i= 0; i < labels.length; i++){
					labels[i].setIcon(icon);
				}
				
			}
		});
		
	}
}
