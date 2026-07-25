package test;

import com.fasterxml.jackson.databind.SerializationFeature;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.StringReader;
import java.io.StringWriter;

public class XmlFormatter {
    public static void main(String[] args) {
        String inputXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                "<server>" +
                "   <id>" + 1 + "</id>" +
                "   <name>" + "Server 1" + "</name>" +
                "<status>" + "Running" + "</status>" +
                "<cpu>" + 80 + "</cpu>" +
                "<memory>" + 16384 + "</memory>" +
                "<location>" + "Data Center A" + "</location>" +
                "</server>";
        System.out.println("inputXml = " + inputXml);

        String formattedXml = formatXml(inputXml);
        System.out.println(formattedXml);
    }

    public static String formatXml(String inputXml) {
        try {
            inputXml = inputXml.replaceAll(">\\s+<", "><");
            Source xmlInput = new StreamSource(new StringReader(inputXml));
            StringWriter stringWriter = new StringWriter();
            StreamResult xmlOutput = new StreamResult(stringWriter);
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
            transformer.transform(xmlInput, xmlOutput);
            return xmlOutput.getWriter().toString();
        } catch (Exception e) {
            throw new RuntimeException("Failed to format XML string", e);
        }
    }
}