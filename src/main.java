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
	static Random random;
	static Calendar calendar;
	
	static int delay;
	static int delay_std = 200;
	static int delay_mean = 800;
	
	static int phase_length = 500;
	
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
		delay = delay_mean;
		
		System.out.println("Experiment with delay_mean = " + delay_mean + " and delay_std = " + delay_std);
		
		ActionListener delayListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				do {
					delay = (int) Math.round(random.nextGaussian() * delay_std + delay_mean);
				} while(delay < 0);
			}
		};
		Timer delayTimer = new Timer(phase_length, delayListener);
		delayTimer.start();
		
		ActionListener updateListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				myPanel.moveSquare();
			}	
		};
		Timer updateTimer = new Timer(1, updateListener);
		updateTimer.start();
	}

	static class MyPanel extends JPanel {
		private static final long serialVersionUID = 1L;
		private int squareX = 50;
		private int squareY = 50;
		private int squareH = 20;
		private int squareW = 20;
		
		private int height = 800;
		private int width = 1200;
		
		private int x = 0;
		private int y = 0;
		
		private int uDelay, dDelay, lDelay, rDelay;
		
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
					calendar = Calendar.getInstance();
					System.out.print(calendar.getTimeInMillis() + ": Press " + e.getKeyText(e.getKeyCode()));
					System.out.print(" object at (" + squareX + ", " + squareY + ")");					
					switch(e.getKeyCode()) {
					case KeyEvent.VK_W: // W
						uDelay = delay;
						System.out.println(" with delay " + uDelay + " ms");
						ActionListener upPressListener = new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent e) {
								// TODO Auto-generated method stub
								changeDirection(0, 0);
							}	
						};
						Timer upPressTimer = new Timer(uDelay, upPressListener);
						upPressTimer.setRepeats(false);
						upPressTimer.start();
						break;
					case KeyEvent.VK_S: // S
						dDelay = delay;
						System.out.println(" with delay " + dDelay + " ms");
						ActionListener downPressListener = new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent e) {
								// TODO Auto-generated method stub
								changeDirection(1, 0);
							}	
						};
						Timer downPressTimer = new Timer(dDelay, downPressListener);
						downPressTimer.setRepeats(false);
						downPressTimer.start();
						break;
					case KeyEvent.VK_A: // A
						lDelay = delay;
						System.out.println(" with delay " + lDelay + " ms");
						ActionListener leftPressListener = new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent e) {
								// TODO Auto-generated method stub
								changeDirection(2, 0);
							}	
						};
						Timer leftPressTimer = new Timer(lDelay, leftPressListener);
						leftPressTimer.setRepeats(false);
						leftPressTimer.start();
						break;
					case KeyEvent.VK_D: // D
						rDelay = delay;
						System.out.println(" with delay " + rDelay + " ms");
						ActionListener rightPressListener = new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent e) {
								// TODO Auto-generated method stub
								changeDirection(3, 0);
							}	
						};
						Timer rightPressTimer = new Timer(rDelay, rightPressListener);
						rightPressTimer.setRepeats(false);
						rightPressTimer.start();
						break;
					}
				}
	
				@Override
				public void keyReleased(KeyEvent e) {
					// TODO Auto-generated method stub
					calendar = Calendar.getInstance();
					System.out.print(calendar.getTimeInMillis() + ": Release " + e.getKeyText(e.getKeyCode()));
					System.out.print(" object at (" + squareX + ", " + squareY + ")");
					switch(e.getKeyCode()) {
					case KeyEvent.VK_W: // W
						System.out.println(" with delay " + uDelay + " ms");
						ActionListener upReleaseListener = new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent e) {
								// TODO Auto-generated method stub
								changeDirection(0, 1);
								calendar = Calendar.getInstance();
								System.out.println(calendar.getTimeInMillis() + ": Move object up to (" + squareX + ", " + squareY + ")");
							}	
						};
						Timer upReleaseTimer = new Timer(uDelay, upReleaseListener);
						upReleaseTimer.setRepeats(false);
						upReleaseTimer.start();
						break;
					case KeyEvent.VK_S: // S
						System.out.println(" with delay " + dDelay + " ms");
						ActionListener downReleaseListener = new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent e) {
								// TODO Auto-generated method stub
								changeDirection(1, 1);
								calendar = Calendar.getInstance();
								System.out.println(calendar.getTimeInMillis() + ": Move object down to (" + squareX + ", " + squareY + ")");
							}	
						};
						Timer downReleaseTimer = new Timer(dDelay, downReleaseListener);
						downReleaseTimer.setRepeats(false);
						downReleaseTimer.start();
						break;
					case KeyEvent.VK_A: // A
						System.out.println(" with delay " + lDelay + " ms");
						ActionListener leftReleaseListener = new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent e) {
								// TODO Auto-generated method stub
								changeDirection(2, 1);
								calendar = Calendar.getInstance();
								System.out.println(calendar.getTimeInMillis() + ": Move object left to (" + squareX + ", " + squareY + ")");
							}	
						};
						Timer leftReleaseTimer = new Timer(lDelay, leftReleaseListener);
						leftReleaseTimer.setRepeats(false);
						leftReleaseTimer.start();
						break;
					case KeyEvent.VK_D: // D
						System.out.println(" with delay " + rDelay + " ms");
						ActionListener rightReleaseListener = new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent e) {
								// TODO Auto-generated method stub
								changeDirection(3, 1);
								calendar = Calendar.getInstance();
								System.out.println(calendar.getTimeInMillis() + ": Move object right to (" + squareX + ", " + squareY + ")");
							}	
						};
						Timer rightReleaseTimer = new Timer(rDelay, rightReleaseListener);
						rightReleaseTimer.setRepeats(false);
						rightReleaseTimer.start();
						break;
					}
				}
			});
		}
		
		// direction: U - 0, D - 1, L - 2, R - 3
		// mode: Pressed - 0, Released - 1
		public void changeDirection(int direction, int mode) {
			if(mode == 0) {
				switch(direction) {
				case 0:
					y = -1;
					break;
				case 1:
					y = 1;
					break;
				case 2:
					x = -1;
					break;
				case 3:
					x = 1;
					break;
				}
			}
			else if(mode == 1) {
				switch(direction) {
				case 0:
				case 1:
					y = 0;
					break;
				case 2:
				case 3:
					x = 0;
					break;
				}
			}
			else {
				System.out.println("Error mode!");
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
			
			g.drawRect(width / 2, height / 2, squareW + 10, squareH + 10);
		}
	};
}
