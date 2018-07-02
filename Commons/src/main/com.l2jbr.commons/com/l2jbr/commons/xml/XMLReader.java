package com.l2jbr.commons.xml;

import com.l2jbr.commons.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.bind.*;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.File;

public abstract class XMLReader<T> implements ValidationEventHandler {

    protected  static final Logger logger = LoggerFactory.getLogger(XMLReader.class);
    private Schema schema;
    private String processingFile;
    private Unmarshaller unmarshaller;

    public XMLReader() throws JAXBException {
        loadSchema();
        createUnmarshaller();
    }

    protected void createUnmarshaller() throws JAXBException {
        JAXBContext context = getJAXBContext();
        unmarshaller = context.createUnmarshaller();
        unmarshaller.setSchema(schema);
        unmarshaller.setEventHandler(this);
    }

    private void loadSchema()  {
        try {
            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            schema = factory.newSchema(new File(getSchemaFilePath()));
        } catch (SAXException e) {
           logger.warn(e.getLocalizedMessage(), e);
        }
    }

    public void readAll() {
        for (String directory : getXmlFileDirectories()) {
            read(getDirectoryFiles(directory));
        }
    }

    private String[] getDirectoryFiles(String directory) {
        File fileDir = new File(Config.DATAPACK_ROOT, directory);
        return fileDir.list((dir, name) -> name.endsWith(".xml"));
    }

    private void read(String... files) {
        if(files == null || files.length < 1) {
            return;
        }

        for (String file : files) {
            try {
                readFile(file);
            } catch (JAXBException e) {
                logger.error(e.getLocalizedMessage(), e);
            }
        }
    }

    private void readFile(String file) throws JAXBException{
        T entity = processFile(file);
        processEntity(entity);
    }

    @SuppressWarnings("unchecked")
    protected <T> T processFile(String filePath) throws JAXBException {
        processingFile = filePath;
        return (T) unmarshaller.unmarshal(new File(filePath));
    }

    @Override
    public boolean handleEvent(ValidationEvent event) {
        ValidationEventLocator locator = event.getLocator();
        logger.error("reading {} on line {} column {} : {} ", processingFile, locator.getLineNumber(), locator.getColumnNumber(), event.getMessage());
        return true;
    }

    protected abstract void processEntity(T entity);
    protected abstract JAXBContext getJAXBContext() throws JAXBException;
    protected abstract String getSchemaFilePath();
    protected abstract String[] getXmlFileDirectories();

}
