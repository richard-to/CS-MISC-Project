package to.richard.misc.gui;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.filechooser.FileFilter;

import scott.kirk.misc.MachineV1;
import scott.kirk.misc.OsystemV1;

public class MiscFrame extends JFrame
{
	private static final String TITLE = "MISC Frame";
	
	private static final int FRAME_WIDTH = 500;
	private static final int FRAME_HEIGHT = 500;
	
	private RegisterSetPanel _registerSet;
	private OsystemV1 _os;
	
	/**
	 * Constructor for MISC application
	 * 
	 * @param os
	 */
	public MiscFrame(OsystemV1 os)
	{
		_os = os;
		
		setTitle(TITLE);
		setSize(FRAME_WIDTH, FRAME_HEIGHT);
		
		setJMenuBar(new MiscMenu(
			new LoadAction(),
			new RunAction(),
			new DumpAction(),
			new ExitAction()));
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		_registerSet = new RegisterSetPanel();
		Container contentPane = getContentPane();
		contentPane.add(_registerSet);
	}
	
	/**
	 * Loads program into OsystemV1
	 */
	class LoadAction implements ActionListener
	{
		public void actionPerformed(ActionEvent event)
		{
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setCurrentDirectory(new File("."));
			fileChooser.showOpenDialog(MiscFrame.this);
			fileChooser.setAcceptAllFileFilterUsed(false);
			fileChooser.addChoosableFileFilter(new FileFilter(){
				
				private static final String ALLOWED_EXT = "txt";
				private static final String DESCRIPTION = "Text files";
				
				public boolean accept(File file){
					String filename = file.getName();
					int i = filename.lastIndexOf('.');
			        if (i > 0 &&  i < filename.length() - 1) {
			        	String ext = filename.substring(i+1).toLowerCase();
			        	if(ext.equals(ALLOWED_EXT)){
			        		return true;
			        	}
			        }							
					return false;
				}
				
				public String getDescription(){
					return DESCRIPTION;
				}
			});
			
			MiscFrame.this._os.loadProgramFile(fileChooser.getSelectedFile().getPath());
		}
	}
	
	/**
	 * Runs the loaded program
	 */
	class RunAction implements ActionListener
	{
		public void actionPerformed(ActionEvent event)
		{
			MiscFrame.this._os.runProgramFile();
			MachineV1 machine = MiscFrame.this._os.getMachine();
			_registerSet.updateRegisters(machine);
		}
	}
	
	/**
	 * Dumps memory contents to file
	 */
	class DumpAction implements ActionListener
	{
		public void actionPerformed(ActionEvent event)
		{
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setCurrentDirectory(new File("."));
			fileChooser.showSaveDialog(MiscFrame.this);
			String filename = fileChooser.getSelectedFile().getPath();
			MiscFrame.this._os.dumpMemoryContents(filename);
		}
	}
	
	/**
	 * Exits MISC GUI application
	 */
	class ExitAction implements ActionListener
	{
		public void actionPerformed(ActionEvent event)
		{
			System.exit(0);
		}
	}	
}
