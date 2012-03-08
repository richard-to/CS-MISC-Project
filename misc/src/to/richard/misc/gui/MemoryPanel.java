package to.richard.misc.gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import scott.kirk.misc.MachineV1;

public class MemoryPanel extends JPanel
{
	private static final int TEXTAREA_COLS = 36;
	private static final int TEXTAREA_ROWS = 8;
	
	private JTextArea _textArea;
	
	public MemoryPanel()
	{
		setLayout(new GridLayout(1, 1));		
		_textArea = new JTextArea();
		JScrollPane scrollPane = new JScrollPane(_textArea);
		_textArea.setEditable(false);
		add(scrollPane);
	}
	
	/**
	 * Displays memory in textarea
	 * 
	 * @param machine
	 */
	public void updateMemory(MachineV1 machine)
	{
		_textArea.setText(machine.getMemory());
	}
}
