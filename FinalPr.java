// Mingyu(Miranda) Liu
// Final Project
// June 12, 2018
// ICS3U1 Ms. Strelkvoska

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class FinalPr extends JFrame {
	
	private Container c;
	private MyPanel mainPanel = new MyPanel();
	private JPanel southPanel = new JPanel();
	private JButton btn1 = new JButton("New Game");      // left label on action buttons
	private JButton btn2 = new JButton("How To Play");   // middle label on action buttons
	private JButton btn3 = new JButton("Exit");          // right label on action buttons

	public FinalPr() {          // This section is from Ms.S's template (start)
		super("Bomb It");

		southPanel.setLayout(new GridLayout(1,3));

		c = getContentPane();
		c.setLayout(new BorderLayout());

		// create and add buttons 
		btn1.addActionListener(mainPanel);
		btn1.addKeyListener(mainPanel);
		southPanel.add(btn1);
		btn2.addActionListener(mainPanel);
		btn2.addKeyListener(mainPanel);
		southPanel.add(btn2);
		btn3.addActionListener(mainPanel);
		btn3.addKeyListener(mainPanel);
		southPanel.add(btn3);
		addKeyListener(mainPanel);

		c.add(southPanel, BorderLayout.SOUTH);
		c.add(mainPanel, BorderLayout.CENTER);
		setSize(680, 850);    // size of the window
		setVisible(true);
	}                            

	public static void main( String args[] ) {
		FinalPr app = new FinalPr();

		app.addWindowListener(
				new WindowAdapter() {
					public void windowClosing( WindowEvent e )
					{
						System.exit( 0 );
					}
				}
				);
	}                          
}                             // This section is from Ms.S's template (end)

class MyPanel extends JPanel implements ActionListener, KeyListener {
	
	// variables
	int p1X = 0, p1Y = 0;       // coordinates of the two players, p1 and p2
	int p2X = 0, p2Y = 0;
	int num = 17;
	int map[][] = new int[num][num];
	int timer = 0, timer2 = 0;
	int bomb1X = 1000, bomb1Y = 1000;
	int bomb2X = 1000, bomb2Y = 1000;
	int numStar1 = 0, numStar2 = 0;    // number of stars already collected by players
	boolean ifBomb1 = false, ifBomb2 = false;
	int cnt = 0;
	int bombTime1 = 0;
	int bombTime2 = 0;

	Image gif = Toolkit.getDefaultToolkit().createImage("/Users/mingyuliu/eclipse-workspace/ICS3U - Final Project/Explosion1.gif");  
	Image star = Toolkit.getDefaultToolkit().createImage("/Users/mingyuliu/eclipse-workspace/ICS3U - Final Project/Star.gif");    

	ImageIcon pic1 = new ImageIcon("/Users/mingyuliu/eclipse-workspace/ICS3U - Final Project/cupcake.png");
	ImageIcon pic2a = new ImageIcon("/Users/mingyuliu/eclipse-workspace/ICS3U - Final Project/lollipop1.png");   
	ImageIcon pic2b = new ImageIcon("/Users/mingyuliu/eclipse-workspace/ICS3U - Final Project/lollipop2.png");
	ImageIcon pic2c = new ImageIcon("/Users/mingyuliu/eclipse-workspace/ICS3U - Final Project/lollipop3.png");
	ImageIcon p1 = new ImageIcon("/Users/mingyuliu/eclipse-workspace/ICS3U - Final Project/p1.png");         
	ImageIcon p2 = new ImageIcon("/Users/mingyuliu/eclipse-workspace/ICS3U - Final Project/p2.png");      
	private JLabel lbl[][] = new JLabel[num][num];   

	int x;
	private Timer myTimer= new Timer(60, this);

