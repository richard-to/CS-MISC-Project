package to.richard.misc.gui;

import java.awt.GridLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.text.BadLocationException;

import scott.kirk.misc.MachineByteV1;
import scott.kirk.misc.MachineV1;
import scott.kirk.misc.MachineWordV1;

public class MemoryPanel extends JPanel
{
	private static final int TEXTAREA_COLS = 36;
	private static final int TEXTAREA_ROWS = 8;
	private static final int BITS_PER_LINE = MachineV1.bitsinbyte * MachineV1.bytesinword;
	private JTextArea _textArea;
	
	private MachineV1 _machine;
	
	public MemoryPanel(MachineV1 machine)
	{
		_machine = machine;
		setLayout(new GridLayout(1, 1));		
		_textArea = new JTextArea();
		JScrollPane scrollPane = new JScrollPane(_textArea);
		add(scrollPane);
	}
	
	/**
	 * Displays memory in textarea
	 */
	public void updateMemory()
	{
		int position = _machine.getMemOffset() * (BITS_PER_LINE + 1);
		int positionEnd = position + BITS_PER_LINE;
		
		_textArea.setText(_machine.getMemory());
		_textArea.requestFocus();
        _textArea.select(position, positionEnd);
	}
	
	/**
	 * Updates memory in textarea to machine
	 */
	public void updateMachineMemory()
	{
		for(int i = 0; i < MachineV1.memorysizeinwords; i++)
		{
			int position = i * (BITS_PER_LINE + 1);
			try {
				MachineByteV1[] machineBytes = new MachineByteV1[MachineV1.bytesinword];
				String line = _textArea.getText(position, BITS_PER_LINE);
				for(int g = 0; g < MachineV1.bytesinword; g++)
				{
					int index = g * MachineV1.bitsinbyte;
					machineBytes[g] = new MachineByteV1();
					machineBytes[g].copyStringToByte(line.substring(index, index+MachineV1.bitsinbyte));
				}
				MachineWordV1 machineWord = new MachineWordV1(
						machineBytes[0], machineBytes[1], machineBytes[2], machineBytes[3]);
				_machine.loadWordToOffset(machineWord, i);
			} catch (BadLocationException e) {
				e.printStackTrace();
			}
		}		
	}
}
