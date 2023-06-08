import java.util.ArrayList;
import java.util.List;

public class Cache {
  public static List<CacheSet> sets;

  Cache(int s, int E, int b) {
    int S = 1 << s; // number of sets
    int B = 1 << b; // block size

    sets = new ArrayList<>(S);
    for (int i = 0; i < S; i++) {
      CacheSet set = new CacheSet(E);
      for (int j = 0; j < E; j++)
        set.lines.add(j, new CacheLine(B));
      sets.add(i, set);
    }
  }

  @Override
  public String toString() {
    return "Cache:" + sets;
  }
}