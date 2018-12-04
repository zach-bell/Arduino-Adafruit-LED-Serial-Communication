package main.comms;

import java.io.*;
import javax.comm.*;
import java.util.*;

public class PortWriter {
	@SuppressWarnings("rawtypes")
	public Enumeration ports;
	public CommPortIdentifier pID;
	public OutputStream outStream;
	public SerialPort serPort;

	public PortWriter() {
		ports = CommPortIdentifier.getPortIdentifiers();		// Populate all ports
		while (ports.hasMoreElements()) {						// Iterate through all ports
			pID = (CommPortIdentifier) ports.nextElement();		// get the pID of port
			System.out.println("Port " + pID.getName());		// Prints it to console
			if (pID.getPortType() == CommPortIdentifier.PORT_SERIAL) {
				if (pID.getName().equals("COM1")) {
					System.out.println("COM1 found");
				}
			}
		} // end of while
		
		initialize();
	}
	
	public void initialize() {
		try {
			serPort = (SerialPort) pID.open("PortWriter", 2000);
			outStream = serPort.getOutputStream();
			serPort.setSerialPortParams(2000000, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
		} catch (Exception e) {
			System.out.println("Yea. Something bad happened in initiallizing portwriter.\n" + e.getMessage());
			e.printStackTrace();
		}
	}
	
	public void writeToPort(int data) {
		try {
			outStream.write(data);
		} catch (Exception e) {
			System.out.println("Yea. Something bad happened when writing to port.\n" + e.getMessage());
			e.printStackTrace();
		}
	}
}
