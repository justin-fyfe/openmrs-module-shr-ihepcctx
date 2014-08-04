package org.openmrs.module.shr.ihe.pcc.util;

import java.util.Arrays;
import java.util.UUID;

import org.marc.everest.datatypes.ED;
import org.marc.everest.datatypes.EN;
import org.marc.everest.datatypes.ENXP;
import org.marc.everest.datatypes.II;
import org.marc.everest.datatypes.NullFlavor;
import org.marc.everest.datatypes.SC;
import org.marc.everest.datatypes.TEL;
import org.marc.everest.datatypes.TS;
import org.marc.everest.datatypes.generic.BAG;
import org.marc.everest.datatypes.generic.SET;
import org.marc.everest.rmim.uv.ne2008.interaction.MCCI_IN000002UV01;
import org.marc.everest.rmim.uv.ne2008.interaction.QUPC_IN043200UV01;
import org.marc.everest.rmim.uv.ne2008.mcci_mt000300uv01.Message;
import org.marc.everest.rmim.uv.ne2008.mcci_mt100200uv01.Device;
import org.marc.everest.rmim.uv.ne2008.mcci_mt100200uv01.Receiver;
import org.marc.everest.rmim.uv.ne2008.mcci_mt100200uv01.Sender;
import org.marc.everest.rmim.uv.ne2008.vocabulary.AcknowledgementCondition;
import org.marc.everest.rmim.uv.ne2008.vocabulary.ProcessingID;
import org.openmrs.ImplementationId;
import org.openmrs.api.AdministrationService;
import org.openmrs.api.context.Context;
import org.openmrs.module.shr.ihe.pcc.exception.ProcessingIssueCollection;
import org.openmrs.util.OpenmrsConstants;
import org.springframework.ws.transport.context.TransportContext;
import org.springframework.ws.transport.context.TransportContextHolder;
import org.springframework.ws.transport.http.HttpServletConnection;


/**
 * Utilities for dealing with the transport wrapper
 */
public final class TransportWrapperUtil {

	/**
	 * Populates the response transport wrapper
	 */
	public static void populateTransportWrapper(Message<?> retVal, Sender originalSender) {
		if(retVal == null)
			throw new IllegalArgumentException("retVal");
		
		// Now, populate the common transport wrapper message properties
		retVal.setAcceptAckCode(AcknowledgementCondition.Never);
		retVal.setCreationTime(TS.now());
		retVal.setId(UUID.randomUUID());
		retVal.setProfileId(new SET<II>(MCCI_IN000002UV01.defaultProfileId()));
		retVal.setProcessingCode(ProcessingID.Production);
		retVal.setProcessingModeCode("T");
		retVal.getReceiver().add(TransportWrapperUtil.translateSenderToReceiver(originalSender));
		retVal.setSender(TransportWrapperUtil.createSender());
		
    }

	/**
	 * Create sender node
	 */
	private static Sender createSender() {
		Sender retVal = new Sender();
		Device device = new Device();
		ImplementationId implementation = Context.getAdministrationService().getImplementationId();
		
		TransportContext transportContext = TransportContextHolder.getTransportContext();
		HttpServletConnection connection = (HttpServletConnection)transportContext.getConnection();
		
		// Set endpoint
		retVal.setTelecom(new TEL(connection.getHttpServletRequest().getRequestURI()));
		
		// Set name
		if(implementation == null)
		{
			II deviceId = new II();
			deviceId.setNullFlavor(NullFlavor.NoInformation);
			device.setId(SET.createSET(deviceId));
		}
		else
		{
			device.setName(BAG.createBAG(new EN(Arrays.asList(new ENXP(implementation.getName())))));
			// Set description
			device.setDesc(new ED(implementation.getDescription()));
			// Set Id
			// TODO: Get a root assigned for OpenMRS implementation IDs? Or make the id long enough for an OID
			device.setId(SET.createSET(new II("1.2.3.4.5.6.7", implementation.getImplementationId())));
		}
		// OpenMRS data
		device.setSoftwareName(new SC("OpenMRS"));
		device.setManufacturerModelName(new SC(OpenmrsConstants.OPENMRS_VERSION));
		
		retVal.setDevice(device);
		return retVal;
    }

	/**
	 * Translates a sender RMIM structure to a receiver RMIM structure
	 * @param originalSender
	 * @return
	 */
	private static Receiver translateSenderToReceiver(Sender originalSender) {
		Receiver retVal = new Receiver();
		retVal.setDevice(originalSender.getDevice());
		return retVal;
    }
	
	/**
	 * Validate the transport wrapper portion
	 */
	private static ProcessingIssueCollection validateTransportWrapper()
	{
		
	}

	
}
