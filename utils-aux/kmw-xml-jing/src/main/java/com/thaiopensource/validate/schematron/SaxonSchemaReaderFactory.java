package com.thaiopensource.validate.schematron;

import net.sf.saxon.TransformerFactoryImpl;

import javax.xml.transform.sax.SAXTransformerFactory;

public class SaxonSchemaReaderFactory extends SchematronSchemaReaderFactory {
  @Override
  public SAXTransformerFactory newTransformerFactory() {
    return new TransformerFactoryImpl();
  }
}
