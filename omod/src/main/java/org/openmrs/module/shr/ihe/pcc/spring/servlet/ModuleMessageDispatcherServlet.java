package org.openmrs.module.shr.ihe.pcc.spring.servlet;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.NotSupportedException;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFault;
import javax.xml.soap.SOAPMessage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.marc.everest.datatypes.II;
import org.marc.everest.datatypes.generic.LIST;
import org.openmrs.module.shr.ihe.pcc.everest.EverestMarshaller;
import org.openmrs.module.shr.ihe.pcc.everest.EverestUnmarshaller;
import org.openmrs.module.shr.ihe.pcc.everest.QedFormatter;
import org.openmrs.module.web.ModuleServlet;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.AssignableTypeFilter;
import org.springframework.ws.soap.SoapHeaderElement;
import org.springframework.ws.soap.SoapHeaderException;
import org.springframework.ws.soap.SoapVersion;
import org.springframework.ws.soap.addressing.server.annotation.Action;
import org.springframework.ws.soap.saaj.SaajSoapMessage;
import org.springframework.ws.soap.saaj.SaajSoapMessageFactory;
import org.springframework.ws.server.endpoint.annotation.Endpoint;

/**
 * A SOAP message dispatcher servlet
 * HACK: This is a hack to get working for now, this needs to be converted to 
 *       to SpringWS MessageDispatcherServlet when I figure out a way to get around the 
 *       missing implementation of getInitParameterNames in oMRS and the need to have a 
 *       /WEB-INF/v3-serg
 */
public class ModuleMessageDispatcherServlet extends HttpServlet {

	// Log
	protected final Log log = LogFactory.getLog(this.getClass());

	// Endpoing classes
	protected Map<String, Method> m_actionMap = new HashMap<String, Method>();
	
	protected QedFormatter m_formatter = new QedFormatter();
	protected EverestUnmarshaller m_unMarshaller = new EverestUnmarshaller(this.m_formatter);
	protected EverestMarshaller m_marshaller = new EverestMarshaller(this.m_formatter);
	
	
	/**
	 * Init the servlet
	 */
	@Override
    public void init() throws ServletException {
	    super.init();
	    
	    // Initialize the endpoints
	    this.initializeEndpointList();
    }

	/**
	 * Initialize endpoint list
	 */
	private void initializeEndpointList() {
		ClassPathScanningCandidateComponentProvider classPathScanner = new ClassPathScanningCandidateComponentProvider(true);
		classPathScanner.addIncludeFilter(new AnnotationTypeFilter(Endpoint.class));
	
		// scan in org.openmrs.module.RegenstriefHl7Adapter.preprocessorHandler package
		Set<BeanDefinition> components = classPathScanner.findCandidateComponents("org.openmrs.module.shr");
		for (BeanDefinition component : components) {
			try {
				Class<?> cls = Class.forName(component.getBeanClassName());
				
				// Scan the type for handlers
				for(Method m : cls.getMethods())
				{
					Action action = m.getAnnotation(Action.class);
					if(action != null) 
						m_actionMap.put(action.value(), m);
				}
			}
			catch (ClassNotFoundException e) {
				log.error(e.getMessage(), e);
			}
		}	    
    }

	/**
	 * Do a get operation
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		resp.setStatus(415);
    }

	/**
	 * Handle a post and send it to the OpenMRS context
	 * @see org.springframework.web.servlet.FrameworkServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// Parse the soap message using SAAJ
		// TODO: See how this can be configured instead of hard coded
		SaajSoapMessageFactory soapMessageFactory = new SaajSoapMessageFactory();
		soapMessageFactory.setSoapVersion(SoapVersion.SOAP_12);
		SaajSoapMessage responseSoapMessage = soapMessageFactory.createWebServiceMessage();

		// Set response message stuff
		resp.setContentType("application/soap+xml");
		try
		{
			
			SaajSoapMessage requestMessage = soapMessageFactory.createWebServiceMessage(req.getInputStream());
	
			// Route to an appropriate method on an endpoint
			String action = requestMessage.getSoapAction();
			log.debug(String.format("Processing message with action %s", action));
			
			// TODO: Validate WSA-TO
			// TODO: Validate all mustUnderstand headers
			Iterator<SoapHeaderElement> headers = requestMessage.getSoapHeader().examineAllHeaderElements();
			while(headers.hasNext())
			{
				SoapHeaderElement current = headers.next();
				if(current.getName().getLocalPart().equals("To")) // TODO: validate WSA-TO
					;
				else if(current.getMustUnderstand())
					throw new SoapHeaderException(current.getName().getLocalPart());
			}
			
			Method invokeMethod = this.m_actionMap.get(action);
			if(invokeMethod == null)
				throw new NotSupportedException(action);
			
			// Process the body as HL7
			Object request = this.m_unMarshaller.unmarshal(requestMessage.getPayloadSource());
			if(invokeMethod.getParameterTypes()[0].isAssignableFrom(request.getClass())) // Safety for types
			{
				Object response = invokeMethod.invoke(null, request);
				// Get the response action
	        	responseSoapMessage.setSoapAction(action + "_Response");
	        	this.m_marshaller.marshal(response, responseSoapMessage.getPayloadResult());
			}
			else
				throw new IllegalArgumentException("Invalid request payload");
		}
		catch(SoapHeaderException e)
		{
			log.error(e);
			responseSoapMessage.getSoapBody().addMustUnderstandFault(e.getMessage(), Locale.getDefault());
        	resp.setStatus(500);
		}
        catch (NotSupportedException e) {
        	log.error(e);
        	responseSoapMessage.getSoapBody().addClientOrSenderFault(e.getMessage(), Locale.getDefault());
        	resp.setStatus(415);
        }
        catch (IllegalArgumentException e) {
        	log.error(e);
        	responseSoapMessage.getSoapBody().addClientOrSenderFault(e.getMessage(), Locale.getDefault());
        	resp.setStatus(400);
        }
        catch (Exception e) {
        	log.error(e);
        	responseSoapMessage.getSoapBody().addClientOrSenderFault(e.getMessage(), Locale.getDefault());
        	resp.setStatus(500);
        }
		finally
		{
			responseSoapMessage.writeTo(resp.getOutputStream());
		}
    }


}
