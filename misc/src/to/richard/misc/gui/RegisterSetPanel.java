package to.richard.misc.gui;

import java.awt.GridLayout;
import java.util.HashMap;

import javax.swing.JPanel;

import scott.kirk.misc.MachineV1;

public class RegisterSetPanel extends JPanel
{
	private static final int ROWS = 6;
	private static final int COLS = 3;
	private static final int TOTAL_REGISTERS = 18;
	
	private HashMap<Integer, RegisterPanel> _registers;
	
	/**
	 * Constructor for register set panel
	 */
	public RegisterSetPanel()
	{
		setLayout(new GridLayout(ROWS, COLS));
		_registers = new HashMap<Integer, RegisterPanel>();
		
		for(int i = TOTAL_REGISTERS-1; i >= 0; i--){
			RegisterPanel registerPanel = new RegisterPanel(i);
			_registers.put(i, registerPanel);
			add(registerPanel);
		}	
	}
	
	/**
	 * Updates registers with machine
	 * 
	 * @param machine
	 */
	public void updateRegisters(MachineV1 machine)
	{
		for(int i = 0; i < TOTAL_REGISTERS; i++){
			RegisterPanel register = _registers.get(i);
			register.updateByte(machine.getRegisterValue(i));
		}		
	}
}
