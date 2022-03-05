package com.github;


import com.github.sts.api.InserimentoDocumentoSpesaRequest;
import com.github.sts.api.InserimentoDocumentoSpesaResponse;
import com.github.sts.api.ObjectFactory;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.TrustManagerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.webservices.client.WebServiceTemplateBuilder;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.soap.security.wss4j2.Wss4jSecurityInterceptor;

public class STSClient {

  private static final Logger LOGGER = LoggerFactory.getLogger(STSClient.class);

  public void sendRequest() {

    InserimentoDocumentoSpesaRequest inserimentoDocumentoSpesaRequest = new InserimentoDocumentoSpesaRequest();
    Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
    // this package must match the package in the <generatePackage> specified in
    // pom.xml
    marshaller.setContextPath("com.github.sts.api");

    //HttpsUrlConnectionMessageSender sender = new HttpsUrlConnectionMessageSender();
    //sender.setKeyManagers(createKeyManagerFactory().getKeyManagers());
    //sender.setTrustManagers(createTrusterManagerFactory(trustStore, password).getTrustManagers());

    Wss4jSecurityInterceptor interceptor = new Wss4jSecurityInterceptor();
    interceptor.setSecurementUsername("MTOMRA66A41G224M");
    interceptor.setSecurementPassword("Salve123");

    WebServiceTemplate template = new WebServiceTemplateBuilder()
        .setDefaultUri("https://invioSS730pTest.sanita.finanze.it/DocumentoSpesa730pWeb/DocumentoSpesa730pPort")
        .setMarshaller(marshaller)
        .additionalInterceptors(interceptor, new LoggingClientInterceptor())
        //.additionalMessageSenders(sender)
        .build();

    InserimentoDocumentoSpesaResponse response = (InserimentoDocumentoSpesaResponse) template
        .marshalSendAndReceive(
            new ObjectFactory().createInserimentoDocumentoSpesaRequest(new InserimentoDocumentoSpesaRequest()));

  }

  KeyManagerFactory createKeyManagerFactory(InputStream keyStore, String password)
      throws KeyStoreException, CertificateException, IOException, NoSuchAlgorithmException, UnrecoverableKeyException {
    KeyStore ks = KeyStore.getInstance("JKS");
    ks.load(keyStore, password.toCharArray());

    try {
      keyStore.close();
    } catch (IOException e) {
    }
    KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
    keyManagerFactory.init(ks, password.toCharArray());

    return keyManagerFactory;
  }

  TrustManagerFactory createTrusterManagerFactory(InputStream trustStore, String password)
      throws KeyStoreException, CertificateException, IOException, NoSuchAlgorithmException {
    KeyStore ts = KeyStore.getInstance("JKS");
    ts.load(trustStore, password.toCharArray());
    try {
      trustStore.close();
    } catch (IOException e) {
    }
    TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
    trustManagerFactory.init(ts);


    return trustManagerFactory;
  }
}
