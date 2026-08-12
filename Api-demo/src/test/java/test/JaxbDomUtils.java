package test;

import jakarta.xml.bind.*;
import org.glassfish.jaxb.runtime.marshaller.NamespacePrefixMapper;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.transform.dom.DOMResult;
import java.io.StringWriter;

public class JaxbDomUtils {

    public static String marshal(JAXBElement<?> element) throws Exception {

        JAXBContext context =
                JAXBContext.newInstance(element.getDeclaredType());

        Marshaller marshaller = context.createMarshaller();

        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        //JAXB Reference Implementation
        marshaller.setProperty(
                "org.glassfish.jaxb.namespacePrefixMapper",
                new NamespacePrefixMapper() {
                    @Override
                    public String getPreferredPrefix(
                            String namespaceUri,
                            String suggestion,
                            boolean requirePrefix) {

                        switch (namespaceUri) {
                            case "urn:example":
                                return "";       // default namespace

                            case "urn:other":
                                return "other";  // keep prefix

                            default:
                                return suggestion;
                        }


                    }
                });
        // JAXB 2.x
        marshaller.setProperty(
                "com.sun.xml.bind.namespacePrefixMapper",
                new NamespacePrefixMapper() {
                    @Override
                    public String getPreferredPrefix(
                            String namespaceUri,
                            String suggestion,
                            boolean requirePrefix) {

                        switch (namespaceUri) {
                            case "urn:example":
                                return "";       // default namespace

                            case "urn:other":
                                return "other";  // keep prefix

                            default:
                                return suggestion;
                        }
                    }
                }
        );
        StringWriter writer = new StringWriter();
        marshaller.marshal(element, writer);

        return writer.toString();
    }

    /**
     * Converts a DOM Element to an object of the specified class.
     *
     * @param element the DOM Element to convert
     * @param clazz   the class of the object to create
     * @return the converted object
     * @throws JAXBException if an error occurs during unmarshalling
     */
    public static <T> T toObject(Element element, Class<T> clazz)
            throws JAXBException {

        JAXBContext context = JAXBContext.newInstance(clazz);
        Unmarshaller u = context.createUnmarshaller();

        return clazz.cast(u.unmarshal(element));
    }

    /**
     * Converts an object to a DOM Element.
     *
     * @param object the object to convert
     * @return the converted DOM Element
     * @throws Exception if an error occurs during marshalling
     */
    public static Element toElement(Object object)
            throws Exception {

        JAXBContext context = JAXBContext.newInstance(object.getClass());

        Marshaller m = context.createMarshaller();
        m.setProperty(Marshaller.JAXB_FRAGMENT, true);

        DOMResult result = new DOMResult();
        m.marshal(object, result);

        return ((Document) result.getNode()).getDocumentElement();
    }
}
