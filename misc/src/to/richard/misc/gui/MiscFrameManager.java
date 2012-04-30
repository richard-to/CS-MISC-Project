package to.richard.misc.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import scott.kirk.misc.OsystemV1;

public class MiscFrameManager extends JFrame
{
	private static final String TITLE = "MISC Manager";	
	private static final int FRAME_WIDTH = 500;
	private static final int FRAME_HEIGHT = 500;
	
	/**
	 * Constructor for MISC Frame Manager
	 */
	public MiscFrameManager()
	{
		setTitle(TITLE);
		setSize(FRAME_WIDTH, FRAME_HEIGHT);
		
		JMenuBar menuBar = new JMenuBar();
		JMenu fileMenu = new JMenu("File");
		menuBar.add(fileMenu);
		
		JMenuItem newItem = new JMenuItem("New OS");
		newItem.addActionListener(new CreateAction());
		fileMenu.add(newItem);
		
		JMenuItem exitItem = new JMenuItem("Exit");
		exitItem.addActionListener(new ExitAction());
		fileMenu.add(exitItem);
		
		setJMenuBar(menuBar);
		setVisible(true);
	}
	
	/**
	 * Creates MISC GUI application
	 */
	class CreateAction implements ActionListener
	{
		public void actionPerformed(ActionEvent event)
		{
			OsystemV1 os = new OsystemV1();
			MiscFrame miscFrame = new MiscFrame(os);			
			miscFrame.setVisible(true);	
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
