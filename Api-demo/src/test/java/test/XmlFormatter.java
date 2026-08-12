package test;

import com.fasterxml.jackson.databind.SerializationFeature;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import java.io.StringReader;
import java.io.StringWriter;
import javax.xml.XMLConstants;
import javax.xml.namespace.NamespaceContext;
import java.util.Iterator;

class IsoNamespaceContext implements NamespaceContext {

    @Override
    public String getNamespaceURI(String prefix) {

        switch (prefix) {

            case "iso":
                return "urn:iso:std:iso:20022:tech:xsd:pacs.008.001.10";

            case "xsi":
                return XMLConstants.W3C_XML_SCHEMA_INSTANCE_NS_URI;

            default:
                return XMLConstants.NULL_NS_URI;
        }
    }

    @Override
    public String getPrefix(String namespaceURI) {
        return null;
    }

    @Override
    public Iterator<String> getPrefixes(String namespaceURI) {
        return null;
    }
}

public class XmlFormatter {
    public static void main(String[] args) throws Exception {
        String inputXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                "<server>" +
                "   <id>" + 1 + "</id>" +
                "   <name>" + "Server 1" + "</name>" +
                "<status>" + "Running" + "</status>" +
                "<cpu>" + 80 + "</cpu>" +
                "<memory>" + 16384 + "</memory>" +
                "<location>" + "Data Center A" + "</location><body></body>" +
                "</server>";
        System.out.println("inputXml : " + inputXml);

        String formattedXml = formatXml(inputXml);
        System.out.println(formattedXml);

        DocumentBuilderFactory factory =  DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document =   builder.parse(new InputSource(new StringReader(formattedXml)));

        document.getDocumentElement().setAttributeNS(XMLConstants.XMLNS_ATTRIBUTE_NS_URI,
                "xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");

        Element supplemental =   document.createElement("Supplemental");

        Element risk =    document.createElement("RiskScore");
        risk.setTextContent("HIGH");
        supplemental.appendChild(risk);

        Element processed =  document.createElement("ProcessedTime");
        processed.setTextContent("2026-08-04T16:30:00");
        supplemental.appendChild(processed);

//        Element root = document.getDocumentElement();
//        root.appendChild(supplemental);

//        String ns = "urn:test";
//        Element header = (Element) document.getElementsByTagNameNS(ns, "Header").item(0);
//        Element body = (Element) document.getElementsByTagName("body").item(0);
//        body.appendChild(supplemental);

        XPathFactory xpf = XPathFactory.newInstance();
        XPath xpath = xpf.newXPath();
        xpath.setNamespaceContext(new IsoNamespaceContext());

        Element body = (Element) xpath.evaluate(
                "/server/body",
                document,
                XPathConstants.NODE);

        body.appendChild(supplemental);

        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        StringWriter writer = new StringWriter();

        transformer.transform( new DOMSource(document),  new StreamResult(writer));

        String newXml = formatXml(writer.toString());
        System.out.println(newXml);

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
            String outputXml = xmlOutput.getWriter().toString();
            // remove blank line
            outputXml = outputXml.replaceAll("\\n\\s*\\n", "\n");

            return outputXml;

        } catch (Exception e) {
            throw new RuntimeException("Failed to format XML string", e);
        }
    }
}