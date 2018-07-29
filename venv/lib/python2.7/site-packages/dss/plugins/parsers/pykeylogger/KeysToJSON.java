import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date; 
public class KeysToJSON{
	public static void main(String args[]) {
		try {
			if (args.length != 2)
			{
				System.out.println("Usage: java KeysToJSON <filename> <output-directory>");
				System.exit(-1);
			}
			String answer = "[";
			String filename = args[0];
			String outputDirectory = args[1];
			// if file doesnt exists, then create it

			//{content:'<2 p/s', className:'traffic', title:'eth:ipv6:udp:dhcpv6 \n', start:'Wed Oct 08 10:56:33 EDT 2014'},

			FileReader fr = new FileReader(filename);
			BufferedReader br = new BufferedReader(fr);
			String line;
			String parsedLine[];
			//int delayBeforeNewString = 2; REMOVED FOR TESTING AF
			int delayBeforeNewString = 3;
			double currKeyTime = 0;
			double prevKeyTime = 0;
			double currWindowStartTime = 0;
			String setOfKeys = "";
			line = br.readLine();
			if(line != null)
			{
			currWindowStartTime = Double.parseDouble(line.trim().split("\\|",-1)[1]);
			prevKeyTime = currWindowStartTime;
			//check if we have another line to read
			while (line != null) {
				//remove new line and split the line using | as the delimeter
				//parsedLine = line.trim().split("\\|",-1);
				parsedLine = line.split("\\|",-1);
				//if we have data
				if(parsedLine.length > 1)
				{
				currKeyTime = Double.parseDouble(parsedLine[1]);
				//if this line is within the time threshold and it is not the last line
				if(parsedLine[parsedLine.length-1].contains("KeyName:")){
						parsedLine[parsedLine.length-1] = parsedLine[parsedLine.length-1].replace("KeyName:","");
				}
				//if(currKeyTime - prevKeyTime < delayBeforeNewString && !parsedLine[parsedLine.length-1].equals("[Return]") && br.ready()){ REMOVED FOR TESTING TIMEFRAMES AF
				if(currKeyTime - prevKeyTime < delayBeforeNewString && br.ready()){
					
					//need to add the space back in (trim removes it).
					if(parsedLine[parsedLine.length-1].equals("")){
						setOfKeys += " ";
					}
					else{												
						setOfKeys += parsedLine[parsedLine.length-1];
					}
				}
				else{
					if(!setOfKeys.trim().equals(""))
					{
						//unescape characters for use by html
						answer += "{";
						answer += "content:'";
						answer += setOfKeys;
						answer += "', ";
						
						answer += "className:'Keypresses";
						answer += "', ";
										
						answer += "start:'";
						//answer += new Date(((long)(currWindowStartTime*1000))).toString() + "',";
						answer += new Date(((long)(prevKeyTime*1000))).toString();
						answer += "'";
						answer += "}";
						
						if(parsedLine[parsedLine.length-1].equals(""))
							setOfKeys = " ";
						else
							setOfKeys = parsedLine[parsedLine.length-1];
						currWindowStartTime = currKeyTime;
						//check if this was the last of the input, if not add a comma
						if(br.ready())
							answer += ",";
						answer += "\n";
					}
				}
			}
				prevKeyTime = currKeyTime;
				line = br.readLine();
			} 
		}
		//System.out.println(answer + "]\n");
		br.close();
		answer += "]\n";
		String strOSName = System.getProperty("os.name");
        if(strOSName.toLowerCase().indexOf("windows") != -1)
		    FileOutput.WriteToFile(outputDirectory+"/keypressData.JSON", answer);
        else
           		FileOutput.WriteToFile(outputDirectory+"\\keypressData.JSON", answer);
		
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
