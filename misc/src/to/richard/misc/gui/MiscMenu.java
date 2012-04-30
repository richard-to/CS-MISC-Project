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
	 * @param loadStateAction
	 * @param saveStateAction
	 * @param loadAction
	 * @param runAction
	 * @param dumpAction
	 * @param exitAction
	 */
	public MiscMenu(
		ActionListener loadStateAction,
		ActionListener saveStateAction,
		ActionListener loadAction, 
		ActionListener runAction, 
		ActionListener dumpAction,
		ActionListener exitAction)
	{
		JMenu fileMenu = new JMenu("File");
		this.add(fileMenu);
		
		JMenuItem loadStateItem = new JMenuItem("Load State");
		loadStateItem.addActionListener(loadStateAction);
		fileMenu.add(loadStateItem);
		
		JMenuItem saveStateItem = new JMenuItem("Save State");
		saveStateItem.addActionListener(saveStateAction);
		fileMenu.add(saveStateItem);
		
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
