package main;

import main.comms.PortWriter;

public class Serial_Launcher {

	public static PortWriter pWriter;

	public static void main(String[] args) {
		pWriter = new PortWriter();
		pWriter.writeToPort(255);
	}

}
