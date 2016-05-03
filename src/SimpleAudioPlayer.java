import javax.media.*;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.MalformedURLException;

public class SimpleAudioPlayer {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			//File audioFile = new File(args[0]);
			//SimpleAudioPlayer player = new SimpleAudioPlayer(audioFile);
			SimpleAudioPlayer player = new SimpleAudioPlayer(new URL(args[0]));
			player.play();
			
			System.in.read();
			player.stop();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	private Player audioPlayer = null;
	
	public SimpleAudioPlayer(URL url) throws IOException, NoPlayerException,
	CannotRealizeException {
		audioPlayer = Manager.createRealizedPlayer(url);
	}
	
	public SimpleAudioPlayer(File file) throws IOException, NoPlayerException,
	CannotRealizeException {
		this(file.toURI().toURL());
	}
	
	public void play() {
		audioPlayer.start();
	}
	
	public void stop() {
		audioPlayer.stop();
		audioPlayer.close();
	}
}
