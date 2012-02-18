package to.richard.misc.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.filechooser.FileFilter;

public class MiscFrame extends JFrame
{
	private static final String TITLE = "MISC Frame";
	private static final int FRAME_WIDTH = 500;
	private static final int FRAME_HEIGHT = 500;
	
	private String _filename;
	
	public MiscFrame()
	{
		setTitle(TITLE);
		setSize(FRAME_WIDTH, FRAME_HEIGHT);
		
		JMenuBar menuBar = new JMenuBar();
		
		JMenu fileMenu = new JMenu("File");
		menuBar.add(fileMenu);
		
		JMenuItem loadItem = new JMenuItem("Load");
		loadItem.addActionListener(new LoadAction());
		fileMenu.add(loadItem);
		
		JMenuItem runItem = new JMenuItem("Run");
		fileMenu.add(runItem);
		
		JMenuItem dumpItem = new JMenuItem("Dump");
		fileMenu.add(dumpItem);
		
		JMenuItem exitItem = new JMenuItem("Exit");
		exitItem.addActionListener(new ExitAction());
		fileMenu.add(exitItem);
		
		setJMenuBar(menuBar);
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public void setFilename(String filename)
	{
		_filename = filename;
		System.out.println(_filename);
	}
	
	public String getFilename()
	{
		return _filename;
	}
	
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
					return "Text files";
				}
			});
			MiscFrame.this.setFilename(fileChooser.getSelectedFile().getPath());
		}
	}
	
	class RunAction implements ActionListener
	{
		public void actionPerformed(ActionEvent event)
		{
			
		}
	}
	
	class DumpAction implements ActionListener
	{
		public void actionPerformed(ActionEvent event)
		{
			
		}
	}
	
	class ExitAction implements ActionListener
	{
		public void actionPerformed(ActionEvent event)
		{
			System.exit(0);
		}
	}	
}
