package com.soap.util;

import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.StringWriter;

import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

public class SoapRequest {
	private SOAPConnectionFactory soapConnectionFactory;
	private SOAPConnection soapConnection;
	private MessageFactory messageFactory;
	private SOAPMessage soapMessage;
	private SOAPPart soapPart;

	public static void main(String[] args) {
		SoapRequest myservice = new SoapRequest();

		System.setProperty("java.net.useSystemProxies", "true");
		System.getProperties().put("http.proxyHost", "proxy.dll.corp");
		System.getProperties().put("http.proxyPort", "8080");

		myservice.setUp();
		myservice.testSOAPRequest(args[0], args[1], args[2], args[3]);
	}

	public void setUp() {
		try {
			this.soapConnectionFactory = SOAPConnectionFactory.newInstance();
			this.soapConnection = this.soapConnectionFactory.createConnection();

			this.messageFactory = MessageFactory.newInstance();
			this.soapMessage = this.messageFactory.createMessage();
			this.soapPart = this.soapMessage.getSOAPPart();
		} catch (UnsupportedOperationException e) {
			e.printStackTrace();
		} catch (SOAPException e) {
			e.printStackTrace();
		}
	}

	public void testSOAPRequest(String datamsg, String headermsg, String endpointurl, String RespFilepath) {
		try {
			StreamSource prepMsg = new StreamSource(new FileInputStream(datamsg));
			this.soapPart.setContent(prepMsg);

			this.soapMessage.saveChanges();

			MimeHeaders mimeHeader = this.soapMessage.getMimeHeaders();
			mimeHeader.setHeader("SOAPAction", headermsg);

			String endPoinUrl = endpointurl;
			SOAPMessage messagerequest = this.soapConnection.call(this.soapMessage, endPoinUrl);

			Source source = messagerequest.getSOAPPart().getContent();

			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();

			StringWriter writer = new StringWriter();
			StreamResult sResult = new StreamResult(writer);
			transformer.transform(source, sResult);
			String result1 = writer.toString();

			System.out.println(result1);
			try {
				BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(RespFilepath));
				bos.write(result1.getBytes());
				bos.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			this.soapConnection.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}
