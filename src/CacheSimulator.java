public class CacheSimulator {
  // Simulator variables here

  public void accessCache(Cache cache, String address) {
    // Convert hex address to binary and split into tag, set index, and block data
    String binaryAddress = hexToBinary(address);
    String tag = binaryAddress.substring(0, binaryAddress.length() - (cache.s + cache.b));
    String setIndexString = binaryAddress.substring(tag.length(), tag.length() + cache.s);
    int setIndex = Integer.parseInt(setIndexString, 2);
    String blockData = binaryAddress.substring(tag.length() + cache.s);

    // Print computed variables to check
    System.out.println("Binary Address: " + binaryAddress);
    System.out.println("Tag: " + tag);
    System.out.println("Set Index: " + setIndexString);
    System.out.println("Block Data: " + blockData);

    // Check if the line is in the cache
    boolean hit = false;
    for (CacheLine line : cache.cache[setIndex]) {
      if (line.valid && line.tag.equals(tag)) {
        hit = true;
        break;
      }
    }

    if (hit) {
      // ... (increment hit count)
    } else {
      // ... (increment miss count and handle eviction)
    }
  }

  private String hexToBinary(String hex) {
    String binaryAddress = Integer.toBinaryString(Integer.parseInt(hex, 16));
    String result = String.format("%32s", binaryAddress).replace(' ', '0');
    return result;
  }
}