	public MyPanel() { 			// initial all the variables

		// Constructor: set background color to white set up listeners to respond to mouse actions
		createMap();

		setLayout(new GridLayout(lbl.length, lbl[0].length));  
		for(int i = 0; i<lbl.length; i++) {
			for(int j = 0; j<lbl[0].length; j++) {
				lbl[i][j] = new JLabel();                    
				add(lbl[i][j]);
			}  
		}
		printMap();		 
		addKeyListener(this);	
		x = 20;	
		myTimer.start();
	}	

	public void createMap() {                            // This method generates a random map and fills the array with 0-5.
		for(int row = 0; row<map.length; row++) {        // 0-Empty, 1-Fixed Walls, 2-Walls that can be destroyed 
			for(int col = 0; col<map[0].length; col++) { // 3-Player 1, 4-Player 2, 5-Stars to be collected
				if(row%2==1 && col%2==1)
					map[row][col] = 1;
				else if((row+col)==1 || (row+col)==(2*num-3))
					map[row][col] = 0;                        
				else if(row+col==0){                          
					map[row][col] = 3;
					p1X = col;
					p1Y = row;
				}   
				else if(row+col==2*(num-1)) {                         
					map[row][col] = 4;
					p2X = col;
					p2Y = row;
				}
				else{
					int n = (int)(Math.random()*3);    // For the rest, about 1/3 will be 0, about 2/3 will be 2
					if(n==0)
						map[row][col] = 0;
					else
						map[row][col] = 2;
				}
			}
		}	
		for(int row = 0; row<map.length; row++)
			for(int col = 0; col<map[0].length; col++)
				if(map[row][col]==0){
					int n = (int)(Math.random()*5);
					if(n==0)
						map[row][col] = 5;            // About 1/5 of the 0(empty) squares will be 5
				}	
	}

	// The method draws the game board according to the map array.
	public void drawMap(Graphics gr) {
		if(timer>0)
			timer--;
		if(timer2>0)
			timer2--;
		for(int i = 0; i<lbl.length; i++) 
			for(int j = 0; j<lbl[0].length; j++) 
				if(map[i][j]==1)
					gr.drawImage(pic1.getImage(),j*40, i*40, null);
				else if(map[i][j]==2) {
					int abc = (int)(Math.random()*3);
					if(abc==0)        // create a random number every time when it paints to make it "blink"
						gr.drawImage(pic2a.getImage(),j*40, i*40, null);
					else if(abc==1)
						gr.drawImage(pic2b.getImage(),j*40, i*40, null);
					else
						gr.drawImage(pic2c.getImage(),j*40, i*40, null);
				}
				else if(map[i][j]==5)
					gr.drawImage(star,j*40, i*40, null);
				else if(map[i][j]==100 && timer !=0) {  // When a bomb is placed and it hasn't exploded
					gr.setColor(Color.black);
					gr.fillOval(j*40+5,i*40+5, 30, 30); // Draw a circle to represent the bomb
					gr.setColor(Color.white);
				}
				else if(map[i][j]==100 && timer ==0) {  // When time's up and the bomb explodes
					bomb1X = j;      // store the coordinates of the bomb because they'll be used in painting
					bomb1Y = i;
					ifBomb1 = true;  // true means the bomb explodes (when I need the explosion gif)
					bombTime1 = 10;
					explosion(i,j);
				}

				else if(map[i][j]==200 && timer2 !=0) {
					gr.setColor(Color.black);
					gr.fillOval(j*40+5,i*40+5, 30, 30 );
					gr.setColor(Color.white);
				}
				else if(map[i][j]==200 && timer2 ==0) { 
					bomb2X = j;
					bomb2Y = i;
					ifBomb2 = true;
					bombTime2 = 10;
					explosion(i,j);
				}
	}

