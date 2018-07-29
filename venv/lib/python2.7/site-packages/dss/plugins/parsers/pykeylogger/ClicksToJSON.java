import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date; 
public class ClicksToJSON {
	public static void main(String args[]) {
		try {
			if (args.length != 2)
			{
				System.out.println("Usage: java ClicksToJSON <PathToImages> <Output Path>");
				System.exit(-1);
			}
			String answer = "{\ndateTimeFormat: 'ISO-8601',\n\nevents : [\n";
 
			String path = args[0];
			String outputPath = args[1];
			String parsedFilename[];
			//For each file in the directory...
			File dir = new File(path);
			File[] directoryListing = dir.listFiles();
			if (directoryListing != null) {
				for (File child : directoryListing) {
					parsedFilename = child.getName().trim().split("_");
					if(parsedFilename.length < 2)
						continue;
					//unescape characters for use by html
					answer += "\t{content:' ',\n";
					answer += "\ttype:'point',\n";
					answer += "\tclassname:'imgPoint',\n";
					answer += "\ttitle: '"+child.getAbsolutePath()+"',\n";
					answer += "start': '" + new Date(((long)Double.parseDouble(parsedFilename[0])*1000)).toString() + "'\n";			
					answer += "\t},\n\n";
				}
			} 
		System.out.println(answer + "]\n}");
        FileOutput.WriteToFile(outputPath + "/clickedImages.JSON", answer);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
