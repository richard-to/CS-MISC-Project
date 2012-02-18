package scott.kirk.misc;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;


/*******************************************************************
  This is the architecture with one common piece of memory.
The other segments and offset registers still exist in this
code, but they are not used at all.


  This is the Machine class.  Here is a brief rundown on the
hardware structure of the machine defined by this class.


REGISTERS

  There is an array of registers.  Each of the registers consists
of one machine word.

register name		decimal index	binary code
identification		in reg array	of index

unused				0				"00000000"

general
purpose

A					1				"00000001"
B					2				"00000010"
C					3				"00000011"
D					4				"00000100"

memory
offsets

codeoffset			5				"00000101"
dataoffset			6				"00000110"
unusedreg1			7				"00000111"
unusedreg2			8				"00001000"
unusedreg3			9				"00001001"

flag				10				"00001010"

control unit
registers

instruction			11				"00001011"
operand1			12				"00001100"
operand2			13				"00001101"
extra				14				"00001110"

ALU registers

aluinreg1			15				"00001111"
aluinreg2			16				"00010000"
aluoutreg			17				"00010001"

  The three ALU registers are used for scratch work in all of the
arithmetic methods.  These registers don't have their own methods
that give access to them.


MEMORY SEGMENTS

  The memory is structured as an array containing machine
words.  The index into the array represents the offset
into the segment.

segment name		decimal			binary code
identification		designation		designation

memory				1				"00000001"


INSTRUCTIONS

  At the machine language level there are different instructions
for the various allowable combinations of operands.  The
instructions are identified numerically.

names of							binary code
instructions						designation

MOVES

moveDestRegSrcReg()					"10000001"
moveToMemFromReg()					"10000010"
moveToRegFromMem()					"10000011"
moveToMemFromConst()				"10000100"
moveToRegFromConst()				"10000101"

ADDS

addDestRegSrcReg()					"10000110"
addToMemFromReg()					"10000111"
addToRegFromMem()					"10001000"
addToMemFromConst()					"10001001"
addToRegFromConst()					"10001010"

SUBS

subDestRegSrcReg()					"10001011"
subFromMemSrcReg()					"10001100"
subFromRegSrcMem()					"10001101"
subFromMemSrcConst()				"10001110"
subFromRegSrcConst()				"10001111"

JUMPS

jumpUnconditional()					"10010000"
jumpOnPositive()					"10010001"
jumpOnNegative()					"10010010"
jumpOnZero()						"10010011"
jumpOnOverflow()					"10010100"

********************************************************************/

/**
    The machine class defines the basic hardware and machine language
  instruction set of the MISC simulation.  A full treatment of the
  registers, memory, and instructions is given in a separate text
  document.
*/

public class MachineV1
{

	public static final int numberofregisters = 18;
	public static final int indexoffirstoffsetregister = 5;
	public static final int indexoflastoffsetregister = 9;
    public static final int bitsinbyte = MachineByteV1.bitsinbyte;
    public static final int bytesinword = MachineWordV1.bytesinword;
	public static final int memorysizeinwords = 256;
    public static final int memorysizeinbytes = memorysizeinwords * bytesinword;

    private MachineByteV1[] reg = new MachineByteV1[numberofregisters];

	private MachineByteV1[] memory = new MachineByteV1[memorysizeinbytes];


	/**
	  MachineV1()

	  The constructor takes no parameters.  It constructs the
	  machine's registers and memory.
	*/

    public MachineV1()
    {
		int i;

		for(i = 0; i < numberofregisters; i++)
		{
			reg[i] = new MachineByteV1();
		}

		for(i = 0; i < memorysizeinbytes; i++)
		{
			memory[i] = new MachineByteV1();
		}
	}


	/**
	  showStuff()

	  This method exists for help in debugging the machine.
	  Calls to the method can be placed at various locations
	  in the Machine or Osystem code.  It will generate a
	  text file named showfile.txt which will contain the
	  state of the machine and the memory at that point.
	*/

