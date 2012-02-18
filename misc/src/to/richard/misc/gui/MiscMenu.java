package to.richard.misc.gui;

import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class MiscMenu extends JMenuBar
{
	/**
	 * MISC Menu constructor
	 * 
	 * @param loadAction
	 * @param runAction
	 * @param dumpAction
	 * @param exitAction
	 */
	public MiscMenu(
		ActionListener loadAction, 
		ActionListener runAction, 
		ActionListener dumpAction,
		ActionListener exitAction)
	{
		JMenu fileMenu = new JMenu("File");
		this.add(fileMenu);
		
		JMenuItem loadItem = new JMenuItem("Load");
		loadItem.addActionListener(loadAction);
		fileMenu.add(loadItem);
		
		JMenuItem runItem = new JMenuItem("Run");
		runItem.addActionListener(runAction);
		fileMenu.add(runItem);
		
		JMenuItem dumpItem = new JMenuItem("Dump");
		dumpItem.addActionListener(dumpAction);
		fileMenu.add(dumpItem);
		
		JMenuItem exitItem = new JMenuItem("Exit");
		exitItem.addActionListener(exitAction);
		fileMenu.add(exitItem);		
	}
}
