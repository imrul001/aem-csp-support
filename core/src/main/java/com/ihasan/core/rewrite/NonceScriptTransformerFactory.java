package com.ihasan.core.rewrite;

import org.apache.sling.rewriter.ProcessingComponentConfiguration;
import org.apache.sling.rewriter.ProcessingContext;
import org.apache.sling.rewriter.Transformer;
import org.apache.sling.rewriter.TransformerFactory;
import org.osgi.service.component.annotations.Component;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator; // Import Locator
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

@Component(service = TransformerFactory.class, property = {
        "pipeline.type=NonceScriptTransformer" })
public class NonceScriptTransformerFactory implements TransformerFactory {

    @Override
    public Transformer createTransformer() {
        return new NonceScriptTransformer();
    }

    private static class NonceScriptTransformer implements Transformer {

        private static final String SCRIPT_TAG = "script";
        private static final String NONCE_ATTRIBUTE = "nonce";
        private static final String UNIQUE_ID_PLACEHOLDER = "<!--#echo var=\"UNIQUE_ID\"-->";

        private ContentHandler contentHandler;

        @Override
        public void init(ProcessingContext context, ProcessingComponentConfiguration config) {
            // Initialization logic if needed
        }

        @Override
        public void dispose() {
            // Cleanup logic if needed
        }

        @Override
        public void setContentHandler(ContentHandler contentHandler) {
            this.contentHandler = contentHandler;
        }

        @Override
        public void setDocumentLocator(Locator locator) {
            contentHandler.setDocumentLocator(locator);
        }

        @Override
        public void startDocument() throws SAXException {
            contentHandler.startDocument();
        }

        @Override
        public void endDocument() throws SAXException {
            contentHandler.endDocument();
        }

        @Override
        public void startPrefixMapping(String prefix, String uri) throws SAXException {
            contentHandler.startPrefixMapping(prefix, uri);
        }

        @Override
        public void endPrefixMapping(String prefix) throws SAXException {
            contentHandler.endPrefixMapping(prefix);
        }

        @Override
        public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
            if (SCRIPT_TAG.equalsIgnoreCase(localName)) {
                AttributesImpl newAttrs = new AttributesImpl(atts);
                int nonceIndex = newAttrs.getIndex(NONCE_ATTRIBUTE);

                if (nonceIndex == -1 || !UNIQUE_ID_PLACEHOLDER.equals(newAttrs.getValue(nonceIndex))) {
                    // Only add the nonce if it doesn't already exist or is different from the UNIQUE_ID_PLACEHOLDER
                    if (nonceIndex == -1) {
                        newAttrs.addAttribute("", NONCE_ATTRIBUTE, NONCE_ATTRIBUTE, "CDATA", UNIQUE_ID_PLACEHOLDER);
                    } else {
                        newAttrs.setValue(nonceIndex, UNIQUE_ID_PLACEHOLDER);
                    }
                }

                contentHandler.startElement(uri, localName, qName, newAttrs);
            } else {
                contentHandler.startElement(uri, localName, qName, atts);
            }
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            contentHandler.endElement(uri, localName, qName);
        }

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            contentHandler.characters(ch, start, length);
        }

        @Override
        public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {
            contentHandler.ignorableWhitespace(ch, start, length);
        }

        @Override
        public void processingInstruction(String target, String data) throws SAXException {
            contentHandler.processingInstruction(target, data);
        }

        @Override
        public void skippedEntity(String name) throws SAXException {
            contentHandler.skippedEntity(name);
        }
    }
}