    public void showStuff()
    {
		int i;

		FileWriter mywriter = null;

		try
		{
			mywriter = new FileWriter("showfile.txt");
			PrintWriter out = new PrintWriter(mywriter);

			out.println("general purpose registers");

			for(i = 1; i <= 4; i++)
			{
				out.println(reg[i].getStringFromByte());
			}

			out.println("offset registers");

			for(i = 5; i <= 9; i++)
			{
				out.println(reg[i].getStringFromByte());
			}

			out.println("instruction registers");

			for(i = 11; i <= 13; i++)
			{
				out.println(reg[i].getStringFromByte());
			}

			out.println("memory");

			for(i = 0; i < memorysizeinbytes; i++)
			{
				out.println(memory[i].getStringFromByte());
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


	/****************************************/
    /*  Instructions that can be called by  */
    /*  the Osystem in order to do I/O.     */
    /****************************************/


	/**
	public void loadWordToOffset()

	  This method takes in a MachineWord and a word offset from outside
	of the machine and loads it into memory starting at that word offset.
	@param loadword, the MachineWord to be loaded
	@param wordoffset, the offset to load the MachineWord at
	*/

	public void loadWordToOffset(MachineWordV1 loadword, int wordoffset)
	{
		int byteoffset = bytesinword * wordoffset;

		for(int i = 0; i < bytesinword; i++)
		{
			memory[byteoffset + i].copyByteIn(loadword.getByte(i));
		}
	}


	/**
	public MachineWordV1 getWordFromOffset()

	  This method returns a word from the offset in the machine's memory
	specified in the call.
	@param wordoffset, the offset of the word to be gotten
	@return tempmachineword, the MachineWord filled with these contents
	*/

	public MachineWordV1 getWordFromOffset(int wordoffset)
	{
		int byteoffset = bytesinword * wordoffset;

		MachineWordV1 tempmachineword = new MachineWordV1(memory[byteoffset],
					memory[byteoffset + 1], memory[byteoffset + 2],
					memory[byteoffset + 3]);

		return tempmachineword;
	}


	/******************************************************/
	/*  Instructions that can be called by the Osystem    */
	/*  in order to run machine language programs.        */
	/******************************************************/

	/**
	totalReset()

	  Total reset.

	  Reset the codeoffset register to 8.  Reset all other registers
	and memory to zeros.  This machine instruction is called by the
	Osystem.  It is something that should be done between unrelated
	sequences of executions of machine language instructions.  This
	is not a general purpose instruction for use in a machine language
	program, so it does not have a code.
	*/

	public void totalReset()
	{
		int i;

		for(i = 0; i < numberofregisters; i++)
		{
			reg[i].setByteToZeros();
		}

		reg[5].copyStringToByte("00001000");

		for(i = 0; i < memorysizeinbytes; i++)
		{
			memory[i].setByteToZeros();
		}
	}


	/**
	resetOffsets()

	  Reset offsets.

	  Reset contents of the dataoffset register to 0 and the
	codeoffset register to 8.  This is not a general purpose
	instruction for use in a machine language program, so it
	does not have a code.
	*/

	public void resetOffsets()
	{
		reg[6].setByteToZeros();
		reg[5].copyStringToByte("00001000");
	}


	/**
	takeControl()

	  Take control.

	  This is the method where the fetch-decode-execute cycle is
	implemented after data and a program have been loaded into
	memory by the Osystem.  This machine instruction is called by the
	Osystem and turns over execution of a whole program to the machine.
	This is more or less the heart of the control unit.  It is just a
	big loop.  It copies an instruction and its operands found in memory
	into the instruction and operand registers.  The codeoffset register
	is incremented for every instruction read.  Note that the word size
	of the machine is 4 bytes.  In other words, every 4th byte in memory
	is addressable.

	  takeControl() then checks to see what's in the instruction register.
	If it contains "00000000", that signals the end of the program.  The
	rest of the body of the loop is a big if statement that checks the
	contents of the instruction register against the codes for the general
	purpose instructions of the machine language, calling the corresponding
	machine method to execute a valid one.  The machine doesn't return
	control to the operating system until all of the instructions in program
	memory have been executed in order.  There are no safeties in this
	simulation.  If the code in the instruction register is not valid,
	a simple error message is generated.  If the program in memory doesn't
	work right or doesn't terminate, the system locks.  This is not a
	general purpose instruction for use in a machine language program,
	so it does not have a code.

	  It should be emphasized that it is a design decision to make
	incrementation of the code offset a part of takeControl().  Stepping
	through program code is in effect an "automatic" feature of the system.
	It would be possible to implement differently by putting suitable offset
	incrementation into the implementation of each individual executable
	instruction.  Because incrementation is done in takeControl(), it is
	critical that it be done before the execution of the instruction
	retrieved.  If the instruction were a jump, for example, it would place
	a new value in the code offset register, the address of the instruction
	to jump to.  If automatic incrementation were done after the execution
	of the instruction, it would increment the address to be jumped to,
	which would be incorrect.
	*/

	public void takeControl()
	{
		int i;
		int memoffset;

		resetOffsets();

		/*  Convert the word offset to a byte offset.  */

		memoffset = bytesinword * reg[5].getIntFromByte();

		/*  Now copy the instruction and operands from the
		byte addresses in memory into the instruction and
		operand registers.  */

		reg[11].copyByteIn(memory[memoffset]);
		reg[12].copyByteIn(memory[memoffset + 1]);
		reg[13].copyByteIn(memory[memoffset + 2]);
		reg[14].copyByteIn(memory[memoffset + 3]);

		while(!reg[11].compareByteWithString("00000000"))
		{

			/*  Increment code offset by 1.  */

			reg[5].incrementByte();

			/*  MOVES  */
			if(reg[11].compareByteWithString("10000001"))
				moveDestRegSrcReg();
			else if(reg[11].compareByteWithString("10000010"))
				moveToMemFromReg();
			else if(reg[11].compareByteWithString("10000011"))
				moveToRegFromMem();
			else if(reg[11].compareByteWithString("10000100"))
				moveToMemFromConst();
			else if(reg[11].compareByteWithString("10000101"))
				moveToRegFromConst();

			/*  ADDS  */
			else if(reg[11].compareByteWithString("10000110"))
				addDestRegSrcReg();
			else if(reg[11].compareByteWithString("10000111"))
				addToMemFromReg();
			else if(reg[11].compareByteWithString("10001000"))
				addToRegFromMem();
			else if(reg[11].compareByteWithString("10001001"))
				addToMemFromConst();
			else if(reg[11].compareByteWithString("10001010"))
				addToRegFromConst();

			/*  SUBS  */
			else if(reg[11].compareByteWithString("10001011"))
				subDestRegSrcReg();
			else if(reg[11].compareByteWithString("10001100"))
				subFromMemSrcReg();
			else if(reg[11].compareByteWithString("10001101"))
				subFromRegSrcMem();
			else if(reg[11].compareByteWithString("10001110"))
				subFromMemSrcConst();
			else if(reg[11].compareByteWithString("10001111"))
				subFromRegSrcConst();

			/*  JUMPS  */
			else if(reg[11].compareByteWithString("10010000"))
				jumpUnconditional();
			else if(reg[11].compareByteWithString("10010001"))
				jumpOnPositive();
			else if(reg[11].compareByteWithString("10010010"))
				jumpOnNegative();
			else if(reg[11].compareByteWithString("10010011"))
				jumpOnZero();
			else if(reg[11].compareByteWithString("10010100"))
				jumpOnOverflow();

			else
			{
				System.out.println("Didn't find instruction match in takecontrol loop.");
				reg[11].showByte();
			}

			memoffset = bytesinword * reg[5].getIntFromByte();
			reg[11].copyByteIn(memory[memoffset]);
			reg[12].copyByteIn(memory[memoffset + 1]);
			reg[13].copyByteIn(memory[memoffset + 2]);
			reg[14].copyByteIn(memory[memoffset + 3]);
		}
	}


	/*****************************/
	/*  Move instructions  */
	/*****************************/


	/**
	moveDestRegSrcReg()

	  Move to destination register from source register.

	  This is given code "10000001".
	*/

	public void moveDestRegSrcReg()
	{
		int destregindex;
		int srcregindex;

		destregindex = reg[12].getIntFromByte();
		srcregindex = reg[13].getIntFromByte();

		if(destregindex >= 0 && destregindex < numberofregisters
		  && srcregindex >= 0 && srcregindex < numberofregisters)
		  {
		  reg[destregindex].copyByteIn(reg[srcregindex]);
		  }
	}


	/**
	moveToMemFromReg()

	  Move to memory from register.

	  This is given the code "10000010".
	*/

	public void moveToMemFromReg()
	{
		int srcregindex;
		int memoffset;

		/*  Find the index of the source register in the operand2 register.  */

		srcregindex = reg[13].getIntFromByte();

		/*  Make sure that the register index is within range.  */

		if(srcregindex >= 0 && srcregindex < numberofregisters)
		{
			/*  Get the offset into memory from the operand1 register
			and change it to a byte offset.  */

			memoffset = bytesinword * reg[12].getIntFromByte();

			/*  Then do the move.  */

			memory[memoffset].copyByteIn(reg[srcregindex]);
		}
	}


	/**
	moveToRegFromMem()

	  Move to register from memory.

	  This is given the code "10000011".
	*/

	public void moveToRegFromMem()
	{
		int destregindex;
		int memoffset;

		/*  Find the index of the destination register in the operand1 register.  */

		destregindex = reg[12].getIntFromByte();

		/*  Make sure that the register index is within range.  */

		if(destregindex >= 0 && destregindex < numberofregisters)
		{

			/*  Get the offset into memory from the operand2 register
			and change it to a byte offset.  */

			memoffset = bytesinword * reg[13].getIntFromByte();

			/*  Then do the move.  */

			reg[destregindex].copyByteIn(memory[memoffset]);
		}
	}


	/**
	moveToMemFromConst()

	  Move to memory from constant.

	  This is given the code "10000100".
	*/

	public void moveToMemFromConst()
	{
		int memoffset;

		/*  Get the offset into memory from the operand1 register
			and change it to a byte offset.  */

		memoffset = bytesinword * reg[12].getIntFromByte();

		memory[memoffset].copyByteIn(reg[13]);
	}


	/**
	moveToRegFromConst()

	  Move to register from constant.

	  This is given the code "10000101".
	*/

	public void moveToRegFromConst()
	{
		int destregindex;

		/*  Get the index of the register to copy into from the operand1 register.  */

		destregindex = reg[12].getIntFromByte();

		/*  Check to make sure it's within range and copy the constant from the operand2 register.  */

		if(destregindex >= 0 && destregindex < numberofregisters)
		{
		 	reg[destregindex].copyByteIn(reg[13]);
		}
	}


	/************************************/
	/*  Helper methods for arithmetic.  */
	/************************************/

	/**
	doTheAdd()

	  Do the add.

	  This helper method saves some repetitive code writing in the
	arithmetic instructions.  It works directly on the contents of
	the ALU registers.  The arithmetic methods that make use of this
	method have to have placed operands in the ALU registers before
	making the call.

	  Note that indexing of the positions in the MachineByte objects
	is left to right, as one might expect of something built on an array.
	However, conceptually, the place values of binary quantities stored
	in machine bytes is ascending from right to left, as is customary in
	a human representation.  This accounts for the loop's running down
	instead of up.
	*/

	public void doTheAdd()
	{
		int i;
		char carrybit = '0';
		int placetotal;

		for(i = bitsinbyte - 1; i >= 0; i--)
		{
			placetotal = 0;
			if(carrybit == '1')
			  placetotal++;
			if(reg[15].getPosition(i) == '1')
			  placetotal++;
			if(reg[16].getPosition(i) == '1')
			  placetotal++;

			if(placetotal == 0)
			{
				reg[17].setPosition(i, '0');
				carrybit = '0';
			}
			else if(placetotal == 1)
			{
				reg[17].setPosition(i, '1');
				carrybit = '0';
			}
			else if(placetotal == 2)
			{
				reg[17].setPosition(i, '0');
				carrybit = '1';
			}
			else
			{
				reg[17].setPosition(i, '1');
				carrybit = '1';
			}
		}
		setFlags(carrybit);
	}


	/**
	setFlags()

	  Set flags.

	  This helper method checks the contents of the destination
	register of an arithmetic operation and sets the flag bits according
	to whether the result was positive, negative, zero, or overflow.
	@param char overflow, the value to be placed in the overflow
	position in the flag register.
	*/

	public void setFlags(char overflow)
	{
		int flagvar = 0;
		int i;

		/*  check for 0 result  */

		for(i = 0; i < bitsinbyte; i++)
		  {
			  if(reg[17].getPosition(i) != '0')
			    flagvar = 1;
		  }

		/*  set positive, negative, or zero flag bits  */

		if(flagvar == 0)
		{
			reg[10].setPosition(0, '0');
			reg[10].setPosition(1, '0');
		}
		else if(reg[17].getPosition(0) == '0')
		{
			reg[10].setPosition(0, '1');
			reg[10].setPosition(1, '0');
		}
		else
		{
			reg[10].setPosition(0, '0');
			reg[10].setPosition(1, '1');
		}

		/*  set the overflow flag bit  */

		reg[10].setPosition(2, overflow);
	}


	/**********************/
	/*  Add instructions  */
	/**********************/

	/**
	addDestRegSrcReg()

	  Add to destination register from source register.

	  This is given the code "10000110".
	*/

	public void addDestRegSrcReg()
	{
		int destregindex;
		int srcregindex;

		/*  Pick up the register indexes from the operand registers.  */

		destregindex = reg[12].getIntFromByte();
		srcregindex = reg[13].getIntFromByte();

		/*  Copy the contents of those registers into the ALU registers.  */

		reg[15].copyByteIn(reg[destregindex]);
		reg[16].copyByteIn(reg[srcregindex]);

		/*  Do the add.  */

		doTheAdd();

		/*  Copy from the ALU result register to the destination register.  */

		reg[destregindex].copyByteIn(reg[17]);
	}


	/**
	addToMemFromReg()

	  Add to memory from register.

	  This is given the code "10000111".
	*/

	public void addToMemFromReg()
	{
		int srcregindex;
		int memoffset;

		/*  Find the index of the source register in the operand2 register.  */

		srcregindex = reg[13].getIntFromByte();

		/*  Make sure that the register index is within range.  */

		if(srcregindex >= 0 && srcregindex < numberofregisters)
		{
			/*  Get the offset into memory from the operand1 register
			and change it to a byte offset.  */

			memoffset = bytesinword * reg[12].getIntFromByte();

			/*  If the offset is within range, copy the operands into the
			ALU registers, do the add, copy from the ALU result register
			to the destination register, and increment the offset.  */

			reg[15].copyByteIn(memory[memoffset]);
			reg[16].copyByteIn(reg[srcregindex]);
			doTheAdd();
			memory[memoffset].copyByteIn(reg[17]);
		}
	}


	/**
	addToRegFromMem()

	  Add to register from memory.

	  This is given the code "10001000".
	*/

	public void addToRegFromMem()
	{
		int destregindex;
		int memoffset;

		/*  Find the index of the destination register in the operand1 register.  */

		destregindex = reg[12].getIntFromByte();

		/*  Make sure that the register index is within range.  */

		if(destregindex >= 0 && destregindex < numberofregisters)
		{
			/*  Get the offset into memory from the operand2 register
			and change it to a byte offset.  */

			memoffset = bytesinword * reg[13].getIntFromByte();

			/*  If the offset is within range, copy the operands into the
			ALU registers, do the add, copy from the ALU result register
			to the destination register, and increment the offset.  */

			reg[15].copyByteIn(reg[destregindex]);
			reg[16].copyByteIn(memory[memoffset]);
			doTheAdd();
			reg[destregindex].copyByteIn(reg[17]);
		}
	}


	/**
	addToMemFromConst()

	  Add to memory from constant.

	  This is given the code "10001001".
	*/

	public void addToMemFromConst()
	{
		int memoffset;

		/*  Get the offset into memory from the operand1 register
			and change it to a byte offset.  */

		memoffset = bytesinword * reg[12].getIntFromByte();

		/*  If the offset is within range, copy the operands into the
		ALU registers, do the add, copy from the ALU result register
		to the memory segment, and increment the offset.  */

		reg[15].copyByteIn(memory[memoffset]);
		reg[16].copyByteIn(reg[13]);
		doTheAdd();
		memory[memoffset].copyByteIn(reg[17]);
	}


	/**
	addToRegFromConst()

	  Add to register from constant.

	  This is given the code "10001010".
	*/

	public void addToRegFromConst()
	{
		int destregindex;

		/*  Get the index of the register to add into from the operand1 register.  */

		destregindex = reg[12].getIntFromByte();

		/*  Check to make sure it's within range, copy the operands to the ALU
		registers, do the add, and copy the ALU result to the destination.  */

		if(destregindex >= 0 && destregindex < numberofregisters)
		{
			reg[15].copyByteIn(reg[destregindex]);
			reg[16].copyByteIn(reg[13]);
			doTheAdd();
			reg[destregindex].copyByteIn(reg[17]);
		}
	}


	/**********************/
	/*  Sub instructions  */
	/**********************/

	/**
	subDestRegSrcReg()

	  Subtract from destination register, source register.

	  Subtraction is implemented by adding the negative in twos complement.

	  This is given the code "10001011".
	*/

	public void subDestRegSrcReg()
	{
		int destregindex;
		int srcregindex;

		/*  Pick up the register indexes from the operand registers.  */

		destregindex = reg[12].getIntFromByte();
		srcregindex = reg[13].getIntFromByte();

		/*  Copy the contents of those registers into the ALU registers.  */

		reg[15].copyByteIn(reg[destregindex]);
		reg[16].copyByteIn(reg[srcregindex]);

		/*  Change the value in the second ALU register to negative twos
		complement and then add.  */

		reg[16].changeToNegativeTwosComplement();
		doTheAdd();

		/*  Copy from the ALU result register to the destination register.  */

		reg[destregindex].copyByteIn(reg[17]);
	}


	/**
	subFromMemSrcReg()

	  Subtract from memory, source register.

	  Subtraction is implemented by adding the negative in twos complement.

	  This is given the code "10001100".
	*/

	public void subFromMemSrcReg()
	{
		int srcregindex;
		int memoffset;

		/*  Find the index of the source register in the operand2 register.  */

		srcregindex = reg[13].getIntFromByte();

		/*  Make sure that the register index is within range.  */

		if(srcregindex >= 0 && srcregindex < numberofregisters)
		{
			/*  Get the offset into memory from the operand1 register
			and change it to a byte offset.  */

			memoffset = bytesinword * reg[12].getIntFromByte();

			/*  If the offset is within range, copy the operands into the ALU
			registers, change the value in the second ALU register to negative
			twos complement, do the add, copy from the ALU result register to
			the destination register, and increment the offset.  */

			{
				reg[15].copyByteIn(memory[memoffset]);
				reg[16].copyByteIn(reg[srcregindex]);
				reg[16].changeToNegativeTwosComplement();
				doTheAdd();
				memory[memoffset].copyByteIn(reg[17]);
			}
		}
	}


	/**
	subFromRegSrcMem()

	  Subtract from register, source memory.

	  Subtraction is implemented by adding the negative in twos complement.

	  This is given the code "10001101".
	*/

	public void subFromRegSrcMem()
	{
		int destregindex;
		int memoffset;

		/*  Find the index of the destination register in the operand1 register.  */

		destregindex = reg[12].getIntFromByte();

		/*  Make sure that the register index is within range.  */

		if(destregindex >= 0 && destregindex < numberofregisters)
		{
			/*  Get the offset into memory from the operand2 register
			and change it to a byte offset.  */

			memoffset = bytesinword * reg[13].getIntFromByte();

			/*  If the offset is within range, copy the operands into the
			ALU registers, change the value in the second ALU register to
			negative twos complement, do the add, copy from the ALU result
			register to the destination register, and increment the offset.  */

			reg[15].copyByteIn(reg[destregindex]);
			reg[16].copyByteIn(memory[memoffset]);
			reg[16].changeToNegativeTwosComplement();
			doTheAdd();
			reg[destregindex].copyByteIn(reg[17]);
		}
	}


	/**
	SubFromMemSrcConst()

	  Subtract from memory, source constant.

	  Subtraction is implemented by adding the negative in twos complement.

	  This is given the code "10001110".
	*/

	public void subFromMemSrcConst()
	{
		int memoffset;

		/*  Get the offset into memory from the operand1 register
			and change it to a byte offset.  */

		memoffset = bytesinword * reg[12].getIntFromByte();

		/*  If the offset is within range, copy the operands into the
		ALU registers, change the value in the second ALU register to
		negative twos complement, do the add, copy from the ALU result
		register to the memory segment, and increment the offset.  */

		reg[15].copyByteIn(memory[memoffset]);
		reg[16].copyByteIn(reg[13]);
		reg[16].changeToNegativeTwosComplement();
		doTheAdd();
		memory[memoffset].copyByteIn(reg[17]);
	}


	/**
	subFromRegSrcConst()

	  Subtract from register, source constant.

	  Subtraction is implemented by adding the negative in twos complement.

	  This is given the code "10001111".
	*/

	public void subFromRegSrcConst()
	{
		int destregindex;

		/*  Get the index of the register to add into from the operand1 register.  */

		destregindex = reg[12].getIntFromByte();

		/*  Check to make sure it's within range, copy the operands to the ALU
		registers, change the value in the second ALU register to negative twos
		complement, do the add, and copy the ALU result to the destination.  */

		if(destregindex >= 0 && destregindex < numberofregisters)
		{
			reg[15].copyByteIn(reg[destregindex]);
			reg[16].copyByteIn(reg[13]);
			reg[16].changeToNegativeTwosComplement();
			doTheAdd();
			reg[destregindex].copyByteIn(reg[17]);
		}
	}


	/***********************/
	/*  Jump instructions  */
	/***********************/

	/**
	jumpUnconditional()

	  Jump unconditionally.

	  Copy the operand to the code segment offset register.

	  This is given the code "10010000".
	*/

	public void jumpUnconditional()
	{
		reg[5].copyByteIn(reg[12]);
	}


	/**
	jumpOnPositive()

	  Jump if the result of the most recent arithmetic instruction was positive.

	  If the first two bits of the flag register contain 1 and 0,
	respectively, copy the operand to the code offset register.

	  This is given the code "10010001".
	*/

	public void jumpOnPositive()
	{
		if(reg[10].getPosition(0) == '1' && reg[10].getPosition(1) == '0')
		  reg[5].copyByteIn(reg[12]);
	}


	/**
	jumpOnNegative()

	  Jump if the result of the most recent arithmetic instruction was negative.

	  If the first two bits of the flag register contain 0 and 1,
	respectively, copy the operand to the code offset register.

	  This is given the code "10010010".
	*/

	public void jumpOnNegative()
	{
		if(reg[10].getPosition(0) == '0' && reg[10].getPosition(1) == '1')
		  reg[5].copyByteIn(reg[12]);
	}


	/**
	jumpOnZero()

	  Jump if the result of the most recent arithmetic instruction was zero.

	  If the first two bits of the flag register contain 0 and 0,
	respectively, copy the operand to the code offset register.

	  This is given the code "10010011".
	*/

	public void jumpOnZero()
	{
		if(reg[10].getPosition(0) == '0' && reg[10].getPosition(1) == '0')
		  reg[5].copyByteIn(reg[12]);
	}


	/**
	jumpOnOverflow()

	  Jump if the result of the most recent arithmetic instruction led to overflow.

	  If the bit at index 2 of the flag register contains a 1,
	copy the operand to the code offset register.

	  This is given the code "10010100".
	*/

	public void jumpOnOverflow()
	{
		if(reg[10].getPosition(2) == '1')
		  reg[5].copyByteIn(reg[12]);
	}

}
