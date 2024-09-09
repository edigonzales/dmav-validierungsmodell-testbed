package ch.interlis.testbed;

public class Main {

    public static void main(String[] args) {
        String dataDirectory = null;
        String config = null;
        String modeldir = null;

        int argi=0;
        for (; argi<args.length; argi++){
            String arg = args[argi];
            
            if (arg.equals("--data")) {
                argi++;
                dataDirectory = args[argi];
            } else if (arg.equals("--config")) {
                argi++;
                config = args[argi];
            } else if (arg.equals("--modeldir")) {
                argi++;
                modeldir = args[argi];
            }
        }
        
        System.out.println("Hallo Welt.");
        System.out.println(dataDirectory);
        System.out.println(config);
        System.out.println(modeldir);

        Testbed testbed = new Testbed();
        testbed.run(dataDirectory, config, modeldir);
    }
}
