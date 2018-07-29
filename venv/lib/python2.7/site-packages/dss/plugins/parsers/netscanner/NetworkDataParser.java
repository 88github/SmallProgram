import java.io.*;
import java.util.*;

public class NetworkDataParser {

  public static void main(String args[]) {
	  String ifx = "";
	  String inputFilename = "";
	  String outputDirectory = "";
	  try
	  {
		  
		if(args.length == 2)
		{
			//ifx = args[0]; //eventually we will have the option to output this in-vivo
			inputFilename = args[0];
			outputDirectory = args[1];
		}
		else
		{
			System.out.println("Usage: java NetworkDataParser <input_pcap_filename> <output-directory>");
			System.exit(-1);
		}
	startPassiveScanner(ifx, inputFilename, outputDirectory);
	}
	catch(Exception err)
	{
		System.out.println("ERROR: " + err.toString());
	} 
	  
  }
  
  protected static void startPassiveScanner(String ifx, String inputFilename, String outputDirectory)
  {
	  int windowSize = 10;
	  
	  try {
			String buffer = "";
			String tsharkCommand = "tshark";
			Process process = new ProcessBuilder(
			//tsharkCommand, "-i", ifx, "-n", "-T", "fields", "-E", "separator=,", "-eframe.protocols", "-eframe.time_epoch", "-eeth.src", "-eeth.dst", "-earp.src.proto_ipv4", "-earp.dst.proto_ipv4", "-eip.src", "-eip.dst", "-eudp.srcport", "-eudp.dstport", "-etcp.srcport", "-etcp.dstport", "-erip.ip", "-erip.netmask", "-erip.next_hop", "-erip.metric" ).start();
			tsharkCommand, "-r", inputFilename, "-T", "fields", "-E", "separator=,", "-eframe.protocols", "-eframe.time_epoch", "-eeth.src", "-eeth.dst", "-earp.src.proto_ipv4", "-earp.dst.proto_ipv4", "-eip.src", "-eip.dst", "-eudp.srcport", "-eudp.dstport", "-etcp.srcport", "-etcp.dstport", "-erip.ip", "-erip.netmask", "-erip.next_hop", "-erip.metric" ).start();
			InputStream is = process.getInputStream();
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
			String line;
			FramePacketData frame;
			PacketData packetData = null;
			while ((line = br.readLine()) != null) {
				frame = new FramePacketData(line);
				if (frame.getFrameProtocols().startsWith("eth:ip:tcp"))
					packetData = new EthIpTcpPacketData(line);
				else if (frame.getFrameProtocols().startsWith("eth:ip:udp:rip"))
					packetData = new EthIpUdpRipPacketData(line);
				else if (frame.getFrameProtocols().startsWith("eth:ip:udp"))
					packetData = new EthIpUdpPacketData(line);
				else if (frame.getFrameProtocols().startsWith("eth:ip"))
					packetData = new EthIpPacketData(line);
				else if (frame.getFrameProtocols().startsWith("eth:arp"))
					packetData = new EthArpPacketData(line);
				else if (frame.getFrameProtocols().startsWith("eth"))
					packetData = new EthPacketData(line);
				buffer+=packetData.toString() + "\n";
		}

    String strOSName = System.getProperty("os.name");
    //TimeDisplayStringFormatter.formatXMLString(buffer, windowSize);
	//TimeDisplayStringFormatter.formatJSONString(buffer, windowSize);
	String output = TimeDisplayStringFormatter.formatJSONString(buffer, windowSize);

    if(strOSName.toLowerCase().indexOf("windows") != -1){
        FileOutput.WriteToFile(outputDirectory+"/networkDataAll.JSON", output);
        output = TimeDisplayStringFormatter.formatJSONStringXY(buffer, windowSize);
        FileOutput.WriteToFile(outputDirectory+"/networkDataXY.JSON", output);
        }
    else{
        FileOutput.WriteToFile(outputDirectory+"\\networkDataAll.JSON", output);
        output = TimeDisplayStringFormatter.formatJSONStringXY(buffer, windowSize);
        FileOutput.WriteToFile(outputDirectory+"\\networkDataXY.JSON", output);
	}
    }
    catch (Exception err) {
      err.printStackTrace();
    }
  }  
}
