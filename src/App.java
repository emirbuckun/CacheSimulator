import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class App {
    public static final StringBuilder statusTracking = new StringBuilder();
    private static int setIndex;
    private static int linesPerSet;
    private static int blockBits;
    private static String traceFileName;
    private static byte[] ram;
    private static Cache cache;

    public static void main(String[] args) throws Exception {
        parseArgs(args);
        createCache();
        readRam();
        writeRam();

        // File operations
        FileReader reader = new FileReader("traces/" + traceFileName);
        int data = reader.read();
        ArrayList<String> traceLine = new ArrayList<>();
        traceLine.add("");
        int traceTemp = 0;

        // We read one line at a time
        // Only saving one line in the same array at a time
        // Access the line elements in the else block.
        while (data != -1) {
            if (data == ' ') {
                traceTemp++;
                data = reader.read();
                traceLine.add("");
            } else if (data == ',') {
                data = reader.read();
            } else if (Character.isAlphabetic(data) || Character.isDigit(data)) {
                traceLine.set(traceTemp, traceLine.get(traceTemp).concat("" + (char) data));
                data = reader.read();
            } else {
                for (int i = 0; i < traceLine.size(); i++) {
                    System.out.print(traceLine.get(i) + " ");
                }
                System.out.println();
                traceLine.clear();
                traceLine.add("");
                traceTemp = 0;
                data = reader.read();
            }
        }
        reader.close();
        printCacheContent(cache);
    }

    // RAM FUNCTIONS
    private static void readRam() throws IOException {
        Path filepath = FileSystems.getDefault().getPath("RAM.dat");
        if (!Files.exists(filepath)) {
            System.err.printf("File not found: %s\n", "RAM.dat");
        }
        try {
            ram = Files.readAllBytes(filepath);
        } catch (IOException e) {
            System.err.println("Error in readRam function.");
            e.printStackTrace();
            System.exit(e.hashCode());
        }
    }

    private static void writeRam() {
        Path filepath = FileSystems.getDefault().getPath("RAM_out.dat");
        try {
            Files.write(filepath, ram);
        } catch (IOException e) {
            System.out.println("Error in writeRam function.");
            e.printStackTrace();
            System.exit(e.hashCode());
        }
    }
    // END

    // ARGUMENT FUNCTIONS
    private static void parseArgs(String[] args) {
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
                    traceFileName = args[++i];
                    break;
                default:
                    System.out.println("Invalid argument: " + args[i]);
                    System.exit(1);
            }
        }
    }
    // END

    // CACHE FUNCTIONS
    private static void createCache() {
        cache = new Cache(setIndex, linesPerSet, blockBits);
    }

    private static void printCacheContent(Cache cache) {
        try {
            Path filePath = FileSystems.getDefault().getPath("Cache.txt");
            Files.writeString(filePath, ""); // Clear content
            System.out.println("Cache Content: "); // Print content
            System.out.println(cache.toString());
            Files.writeString(filePath, cache + "\n", StandardOpenOption.APPEND);
        } catch (Exception e) {
            System.out.println("Error in printCacheContents function.");
            e.printStackTrace();
            System.exit(e.hashCode());
        }
    }
    // END
}