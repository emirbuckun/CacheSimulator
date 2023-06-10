import java.util.ArrayList;
import java.util.List;

public class Cache {
  int s; // set index bits
  int E; // number of lines per set
  int b; // block bits
  int S; // number of sets
  int B; // block size
  public List<CacheSet> sets;
  public int hitCount = 0;
  public int missCount = 0;
  public int evictionCount = 0;

  Cache(int s, int E, int b) {
    this.s = s;
    this.E = E;
    this.b = b;
    this.S = (s == 0) ? 1 : (1 << s); // number of sets
    this.B = 1 << b; // block size
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