package scott.kirk.misc;

/**
  The MachineByte class is the building block for both
registers and memory.  Although in theory the byte size can
change, throughout this simulation a word will equal 4 bytes.
In the simulation the 8 bits of a byte are represented by
an array of 8 characters.  Throughout the simulation particular
hardcoded values to be stored in bytes will be treated as
strings.  This means that the methods for MachineByte rely
on some convenient String methods.  Internally, using
characters to represent the individual bits in machine bytes
should be straightforward.  It will be necessary to cast byte
oriented machine language input files to character form in
order for them to be handled correctly by the rest of the
machine.
*/

public class MachineByteV1
{
	public static final int bitsinbyte = 8;
	private static final String junk = "00000000";
	private char bytearray[] = new char[bitsinbyte];

	/**
	  This constuctor puts all zeros into the byte.
	*/

	public MachineByteV1()
	{
		junk.getChars(0, bitsinbyte, bytearray, 0);
	}

	/**
	  This constructor loads the byte with the contents of a character array.
	*/

	public MachineByteV1(char[] arrayin)
	{
		int i = 0;

		while(i < arrayin.length && i < bitsinbyte)
		{
			bytearray[i] = arrayin[i];
			i++;
		}
	}


	/**
	  ShowByte() prints the contents of the byte on the screen.
	*/

	public void showByte()
	{
		System.out.println(bytearray);
	}

	/**
	  setByteToZeros() puts all zeros into an existing byte.
	*/

	public void setByteToZeros()
	{
		junk.getChars(0, bitsinbyte, bytearray, 0);
	}

	/**
	  copyByteIn() copies the value of one byte to another.
	  @param abyte, the byte to be copied in.
	*/

	public void copyByteIn(MachineByteV1 abyte)
	{
		int i;

		for(i = 0; i < bitsinbyte; i++)
		{
			this.bytearray[i] = abyte.bytearray[i];
		}
	}

	/**
	  copyStringToByte() copies a string into a byte.
	  @param astring, the string to be copied in.
	*/

	public void copyStringToByte(String astring)
	{
		junk.getChars(0, bitsinbyte, bytearray, 0);
		int stringlength = astring.length();
		if(stringlength > bitsinbyte)
		  stringlength = bitsinbyte;
		astring.getChars(0, stringlength, bytearray, 0);
	}

	/**
	  getStringFromByte() returns the contents of a byte as a string.
	  @return String.copyValueOf(bytearray), the contents as a string.
	*/

	public String getStringFromByte()
	{
		return String.copyValueOf(bytearray);
	}

	/**
	  compareByteWithString() compares the contents of a byte with a
	  string.
	  @param astring, the string to compare with
	  @return String.copyValue(bytearray).equals(astring), a boolean
	*/

	public boolean compareByteWithString(String astring)
	{
		return String.copyValueOf(bytearray).equals(astring);
	}

	/**
	  copyIntToByte() takes an int and stores it in binary notation in a byte.
	  @param anint, the int to be converted to binary notation and stored in the byte
	*/

	public void copyIntToByte(int anint)
	{
		/*  Given a positive integer less than 256, produce the 8 character sequence
		of 1's and 0's that would represent the number in binary and put these
		characters into the bytearray.  */

		int remainder;
		int i;

		junk.getChars(0, bitsinbyte, bytearray, 0);

		if(anint >= 0 && anint < 256)
		{
			remainder = anint;

			if((remainder / 128) >= 1)
			{
				bytearray[0] = '1';
				remainder = remainder % 128;
			}
			else
			  bytearray[0] = '0';

			if((remainder / 64) >= 1)
			{
				bytearray[1] = '1';
				remainder = remainder % 64;
			}
			else
			  bytearray[1] = '0';

			if((remainder / 32) >= 1)
			{
				bytearray[2] = '1';
				remainder = remainder % 32;
			}
			else
			  bytearray[2] = '0';

			if((remainder / 16) >= 1)
			{
				bytearray[3] = '1';
				remainder = remainder % 16;
			}
			else
			  bytearray[3] = '0';

			if((remainder / 8) >= 1)
			{
				bytearray[4] = '1';
				remainder = remainder % 8;
			}
			else
			  bytearray[4] = '0';

			if((remainder / 4) >= 1)
			{
				bytearray[5] = '1';
				remainder = remainder % 4;
			}
			else
			  bytearray[5] = '0';

			if((remainder / 2) >= 1)
			{
				bytearray[6] = '1';
				remainder = remainder % 2;
			}
			else
			  bytearray[6] = '0';

			if((remainder / 1) >= 1)
			{
				bytearray[7] = '1';
			}
			else
			  bytearray [7] = '0';
		}
	}

	/**
	  getIntFromByte() returns the integer value of the binary contents of the byte.
	  @return intsum, the integer value of the binary contents of the byte.
	*/

	public int getIntFromByte()
	{
		/*  Interpret the 1's and 0's in the bytearray as binary digits and
		convert the whole mess into a single decimal number and return it.  */

		int intsum = 0;

		if(bytearray[0] == '1')
		  intsum+=128;
		if(bytearray[1] == '1')
		  intsum+=64;
		if(bytearray[2] == '1')
		  intsum+=32;
		if(bytearray[3] == '1')
		  intsum+=16;
		if(bytearray[4] == '1')
		  intsum+=8;
		if(bytearray[5] == '1')
		  intsum+=4;
		if(bytearray[6] == '1')
		  intsum+=2;
		if(bytearray[7] == '1')
		  intsum+=1;

		return intsum;
	}

	/**
	  incrementByte() increases the binary value stored in the byte by 1.
	  If the value is already 255, it changes it to 0.
	*/

	public void incrementByte()
	{
		/*  This method is just a little convenience.  Since this is just a
		simulation, there's no reason not to have a method to increment.  */

		int intval;

		intval = getIntFromByte();
		if(intval == 255)
		  intval = 0;
		else
		  intval++;
	    copyIntToByte(intval);
	}

	/**
	  setPosition() puts a given bit in a given position in a byte.
	  @param position, the index in the byte to be change
	  @param value, the binary digit (as a character) to be put there.
	*/

	public void setPosition(int position, char value)
	{
		bytearray[position] = value;
	}

	/**
	  getPosition() returns the bit in a given position in a byte.
	  @return bytearray[position], the bit as a character
	*/

	public char getPosition(int position)
	{
		return bytearray[position];
	}

	/**
	  changeToNegativeTwosComplement() changes the contents of a
	  byte to their negative in the twos complement notation.  This
	  is only applied to an ALU operand register in order to be
	  able to implement subtraction as addition of the opposite.
	*/

	public void changeToNegativeTwosComplement()
	{

		int i;
		char carrybit = '0';

		for(i = 0; i < bitsinbyte; i++)
		{
			if(bytearray[i] == '0')
			  bytearray[i] = '1';
			else
			  bytearray[i] = '0';
		}

		carrybit = '1';
		i = bitsinbyte - 1;

		while(carrybit == '1' && i >= 0)
		{
			if(bytearray[i] == '1' && carrybit == '1')
			{
				bytearray[i] = '0';
				carrybit = '1';
			}
			else if(bytearray[i] == '0' && carrybit == '0')
			{
				bytearray[i] = '0';
				carrybit = '0';
			}
			else
			{
				bytearray[i] = '1';
				carrybit = '0';
			}

			i--;
		}
	}

}