	// The method clears the objects around a bomb when it explodes
	// and change the map accordingly, or end the game. 
	public void explosion(int i, int j) {
		int temp;
		if(i>0 && (map[i-1][j]==0 || map[i-1][j]==2 || map[i-1][j]==3 || map[i-1][j]==4)) { // the square above the bomb
			temp = map[i-1][j];
			map[i-1][j] = 0;
			if(temp==3)
				p1X = 2000;
			else if(temp==4)
				p2X = 2000;
			if(cnt>6 && i>1 && temp==0 && (map[i-2][j]==2 || map[i-2][j]==3 || map[i-2][j]==4)) {
				temp = map[i-2][j];       // after 8 bombs have been placed, the power of the bombs after will be doubled
				map[i-2][j] = 0;
				if(temp==3)
					p1X = 2000;
				else if(temp==4)
					p2X = 2000;
			}
		}  
		if(i<num-1 && (map[i+1][j]==0 || map[i+1][j]==2 || map[i+1][j]==3 || map[i+1][j]==4)) { // the square below the bomb
			temp = map[i+1][j];
			map[i+1][j] = 0;
			if(temp==3)
				p1X = 2000;
			else if(temp==4)
				p2X = 2000;
			if(cnt>6 && i<num-2 && temp==0 && (map[i+2][j]==2 || map[i+2][j]==3 || map[i+2][j]==4)){
				temp = map[i+2][j];
				map[i+2][j] = 0;
				if(temp==3)
					p1X = 2000;
				else if(temp==4)
					p2X = 2000;
			}
		}  
		if(j>0 && (map[i][j-1]==0 || map[i][j-1]==2 || map[i][j-1]==3 || map[i][j-1]==4)) { // the square on the left
			temp = map[i][j-1];
			map[i][j-1] = 0;
			if(temp==3)
				p1X = 2000;
			else if(temp==4)
				p2X = 2000;
			if(cnt>6 && j>1 && temp==0 && (map[i][j-2]==2 || map[i][j-2]==3 || map[i][j-2]==4)){
				temp = map[i][j-2];
				map[i][j-2] = 0;
				if(temp==3)
					p1X = 2000;
				else if(temp==4)
					p2X = 2000;
			}
		}  
		if(j<num-1 && (map[i][j+1]==0 || map[i][j+1]==2 || map[i][j+1]==3 || map[i][j+1]==4)) { // the square on the right
			temp = map[i][j+1];
			map[i][j+1] = 0;
			if(temp==3)
				p1X = 2000;
			else if(temp==4)
				p2X = 2000;
			if(cnt>6 && j<num-2 && temp==0 && (map[i][j+2]==2 || map[i][j+2]==3 || map[i][j+2]==4)){
				temp = map[i][j+2];
				map[i][j+2] = 0;
				if(temp==3)
					p1X = 2000;
				else if(temp==4)
					p2X = 2000;
			}
		}

		map[i][j] = 0;  // Where the bomb used to be will always be 0 after it explodes
		cnt++;          // Count how many bombs have been used

		String output = "";               // Game will be over if any of the players is killed by the bomb or collects 6 stars
		if(p1X==2000 || numStar2==6)
			output = "Sponge Bob Wins!\n\nDo you want to start a new game?";
		else if(p2X==2000 || numStar1==6)
			output = "Patrick Star Wins!\n\nDo you want to start a new game?";

		if(p1X==2000 || p2X==2000 || numStar2==6 || numStar1==6) {
			int opt = JOptionPane.showConfirmDialog( null, output, "Game Is Over!", JOptionPane.YES_NO_OPTION );
			if(opt==JOptionPane.NO_OPTION) 
				System.exit(0);   // exit the game
			else{
				timer=0;          // reinitialize the variables to restart the game
				timer2=0;
				bomb1X = 1000; bomb1Y = 1000;
				bomb2X = 1000; bomb2Y = 1000;
				ifBomb1 = false; ifBomb2 = false;
				numStar1 = 0; 
				numStar2 = 0;
				cnt = 0;
				createMap();
			}
		}

	}

	public void printMap() {        // Print the map in numbers
		for (int i = 0; i < map.length; i++ ) {
			for (int j = 0; j < map[0].length; j++ ) {
				System.out.print("  "+map[i][j]);               
			} 
			System.out.println();
		} 
	}

