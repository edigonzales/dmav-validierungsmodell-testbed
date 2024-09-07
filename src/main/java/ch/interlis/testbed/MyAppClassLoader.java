package ch.interlis.testbed;

import java.net.URL;
import java.net.URLClassLoader;

public class MyAppClassLoader extends URLClassLoader {
    public MyAppClassLoader(URL[] urls, ClassLoader parent) {
        super(urls, parent);
     }
}
