package to.richard.misc.gui;

import scott.kirk.misc.OsystemV1;

public class MiscApplication 
{
	/**
	 * @param args
	 */
	public static void main(String[] args) 
	{
		OsystemV1 os = new OsystemV1();
		MiscFrame miscFrame = new MiscFrame(os);
		miscFrame.setVisible(true);		
	}
}
