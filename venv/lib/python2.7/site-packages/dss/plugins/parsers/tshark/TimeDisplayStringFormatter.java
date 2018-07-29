import java.util.HashSet;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

public class TimeDisplayStringFormatter {
	static HashSet<String> uniqueElementsPerWindow = new HashSet<String>();
	
	public static String formatXMLString(String buffer, double windowSize) {
		String answer = "<?xml version='1.0' encoding='UTF-8'?>\n"+
					"<data>\n";
		String[] lines = buffer.split(System.getProperty("line.separator"));
		double currWindowStartTime = 0.0;
		double currPacketTime = 0.0;
		int packetsPerSecond = 0;
		String tempString;
		String[] parsedPacket;
		try {						
			//first grab the time_epoch from the line:
			if(lines.length > 0)
			{
				currWindowStartTime = Double.parseDouble(lines[0].split(" ",-1)[PacketData.Fields.FRAME_TIMEEPOCH.ordinal()]);

			for (int i=0; i<lines.length; i++)
			{
				//System.out.println("line: " + lines[i]);
				parsedPacket = lines[i].split(" ",-1);
				currPacketTime = Double.parseDouble(parsedPacket[PacketData.Fields.FRAME_TIMEEPOCH.ordinal()]);;
				//System.out.println(lines[i]);
				if((currPacketTime - currWindowStartTime) < windowSize && i != lines.length-1)
				{
					//remove any unwanted elements from the array before storing in the map
					//System.out.println(parsedPacket[PacketData.Fields.FRAME_TIMEEPOCH.ordinal()] );
					lines[i] = lines[i].replace(parsedPacket[PacketData.Fields.FRAME_TIMEEPOCH.ordinal()], "");
					lines[i] = lines[i].replace(parsedPacket[PacketData.Fields.ETH_SRC.ordinal()], "");
					lines[i] = lines[i].replace(parsedPacket[PacketData.Fields.ETH_DST.ordinal()], "");
					//System.out.println(lines[i] + "\nremoved: " + parsedPacket[PacketData.Fields.FRAME_TIMEEPOCH.ordinal()] + "\n");
					uniqueElementsPerWindow.add(lines[i]+"\n");
				}
				else
				{
					//System.out.println("Starting new window");
					packetsPerSecond = (int)(uniqueElementsPerWindow.size()/windowSize);
					answer += "<event durationEvent='true' start='" + new Date((long)(currWindowStartTime*1000)).toString();
					answer += "' title='" ;
					packetsPerSecond = (int)(uniqueElementsPerWindow.size()/windowSize);
					if(packetsPerSecond<2)
						answer+="&lt;2 p/s";
					else answer+=packetsPerSecond+" p/s";
					answer += "' end='" +new Date((long)(currPacketTime*1000)).toString()+"'>\n";
					for(String element : uniqueElementsPerWindow)
					{
						tempString = element.replace(";","&lt;br/&gt;");
						tempString = tempString.replace("\n","&lt;br/&gt;");
						answer += tempString;
					}
					answer += "\n</event>";
					currWindowStartTime = currPacketTime;
					uniqueElementsPerWindow.clear();
					//remove any unwanted elements from the array before storing in the map
					
					lines[i] = lines[i].replace(parsedPacket[PacketData.Fields.FRAME_TIMEEPOCH.ordinal()], "");
					lines[i] = lines[i].replace(parsedPacket[PacketData.Fields.ETH_SRC.ordinal()], "");
					lines[i] = lines[i].replace(parsedPacket[PacketData.Fields.ETH_DST.ordinal()], "");
					//System.out.println("removed: " + parsedPacket[PacketData.Fields.FRAME_TIMEEPOCH.ordinal()] + "\n");
					uniqueElementsPerWindow.add(lines[i]+"\n");
				} 
			}
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		answer += "</data>";
		return answer;
	}
	
		public static String formatJSONString(String buffer, double windowSize) {
		String answer = "[";
		String[] lines = buffer.split(System.getProperty("line.separator"));
		double currWindowStartTime = 0.0;
		double currPacketTime = 0.0;
		int packetsPerSecond = 0;
		String tempString;
		String[] parsedPacket;
		try {						
			//first grab the time_epoch from the line:
			if(lines.length > 0)
			{
				currWindowStartTime = Double.parseDouble(lines[0].split(" ",-1)[PacketData.Fields.FRAME_TIMEEPOCH.ordinal()]);

			for (int i=0; i<lines.length; i++)
			{
				//System.out.println("line: " + lines[i]);
				parsedPacket = lines[i].split(" ",-1);
				currPacketTime = Double.parseDouble(parsedPacket[PacketData.Fields.FRAME_TIMEEPOCH.ordinal()]);;
				//System.out.println(lines[i]);
				if((currPacketTime - currWindowStartTime) < windowSize && i != lines.length-1)
				{
					//remove any unwanted elements from the array before storing in the map
					//System.out.println(parsedPacket[PacketData.Fields.FRAME_TIMEEPOCH.ordinal()] );
					lines[i] = lines[i].replace(parsedPacket[PacketData.Fields.FRAME_TIMEEPOCH.ordinal()], "");
					lines[i] = lines[i].replace(parsedPacket[PacketData.Fields.ETH_SRC.ordinal()], "");
					lines[i] = lines[i].replace(parsedPacket[PacketData.Fields.ETH_DST.ordinal()], "");
					//System.out.println(lines[i] + "\nremoved: " + parsedPacket[PacketData.Fields.FRAME_TIMEEPOCH.ordinal()] + "\n");
					uniqueElementsPerWindow.add(lines[i]+"\n");
				}
				else
				{
					//System.out.println("Starting new window");
					packetsPerSecond = (int)(uniqueElementsPerWindow.size()/windowSize);
					answer += "{ id:'af"+i+"', ";
					answer += "content:'" ;
					packetsPerSecond = (int)(uniqueElementsPerWindow.size()/windowSize);
					if(packetsPerSecond<1)
						answer+="<1 p/s";
					else answer+=packetsPerSecond+" p/s";
					answer += "', ";
					
					answer += "className:'traffic";
					answer += "', ";
					
					answer += "title:'";
					for(String element : uniqueElementsPerWindow)
					{
						tempString = element.replace(";","\n");
						tempString = tempString.replace("\n","\\n");
						answer += tempString;
					}
					answer += "', ";
										
					answer += "start:'" + new Date((long)(currWindowStartTime*1000)).toString();
					if(i == lines.length-1){
						answer += "' }\n";
					}
					else{
						answer += "' },\n";
					}

					currWindowStartTime = currPacketTime;
					uniqueElementsPerWindow.clear();
					//remove any unwanted elements from the array before storing in the map
					
					lines[i] = lines[i].replace(parsedPacket[PacketData.Fields.FRAME_TIMEEPOCH.ordinal()], "");
					lines[i] = lines[i].replace(parsedPacket[PacketData.Fields.ETH_SRC.ordinal()], "");
					lines[i] = lines[i].replace(parsedPacket[PacketData.Fields.ETH_DST.ordinal()], "");
					//System.out.println("removed: " + parsedPacket[PacketData.Fields.FRAME_TIMEEPOCH.ordinal()] + "\n");
					uniqueElementsPerWindow.add(lines[i]+"\n");
				} 
			}
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		answer += "]";
		return answer;
	}
		
	public static String formatJSONStringXY(String buffer, double windowSize) {
		String answer = "[";
		String[] lines = buffer.split(System.getProperty("line.separator"));
		double currWindowStartTime = 0.0;
		double currPacketTime = 0.0;
		int packetsPerSecond = 0;
		String tempString;
		String[] parsedPacket;
		try {						
			//first grab the time_epoch from the line:
			if(lines.length > 0)
			{
				currWindowStartTime = Double.parseDouble(lines[0].split(" ",-1)[PacketData.Fields.FRAME_TIMEEPOCH.ordinal()]);

			for (int i=0; i<lines.length; i++)
			{
				//System.out.println("line: " + lines[i]);
				parsedPacket = lines[i].split(" ",-1);
				currPacketTime = Double.parseDouble(parsedPacket[PacketData.Fields.FRAME_TIMEEPOCH.ordinal()]);;
				//System.out.println(lines[i]);
				if((currPacketTime - currWindowStartTime) < windowSize && i != lines.length-1)
				{
					//remove any unwanted elements from the array before storing in the map
					//System.out.println(parsedPacket[PacketData.Fields.FRAME_TIMEEPOCH.ordinal()] );
					lines[i] = lines[i].replace(parsedPacket[PacketData.Fields.FRAME_TIMEEPOCH.ordinal()], "");
					lines[i] = lines[i].replace(parsedPacket[PacketData.Fields.ETH_SRC.ordinal()], "");
					lines[i] = lines[i].replace(parsedPacket[PacketData.Fields.ETH_DST.ordinal()], "");
					//System.out.println(lines[i] + "\nremoved: " + parsedPacket[PacketData.Fields.FRAME_TIMEEPOCH.ordinal()] + "\n");
					uniqueElementsPerWindow.add(lines[i]+"\n");
				}
				else
				{
					//System.out.println("Starting new window");
						
					packetsPerSecond = (int)(uniqueElementsPerWindow.size()/windowSize);
					answer += "{";
					answer += "x:'" + new Date((long)(currWindowStartTime*1000)).toString();
					answer += "', ";			
					
					answer += "y: " ;
					packetsPerSecond = (int)(uniqueElementsPerWindow.size()/windowSize);
					answer+=packetsPerSecond;
					if(i == lines.length-1){
						answer += " }\n";
					}
					else{
						answer += " },\n";
					}


					currWindowStartTime = currPacketTime;
					uniqueElementsPerWindow.clear();
					//remove any unwanted elements from the array before storing in the map
					
					lines[i] = lines[i].replace(parsedPacket[PacketData.Fields.FRAME_TIMEEPOCH.ordinal()], "");
					lines[i] = lines[i].replace(parsedPacket[PacketData.Fields.ETH_SRC.ordinal()], "");
					lines[i] = lines[i].replace(parsedPacket[PacketData.Fields.ETH_DST.ordinal()], "");
					//System.out.println("removed: " + parsedPacket[PacketData.Fields.FRAME_TIMEEPOCH.ordinal()] + "\n");
					uniqueElementsPerWindow.add(lines[i]+"\n");
				} 
			}
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		answer += "]";
		return answer;
	}
}
