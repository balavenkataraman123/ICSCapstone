// this sub routine is used to read the XML files. It is taken from howtodoinjava.com . However, it is only a minor part of the program and just used to load the car data from its XML files into the classes.
// THIS CLASS IS NOT IN USE RIGHT NOW. PLEASE IGNORE.


import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;

public class XMLReader {
    public static Document readXMLDocumentFromFile(String fileNameWithPath) throws Exception {

        //Get Document Builder
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();

        //Build Document
        Document document = builder.parse(new File(fileNameWithPath));
        document.getDocumentElement().normalize();

        //Normalize the XML Structure; It's just too important !!
        return document;
    }
}
