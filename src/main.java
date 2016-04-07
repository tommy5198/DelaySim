import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.Timestamp;
import java.sql.Date;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

import javax.swing.*;

public class main {
	static Queue<KeyEvent> queue = new LinkedList<KeyEvent>();
	static Timer timer;
	static Random random;
	
	static int mean = 800;
	static int std = 200;
	
	static int count = 0;
	static int countLimit;
	
	static int randomTime = 0;
	
	static Calendar calendar;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		JFrame demo = new JFrame();
		
		demo.setSize(400, 300);
		demo.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		MyPanel myPanel = new MyPanel();
		myPanel.setFocusable(true);
		myPanel.requestFocusInWindow();
		demo.add(myPanel);
		demo.pack();
		demo.setVisible(true);
		
		random = new Random();
		
		countLimit = 5;
		
		ActionListener listener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				KeyEvent event = queue.poll();
				myPanel.changeDirection(event);
				count++;
				
				if(count == countLimit) {
					do {
						randomTime = (int) Math.round(random.nextGaussian() * std + mean);
					} while(randomTime < 0);
					timer.stop();
					timer.setInitialDelay(randomTime);
					timer.setDelay(5);
					timer.restart();
					count = 0;
					do {
						countLimit = (int) Math.round(random.nextGaussian() * 2 + 5);
					} while(countLimit < 1);
					
					calendar = Calendar.getInstance();
					Timestamp ts = new Timestamp(calendar.getTime().getTime());
					System.out.print(ts.getTime() + " ");
					System.out.println("Tick: " + randomTime + ", countLimit: " + countLimit);
				}
			}
		};
		timer = new Timer(0, listener);
		timer.start();
		
		
		ActionListener keyListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				myPanel.moveSquare();
			}	
		};
		Timer keyTimer = new Timer(1, keyListener);
		keyTimer.start();
	}

	static class MyPanel extends JPanel {
		private static final long serialVersionUID = 1L;
		private int squareX = 50;
		private int squareY = 50;
		private int square2X = 1130;
		private int square2Y = 730;
		
		private int squareH = 20;
		private int squareW = 20;
		
		private int height = 800;
		private int width = 1200;
		
		private int x = 0;
		private int y = 0;
		private int x2 = 0;
		private int y2 = 0;
		
		public MyPanel() {
			setBorder(BorderFactory.createLineBorder(Color.black));
			
			addKeyListener(new KeyListener() {
				@Override
				public void keyTyped(KeyEvent e) {
					// TODO Auto-generated method stub			
				}
	
				@Override
				public void keyPressed(KeyEvent e) {
					// TODO Auto-generated method stub
					queue.add(e);
				}
	
				@Override
				public void keyReleased(KeyEvent e) {
					// TODO Auto-generated method stub
					queue.add(e);
				}
			});
		}
		
		public void changeDirection(KeyEvent event) {
			if(event != null && event.getID() == KeyEvent.KEY_RELEASED) {
				switch(event.getKeyCode()) {
				case KeyEvent.VK_UP: // Up
				case KeyEvent.VK_DOWN: // Down
					y2 = 0;
					break;
				case KeyEvent.VK_W: // W
				case KeyEvent.VK_S: // S
					y = 0;
					break;
				case KeyEvent.VK_LEFT: // Left
				case KeyEvent.VK_RIGHT: // Right
					x2 = 0;
					break;
				case KeyEvent.VK_A: // A
				case KeyEvent.VK_D: // D
					x = 0;
					break;
				}
			}
				
			if(event != null && event.getID() == KeyEvent.KEY_PRESSED) {
				switch(event.getKeyCode()) {
				case KeyEvent.VK_UP: // Up
					y2 = -1;
					break;
				case KeyEvent.VK_W: // W
					y = -1;
					break;
				case KeyEvent.VK_DOWN: // Down
					y2 = 1;
					break;
				case KeyEvent.VK_S: // S
					y = 1;
					break;
				case KeyEvent.VK_LEFT: // Left
					x2 = -1;
					break;
				case KeyEvent.VK_A: // A
					x = -1;
					break;
				case KeyEvent.VK_RIGHT: // Right
					x2 = 1;
					break;
				case KeyEvent.VK_D: // D
					x = 1;
					break;
				}
			}
		}
		
		public void moveSquare() {
			int OFFSET = 1;
			int step = 1;
			
			repaint(squareX, squareY, squareW + OFFSET, squareH + OFFSET);
			
			if(x == 1 && squareX + squareW + step < width) {
				squareX += step;
			}
			else if(x == -1 && squareX - step > 0) {
				squareX -= step;
			}
			if(y == 1 && squareY + squareH + step < height) {
				squareY += step;
			}
			else if(y == -1 && squareY - step > 0) {
				squareY -= step;
			}
			
			repaint(squareX, squareY, squareW + OFFSET, squareH + OFFSET);
			
			repaint(square2X, square2Y, squareW + OFFSET, squareH + OFFSET);
			
			if(x2 == 1 && square2X + squareW + step < width) {
				square2X += step;
			}
			else if(x2 == -1 && square2X - step > 0) {
				square2X -= step;
			}
			if(y2 == 1 && square2Y + squareH + step < height) {
				square2Y += step;
			}
			else if(y2 == -1 && square2Y - step > 0) {
				square2Y -= step;
			}
			
			repaint(square2X, square2Y, squareW + OFFSET, squareH + OFFSET);
		}
		
		public Dimension getPreferredSize() {
			return new Dimension(width, height);
		}
		
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			g.setColor(Color.RED);
			g.fillRect(squareX, squareY, squareW, squareH);
			g.setColor(Color.BLACK);
			g.drawRect(squareX, squareY, squareW, squareH);
			
			g.setColor(Color.BLUE);
			g.fillRect(square2X, square2Y, squareW, squareH);
			g.setColor(Color.BLACK);
			g.drawRect(square2X, square2Y, squareW, squareH);
			
			g.drawRect(width / 2, height / 2, squareW + 10, squareH + 10);
		}
	};
}
