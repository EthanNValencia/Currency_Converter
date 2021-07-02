package CC_Directory;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/***
 * This is a method that I found in one of my textbooks. I thought it was entertaining, but it takes too long to begin running.
 */
public class PlayMusic implements Runnable {

    /***
     * This is a fun little method that plays audio from a specified URL. It takes a while to load, so it isn't optimal.
     */
    public void playMusic() {
        Clip clip;
        URL url = null;
        try {
            url = new URL("http://cooplogic.com/cheyney/sound/liftMusic.wav");
            AudioInputStream audio =
                    AudioSystem.getAudioInputStream(url);
            DataLine.Info info =
                    new DataLine.Info(Clip.class, audio.getFormat());

            if(!AudioSystem.isLineSupported(info)){
                System.err.println("Audio file not supported: " + info);
                return;
            }
            try {
                clip = (Clip) AudioSystem.getLine(info);
                clip.open(audio);
                clip.start();
                while(clip.getFramePosition() < clip.getFrameLength()){
                    try {
                        Thread.sleep(10);
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                }
            } catch (LineUnavailableException e) {
                e.printStackTrace();
            }
        } catch (MalformedURLException e) {
            System.out.println("Malformed URL: " + url.toString());
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /***
     * This method allows the instantiated object to be run as a thread.
     */
    @Override
    public void run() {
        playMusic();
    }
}
