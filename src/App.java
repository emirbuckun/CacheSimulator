import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;
import java.nio.file.StandardOpenOption;

public class App {
    public static final StringBuilder log = new StringBuilder();
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
        readTrace();
        printOutput();
        writeRam();
        printCache();
    }

    // TRACE FUNCTIONS
    private static void readTrace() {
        Path filePath = FileSystems.getDefault().getPath(traceFileName);

        if (!Files.exists(filePath))
            System.err.printf("File not found: %s\n", traceFileName);

        Scanner traceReader = null;
        try {
            traceReader = new Scanner(filePath);
        } catch (IOException e) {
            System.err.printf("IO error while reading trace file %s\n", traceFileName);
            e.printStackTrace();
            System.exit(e.hashCode());
        }

        while (traceReader.hasNextLine()) {
            String line = traceReader.nextLine();
            parseTraceLine(line);
        }
        traceReader.close();
    }

    private static void parseTraceLine(String line) {
        log.append(line + "\n");
        char operation = line.charAt(0);
        String sAddress = line.substring(2, 10);
        long address = Long.parseLong(sAddress, 16) % ram.length;

        if (operation == 'L') { // Format: operation address, size
            loadData(address);
        } else if (operation == 'M' || operation == 'S') { // Format: operation address, size, data
            String sSize = line.substring(line.indexOf(',') + 2, line.lastIndexOf(','));
            String sData = line.substring(line.lastIndexOf(',') + 2);
            int size = Integer.parseInt(sSize);
            byte[] data = new byte[sData.length() / 2];

            for (int i = 0; i < data.length; i++) {
                data[i] = (byte) Integer.parseInt(sData.substring(i * 2, (i * 2) + 2), 16);
            }

            // execute modify or store operation here
        } else {
            System.err.printf("Invalid operation found in trace:\n%s", line);
            System.exit(-1);
        }
    }
    // END

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

    private static byte[] getData(long address, int blockData) {
        int length = cache.B; // block size
        int start = (int) (address - blockData); // start index
        byte[] result = new byte[length];
        System.arraycopy(ram, start, result, 0, length);
        return result;
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
                    traceFileName = "traces/" + args[++i];
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

    private static void printCache() {
        try {
            Path filePath = FileSystems.getDefault().getPath("Cache.txt");
            Files.writeString(filePath, ""); // Clear content
            Files.writeString(filePath, cache + "\n", StandardOpenOption.APPEND); // Print content
        } catch (Exception e) {
            System.out.println("Error in printCacheContents function.");
            e.printStackTrace();
            System.exit(e.hashCode());
        }
    }

    private static void printOutput() {
        System.out.println(
                "\thits: " + cache.hitCount + " misses: " + cache.missCount + " evictions: " + cache.evictionCount
                        + "\n");
        System.out.print(log);
    }

    private static void loadData(long address) {
        // Convert hex address to binary and split into tag, set index, and block data
        String binaryAddress = String.format("%32s", Long.toBinaryString(address)).replace(' ', '0');
        String sTag = binaryAddress.substring(0, binaryAddress.length() - (cache.s + cache.b));
        String sSetIndex = binaryAddress.substring(sTag.length(), sTag.length() + cache.s);
        String sBlockData = binaryAddress.substring(sTag.length() + cache.s);
        int setIndex = Integer.parseInt(sSetIndex, 2);
        int blockData = Integer.parseInt(sBlockData, 2);

        // Check if the line is in the cache
        boolean hit = false;
        CacheSet set = cache.sets.get(setIndex);
        for (CacheLine line : set.lines) {
            if (line.valid && line.tag.equals(sTag)) {
                hit = true;
                break;
            }
        }

        if (hit) {
            cache.hitCount++;
            log.append("  Hit\n");
            log.append("  Found in cache set " + setIndex + "\n");
        } else {
            cache.missCount++;
            byte[] data = getData(address, blockData);
            set.write(data, sTag);
            log.append("  Miss\n");
            log.append("  Place in cache set " + setIndex + "\n");
        }
    }
    // END
}