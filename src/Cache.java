public class Cache {
  int s; // set index bits
  int E; // number of lines per set
  int b; // block bits
  CacheLine[][] cache; // cache lines

  Cache(int s, int E, int b) {
    this.s = s;
    this.E = E;
    this.b = b;
    int S = 1 << s; // number of sets
    int B = 1 << b; // number of sets
    this.cache = new CacheLine[S][E];
    for (int i = 0; i < S; i++) {
      for (int j = 0; j < E; j++) {
        this.cache[i][j] = new CacheLine(B);
      }
    }
  }

  @Override
  public String toString() {
    int S = 1 << s; // number of sets
    String result = "";
    for (int i = 0; i < S; i++) {
      result += "Set" + (i + 1) + "\n";
      for (int j = 0; j < E; j++) {
        result += this.cache[i][j].toString() + "\n";
      }
    }
    return result;
  }
}

class CacheLine {
  boolean valid;
  String tag;
  byte[] data;
  int age;

  CacheLine(int blockSize) {
    this.valid = false;
    this.tag = "";
    this.data = new byte[blockSize];
    this.age = 0;
  }

  private String byteArrayToHex(byte[] byteArray) {
    StringBuilder sb = new StringBuilder(byteArray.length * 2);
    for (byte byteData : byteArray)
      sb.append(String.format("%02x", byteData));
    return sb.toString();
  }

  @Override
  public String toString() {
    return " Cache Line: " +
        "valid = " + valid +
        ", tag = " + (tag == "" ? "null" : tag) +
        ", age = " + age +
        ", data = " + byteArrayToHex(data);
  }
}