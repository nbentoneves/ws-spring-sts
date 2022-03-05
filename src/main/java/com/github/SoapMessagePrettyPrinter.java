package com.github;

import java.io.ByteArrayOutputStream;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.soap.saaj.SaajSoapMessage;

public class SoapMessagePrettyPrinter {

  private static final Logger LOGGER = LoggerFactory.getLogger(SoapMessagePrettyPrinter.class);

  public static void log(WebServiceMessage message) {

    if (message instanceof SaajSoapMessage) {
      SOAPMessage soapMessage = ((SaajSoapMessage) message).getSaajMessage();

      try {
        Source source = soapMessage.getSOAPPart().getContent();

        Transformer transformer = createTransformer();
        ByteArrayOutputStream formattedMessage = new ByteArrayOutputStream();
        transformer.transform(source, new StreamResult(formattedMessage));

        LOGGER.info("\n{}", formattedMessage);

      } catch (TransformerConfigurationException e) {
        e.printStackTrace();
      } catch (TransformerException e) {
        e.printStackTrace();
      } catch (SOAPException e) {
        e.printStackTrace();
      }

    }
  }

  private static Transformer createTransformer() throws TransformerConfigurationException {
    Transformer transformer = TransformerFactory.newInstance().newTransformer();
    transformer.setOutputProperty(OutputKeys.INDENT, "yes");
    transformer.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, "yes");
    transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
    return transformer;
  }
}
