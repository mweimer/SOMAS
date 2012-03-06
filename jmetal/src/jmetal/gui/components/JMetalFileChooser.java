package jmetal.gui.components;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JFrame;

public class JMetalFileChooser extends JFrame {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2015168568267285572L;
	private JFileChooser fileChooser_;
    /**
     * Returns the file chosen by the user.
     * 
     * @return The file chosen by the user.
     */
    public File chooseFile() {
        if (fileChooser_ == null) {
            fileChooser_ = new JFileChooser();
            fileChooser_.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        }
        if (JFileChooser.APPROVE_OPTION == fileChooser_.showOpenDialog(this)) {
            return fileChooser_.getSelectedFile();
        } else {
            return null;
        }
    }
    
} // JMetalFileChooser
