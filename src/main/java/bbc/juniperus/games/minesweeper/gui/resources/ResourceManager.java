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

	private static final String PATH_MINEFIELD_NUMBERS = "numbers.png";
	private static final String PATH_DISPLAY_NUMBERS = "display.png";
	private static final String PATH_MINES = "mines.png";
	private static final String PATH_FLAG = "flag.png";
	private static final String PATH_QUESTION_MARK = "question_mark.png";
	
	private static final int MINEFIELD_NUMBER_IMG_WIDTH = 20;
	private static final int DISPLAY_NUMBER_IMG_WIDTH = 26;
	private static final int MINE_IMG_WIDTH = 26;
	private static final int FLAG_IMG_WIDTH = 16;
	private static final int QUESTION_MARK_IMG_WIDTH = 12;

	private Image[] minefieldNumbers, displayNumbers;
	private Image imgMine, imgCrossedMine, imgFlag, imgQuestionMark;
	
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
			minefieldNumbers = loadImagesFromSprite(PATH_MINEFIELD_NUMBERS ,8, MINEFIELD_NUMBER_IMG_WIDTH);
			imgFlag = loadImg(PATH_FLAG);
			imgQuestionMark = loadImg(PATH_QUESTION_MARK);
			
			Image[] mines = loadImagesFromSprite(PATH_MINES, MINE_IMG_WIDTH, 2);
			imgMine = mines[0];
			imgCrossedMine = mines[1];
			
			displayNumbers = loadImagesFromSprite(PATH_DISPLAY_NUMBERS, DISPLAY_NUMBER_IMG_WIDTH, 11);
			
		} catch (URISyntaxException | IOException e) {
			e.printStackTrace();
			throw new ResourceLoadingException("Icons could not be loaded", e);
		}
		
		initialized = true;
	}
	
	/**
	 * Returns icons array for mine field numbers for the given icon width. Numbers range from 1 to 8. The icon for number <code>n</code>
	 * is in the returned array on position <code>n - 1</code>.  The original image width is {@link #MINEFIELD_NUMBER_IMG_WIDTH} and the
	 * image is scaled based on the proportion of the given width to this original width.<br> 
	 * 
	 * @param width the width of the icons
	 * @return newly created icon array from the mine field number images
	 */
	public Icon[] createMineFieldNumberIcons(int width){
		checkIfInitialized();
		
		Icon[] icons = new Icon[minefieldNumbers.length]; 
		//Calculate scale factor based on the original and given width ratio. 
		float  scaleFactor = ((float) width )/ MINEFIELD_NUMBER_IMG_WIDTH; 
		
		for (int i = 0; i < minefieldNumbers.length; i++) 
			icons[i] = getScaledImageIcon(minefieldNumbers[i], scaleFactor);
		
		
		return icons; 
	}
	
	public Icon[] createDisplayNumberIcons(int width){
		checkIfInitialized();
		
		Icon[] icons = new Icon[displayNumbers.length]; 
		//Calculate scale factor based on the original and given width ratio. 
		float  scaleFactor = ((float) width )/ DISPLAY_NUMBER_IMG_WIDTH; 
		
		for (int i = 0; i < displayNumbers.length; i++) 
			icons[i] = getScaledImageIcon(displayNumbers[i], scaleFactor);
		
		
		return icons; 
	}
	
	
	
	public Icon createMineIcon(int width){
		checkIfInitialized();
		//Calculate scale factor based on the original and given width ratio. 
		float  scaleFactor = ((float) width )/ MINE_IMG_WIDTH; 
		
		return getScaledImageIcon(imgMine, scaleFactor); 
	}

	public Icon createCrossedMineIcon(int width){
		checkIfInitialized();
		float  scaleFactor = ((float) width )/ MINE_IMG_WIDTH; 
		
		return getScaledImageIcon(imgCrossedMine, scaleFactor); 
	}
	

	public Icon createFlagIcon(int width){
		checkIfInitialized();
		float  scaleFactor = ((float) width )/ FLAG_IMG_WIDTH; 
		
		return getScaledImageIcon(imgFlag, scaleFactor); 
	}
	
	public Icon createQuestionMarkIcon(int width){
		checkIfInitialized();
		float  scaleFactor = ((float) width )/ QUESTION_MARK_IMG_WIDTH; 
		
		return getScaledImageIcon(imgQuestionMark, scaleFactor); 
	}
	
	
	
	private Icon getScaledImageIcon(Image originalImg, float scaleFactor){
		return new ImageIcon(getScaledImage(originalImg,scaleFactor));
	}
	
	private Image getScaledImage(Image originalImg, float scaleFactor){
		int h = Math.round(scaleFactor * originalImg.getHeight(null));
		int w = Math.round(scaleFactor  * originalImg.getWidth(null));
		Image newImg = originalImg.getScaledInstance(w, h, BufferedImage.SCALE_SMOOTH);
		
		return newImg;
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
	
	private Image[] loadImagesFromSprite(String imgPath, int width, int count) throws URISyntaxException, IOException{
		URL url = getClass().getResource(imgPath);
		File f = new File(url.toURI());
		BufferedImage img = ImageIO.read(f);
		
		Image[] result = new BufferedImage[count];
		
		int height = img.getHeight();
		
		for (int i = 0; i < count; i++) 
			result[i]=  img.getSubimage(i * width , 
					0, width, height);
		
		return result;
	}
	
	
	
	private Image loadImg(String path) throws URISyntaxException, IOException{
		URL url = getClass().getResource(path);
		File f = new File(url.toURI());
		return ImageIO.read(f);
	}
	
}
