import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.DataInputStream;
import java.io.PrintWriter;
public class App {
    public static void main(String[] args) throws Exception {
        int setIndex = 0, linesPerSet = 0, blockBits = 0;
        String traceFile = "";
        
        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "-s":
                    setIndex = Integer.parseInt(args[++i]);
                    break;
                case "-E":
                    linesPerSet = Integer.parseInt(args[++i]);
                    break;
                case "-b":
                    blockBits = Integer.parseInt(args[++i]);
                    break;
                case "-t":
                    traceFile = args[++i];
                    break;
                default:
                    System.out.println("Invalid argument: " + args[i]);
                    System.exit(1);
            }
        }

        // Now you have the parameters, you can print them to check
        System.out.println("Set Index: " + setIndex);
        System.out.println("Lines Per Set: " + linesPerSet);
        System.out.println("Block Bits: " + blockBits);
        System.out.println("Trace file: " + traceFile);
        
        FileReader reader = new FileReader("..\\traces\\"+traceFile);
    	int data = reader.read();
    	ArrayList<String> traceLine = new ArrayList<>();
    	traceLine.add("");
    	int traceTemp=0;
    	while(data!=-1) {
    		//we read one line at a time, only saving one line in the same array at a time. access the line elements in the else block.
    		if(data==' ') {
    			traceTemp++;
    			data=reader.read();
    			traceLine.add("");
    		}	
    		else if(data==',') {
    			data=reader.read();
    		}
    		else if(Character.isAlphabetic(data)||Character.isDigit(data)) {
    			traceLine.set(traceTemp, traceLine.get(traceTemp).concat(""+(char)data));
    			data=reader.read();
    		}
    		else {
    			for(int i = 0; i<traceLine.size();i++) {
    				System.out.print(traceLine.get(i)+" ");
    			}
    			System.out.println();
    			traceLine.clear();
    			traceLine.add("");
    			traceTemp=0;
    			data=reader.read();
    		}
    	}
    	reader.close();
    	readRam();
    }
    public static void readRam() throws IOException {
    	PrintWriter writer = new PrintWriter("..\\Ram.txt");
    	FileInputStream reader = new FileInputStream("..\\RAM.dat");
    	DataInputStream in = new DataInputStream(reader);
    	int data;
    	byte[] b= {,};
    	boolean eof=false;
    	while(!eof) {
    		try {
				data=in.readInt();
//				System.out.print(Integer.toHexString(data)+" ");
//				writer.write(Integer.toHexString(data)+" ");
				writer.write(String.format("%x", data));
			} catch (EOFException e) {
				eof=true;
			}
    		
    	}
    }
}