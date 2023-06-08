import java.util.ArrayList;
import java.util.List;

public class CacheSet {
  public final List<CacheLine> lines;

  public CacheSet(int associativity) {
    this.lines = new ArrayList<>(associativity);
  }

  private CacheLine getOldest() {
    int max = 0;
    CacheLine maxLine = null;
    for (CacheLine line : lines) {
      if (line.lastUsed > max) {
        max = line.lastUsed;
        maxLine = line;
      }
    }
    return maxLine;
  }

  public void write(byte[] data, String tag, Cache cache) {
    // Increase lastUsed value in each valid line
    for (CacheLine line : lines)
      if (line.valid)
        line.lastUsed++;

    for (CacheLine line : lines) {
      if (!line.valid) {
        line.valid = true;
        line.tag = tag;
        line.data = data;
        line.lastUsed = 0;
        return;
      }
    }

    // Eviction process, FIFO (First In First Out)
    CacheLine oldestLine = getOldest();
    oldestLine.valid = true;
    oldestLine.tag = tag;
    oldestLine.data = data;
    oldestLine.lastUsed = 0;
    cache.evictionCount++;
  }

  @Override
  public String toString() {
    return "\n\tCacheSet" + lines;
  }
}
