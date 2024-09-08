package ch.interlis.testbed;

public class Main {

    public static void main(String[] args) {
        String dataDirectory = null;
        
        
        int argi=0;
        for (; argi<args.length; argi++){
            String arg = args[argi];
            
            if (arg.equals("--data")) {
                argi++;
                dataDirectory = args[argi];
            }
        }
        
        System.out.println("Hallo Welt.");
        System.out.println(dataDirectory);
        
        
        Testbed testbed = new Testbed();
        testbed.run(dataDirectory);
        
//        System.out.println(System.getProperty("java.class.path"));
    }

}
