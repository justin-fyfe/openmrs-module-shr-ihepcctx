/**
 * Copyright 2008-2013 Mohawk College of Applied Arts and Technology
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you 
 * may not use this file except in compliance with the License. You may 
 * obtain a copy of the License at 
 * 
 * http://www.apache.org/licenses/LICENSE-2.0 
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the 
 * License for the specific language governing permissions and limitations under 
 * the License.
 * 
 * User: Justin Fyfe
 * Date: 12-14-2012
 */
package org.openmrs.module.shr.ihe.pcc.everest;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.marc.everest.annotations.Structure;
import org.marc.everest.exceptions.ConnectorException;
import org.marc.everest.formatters.FormatterUtil;
import org.marc.everest.formatters.interfaces.IFormatterParseResult;
import org.marc.everest.formatters.interfaces.IXmlStructureFormatter;
import org.marc.everest.interfaces.IGraphable;
import org.springframework.oxm.Unmarshaller;
import org.springframework.oxm.XmlMappingException;

/**
 * A spring Unmarshaller that leverages the Everest Framework
 */
public class EverestUnmarshaller implements Unmarshaller {

	// The formatter to be used
	private final IXmlStructureFormatter m_formatter = new QedFormatter();
	protected final Log log = LogFactory.getLog(this.getClass());

	/**
	 * Creates a new instance of the Everest unmarshaller
	 * @param fmtr
	 */
	public EverestUnmarshaller()
	{
	}
	
	/**
	 * Determine if the specified class is supported
	 */
	@Override
	public boolean supports(Class<?> t) {
		// Must be IGraphable and must have structure annotation
		if(!FormatterUtil.hasInterface(t, IGraphable.class))
			return false;
		
		log.debug(String.format("Unmarshalling %s with %s", t, this.m_formatter));
		Structure struct = t.getAnnotation(Structure.class);
		return this.m_formatter.getHandledStructures().contains("*") ||
				this.m_formatter.getHandledStructures().contains(struct.name());
	}

	/**
	 * Unmarshal the object from the source
	 */
	@Override
	public Object unmarshal(Source src) throws IOException, XmlMappingException {
		InputStream ins = null;
		if(src instanceof StreamSource)
			ins = ((StreamSource)src).getInputStream();
		else 
		{
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			Result outputTarget = new StreamResult(outputStream);
			try {
				TransformerFactory.newInstance().newTransformer().transform(src, outputTarget);
			} catch(Exception e)
			{
				throw new ConnectorException("Couldn't transform source to input stream for formatter");
			}
			ins = new ByteArrayInputStream(outputStream.toByteArray());
		}
		
		// HACK: I know this looks odd, why don't I just return the IFormatterParseResult right?
		// Well, I want to keep this consistent with the Marshaller, also the IFormatterParseResult isn't
		// really what is intended to be passed back.
		IFormatterParseResult retVal = this.m_formatter.parse(ins);
		return retVal.getStructure();
	}

}