	public void keyPressed( KeyEvent e ) {
		System.out.println(e.getKeyCode());
		if(p1X != 2000){
			if(e.getKeyCode()==86 && timer==0) {     // V place the bomb
				map[p1Y][p1X] = 100;
				timer = 40;
			} 
			if(e.getKeyCode()==65 && p1X>0) {        // A left
				p1X-=1;                             
				if(map[p1Y][p1X] == 0 || map[p1Y][p1X] == 3 || map[p1Y][p1X] == 4 || map[p1Y][p1X] == 5){
					if(map[p1Y][p1X] == 5)
						numStar1++;
					map[p1Y][p1X] = 3;
					if(map[p1Y][p1X+1] == 100)
						map[p1Y][p1X+1] = 100;
					else
						map[p1Y][p1X+1] = 0;
				}
				else
					p1X+=1;   
			} 
			if(e.getKeyCode()==68 && p1X<num-1) {    // D right 
				p1X+=1;                             
				if(map[p1Y][p1X] == 0 || map[p1Y][p1X] == 3 || map[p1Y][p1X] == 4 || map[p1Y][p1X] == 5){
					if(map[p1Y][p1X] == 5)
						numStar1++;
					map[p1Y][p1X] = 3;
					if(map[p1Y][p1X-1] == 100)
						map[p1Y][p1X-1] = 100;
					else
						map[p1Y][p1X-1] = 0;
				}
				else
					p1X-=1;  
			} 
			if(e.getKeyCode()==87 && p1Y>0) {        // W up
				p1Y-=1;
				if(map[p1Y][p1X] == 0 || map[p1Y][p1X] == 3 || map[p1Y][p1X] == 4 || map[p1Y][p1X] == 5){
					if(map[p1Y][p1X] == 5)
						numStar1++;
					map[p1Y][p1X] = 3;
					if(map[p1Y+1][p1X] == 100)
						map[p1Y+1][p1X] = 100;
					else
						map[p1Y+1][p1X] = 0;
				}
				else
					p1Y+=1;
			} 
			if(e.getKeyCode()==83 && p1Y<num-1) {    // S down
				p1Y+=1;
				if(map[p1Y][p1X] == 0 || map[p1Y][p1X] == 3 || map[p1Y][p1X] == 4 || map[p1Y][p1X] == 5){
					if(map[p1Y][p1X] == 5)
						numStar1++;
					map[p1Y][p1X] = 3;
					if(map[p1Y-1][p1X] == 100)
						map[p1Y-1][p1X] = 100;
					else
						map[p1Y-1][p1X] = 0;
				}
				else
					p1Y-=1;
			} 

		}

		if(p2X != 5000){
			if(e.getKeyCode()==10 && timer2==0) {    // ENTER place the bomb
				map[p2Y][p2X] = 200;
				timer2 = 40;
			} 
			if(e.getKeyCode()==37 && p2X>0){        // left
				p2X-=1;                             
				if(map[p2Y][p2X] == 0 || map[p2Y][p2X] == 3 || map[p2Y][p2X] == 4 || map[p2Y][p2X] == 5){
					if(map[p2Y][p2X] == 5)
						numStar2++;
					map[p2Y][p2X] = 4;
					if(map[p2Y][p2X+1] == 200)
						map[p2Y][p2X+1] = 200;
					else
						map[p2Y][p2X+1] = 0;
				}
				else
					p2X+=1;   
			} 
			if(e.getKeyCode()==39 && p2X<num-1) {   // right
				p2X+=1;
				if(map[p2Y][p2X] == 0 || map[p2Y][p2X] == 3 || map[p2Y][p2X] == 4 || map[p2Y][p2X] == 5){
					if(map[p2Y][p2X] == 5)
						numStar2++;
					map[p2Y][p2X] = 4;
					if(map[p2Y][p2X-1] == 200)
						map[p2Y][p2X-1] = 200;
					else
						map[p2Y][p2X-1] = 0;
				}
				else
					p2X-=1;
			} 
			if(e.getKeyCode()==38 && p2Y>0) {      // up
				p2Y-=1;
				if(map[p2Y][p2X] == 0 || map[p2Y][p2X] == 3 || map[p2Y][p2X] == 4 || map[p2Y][p2X] == 5){
					if(map[p2Y][p2X] == 5)
						numStar2++;
					map[p2Y][p2X] = 4;
					if(map[p2Y+1][p2X] == 200)
						map[p2Y+1][p2X] = 200;
					else
						map[p2Y+1][p2X] = 0;
				}
				else
					p2Y+=1;
			} 
			if(e.getKeyCode()==40 && p2Y<num-1) {  // down
				p2Y+=1;
				if(map[p2Y][p2X] == 0 || map[p2Y][p2X] == 3 || map[p2Y][p2X] == 4 || map[p2Y][p2X] == 5){
					if(map[p2Y][p2X] == 5)
						numStar2++;
					map[p2Y][p2X] = 4;
					if(map[p2Y-1][p2X] == 200)
						map[p2Y-1][p2X] = 200;
					else
						map[p2Y-1][p2X] = 0;
				}
				else
					p2Y-=1;
			} 
		}
		printMap();
		repaint();
	}	

