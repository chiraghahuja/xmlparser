package io.github.chiraghahuja.xmlparser;

import org.dom4j.Node;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

// import IO exception
import java.io.IOException;
// import File
import java.io.File;

//import Path
import java.nio.file.Path;

//import Files
import java.nio.file.Files;

//import tempDir
import org.junit.jupiter.api.io.TempDir;

public class TestXMLFileLoad {

    private XMLUtils xmlUtils;

    @BeforeEach
    public void setUp() {
        xmlUtils = new XMLUtils();
    }

    @TempDir
    Path tempDir; // Temporary directory for test files

    @Test
    public void testLoadFromFileWithValidXML() throws Exception {
        // Create a temporary XML file with valid content
        String xmlContent = "<root><child>test</child></root>";
        Path xmlFilePath = tempDir.resolve("valid.xml");
        Files.write(xmlFilePath, xmlContent.getBytes());

        // Load from the temporary XML file
        List<Node> xmlNodes = XMLUtils.loadFromFile(xmlFilePath.toString());

        // Assertions
        assertEquals(1, xmlNodes.size());
        assertEquals("test", xmlNodes.get(0).getText());
    }


    // Create a xml with a single xml object like a person
    @Test
    public void testLoadFromFileWithValidXMLSingleObject() throws Exception {
        // Create a temporary XML file with valid content
        String xmlContent = "<persons><person><name>John</name><age>30</age><city>New York</city></person></persons>";
        Path xmlFilePath = tempDir.resolve("valid.xml");
        Files.write(xmlFilePath, xmlContent.getBytes());

        // Load from the temporary XML file
        List<Node> xmlNodes = XMLUtils.loadFromFile(xmlFilePath.toString());

        //get Person node
        Node personNode = xmlNodes.get(0);

        // Assertions
        assertEquals(1, xmlNodes.size());
        assertEquals("John", personNode.selectSingleNode("name").getText());
        assertEquals("30", personNode.selectSingleNode("age").getText());
        assertEquals("New York", personNode.selectSingleNode("city").getText());

    }

    @Test
    public void testLoadFromFileWithComplexXML() throws Exception {
        // Create a temporary XML file with valid content
        String xmlContent = "<employees>" +
                "<employee>" +
                "<name>Alice</name>" +
                "<age>28</age>" +
                "<position>Software Engineer</position>" +
                "<department>Engineering</department>" +
                "</employee>" +
                "<employee>" +
                "<name>Bob</name>" +
                "<age>35</age>" +
                "<position>Manager</position>" +
                "<department>Management</department>" +
                "</employee>" +
                "</employees>";

        Path xmlFilePath = tempDir.resolve("complex.xml");
        Files.write(xmlFilePath, xmlContent.getBytes());

        // Load from the temporary XML file
        List<Node> employeeNodes = XMLUtils.loadFromFile(xmlFilePath.toString());

        // Assertions
        assertEquals(2, employeeNodes.size());



        Node aliceNode = employeeNodes.get(0);
        assertEquals("Alice", aliceNode.selectSingleNode("name").getText());
        assertEquals("28", aliceNode.selectSingleNode("age").getText());
        assertEquals("Software Engineer", aliceNode.selectSingleNode("position").getText());
        assertEquals("Engineering", aliceNode.selectSingleNode("department").getText());

        Node bobNode = employeeNodes.get(1);
        assertEquals("Bob", bobNode.selectSingleNode("name").getText());
        assertEquals("35", bobNode.selectSingleNode("age").getText());
        assertEquals("Manager", bobNode.selectSingleNode("position").getText());
        assertEquals("Management", bobNode.selectSingleNode("department").getText());
    }

    @Test
    public void testLoadFromFileNonexistentFile() {
        assertThrows(Exception.class, () -> XMLUtils.loadFromFile("nonexistent.xml"));
    }

