package bbc.juniperus.games.minesweeper.gui.resources;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

/**
 * Singleton manager for the resources used by the game. Before using it, {@link initialize()}
 * needs to be invoked. This ensured that all resources are loaded from the file system.
 *
 */
public class ResourceManager {

	private static final String PATH_NUMBERS = "numbers.png";
	private static final int NUMBER_ICON_WIDTH = 20;
	private static final int NUMBER_ICON_SPACING = 12;
	private ImageIcon[] iconsNumber = new ImageIcon[8]; 
	
	private static ResourceManager instance;
	private boolean initialized;

	/**
	 * Private constructor
	 */
	private ResourceManager(){

	}

	/**
	 * Returns singleton instance.
	 * @return singleton instance of ResourceManager
	 */
	synchronized public static ResourceManager getInstance(){
		if (instance == null)
			instance = new ResourceManager();
		return instance;
	}
	
	/**
	 * Initializes the resource manager.
	 * Loads all the resources. This method mut be invoked before any
	 * resource retrieval method is called.
	 * @throws ResourceLoadingException when there was a problem during loading a resource
	 */
	public void initialize() throws ResourceLoadingException {
		
		try {
			loadNumberIcon();
		} catch (URISyntaxException | IOException e) {
			e.printStackTrace();
			throw new ResourceLoadingException("Numbers icons could not be loaded", e);
		}
		
		initialized = true;
	}
	
	/**
	 * Retrieves the Image 
	 * @param number
	 * @return
	 */
	public ImageIcon[] getFieldNumberIcons(int width){
		checkIfInitialized();
		
		ImageIcon[] copy = iconsNumber.clone();
		float  scaleFactor = ((float) width )/ NUMBER_ICON_WIDTH; 
		
		for (int i = 0; i < iconsNumber.length; i++) {
			ImageIcon icon = copy[i];
			Image img = icon.getImage();
			int h = Math.round(scaleFactor * img.getHeight(null));
			int w = Math.round(scaleFactor  * img.getWidth(null));
			Image newImg = icon.getImage().getScaledInstance(w, h, BufferedImage.SCALE_SMOOTH);
			icon.setImage(newImg);
		}
		
		return copy;
	}
	
	
	
	/**
	 * Routine for checking if the resource manager has been initialized.
	 * Throws exception if not.
	 * @throw {@link IllegalStateException} if the manager is not initialized
	 */
	private void checkIfInitialized(){
		if (!initialized)
			throw new IllegalStateException("The resource manager has not been yet initialized");
	}
	
	private void loadNumberIcon() throws URISyntaxException, IOException{
		URL url = getClass().getResource(PATH_NUMBERS);
		File f = new File(url.toURI());
		BufferedImage img = ImageIO.read(f);
		
		int height = img.getHeight();
		for (int i = 0; i < iconsNumber.length; i++) {
			Image iconImg =  img.getSubimage(i * (NUMBER_ICON_WIDTH + NUMBER_ICON_SPACING), 
					0, NUMBER_ICON_WIDTH, height);
			iconsNumber[i] = new ImageIcon(iconImg); 
		}
		
	}
	
	
}