	public void keyReleased(KeyEvent e) {}
	public void keyTyped(KeyEvent e) {}

	public void actionPerformed(ActionEvent e) {  
		// timer events
		if(e.getSource()==myTimer){
			if(bombTime1>0)    // When a bomb is placed, the bombTime1 will be set to 10.
				bombTime1--;   // bombTime1 will decrease and while it's greater than 0, ifBomb1 will be true
			if(bombTime1==0)   // which means the explosion animation will be displayed
				ifBomb1 = false;	 
			if(bombTime2>0)
				bombTime2--;
			if(bombTime2==0)
				ifBomb2 = false;	
			repaint();		
		}
		else {
			JButton b = (JButton)e.getSource();	   
			if(b.getText()=="New Game") {
				// code for button on the left (start a new game)
				int restart = JOptionPane.showConfirmDialog(null,"Are you sure to start a new game?", "New Game", JOptionPane.YES_NO_OPTION);
				if (restart == JOptionPane.YES_OPTION) {
					timer=0; timer2=0;
					bomb1X = 1000; bomb1Y = 1000;
					bomb2X = 1000; bomb2Y = 1000;
					ifBomb1 = false; ifBomb2 = false;
					numStar1 = 0; numStar2 = 0;
					cnt = 0;
					createMap();
				}
			}
			else if(b.getText()=="How To Play") {
				// code for button in the middle(instructions)
				JOptionPane.showMessageDialog(null, 
						"Place bombs to destroy the walls!\n\nBe the first to collect 6 stars or kill the other player to win the game!\n\nPlayer 1: Patrick Star\nUse W S A D to move, V to palce the bomb\n\nPlayer 2: Sponge Bob\nUse four direction keys to move, ENTER to place the bomb", 
						"Instructions", JOptionPane.INFORMATION_MESSAGE );
			}
			else if (b.getText()=="Exit") {
				// code for button on the right(exit)
				int exit = JOptionPane.showConfirmDialog(null,"Are you sure to exit?", "Exit", JOptionPane.YES_NO_OPTION);
				if (exit == JOptionPane.YES_OPTION) 
					System.exit(0);
			}
		}	   
	} // end actionPerformed

