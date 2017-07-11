package demoPack;

import java.util.Scanner;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.math.RoundingMode;
import javax.swing.*;


public class Demo2 extends TempAdjust implements ActionListener{
	 JTextField tft,tfb,tfl, tfr;  
	    JButton b1;  
	    int d = 3;
	    int count = 0;
	    
	    
	    Demo2(){
	    	//THIS IS ALL TEST! NOT IMPLEMENTED FULLY YET
			JPanel selectPanel = new JPanel();
			JPanel displayPanel = new JPanel();

			//Add various widgets to the sub panels.
			addWidgets();

			//Create the main panel to contain the two sub panels.
			
			JPanel mainPanel = new JPanel();
			mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));
			mainPanel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
			mainPanel.add(selectPanel);
			mainPanel.add(displayPanel);
			
			//THIS BIT IS WHERE IT ACTUALLY IMPLEMENTS CURRENTLY
			//Just displays the final temp grid in a new window
			
	    }
	    
	    
	    
	    
	    
	    
	    
	    
		public static void main(String[] args) {
		new Demo2();

			
			
		}
		
		//makes a frame with four text boxes and a button-need to make a non-static class
		//and basically clear all the stuff from the main method into it. Only way to
		//fix error message from eclipse (static/nonstatic issue)
		public void addWidgets() {
			// TODO Auto-generated method stub
			        JFrame f= new JFrame();  
			        tft=new JTextField();  
			        tft.setBounds(50,50,150,20);		    
			        tfb=new JTextField();  
			        tfb.setBounds(50,100,150,20);  
			        tfl=new JTextField();  
			        tfl.setBounds(50,150,150,20);  
			        tfr=new JTextField();  
			        tfr.setBounds(50,200,150,20); 
			        b1=new JButton("Run");  
			        b1.setBounds(50,250,150,50);
			        b1.addActionListener(this);
			        f.add(tft);
			        f.add(tfb);f.add(tfl);f.add(tfr);;f.add(b1);  
			        f.setSize(300,300);  
			        f.setLayout(null);  
			        f.setVisible(true); 
			      f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			    }         
			    
			    
		
	//sets the Strings for top, bot, left, and right to the text box values
		@Override
		public void actionPerformed(ActionEvent e) {
			String sTop, sBot, sLeft, sRight;

			if(!tft.getText().equals("")) {
				sTop = tft.getText();
			}
			else
				sTop = "0";
			if(!tfb.getText().equals("")) {
				sBot = tfb.getText();
			}
			else
				sBot = "0";
			if(!tfl.getText().equals("")) {
				sLeft = tfl.getText();
			}
			else
				sLeft = "0";	
			
			if(!tfr.getText().equals("")) {
				sRight = tfr.getText();
			}
			else
				sRight = "0";
				
				
				if(e.getSource()==b1){
					
					BigDecimal top = new BigDecimal(sTop);
					top = top.setScale(2, RoundingMode.HALF_UP);
					BigDecimal bot = new BigDecimal(sBot);
					bot = bot.setScale(2, RoundingMode.HALF_UP);
					BigDecimal left = new BigDecimal(sLeft);
					left = left.setScale(2, RoundingMode.HALF_UP);
					BigDecimal right = new BigDecimal(sRight);
					right = right.setScale(2, RoundingMode.HALF_UP);
					BigDecimal[][] oldPlate = new BigDecimal[d + 2][d + 2];
					BigDecimal[][] newPlate = new BigDecimal[d + 2][d + 2];
					// sets edge temperatures for plates
					oldPlate = initialize(oldPlate, top, bot, left, right);
					BigDecimal[][] finalPlate = new BigDecimal[d + 2][d + 2]; 
					//makes a final plate for display purposes
					finalPlate = initialize(newPlate, top, bot, left, right);
					
					finalPlate = iterator(oldPlate, newPlate, count);
					JFrame frame = new JFrame("Heat simulation");
					JPanel grid = new JPanel();
			        grid.setLayout(new GridLayout(d, d));
					for (int i = 1; i < d + 1; i++) {
						for (int j = 1; j < d + 1; j++) {
							grid.add(new JLabel(finalPlate[i][j] + "\t"));
						}
						frame.add(grid);
						 frame.pack();
					        frame.setVisible(true);
						
					}
		}
		}

		
	    

}
