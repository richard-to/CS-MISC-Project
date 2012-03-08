package to.richard.misc.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class RegisterPanel extends JPanel
{
	private static final String LABEL_PREFIX = "Register ";
	private static final String DEFAULT_BYTE = "00000000";
	
	private JLabel _label;
	private JTextField _text;
	private RegisterPanel _nextRegister;
	
	/**
	 * Register Panel constructor
	 * 
	 * @param num
	 * @param byteIn
	 */
	public RegisterPanel(int num)
	{
		_label = new JLabel(LABEL_PREFIX + Integer.toString(num));
		_text = new JTextField(DEFAULT_BYTE);
		_text.addActionListener(new TextListener());
		add(_label);
		add(_text);
	}
	
	/**
	 * Updates byte in register
	 * 
	 * @param byteIn
	 */
	public void updateByte(String byteIn)
	{
		_text.setText(byteIn);
	}	
	
	/**
	 * Clears byte in register
	 */
	public void clearByte()
	{
		_text.setText(DEFAULT_BYTE);
	}
	
	/**
	 * Sets next register for focus shift
	 * 
	 * @param registerPanel
	 */
	public void setNextRegister(RegisterPanel registerPanel)
	{
		_nextRegister = registerPanel;
	}
	
	/**
	 * Shifts focus to this register
	 */
	public void shiftFocus()
	{
		_text.requestFocusInWindow();
	}
	

	private class TextListener implements ActionListener
	{
		public void actionPerformed(ActionEvent event)
		{
			_nextRegister.shiftFocus();
		}
	}	
}