	public void paintComponent(Graphics gr) {  // painting
		super.paintComponent(gr);

		gr.setColor(Color.white);
		gr.fillRect(0,0,855,905);   // background(white)

		if(ifBomb1) { // bombing animation
			if(bomb1X>0 && map[bomb1Y][bomb1X-1]!=1) {
				gr.drawImage(gif,bomb1X*40-40, bomb1Y*40, null);
				if(cnt>8 && bomb1X>2 && map[bomb1Y][bomb1X-1]==0 && map[bomb1Y][bomb1X-2]!=1)
					gr.drawImage(gif,bomb1X*40-80, bomb1Y*40, null);
			}
			if(bomb1Y>0 && map[bomb1Y-1][bomb1X]!=1) {
				gr.drawImage(gif,bomb1X*40, bomb1Y*40-40, null);
				if(cnt>8 && bomb1Y>2 && map[bomb1Y-1][bomb1X]==0 && map[bomb1Y-2][bomb1X]!=1)
					gr.drawImage(gif,bomb1X*40, bomb1Y*40-80, null);
			}
			if(bomb1X<num-1 && map[bomb1Y][bomb1X+1]!=1) {
				gr.drawImage(gif,bomb1X*40+40, bomb1Y*40, null);
				if(cnt>8 && bomb1X<num-2 && map[bomb1Y][bomb1X+1]==0 && map[bomb1Y][bomb1X+2]!=1)
					gr.drawImage(gif,bomb1X*40+80, bomb1Y*40, null);
			}
			if(bomb1Y<num-1 && map[bomb1Y+1][bomb1X]!=1) {
				gr.drawImage(gif,bomb1X*40, bomb1Y*40+40, null);
				if(cnt>8 && bomb1Y<num-2 && map[bomb1Y+1][bomb1X]==0 && map[bomb1Y+2][bomb1X]!=1)
					gr.drawImage(gif,bomb1X*40, bomb1Y*40+80, null);
			}
		} 

		if(ifBomb2) {
			if(bomb2X>0 && map[bomb2Y][bomb2X-1]!=1) {
				gr.drawImage(gif,bomb2X*40-40, bomb2Y*40, null);
				if(cnt>8 && bomb2X>2 && map[bomb2Y][bomb2X-1]==0 && map[bomb2Y][bomb2X-2]!=1)
					gr.drawImage(gif,bomb2X*40-80, bomb2Y*40, null);
			}
			if(bomb2Y>0 && map[bomb2Y-1][bomb2X]!=1) {
				gr.drawImage(gif,bomb2X*40, bomb2Y*40-40, null);
				if(cnt>8 && bomb2Y>2 && map[bomb2Y-1][bomb2X]==0 && map[bomb2Y-2][bomb2X]!=1)
					gr.drawImage(gif,bomb2X*40, bomb2Y*40-80, null);
			}
			if(bomb2X<num-1 && map[bomb2Y][bomb2X+1]!=1) {
				gr.drawImage(gif,bomb2X*40+40, bomb2Y*40, null);
				if(cnt>8 && bomb2X<(num-2) && map[bomb2Y][bomb2X+1]==0 && map[bomb2Y][bomb2X+2]!=1)
					gr.drawImage(gif,bomb2X*40+80, bomb2Y*40, null);
			}
			if(bomb2Y<num-1 && map[bomb2Y+1][bomb2X]!=1) {
				gr.drawImage(gif,bomb2X*40, bomb2Y*40+40, null);
				if(cnt>8 && bomb2Y<(num-2) && map[bomb2Y+1][bomb2X]==0 && map[bomb2Y+2][bomb2X]!=1)
					gr.drawImage(gif,bomb2X*40, bomb2Y*40+80, null);
			}
		}

		drawMap(gr);

		if(p1X!=2000) // draw the two players while they're still alive
			gr.drawImage(p1.getImage(),p1X*40, p1Y*40, null);
		if(p2X!=5000)
			gr.drawImage(p2.getImage(),p2X*40, p2Y*40, null);

		gr.setColor(Color.pink);  
		gr.setFont(new Font("TimesNewRoman", Font.BOLD, 15));
		gr.drawString("Stars collected by Patrick Star: " + numStar1, 40,720);
		gr.drawString("Stars collected by SpongeBob: " + numStar2, 400,720);
	}
}


