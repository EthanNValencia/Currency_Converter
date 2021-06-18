package CC_Directory;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class PlayMusic implements Runnable {

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

    @Override
    public void run() {
        playMusic();
    }
}
