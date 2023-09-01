package io.github.chiraghahuja.xmlparser;

import org.dom4j.*;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

public class XMLUtils {


    /**
     * Loads XML from a file and returns a list of Node objects representing the XML structure.
     * Think about a list of Nodes as a list of dictionaries.
     *
     * @param filePath the file path of the XML file to parse
     * @return a list of {@link org.w3c.dom.Node} objects representing the XML structure
     * @throws Exception if an error occurs while reading the XML file
     *
     *
     * API examples:
     * // Provide the path to your XML file
     * String filePath = "path/to/your/xmlfile.xml";
     * try {
     *     List&lt;org.w3c.dom.Node&gt; nodes = XMLUtils.loadFromFile(filePath);
     *
     *     for (org.w3c.dom.Node node : nodes) {
     *         // You can process each node as needed
     *         System.out.println(node.asXML());
     *     }
     * } catch (Exception e) {
     *     e.printStackTrace();
     * }
     */

    public static List<Node> loadFromFile(String filePath) throws Exception {
        List<Node> nodeList = new ArrayList<>();

        File inputFile = new File(filePath);
        if (!inputFile.exists()) {
            throw new Exception("XML file does not exist.");
        }
        if (inputFile.length() == 0) {
            // Handle empty file case
            return nodeList;
        }

        try {
            SAXReader reader = new SAXReader();
            Document document = reader.read(inputFile);

            // Using document.getRootElement() to get the root node
            Element root = document.getRootElement();
            List<Node> childNodes = root.content();

//            if (childNodes.isEmpty()) {
//                throw new Exception("No child nodes found in the XML.");
//            }

            nodeList.addAll(childNodes);
        } catch (Exception e) {
            throw new Exception("Error reading XML: " + e.getMessage());
        }

        return nodeList;
    }


    /**
     * Dumps a list of objects as XML elements to a file, with customizable property names.
     *
     * @param objects       A list of objects to be converted to XML elements.
     * @param filePath      The file path of the output XML file.
     * @param PROPERTY_NAMES An array of property names to include in the XML elements.
     * @throws Exception if an error occurs during XML writing or processing.
     * API Usage Example:
     * <pre>
     * List&lt;Person&gt; persons = new ArrayList&lt;&gt;();
     * persons.add(new Person("Alice", 25));
     * persons.add(new Person("Bob", 30));
     * persons.add(new Person("Charlie", 28));
     *
     * String outputFilePath = "output.xml";
     * String[] propertyNames = {"name", "age"};
     *
     * XMLUtils.dumpToFile(persons, outputFilePath, propertyNames);
     * System.out.println("XML dumped to " + outputFilePath);
     * </pre>
     * <p>
     */
    public static void dumpToFile(List<?> objects, String filePath, String[] PROPERTY_NAMES)
            throws Exception {
        try {
            Document document = DocumentHelper.createDocument();

            //get the class name of the first object in the list. Create root element by appending s to the class name
            String className = objects.get(0).getClass().getSimpleName();
            String elementName = className.toLowerCase();
            String rootElementName = elementName + "s"; // Plural form of the element name

            Element root = document.addElement(rootElementName);



            for (Object obj : objects) {
                Element element = root.addElement(elementName);

                for (String propertyName : PROPERTY_NAMES) {
                    // Use reflection to access property values
                    String getterMethodName = "get" + propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1);
                    Object propertyValue = obj.getClass().getMethod(getterMethodName).invoke(obj);

                    Element propertyElement = element.addElement(propertyName);
                    propertyElement.addText(String.valueOf(propertyValue));
                }
            }

            // Creating an XMLWriter with formatting options
            OutputFormat format = OutputFormat.createPrettyPrint();
            XMLWriter writer = new XMLWriter(new FileWriter(new File(filePath)), format);

            writer.write(document);
            writer.close();
        } catch (Exception e) {
            throw new Exception("Error writing XML: " + e.getMessage());
        }
    }



}


