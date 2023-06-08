public class CacheLine {
  boolean valid;
  String tag;
  byte[] data;
  int lastUsed;

  CacheLine(int blockSize) {
    this.valid = false;
    this.tag = "";
    this.data = new byte[blockSize];
    this.lastUsed = 0;
  }

  private String byteArrayToHex(byte[] byteArray) {
    StringBuilder sb = new StringBuilder(byteArray.length * 2);
    for (byte byteData : byteArray)
      sb.append(String.format("%02x", byteData));
    return sb.toString();
  }

  @Override
  public String toString() {
    return "\n\t\tCache Line: " +
        "valid = " + valid +
        ", tag = " + (tag == "" ? "null" : tag) +
        ", lastUsed = " + lastUsed +
        ", data = " + byteArrayToHex(data);
  }
}
