package bbc.juniperus.games.minesweeper.gui.resources;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;

/**
 * Singleton manager for the resources used by the game. Before using it, {@link initialize()}
 * needs to be invoked. This ensured that all resources are loaded from the file system.
 *
 */
public class ResourceManager {

	private static final String PATH_NUMBERS = "numbers.png";
	private static final String PATH_MINES = "mines.png";
	private static final String PATH_FLAG = "flag.png";
	private static final int MINE_FIELD_NUMBER_SPRITE_WIDTH = 20;
	private static final int MINE_SPRITE_WIDTH = 26;
	private static final int FLAG_SPRITE_WIDTH = 16;
	private Image[] imagesNumbers = new Image[8]; 
	private Image imgMine, imgCrossedMine, imgFlag;
	
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
			loadMineNumberImages();
			loadMineImages();
			loadFlagImage();
		} catch (URISyntaxException | IOException e) {
			e.printStackTrace();
			throw new ResourceLoadingException("Icons could not be loaded", e);
		}
		
		initialized = true;
	}
	
	/**
	 * Returns icons array for mine field numbers for the given icon width. Numbers range from 1 to 8. The icon for number <code>n</code>
	 * is in the returned array on position <code>n - 1</code>.  The original image width is {@link #MINE_FIELD_NUMBER_SPRITE_WIDTH} and the
	 * image is scaled based on the proportion of the given width to this original width.<br> 
	 * 
	 * @param width the width of the icons
	 * @return newly created icon array from the mine field number images
	 */
	public Icon[] createFieldNumberIcons(int width){
		checkIfInitialized();
		
		Icon[] icons = new Icon[8]; 
		//Calculate scale factor based on the original and given width ratio. 
		float  scaleFactor = ((float) width )/ MINE_FIELD_NUMBER_SPRITE_WIDTH; 
		
		for (int i = 0; i < imagesNumbers.length; i++) 
			icons[i] = getScaledImageIcon(imagesNumbers[i], scaleFactor);
		
		
		return icons; 
	}
	
	public Icon createMineIcon(int width){
		checkIfInitialized();
		//Calculate scale factor based on the original and given width ratio. 
		float  scaleFactor = ((float) width )/ MINE_SPRITE_WIDTH; 
		
		return getScaledImageIcon(imgMine, scaleFactor); 
	}

	public Icon createCrossedMineIcon(int width){
		checkIfInitialized();
		float  scaleFactor = ((float) width )/ MINE_SPRITE_WIDTH; 
		
		return getScaledImageIcon(imgCrossedMine, scaleFactor); 
	}
	
	public Icon createFlagIcon(int width){
		checkIfInitialized();
		float  scaleFactor = ((float) width )/ FLAG_SPRITE_WIDTH; 
		
		return getScaledImageIcon(imgFlag, scaleFactor); 
	}
	
	
	private Icon getScaledImageIcon(Image originalImg, float scaleFactor){
		int h = Math.round(scaleFactor * originalImg.getHeight(null));
		int w = Math.round(scaleFactor  * originalImg.getWidth(null));
		Image newImg = originalImg.getScaledInstance(w, h, BufferedImage.SCALE_SMOOTH);
		
		return new ImageIcon(newImg);
	}
	
	/**
	 * Routine for verifying whether the resource manager has been initialized.
	 * Throws exception if not.
	 * @throw {@link IllegalStateException} if the manager is not initialized
	 */
	private void checkIfInitialized(){
		if (!initialized)
			throw new IllegalStateException("The resource manager has not been initialized yet.");
	}
	
	/**
	 * Loads images for number in mine fields.
	 * @throws URISyntaxException
	 * @throws IOException
	 */
	private void loadMineNumberImages() throws URISyntaxException, IOException{
		URL url = getClass().getResource(PATH_NUMBERS);
		File f = new File(url.toURI());
		BufferedImage img = ImageIO.read(f);
		
		int height = img.getHeight();
		for (int i = 0; i < imagesNumbers.length; i++) 
			imagesNumbers[i]=  img.getSubimage(i * MINE_FIELD_NUMBER_SPRITE_WIDTH , 
					0, MINE_FIELD_NUMBER_SPRITE_WIDTH, height);
	}
	
	private void loadMineImages() throws URISyntaxException, IOException{
		URL url = getClass().getResource(PATH_MINES);
		File f = new File(url.toURI());
		BufferedImage img = ImageIO.read(f);
		
		int height = img.getHeight();
		
		imgMine=  img.getSubimage(0,0, MINE_SPRITE_WIDTH, height);
		imgCrossedMine=  img.getSubimage(MINE_SPRITE_WIDTH ,0 ,MINE_SPRITE_WIDTH, height);
	}
	
	private void loadFlagImage() throws IOException, URISyntaxException{
		URL url = getClass().getResource(PATH_FLAG);
		File f = new File(url.toURI());
		imgFlag = ImageIO.read(f);
	}
	
	
}
