package bbc.juniperus.games.minesweeper.gui.swing;

import java.io.IOException;
import java.net.URL;

import javax.media.Manager;
import javax.media.NoPlayerException;
import javax.media.Player;
import javax.media.Time;

import bbc.juniperus.games.minesweeper.gui.swing.ResourceLoader.SoundResource;

public class SoundPlayer {
    
    private Player explosionPlayer, tickPlayer;
    private URL urlExplosion, urlTick;
    private boolean isInitialized;


    public SoundPlayer() {

         urlExplosion = ResourceLoader.getInstance().getSoundResourceUrl(SoundResource.EXPLOSION);
         urlTick = ResourceLoader.getInstance().getSoundResourceUrl(SoundResource.TICK);
    	
    }
    
    public void initialize() throws ResourceLoadingException{
        try {
            explosionPlayer = Manager.createPlayer(urlExplosion);
            explosionPlayer.realize();
    
            tickPlayer = Manager.createPlayer(urlTick);
            tickPlayer.realize();
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
        explosionPlayer.start();
    } 

    public void playTickSound(){
        ensureInitialized();
        tickPlayer.setMediaTime(new Time(0));
        tickPlayer.start();
    } 

//    public static void main(String[] args) throws InterruptedException, ResourceLoadingException{
//    	
//        SoundPlayer sp = new SoundPlayer();
//        sp.initialize();
//        //Thread.sleep(1000); //Let open the file
//        sp.playTickSound();
//        //Thread.sleep(10000); //Wait for the playback to finish
//    }
    
}
