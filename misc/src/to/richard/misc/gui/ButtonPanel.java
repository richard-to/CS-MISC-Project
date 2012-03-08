package to.richard.misc.gui;

import java.awt.GridLayout;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

public class ButtonPanel extends JPanel
{
	private static final String STEP_LABEL = "Step";
	private static final String CLEAR_LABEL = "Clear";
	
	private static final int ROWS = 1;
	private static final int COLS = 2;
	
	public ButtonPanel(ActionListener stepAction, ActionListener clearAction)
	{
		setLayout(new GridLayout(ROWS, COLS));
		JButton stepButton = new JButton();
		stepButton.setText(STEP_LABEL);
		stepButton.addActionListener(stepAction);
		add(stepButton);
		
		JButton clearButton = new JButton();
		clearButton.setText(CLEAR_LABEL);		
		clearButton.addActionListener(clearAction);
		add(clearButton);
	}
}
