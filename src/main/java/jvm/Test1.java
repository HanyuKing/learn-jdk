package jvm;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

public class Test1 {
 public static void main(String[] args) {
  try {
   Process p = null;
   String line = null;
   BufferedReader stdout = null;

   //list the files and directorys under C:\
   p = Runtime.getRuntime().exec("CMD.exe /C dir", null, new File("C:\\"));
   stdout = new BufferedReader(new InputStreamReader(p
           .getInputStream()));
   while ((line = stdout.readLine()) != null) {
    System.out.println(line);
   }
   stdout.close();

   //echo the value of NAME
   p = Runtime.getRuntime().exec("CMD.exe /C echo %NAME%", new String[]{"NAME=TEST"});
   stdout = new BufferedReader(new InputStreamReader(p
           .getInputStream()));
   while ((line = stdout.readLine()) != null) {
    System.out.println(line);
   }
   stdout.close();
  } catch (Exception e) {
   e.printStackTrace();
  }
 }

 @Test
 public void test() {
  String str1 = "[null,null,null,null,2147483647,null,2147483646,null,2147483646,null,null,2147483647,2147483646,null,-2147483648,-2147483648,null,2147483647]";
  String str2 = "[null,null,null,null,2147483647,null,2147483646,null,2147483646,null,null,2147483647,2147483647,null,-2147483648,-2147483648,null,2147483647]";
  System.out.println(str1.equals(str2));
 }
}