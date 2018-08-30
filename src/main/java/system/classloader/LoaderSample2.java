package system.classloader;/*LoaderSample2.java*/

import    java.net. * ; 
import    java.lang.reflect. * ; 
public     class    LoaderSample2 { 
         public     static     void    main(String[] args) { 
                 try    { 
                        String path    =    System.getProperty( "user.dir" );
                     System.out.println(path    +     "/src/main/java/system/classloader");
                        URL[] us    =    { new    URL( "file://"     +    path    +     "/src/main/java/system/classloader" )};
                        ClassLoader loader    =     new    URLClassLoader(us); 
                        Class c    =    loader.loadClass( "LoaderSample3" );
                        Object o    =    c.newInstance(); 
                        Field f    =    c.getField( " age " ); 
                         int    age    =    f.getInt(o); 
                        System.out.println( " age is    "     +    age); 
                }    catch    (Exception e) { 
                        e.printStackTrace(); 
                } 
        } 
}