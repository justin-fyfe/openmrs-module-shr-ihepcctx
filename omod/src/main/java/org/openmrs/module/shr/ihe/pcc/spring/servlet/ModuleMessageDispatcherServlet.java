package org.openmrs.module.shr.ihe.pcc.spring.servlet;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
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
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.AssignableTypeFilter;
import org.springframework.web.util.WebUtils;
import org.springframework.ws.soap.SoapHeaderElement;
import org.springframework.ws.soap.SoapHeaderException;
import org.springframework.ws.soap.SoapVersion;
import org.springframework.ws.soap.addressing.server.annotation.Action;
import org.springframework.ws.soap.saaj.SaajSoapMessage;
import org.springframework.ws.soap.saaj.SaajSoapMessageFactory;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.transport.http.HttpTransportConstants;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.xml.xsd.SimpleXsdSchema;
import org.springframework.xml.xsd.XsdSchema;

/**
 * A SOAP message dispatcher servlet
 * HACK: This is a hack to get working for now, this needs to be converted to 
 *       to SpringWS MessageDispatcherServlet when I figure out a way to get around the 
 *       missing implementation of getInitParameterNames in oMRS and the need to have a 
 *       /WEB-INF/v3-serg
 */
public class ModuleMessageDispatcherServlet extends MessageDispatcherServlet {

	/**
     * 
     */
    private static final long serialVersionUID = 1L;

	/**
	 * Override initialize
	 * @see javax.servlet.GenericServlet#init(javax.servlet.ServletConfig)
	 */
	@Override
    public void init(ServletConfig config) throws ServletException {
	    // TODO Auto-generated method stub
		super.setContextConfigLocation("classpath:v3-servlet.xml");
		super.setTransformWsdlLocations(true);
	    super.init(new SpringFakeConfig(config.getServletName(), config.getServletContext()));
    }

	
	/**
	 * Get XSD schema
	 * @see org.springframework.ws.transport.http.MessageDispatcherServlet#getXsdSchema(javax.servlet.http.HttpServletRequest)
	 */
	@Override
    protected XsdSchema getXsdSchema(HttpServletRequest request) {
		if (HttpTransportConstants.METHOD_GET.equals(request.getMethod()) &&
				  request.getRequestURI().endsWith(".xsd")) {
				  String fileName = WebUtils.extractFilenameFromUrlPath(request.getRequestURI());
				  Resource schemaResource = new ClassPathResource(String.format("classpath:wsdl/xsd/%s", fileName));
				  return new SimpleXsdSchema(schemaResource);
		}
		else {
			return null;
		  }
    }

	/**
	 * Fake spring config
	 */
	public static class SpringFakeConfig extends ModuleServlet.SimpleServletConfig
	{

		/**
		 * Creates an instance of the fake spring configuration
		 */
		public SpringFakeConfig(String name, ServletContext servletContext) {
	        super(name, servletContext);
        }


		/**
		 * Return an empty enumeration
		 * @see org.openmrs.module.web.ModuleServlet.SimpleServletConfig#getInitParameterNames()
		 */
		@Override
		public Enumeration getInitParameterNames() {
			return new Enumeration<Object>() {
		
				@Override
		        public boolean hasMoreElements() {
		            // TODO Auto-generated method stub
		            return false;
		        }
		
				@Override
		        public Object nextElement() {
		            // TODO Auto-generated method stub
		            return null;
		        }
				
				
			};
		}
		
	}

	

}
