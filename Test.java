import java.awt.*;
import java.awt.event.*;
import java.math.BigDecimal;
import java.math.RoundingMode;

import javax.swing.*;

import demoPack.TempAdjust;
 
@SuppressWarnings("serial")
public class Test extends TempAdjust implements ActionListener{
 
   // Private variables of the GUI components
   JTextField tField,bField,lField,rField,dField;
   JButton b1;
   JTextArea tArea;
   JFormattedTextField formattedField;
   
   JTextField tft,tfb,tfl, tfr;  
	 JLabel tLab, bLab, lLab, rLab, dLab; 
	    int d = 3;
	    int count = 0;
 
   /** Constructor to set up all the GUI components */
   public Test() {
      // JPanel for the text fields
      JPanel tfPanel = new JPanel(new GridLayout(6, 2, 10, 2));
      tfPanel.setBorder(BorderFactory.createTitledBorder("Input Plate Information: "));
 
      // Top temp (Row 1)
      tfPanel.add(new JLabel("  Top Temperature: "));
      tField = new JTextField(10);
      tfPanel.add(tField);
  
 
   // Bot temp (Row 2)
      tfPanel.add(new JLabel("  Bottom Temperature: "));
      bField = new JTextField(10);
      tfPanel.add(bField);
     
      
      // Left temp (Row 3)
      tfPanel.add(new JLabel("  Left Temperature: "));
      lField = new JTextField(10);
      tfPanel.add(lField);
  
      
      
      // Right temp (Row 4)
      tfPanel.add(new JLabel("  Right Temperature: "));
      rField = new JTextField(10);
      tfPanel.add(rField);
    
     
   // Dimensions (Row 5)
      tfPanel.add(new JLabel("  Dimension: "));
      dField = new JTextField(10);
      tfPanel.add(dField);
      
      b1=new JButton("Run");  
      tfPanel.add(b1);
      b1.addActionListener(new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e) {
        	  String sTop, sBot, sLeft, sRight;

  			if(!tField.getText().equals("")) {
  				sTop = tField.getText();
  			}
  			else
  				sTop = "0";
  			if(!bField.getText().equals("")) {
  				sBot = bField.getText();
  			}
  			else
  				sBot = "0";
  			if(!lField.getText().equals("")) {
  				sLeft = lField.getText();
  			}
  			else
  				sLeft = "0";	
  			
  			if(!rField.getText().equals("")) {
  				sRight = rField.getText();
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
          
       });
      
          
      // Setup the content-pane of JFrame in BorderLayout
      Container cp = this.getContentPane();
      cp.setLayout(new BorderLayout(5, 5));
      cp.add(tfPanel, BorderLayout.NORTH);
     // cp.add(tAreaScrollPane, BorderLayout.CENTER);
 
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      setTitle("JTextComponent Demo");
      setSize(350, 350);
      setVisible(true);
   }
 
   /** The entry main() method */
   public static void main(String[] args) {
      // Run GUI codes in Event-Dispatching thread for thread safety
      SwingUtilities.invokeLater(new Runnable() {
         @Override
         public void run() {
            new Test();  // Let the constructor do the job
         }
      });
   }

@Override
public void actionPerformed(ActionEvent e) {
	// TODO Auto-generated method stub
	
}
}