    @Test
    public void testLoadFromFileEmptyXML() throws Exception{
        Path xmlFilePath = tempDir.resolve("empty.xml");
        Files.write(xmlFilePath, "".getBytes());

        List<Node> xmlNodes = XMLUtils.loadFromFile(xmlFilePath.toString());

        assertTrue(xmlNodes.isEmpty());
    }

    @Test
    public void testLoadFromFileInvalidXML() throws IOException {
        String invalidXmlContent = "<root>missing_closing_tag";
        Path xmlFilePath = tempDir.resolve("invalid.xml");
        Files.write(xmlFilePath, invalidXmlContent.getBytes());

        assertThrows(Exception.class, () -> XMLUtils.loadFromFile(xmlFilePath.toString()));
    }

    @Test
    public void testLoadFromFileEmptyRootElement() throws Exception {
        String emptyRootXmlContent = "<root></root>";
        Path xmlFilePath = tempDir.resolve("emptyRoot.xml");
        Files.write(xmlFilePath, emptyRootXmlContent.getBytes());

        List<Node> xmlNodes = XMLUtils.loadFromFile(xmlFilePath.toString());

        assertEquals(0, xmlNodes.size()); // Expect zero children nodes
    }


    @Test
    public void testLoadFromFileWithUnicodeXML() throws Exception {
        // Create a temporary XML file with Unicode special characters
        String xmlContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                "<root>" +
                "<text>Unicode special characters: ©, é, 🌟</text>" +
                "</root>";

        Path xmlFilePath = tempDir.resolve("unicode.xml");
        Files.write(xmlFilePath, xmlContent.getBytes());

        // Load from the temporary XML file
        List<Node> xmlNodes = XMLUtils.loadFromFile(xmlFilePath.toString());

        Node rootNode = xmlNodes.get(0);

        // Assertions
        assertEquals(1, xmlNodes.size());


          assertNotNull(rootNode); // Check if the text node is found
          assertEquals("Unicode special characters: ©, é, 🌟", rootNode.getText());
    }

    @Test
    public void testLoadFromFileWithBoundaryValues() throws Exception {
        // Create a temporary XML file with boundary values
        String xmlContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                "<root>" +
                "<integers>" +
                "<max>" + Integer.MAX_VALUE + "</max>" +
                "<min>" + Integer.MIN_VALUE + "</min>" +
                "</integers>" +
                "</root>";

        Path xmlFilePath = tempDir.resolve("boundary.xml");
        Files.write(xmlFilePath, xmlContent.getBytes());

        // Load from the temporary XML file
        List<Node> xmlNodes = XMLUtils.loadFromFile(xmlFilePath.toString());

        // Assertions
        assertEquals(1, xmlNodes.size());

        Node integersNode = xmlNodes.get(0);

        assertNotNull(integersNode); // Check if the integers node is found

        // Assert max integer value
        Node maxNode = integersNode.selectSingleNode("max");
        assertNotNull(maxNode);
        assertEquals(Integer.MAX_VALUE, Integer.parseInt(maxNode.getText()));

        // Assert min integer value
        Node minNode = integersNode.selectSingleNode("min");
        assertNotNull(minNode);
        assertEquals(Integer.MIN_VALUE, Integer.parseInt(minNode.getText()));
    }

    @Test
    public void testLoadFromFileWithSpecialCharacters() throws Exception {
        // Create a temporary XML file with special characters
        String xmlContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                "<root>" +
                "<text>Special characters: !@#$%^&amp;*()_+{}[]|\\</text>" +
                "</root>";

        Path xmlFilePath = tempDir.resolve("special.xml");
        Files.write(xmlFilePath, xmlContent.getBytes());

        // Load from the temporary XML file
        List<Node> xmlNodes = XMLUtils.loadFromFile(xmlFilePath.toString());

        // Assertions
        assertEquals(1, xmlNodes.size());

        Node textNode = xmlNodes.get(0);

        assertNotNull(textNode); // Check if the text node is found
        assertEquals("Special characters: !@#$%^&*()_+{}[]|\\", textNode.getText());
    }


}
