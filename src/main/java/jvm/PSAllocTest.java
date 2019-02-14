package jvm;

public class PSAllocTest {
  private static final int _1KB = 1024;  
  private static final int _1MB = _1KB * 1024;  
    
  /** 
   * -Xmx100M -XX:+PrintGCDetails -XX:MaxNewSize=40M -XX:+PrintTenuringDistribution 
   */  
  public static void testAllocation() {  
    byte[] byte1 = new byte[_1MB*5];  
    byte[] byte2 = new byte[_1MB*10];  
    byte1 = null;  
    byte2 = null;                         
    byte[] byte3 = new byte[_1MB*5];  
    byte[] byte4 = new byte[_1MB*10];  
    byte3 = null;  
    byte4 = null;  
    byte[] byte5 = new byte[_1MB*15];   
  }  
    
  public static void main(String[] args) {  
    testAllocation();  
  }  
} 