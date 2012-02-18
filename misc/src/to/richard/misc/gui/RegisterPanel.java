package to.richard.misc.gui;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class RegisterPanel extends JPanel
{
	private static final String LABEL_PREFIX = "Register ";
	private static final String DEFAULT_BYTE = "00000000";
	
	private JLabel _label;
	private JTextField _text;
	
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
}
