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
	private MachineV1 _machine;
	
	/**
	 * Constructor for register set panel
	 */
	public RegisterSetPanel(MachineV1 machine)
	{
		_machine = machine;
		
		setLayout(new GridLayout(ROWS, COLS));
		_registers = new HashMap<Integer, RegisterPanel>();
		
		for(int i = TOTAL_REGISTERS-1; i >= 0; i--){
			RegisterPanel registerPanel = new RegisterPanel(i);
			_registers.put(i, registerPanel);
			add(registerPanel);
		}
		
		for(int i = 0; i < TOTAL_REGISTERS; i++){
			RegisterPanel registerPanel = _registers.get(i);
			if(i+1 == TOTAL_REGISTERS){
				registerPanel.setNextRegister(_registers.get(0));
			} else {
				registerPanel.setNextRegister(_registers.get(i+1));
			}
		}
	}
	
	/**
	 * Updates registers with machine
	 */
	public void updateRegisters()
	{
		for(int i = 0; i < TOTAL_REGISTERS; i++){
			RegisterPanel register = _registers.get(i);
			register.updateByte(_machine.getRegisterValue(i));
		}		
	}
	
	public void clearRegisters()
	{
		for(int i = 0; i < TOTAL_REGISTERS; i++){
			RegisterPanel register = _registers.get(i);			
			register.clearByte();
		}		
	}
}
