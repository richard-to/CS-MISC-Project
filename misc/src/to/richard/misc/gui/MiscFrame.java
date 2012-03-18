package to.richard.misc.gui;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.BoxLayout;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.filechooser.FileFilter;

import scott.kirk.misc.MachineByteV1;
import scott.kirk.misc.MachineV1;
import scott.kirk.misc.OsystemV1;

public class MiscFrame extends JFrame
{
	private static final String TITLE = "MISC Frame";
	
	private static final int FRAME_WIDTH = 500;
	private static final int FRAME_HEIGHT = 500;
	private static final int BUTTON_PANEL_HEIGHT = 50;
	private RegisterSetPanel _registerSet;
	private MemoryPanel _memory;
	private OsystemV1 _os;
	private MachineV1 _machine;
	/**
	 * Constructor for MISC application
	 * 
	 * @param os
	 */
	public MiscFrame(OsystemV1 os)
	{
		_os = os;
		_machine = _os.getMachine();
		
		setTitle(TITLE);
		setSize(FRAME_WIDTH, FRAME_HEIGHT);
		
		setJMenuBar(new MiscMenu(
			new LoadStateAction(),
			new SaveStateAction(),
			new LoadAction(),
			new RunAction(),
			new DumpAction(),
			new ExitAction()));
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	
		_registerSet = new RegisterSetPanel(_machine);
		_memory = new MemoryPanel(_machine);
		
		ButtonPanel buttonPanel = new ButtonPanel(new StepAction(), new ClearAction());
		Dimension buttonPanelDim = new Dimension(FRAME_WIDTH, BUTTON_PANEL_HEIGHT);
		buttonPanel.setPreferredSize(buttonPanelDim);
		buttonPanel.setMaximumSize(buttonPanelDim);
		
		Container contentPane = getContentPane();
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.PAGE_AXIS));
		contentPane.add(_registerSet);
		contentPane.add(_memory);
		contentPane.add(buttonPanel);
	}
	
	/**
	 * Steps through program one step at a time
	 */
	class StepAction implements ActionListener
	{
		public void actionPerformed(ActionEvent event)
		{
			_memory.updateMachineMemory();
			_os.stepThroughProgram();
			_registerSet.updateRegisters();
			_memory.updateMemory();			
		}
	}
	
	/**
	 * Clears registers
	 */
	class ClearAction implements ActionListener
	{
		public void actionPerformed(ActionEvent event)
		{
			_registerSet.clearRegisters();
		}
	}
	
	class LoadStateAction implements ActionListener
	{
		public void actionPerformed(ActionEvent event)
		{
			try{
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setCurrentDirectory(new File("."));
				int option = fileChooser.showOpenDialog(MiscFrame.this);
				
				if(option == JFileChooser.APPROVE_OPTION){
					String filename = fileChooser.getSelectedFile().getPath();
	
					ObjectInputStream objIn = new ObjectInputStream(
					new FileInputStream(filename));
	
					MachineByteV1[] registers = (MachineByteV1[])objIn.readObject();
					MachineByteV1[] memory = (MachineByteV1[])objIn.readObject();
					objIn.close();
					
					_machine.loadState(registers, memory);
					_registerSet.updateRegisters();
					_memory.updateMemory();
				}						
			}catch(IOException e){
				System.out.println("Error: Could not load game.");
			} catch(ClassNotFoundException e) {
				System.out.println("Error: Invalid or corrupted file.");
			}			
		}
	}
	
	class SaveStateAction implements ActionListener
	{
		public void actionPerformed(ActionEvent event)
		{
			try{
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setCurrentDirectory(new File("."));
				int option = fileChooser.showSaveDialog(MiscFrame.this);
				
				if(option == JFileChooser.APPROVE_OPTION){
					String filename = fileChooser.getSelectedFile().getPath();
					ObjectOutputStream objOut = new ObjectOutputStream(
						new FileOutputStream(filename));
					objOut. writeObject(_machine.getRegisterObj());
					objOut.writeObject(_machine.getMemoryObj());
					objOut.close();
				}
			} catch(IOException e) {
				System.out.println("Could not write to file.");
			}			
		}
	}
	
	/**
	 * Loads program into OsystemV1
	 */
	class LoadAction implements ActionListener
	{
		public void actionPerformed(ActionEvent event)
		{
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setCurrentDirectory(new File("."));
			int option = fileChooser.showOpenDialog(MiscFrame.this);
			
			if(option == JFileChooser.APPROVE_OPTION){
				fileChooser.setAcceptAllFileFilterUsed(false);
				fileChooser.addChoosableFileFilter(new FileFilter(){
					
					private static final String ALLOWED_EXT = "txt";
					private static final String DESCRIPTION = "Text files";
					
					public boolean accept(File file){
						String filename = file.getName();
						int i = filename.lastIndexOf('.');
				        if (i > 0 &&  i < filename.length() - 1) {
				        	String ext = filename.substring(i+1).toLowerCase();
				        	if(ext.equals(ALLOWED_EXT)){
				        		return true;
				        	}
				        }							
						return false;
					}
					
					public String getDescription(){
						return DESCRIPTION;
					}
				});
				
				_os.loadProgramFile(fileChooser.getSelectedFile().getPath());
				_memory.updateMemory();
			}
		}
	}
	
	/**
	 * Runs the loaded program from start to finish
	 */
	class RunAction implements ActionListener
	{
		public void actionPerformed(ActionEvent event)
		{
			_registerSet.clearRegisters();
			_memory.updateMachineMemory();			
			_os.runProgramFile();
			_registerSet.updateRegisters();
			_memory.updateMemory();
		}
	}
	
	/**
	 * Dumps memory contents to file
	 */
	class DumpAction implements ActionListener
	{
		public void actionPerformed(ActionEvent event)
		{
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setCurrentDirectory(new File("."));
			int option = fileChooser.showSaveDialog(MiscFrame.this);
			if(option == JFileChooser.APPROVE_OPTION){
				String filename = fileChooser.getSelectedFile().getPath();
				_os.dumpMemoryContents(filename);
			}
		}
	}
	
	/**
	 * Exits MISC GUI application
	 */
	class ExitAction implements ActionListener
	{
		public void actionPerformed(ActionEvent event)
		{
			System.exit(0);
		}
	}	
}
