import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;

import javax.swing.*;
import java.util.Random;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class GamePanel extends JPanel implements ActionListener {
	long elapsedTime;
	long pauseTime;
	static final int SCREEN_WIDTH = 700;
	static final int SCREEN_HEIGHT = 700;
	static final int UNIT_SIZE = 25;
	static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / UNIT_SIZE;
	static final int DELAY = 75;
	final int x[] = new int[GAME_UNITS];
	final int y[] = new int[GAME_UNITS];
	long startTime = System.currentTimeMillis();
	int bodyParts = 6;
	int applesEaten = 0;
	int appleX;
	int appleY;
	char direction = 'R';
	boolean running = false;
	Timer timer;
	Random random;

	GamePanel() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
		random = new Random();
		this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
		this.setBackground(Color.black);
		this.setFocusable(true);
		this.addKeyListener(new MyKeyAdapter());
		File file = new File("music.wav");
		AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
		Clip clip = AudioSystem.getClip();
		clip.open(audioStream);
		clip.loop(clip.LOOP_CONTINUOUSLY);
		clip.start();
		
		
		
		startGame();

	}

	public void startGame() {
		newApple();
		running = true;
		timer = new Timer(DELAY, this);
		timer.start();
		

	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		draw(g);

	}

	public void draw(Graphics g) {

		if (running) {

			// grid
			for (int i = 0; i < SCREEN_HEIGHT / UNIT_SIZE; i++) {
				g.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, SCREEN_HEIGHT);
				g.drawLine(0, i * UNIT_SIZE, SCREEN_WIDTH, i * UNIT_SIZE);
			}
			// Timer
			if (timer.isRunning()) {
				elapsedTime = System.currentTimeMillis() - startTime - pauseTime;
				long elapsedSeconds = elapsedTime / 1000;
				long secondsDisplay = elapsedSeconds % 60;
				long elapsedMinutes = elapsedSeconds / 60;
				g.setFont(new Font("Calibri Light", Font.BOLD, 35));
				g.setColor(Color.white);
				g.drawString("Time: " + elapsedMinutes + ":" + secondsDisplay, 50, 30);
			}
			// draw random apple
			g.setColor(Color.green);
			g.fillRect(appleX, appleY, UNIT_SIZE, UNIT_SIZE);

			// draw snake
			for (int i = 0; i < bodyParts; i++) {
				if (i == 0) {
					g.setColor(Color.yellow);
					g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
				} else {
					g.setColor(Color.red);
					// g.setColor(new
					// Color(random.nextInt(255),random.nextInt(255),random.nextInt(255)));
					g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
				}

			}
			g.setColor(Color.white);
			g.setFont(new Font("Calibri Light", Font.BOLD, 35));
			g.setColor(Color.white);
			g.drawString("Score: " + applesEaten, 500, 30);
		} else {
			gameOver(g);
		}

	}

	public void move() {

		for (int i = bodyParts; i > 0; i--) {
			x[i] = x[i - 1];
			y[i] = y[i - 1];
		}

		switch (direction) {
		case 'U':
			y[0] = y[0] - UNIT_SIZE;
			break;
		case 'D':
			y[0] = y[0] + UNIT_SIZE;
			break;
		case 'R':
			x[0] = x[0] + UNIT_SIZE;
			break;
		case 'L':
			x[0] = x[0] - UNIT_SIZE;
			break;
		}

	}

	public void newApple() {
		appleX = random.nextInt((int) (SCREEN_WIDTH / UNIT_SIZE)) * UNIT_SIZE;
		appleY = random.nextInt((int) (SCREEN_HEIGHT / UNIT_SIZE)) * UNIT_SIZE;
	}

	public void checkApple() {
		if ((x[0] == appleX) && (y[0] == appleY)) {
			bodyParts++;
			applesEaten++;
			newApple();
		}

	}

	public void checkCollision() {
		// check head collide w/body
		for (int i = bodyParts; i > 0; i--) {
			if ((x[0] == x[i]) && (y[0] == y[i])) {
				running = false;
			}
		}
		// check head w/ left border
		if (x[0] < 0) {
			running = false;
		}
		// right border check
		if (x[0] > SCREEN_WIDTH - 1) {
			running = false;
		}
		// top border check
		if (y[0] < 0) {
			running = false;
		}
		// check bot
		if (y[0] > SCREEN_HEIGHT -1) {
			running = false;
		}
		if (running == false) {
			timer.stop();
		}

	}

	public void gameOver(Graphics g) {
		// gameover text
		g.setColor(Color.red);
		g.setFont(new Font("Ink Free", Font.BOLD, 70));
		FontMetrics metrics = getFontMetrics(g.getFont());
		g.drawString("Game Over!", (SCREEN_WIDTH - metrics.stringWidth("Game Over!")) / 2, SCREEN_HEIGHT / 2);
		g.setFont(new Font("Calibri Light", Font.BOLD, 40));
		g.drawString("Score: " + applesEaten, 275, 500);

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (running) {
			move();
			checkApple();
			checkCollision();
		}
		repaint();

	}

	public class MyKeyAdapter extends KeyAdapter {
		@Override
		public void keyPressed(KeyEvent e) {

			switch (e.getKeyCode()) {
			case KeyEvent.VK_A:
				if (direction != 'R') {
					direction = 'L';
				}
				break;

			case KeyEvent.VK_D:
				if (direction != 'L') {
					direction = 'R';
				}
				break;

			case KeyEvent.VK_W:
				if (direction != 'D') {
					direction = 'U';
				}
				break;

			case KeyEvent.VK_S:
				if (direction != 'U') {
					direction = 'D';
				}
				break;

			case KeyEvent.VK_X:
				if (timer.isRunning()) {
					timer.stop();
				}
				else {
					timer.start();
					
				}
				
			case KeyEvent.VK_R:
				
				break;

			}

		}
	}
}
