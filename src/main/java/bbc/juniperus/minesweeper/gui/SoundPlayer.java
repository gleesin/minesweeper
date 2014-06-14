package bbc.juniperus.minesweeper.gui;

import java.io.IOException;
import java.net.URL;

import javax.media.Manager;
import javax.media.MediaLocator;
import javax.media.NoPlayerException;
import javax.media.Player;
import javax.media.Time;

import bbc.juniperus.minesweeper.gui.ResourceLoader.SoundResource;

public class SoundPlayer {
    
    private Player explosionPlayer, tickPlayer, winPlayer;
    private URL urlExplosion, urlTick, urlWin;
    private boolean isInitialized;
    private MediaLocator a,b,c;


    public SoundPlayer() {
         urlExplosion = ResourceLoader.getInstance().getSoundResourceUrl(SoundResource.EXPLOSION);
         urlTick = ResourceLoader.getInstance().getSoundResourceUrl(SoundResource.TICK);
         urlWin = ResourceLoader.getInstance().getSoundResourceUrl(SoundResource.WIN);
         System.out.println(urlExplosion);
         
         a = new MediaLocator(urlExplosion);
         b = new MediaLocator(urlTick);
         c = new MediaLocator(urlWin);
         
         
    }
    
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
    
    private void ensureInitialized(){
        if (!isInitialized)
            throw new IllegalStateException("The player is not initialized");
    }
    
    public boolean isInitialized(){
        return isInitialized;
    }
    
    public void playExplosionSound(){
        ensureInitialized();
        explosionPlayer.setMediaTime(new Time(0));
        explosionPlayer.start();
    } 

    public void playTickSound(){
        ensureInitialized();
        tickPlayer.setMediaTime(new Time(0));
        tickPlayer.start();
    } 

    public void playWinSound(){
        ensureInitialized();
        winPlayer.setMediaTime(new Time(0));
        winPlayer.start();
    } 

}
