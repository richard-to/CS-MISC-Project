package scott.kirk.misc;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
  The Osystem class allows a program to be loaded and run on the machine.
  It also makes it possible to check the results of the run by dumping
  the contents of the registers and memory into a file.
*/

public class OsystemV1
{
	private MachineV1 mymachine;
	public static final int bitsinbyte = MachineV1.bitsinbyte;
	public static final int bytesinword = MachineV1.bytesinword;
	public static final int memorysizeinbytes = MachineV1.memorysizeinbytes;
	public static final int memorysizeinwords = MachineV1.memorysizeinwords;

	/**
	  The constructor, Osystem(), constructs a single instance
	  of the Machine class.
	*/

	public OsystemV1()
	{
		mymachine = new MachineV1();
	}


	/**
	  loadProgramFile() is the method which causes the contents of a text
	  file to be loaded into the machine.
	  @param filename, the name of the file containing the machine language
	  program to be loaded
	*/

	public void loadProgramFile(String filename)
	{
		char[][] bufferarray = new char[bytesinword][bitsinbyte];
		MachineByteV1[] bytearray = new MachineByteV1[bytesinword];

		String buffer0;

		MachineWordV1 wordtoload;

		int i;

		int returnvalue;

		int offsetcount = 0;

		FileReader myreader = null;

		mymachine.totalReset();

		try
		{
			myreader = new FileReader(filename);

			for(i = 0; i < bytesinword; i++)
			{
				returnvalue = myreader.read(bufferarray[i], 0, bitsinbyte);
				bytearray[i] = new MachineByteV1(bufferarray[i]);
			}

			buffer0 = new String(bufferarray[0]);

			while(!buffer0.equals("********") && offsetcount < 31)
			{
				wordtoload = new MachineWordV1(bytearray[0], bytearray[1],
											bytearray[2], bytearray[3]);

				mymachine.loadWordToOffset(wordtoload, offsetcount);

				offsetcount++;

				for(i = 0; i < bytesinword; i++)
				{
					returnvalue = myreader.read(bufferarray[i], 0, bitsinbyte);
					bytearray[i] = new MachineByteV1(bufferarray[i]);
				}

				buffer0 = new String(bufferarray[0]);
			}

			mymachine.showStuff();
		}

		catch(IOException e)
		{
			System.out.println("Caught an IOException during opening or reading" + e);
		}

		finally
		{
			try
			{
				if(myreader != null)
				  myreader.close();
			}
			catch(IOException e)
			{
				System.out.println("Caught an IOException during closing" + e);
			}
		}

	}

	/**
	  runProgramFile() is the method which causes a program previously
	  loaded into the machine to be run
	*/

	public void runProgramFile()
	{
			mymachine.takeControl();

			mymachine.showStuff();
	}

	/**
	  dumpMemoryContents() is the method which causes the machine contents
	  at the end of a run to be dumped into a file for inspection of the results.
	  @param filename, the name of the file to dump the machine contents into
	*/

	public void dumpMemoryContents(String filename)
	{
		MachineWordV1 wordtoget;
		String wordstring;
		int i, j;

		/*  The dumping has to be routed through general purpose register A.  Recall
		that this register is used as the window between the machine and the Osystem.
		Because machine language instructions are used in order to move things to
		register A for output, register A itself has to be dumped first.  Also, the
		contents of the instruction, operand, and extra registers will only	reflect
		the output operation itself and will not be dumped.  Note that the ALU internal
		registers are also off limits.  It would be possible to devise the instructions
		needed to inspect these registers, but there is no need.  */

		FileWriter mywriter = null;
		PrintWriter out;

		try
		{
			mywriter = new FileWriter(filename);

			out = new PrintWriter(mywriter);
			out.println("memory contents");

			for(i = 0; i < memorysizeinwords; i++)
			{
				wordtoget = mymachine.getWordFromOffset(i);
				for(j = 0; j < bytesinword; j++)
				{
					wordstring = wordtoget.getByte(j).getStringFromByte();
					out.print(wordstring);
				}
				out.println();
			}
		}

		catch(IOException e)
		{
			System.out.println("Caught an IOException during opening or writing" + e);
		}

		finally
		{
			try
			{
				if(mywriter != null)
				  mywriter.close();
			}
			catch(IOException e)
			{
				System.out.println("Caught an IOException during closing" + e);
			}
		}

	}

}