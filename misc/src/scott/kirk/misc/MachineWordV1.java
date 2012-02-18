package scott.kirk.misc;

public class MachineWordV1
{
	public static final int bytesinword = 4;

	private MachineByteV1[] bytearray = new MachineByteV1[bytesinword];

	public MachineWordV1(MachineByteV1 byte0in, MachineByteV1 byte1in,
						MachineByteV1 byte2in, MachineByteV1 byte3in)
	{
		for(int i = 0; i < bytesinword; i++)
		{
			bytearray[i] = new MachineByteV1();
		}
		bytearray[0].copyByteIn(byte0in);
		bytearray[1].copyByteIn(byte1in);
		bytearray[2].copyByteIn(byte2in);
		bytearray[3].copyByteIn(byte3in);
	}

	public MachineByteV1 getByte(int index)
	{
		return bytearray[index];
	}
}