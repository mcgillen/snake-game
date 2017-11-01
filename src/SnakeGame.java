import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Queue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

public class SnakeGame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	Deque<Point> snakeBody;
	Queue<Integer> pressedKeyQueue;
	
	GamePanel gameBoard;
	//int key = KeyEvent.VK_UP;
	int lastKey = KeyEvent.VK_UP;

	public SnakeGame() {
		super("Snake Game");
		snakeBody = new ArrayDeque<Point>();
		pressedKeyQueue= new LinkedList<Integer>();
		setSize(590, 615);
		setResizable(false);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		gameBoard = new GamePanel();
		add(gameBoard);
		snakeBody.add(new Point(5, 5));
		gameBoard.updateGameMap(snakeBody.peek());
		

		validate();

	}

	public void display() {
		setVisible(true);
	}

	public void setKey(int k) {
		pressedKeyQueue.add(k);
		//key = k;
	}

	public int getKey() {	
		if (!(pressedKeyQueue.isEmpty())) {
			lastKey=pressedKeyQueue.peek();
			return pressedKeyQueue.peek();
		}
		else return lastKey;
		
		//return key;
	}
	
	public void removeKey() {
		if (!(pressedKeyQueue.isEmpty())) pressedKeyQueue.remove();
	}

	public void runGame() {

		this.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyPressed(KeyEvent e) {
					setKey(e.getKeyCode());

				

			}
		});

		int delay = 250; // milliseconds
		ActionListener taskPerformer = new ActionListener() {
			int foodCounter = 0;

			public void actionPerformed(ActionEvent evt) {
				Point oldPoint = new Point(snakeBody.peek().x, snakeBody.peek().y);
				Point newPoint = new Point();
				if ((oldPoint.y == 11 && getKey() == KeyEvent.VK_UP) | (oldPoint.y == 0 && getKey() == KeyEvent.VK_DOWN)
						| (oldPoint.x == 11 && getKey() == KeyEvent.VK_RIGHT)
						| (oldPoint.x == 0 && getKey() == KeyEvent.VK_LEFT)) {
					JOptionPane.showMessageDialog(null, "You lose!\nFinal Score: " + snakeBody.size(), "Game Over", JOptionPane.ERROR_MESSAGE);
					System.exit(0);
				}

				switch (getKey()) {
				case KeyEvent.VK_UP:
					newPoint.setLocation(oldPoint.getX(), oldPoint.getY() + 1);
					break;
				case KeyEvent.VK_DOWN:
					newPoint.setLocation(oldPoint.getX(), oldPoint.getY() - 1);
					break;
				case KeyEvent.VK_LEFT:
					newPoint.setLocation(oldPoint.getX() - 1, oldPoint.getY());
					break;
				case KeyEvent.VK_RIGHT:
					newPoint.setLocation(oldPoint.getX() + 1, oldPoint.getY());
					break;
				}
				removeKey();
				
				Boolean result=gameBoard.updateGameMap(newPoint, snakeBody.peekLast(), snakeBody.size());
				if (result==false) {			
					snakeBody.removeLast();
					snakeBody.addFirst(newPoint);
				}
				if (result==true) {
					snakeBody.addFirst(newPoint);
				}
				

				foodCounter++;
				if (foodCounter == 5) {
					foodCounter = 0;
					gameBoard.addFood(snakeBody.size());
				}

				gameBoard.repaint();
			}
		};

		Timer t = new Timer(delay, taskPerformer);
		t.start();

	}

	public static void main(String[] args) {
		SnakeGame sg = new SnakeGame();
		sg.display();
		sg.runGame();
	}

}

class GamePanel extends JPanel {

	int[][] gameMap = new int[12][12];

	public GamePanel() {
		super();
		for (int i = 0; i < gameMap.length; i++) {
			for (int j = 0; j < gameMap[i].length; j++) {
				gameMap[i][j] = 0;
			}
		}
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		for (int i = 0; i < gameMap.length; i++) {
			for (int j = 0; j < gameMap[i].length; j++) {
				g.setColor(new Color(68, 155, 46));
				if (gameMap[i][j] == 1)
					g.fillRect((i) * 50, 500 - (j - 1) * 50, 40, 40);
				g.setColor(new Color(181, 50, 50));
				if (gameMap[i][j] == 2)
					g.fillOval((i) * 50, 500 - (j - 1) * 50, 40, 40);

			}
		}

	}

	public boolean updateGameMap(Point newPoint, Point oldPoint, int bodySize) {
		int newSpaceValue = gameMap[newPoint.x][newPoint.y];

		switch (newSpaceValue) {
		case 0:
			gameMap[newPoint.x][newPoint.y] = 1;
			gameMap[oldPoint.x][oldPoint.y] = 0;
			return false;
		case 1:
			JOptionPane.showMessageDialog(null, "You lose!\nFinal Score: " + bodySize, "Game Over", JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		case 2:
			updateGameMap(newPoint);
			return true;
		}
		return false;	

	}

	public void updateGameMap(Point newPoint) {

		gameMap[newPoint.x][newPoint.y] = 1;

	}

	public void addFood(int bodyLength) {
		int availableSpaces = 144 - bodyLength;
		int targetSpace = (int) (Math.random() * availableSpaces);
		int yValue = targetSpace % 12;
		int xValue = targetSpace / 12;
		if (gameMap[xValue][yValue] != 1)
			gameMap[xValue][yValue] = 2;
		else
			addFood(bodyLength);
	}

}