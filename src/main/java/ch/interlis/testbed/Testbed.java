package ch.interlis.testbed;

import ch.ehi.basics.settings.Settings;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.interlis2.validator.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

public class Testbed {
    private static Logger log = LoggerFactory.getLogger(Main.class);

    public boolean run(String dataDirectory, String config, String modeldir) { 
        // Alle XTF-Dateien, die nicht eine expected Log-Datei ist.
        List<Path> transferFiles = new ArrayList<Path>();
        try (Stream<Path> walk = Files.walk(Paths.get(dataDirectory))) {
            transferFiles = walk
                    .filter(p -> !Files.isDirectory(p))   
                    .filter(f -> {
                        if (f.toString().toLowerCase().endsWith(".xtf") && !f.toString().toLowerCase().endsWith(".log.xtf")) {
                            return true;
                        }
                        return false;
                    })
                    .collect(Collectors.toList());        
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        
        for (Path transferFile : transferFiles) {
            String transferFileName = transferFile.getFileName().toString().substring(0, transferFile.getFileName().toString().length()-4);
            
            String logFileNameExpected = transferFileName + ".log.xtf";
            Path logFileExpected = transferFile.getParent().resolve(logFileNameExpected);
            
            // XTF validieren
            String logFileNameTest = null;
            try {
                log.info(transferFile.getFileName().toString());

                // Falls im gleichen Verzeichnis nicht eine Logdatei (mit den zu erwartenden Resulaten)
                // liegt, wird das zu prüfende XTF ignoriert.
                if (Files.notExists(logFileExpected)) {
                    log.info("No existing log file found. Ignoring test case: " + transferFile.getFileName().toString());
                    continue;
                }
                
                Path tmpdir = Files.createTempDirectory("testbed_");
                logFileNameTest = tmpdir.resolve(transferFileName + ".log.xtf").toFile().getAbsolutePath();
                
                Settings settings = new Settings();
                settings.setValue(Validator.SETTING_XTFLOG, logFileNameTest);
                
                // Falls modeldir verwendet wird, muss ilidirs korrekt abgeleitet werden.
                // Ansonsten wird default-Wert für ilidirs verwendet.
                if (modeldir != null) {
                    List<String> directories = Files.walk(Paths.get(modeldir))
                            .filter(Files::isDirectory)
                            .map(d -> {return d.toString();})
                            .collect(Collectors.toList());
                    settings.setValue(Validator.SETTING_ILIDIRS, String.join(";", directories));
                } else {
                    settings.setValue(Validator.SETTING_ILIDIRS, Validator.SETTING_DEFAULT_ILIDIRS);
                }
                
                if (config != null) {
                    settings.setValue(Validator.SETTING_CONFIGFILE, config);                    
                }
                boolean valid = Validator.runValidation(transferFile.toFile().getAbsolutePath(), settings);
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
            
            // Die jeweiligen LogEvents (expected und test) auslesen
            // und vergleichen (müssen indentisch sein).
            // Anschliessend in Logfile schreiben.
            boolean notEqualLogEvents = false;
            try {
                List<LogEvent> logEventsExpected = getLogEvents(logFileExpected);
                List<LogEvent> logEventsTest = getLogEvents(Paths.get(logFileNameTest));
                {
                    List<LogEvent> differences = logEventsTest.stream()
                            .filter(element -> !logEventsExpected.contains(element))
                            .collect(Collectors.toList());
                    if (differences.size() > 0) {
                        log.info("Test result does not correspond to the expected result (expected minus test): " + differences);   
                        notEqualLogEvents = true;
                    }
                }
                
                {
                    List<LogEvent> differences = logEventsExpected.stream()
                            .filter(element -> !logEventsTest.contains(element))
                            .collect(Collectors.toList());                    
                    if (differences.size() > 0) {
                        log.info("Expected result does not correspond to the test result (test minus expected): " + differences);
                        notEqualLogEvents = true;
                    }
                }                
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
            if (notEqualLogEvents) {
                return false;
            }
        }       
        return true;
    }
    
    private List<LogEvent> getLogEvents(Path logFile) throws IOException {
        List<LogEvent> logEvents = new ArrayList<>();
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(Files.newInputStream(logFile));            
            doc.getDocumentElement().normalize();

            XPathFactory xPathFactory = XPathFactory.newInstance();
            XPath xPath = xPathFactory.newXPath();

            String expression = "//IliVErrors.ErrorLog.Error[Type='Error' or Type='Warnung']";  
            XPathExpression xPathExpression = xPath.compile(expression);

            NodeList nodeList = (NodeList) xPathExpression.evaluate(doc, javax.xml.xpath.XPathConstants.NODESET);

            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);

                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;

                    String typeValue = element.getElementsByTagName("Type").item(0).getTextContent();
                    String message = element.getElementsByTagName("Message").item(0).getTextContent();
                    
                    LogEvent logEvent = new LogEvent(LogEventType.valueOf(typeValue.toUpperCase()), message);
                    logEvents.add(logEvent);
                }
            }
        } catch (ParserConfigurationException | SAXException | XPathExpressionException e) {
            e.printStackTrace();
            throw new IOException(e);
        }        
        return logEvents;
    }
}
