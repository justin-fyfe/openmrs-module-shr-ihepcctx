package org.openmrs.module.shr.ihe.pcc.everest;

import org.marc.everest.formatters.xml.datatypes.r1.DatatypeFormatter;
import org.marc.everest.formatters.xml.datatypes.r1.R1FormatterCompatibilityMode;
import org.marc.everest.formatters.xml.its1.XmlIts1Formatter;
import org.marc.everest.rmim.uv.ne2008.interaction.QUPC_IN043100UV01;
import org.marc.everest.rmim.uv.ne2008.interaction.QUQI_IN000003UV01;

/**
 * A formatter for QED that adds the necessary aides to the 
 * formatter instance (this saves us in the bean creation via
 * spring)
 */
public class QedFormatter extends XmlIts1Formatter {
	
	/**
	 * Initialize the QED formatter
	 */
	public QedFormatter() {
		this.addCachedClass(QUPC_IN043100UV01.class);
		this.addCachedClass(QUQI_IN000003UV01.class);
		this.getGraphAides().add(new DatatypeFormatter(R1FormatterCompatibilityMode.Universal));
	}
}
