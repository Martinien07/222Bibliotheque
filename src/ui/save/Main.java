package ui;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class Main {
	
	
	 public static void main(String[] args) {
	        try {
	            // Réinitialisation forcée du Look & Feel
	            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
	        } catch (Exception e) {
	            e.printStackTrace();
	        }

	        SwingUtilities.invokeLater(() -> {
	            new MainInterface().setVisible(true);
	        });
	    }

}
