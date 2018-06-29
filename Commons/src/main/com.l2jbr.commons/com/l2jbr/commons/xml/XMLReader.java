package com.l2jbr.commons.xml;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.bind.*;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.File;

public abstract class XMLReader implements ValidationEventHandler {

    private static final Logger logger = LoggerFactory.getLogger(XMLReader.class);
    private final Schema schema;
    private String processingFile;

    public XMLReader() throws SAXException {
        SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        schema = factory.newSchema(new File(getSchemaFilePath()));
    }

    protected <T> T load(Class<T> xmlEntityClass, String filePath) throws JAXBException {
        processingFile = filePath;
        JAXBContext context = JAXBContext.newInstance(xmlEntityClass);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        unmarshaller.setSchema(schema);
        unmarshaller.setEventHandler(this);
        return (T) unmarshaller.unmarshal(new File(filePath));
    }

    @Override
    public boolean handleEvent(ValidationEvent event) {
        ValidationEventLocator locator = event.getLocator();
        logger.error("reading {} on line {} column {} : {} ", processingFile, locator.getLineNumber(), locator.getColumnNumber(), event.getMessage());
        return true;
    }

    public abstract String getSchemaFilePath();
}
