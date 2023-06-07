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

        // Print arguments to check
        System.out.println("Set Index: " + setIndex);
        System.out.println("Lines Per Set: " + linesPerSet);
        System.out.println("Block Bits: " + blockBits);
        System.out.println("Trace file: " + traceFile);

        // Test cache simulator
        String testAddress = "000ebe21";
        CacheSimulator cacheSimulator = new CacheSimulator();
        Cache cache = new Cache(setIndex, linesPerSet, blockBits);
        cacheSimulator.accessCache(cache, testAddress);
    }
}