package to.richard.misc.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.filechooser.FileFilter;

import scott.kirk.misc.OsystemV1;

public class MiscFrame extends JFrame
{
	private static final String TITLE = "MISC Frame";
	private static final String DUMP_FILENAME = "dump.txt";
	
	private static final int FRAME_WIDTH = 500;
	private static final int FRAME_HEIGHT = 500;
	
	private OsystemV1 _os;
	
	public MiscFrame(OsystemV1 os)
	{
		_os = os;
		
		setTitle(TITLE);
		setSize(FRAME_WIDTH, FRAME_HEIGHT);
		
		JMenuBar menuBar = new JMenuBar();
		
		JMenu fileMenu = new JMenu("File");
		menuBar.add(fileMenu);
		
		JMenuItem loadItem = new JMenuItem("Load");
		loadItem.addActionListener(new LoadAction());
		fileMenu.add(loadItem);
		
		JMenuItem runItem = new JMenuItem("Run");
		runItem.addActionListener(new RunAction());
		fileMenu.add(runItem);
		
		JMenuItem dumpItem = new JMenuItem("Dump");
		dumpItem.addActionListener(new DumpAction());
		fileMenu.add(dumpItem);
		
		JMenuItem exitItem = new JMenuItem("Exit");
		exitItem.addActionListener(new ExitAction());
		fileMenu.add(exitItem);
		
		setJMenuBar(menuBar);
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
			
			MiscFrame.this._os.loadProgramFile(fileChooser.getSelectedFile().getPath());
		}
	}
	
	class RunAction implements ActionListener
	{
		public void actionPerformed(ActionEvent event)
		{
			MiscFrame.this._os.runProgramFile();
		}
	}
	
	class DumpAction implements ActionListener
	{
		public void actionPerformed(ActionEvent event)
		{
			MiscFrame.this._os.dumpMemoryContents(DUMP_FILENAME);
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
