import java.io.IOException;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JFrame;

public class GameFrame extends JFrame {
	
	GameFrame() throws UnsupportedAudioFileException, IOException, LineUnavailableException{
		
		GamePanel panel = new GamePanel();
		this.add(panel);
		
		this.setTitle("Snake for comp sci");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.pack();
		this.setVisible(true);
		this.setLocationRelativeTo(null);
		}
}
