package bbc.juniperus.minesweeper.gui;

import java.io.IOException;
import java.net.URL;

import javax.media.Manager;
import javax.media.MediaLocator;
import javax.media.NoPlayerException;
import javax.media.Player;
import javax.media.Time;

import bbc.juniperus.minesweeper.gui.ResourceLoader.SoundResource;

/**
 * A player which plays various sounds in the game.
 * The player must be initialised before it can be used to play sounds.
 */
public class SoundPlayer {
    
    private Player explosionPlayer, tickPlayer, winPlayer;
    private URL urlExplosion, urlTick, urlWin;
    private boolean isInitialized;
    private MediaLocator a,b,c;

    /**
     * Creates a new sound player and retrieved the URLs of the 
     * sound resources from {@link ResourceLoader} singleton. Resource
     * loader must be initialised before-hand.
     */
    public SoundPlayer() {
    	//TODO REFACTOR!!!
         urlExplosion = ResourceLoader.getInstance().getSoundResourceUrl(SoundResource.EXPLOSION);
         urlTick = ResourceLoader.getInstance().getSoundResourceUrl(SoundResource.TICK);
         urlWin = ResourceLoader.getInstance().getSoundResourceUrl(SoundResource.WIN);
         
         a = new MediaLocator(urlExplosion);
         b = new MediaLocator(urlTick);
         c = new MediaLocator(urlWin);
         
         
    }
   
    /**
     * 
     * @throws ResourceLoadingException
     */
    public void initialize() throws ResourceLoadingException{
        try {
            explosionPlayer = Manager.createPlayer(a);
            explosionPlayer.realize();
            tickPlayer = Manager.createPlayer(b);
            tickPlayer.realize();
            winPlayer = Manager.createPlayer(c);
            winPlayer.realize();
            
        } catch (NoPlayerException e) {
            throw new ResourceLoadingException("Could not initialize the sound player",e);
        } catch (IOException e) {
            throw new ResourceLoadingException("Could not initialize the sound player",e);
        }
    	
        isInitialized = true;
    }
   
    /**
     * Checks if this player is initialised and if not throws an exception.
     */
    private void ensureInitialized(){
        if (!isInitialized)
            throw new IllegalStateException("The player is not initialized");
    }
    
    /**
     * Plays an explosion sounds.
     */
    public void playExplosionSound(){
        ensureInitialized();
        explosionPlayer.setMediaTime(new Time(0));
        explosionPlayer.start();
    } 

    /**
     * Plays a tick sound.
     */
    public void playTickSound(){
        ensureInitialized();
        tickPlayer.setMediaTime(new Time(0));
        tickPlayer.start();
    } 

    /**
     * Plays a 'game-won' sound.
     */
    public void playWinSound(){
        ensureInitialized();
        winPlayer.setMediaTime(new Time(0));
        winPlayer.start();
    } 

}
