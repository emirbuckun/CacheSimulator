public class Cache {
  int s; // set index bits
  int E; // number of lines per set
  int b; // block bits
  CacheLine[][] cache; // cache itself

  Cache(int s, int E, int b) {
    this.s = s;
    this.E = E;
    this.b = b;
    int S = 1 << s; // number of sets
    this.cache = new CacheLine[S][E];
    for (int i = 0; i < S; i++) {
      for (int j = 0; j < E; j++) {
        this.cache[i][j] = new CacheLine();
      }
    }
  }
}

class CacheLine {
  boolean valid;
  String tag;
  // other fields here..

  CacheLine() {
    this.valid = false;
    this.tag = "";
  }
}