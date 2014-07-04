package bbc.juniperus.minesweeper.gui;

import java.io.IOException;
import java.net.URL;
import java.util.concurrent.CountDownLatch;

import javax.media.Manager;
import javax.media.NoPlayerException;
import javax.media.Player;
import javax.media.Time;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import bbc.juniperus.minesweeper.gui.ResourceLoader.SoundResource;

public class SoundPlayer {

    private Player explosionPlayer, tickPlayer, winPlayer;
    private URL urlExplosion, urlTick, urlWin;
    private boolean isInitialized;

    public SoundPlayer() {
        urlExplosion = ResourceLoader.getInstance().getSoundResourceUrl(
                SoundResource.EXPLOSION);
        urlTick = ResourceLoader.getInstance().getSoundResourceUrl(
                SoundResource.TICK);
        urlWin = ResourceLoader.getInstance().getSoundResourceUrl(
                SoundResource.WIN);

    }

    public void initialize() throws ResourceLoadingException {
        try {

            JarDataSource jdsExplosion = new JarDataSource(urlExplosion);
            jdsExplosion.connect();
            JarDataSource jdsTick = new JarDataSource(urlTick);
            jdsTick.connect();
            JarDataSource jdsWin = new JarDataSource(urlWin);
            jdsWin.connect();

            explosionPlayer = Manager.createPlayer(jdsExplosion);
            explosionPlayer.realize();
            tickPlayer = Manager.createPlayer(jdsTick);
            tickPlayer.realize();
            winPlayer = Manager.createPlayer(jdsWin);
            winPlayer.realize();

        } catch (NoPlayerException e) {
            throw new ResourceLoadingException(
                    "Could not initialize the sound player", e);
        } catch (IOException e) {
            throw new ResourceLoadingException(
                    "Could not initialize the sound player", e);
        }catch(Exception e){
            throw new ResourceLoadingException(
                    "Could not initialize the sound player", e);
        }

        isInitialized = true;
    }

    private void ensureInitialized() {
        if (!isInitialized)
            throw new IllegalStateException("The player is not initialized");
    }

    public boolean isInitialized() {
        return isInitialized;
    }

    public void playExplosionSound() {
        ensureInitialized();
        explosionPlayer.setMediaTime(new Time(0));
        explosionPlayer.start();
    }

    public void playTickSound() {
        ensureInitialized();
        tickPlayer.setMediaTime(new Time(0));
        tickPlayer.start();
    }

    public void playWinSound() {
        ensureInitialized();
        winPlayer.setMediaTime(new Time(0));
        winPlayer.start();
    }
    
    
    public static void main(String[] args) throws LineUnavailableException, UnsupportedAudioFileException, IOException, InterruptedException, NoPlayerException{
    	  JarDataSource jdsExplosion = new JarDataSource(null);
          jdsExplosion.connect();
          Player player  = Manager.createPlayer(jdsExplosion);
          player.realize();
          player.start();
    }

}
