package org.openmrs.module.shr.ihe.pcc.actors.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.marc.everest.rmim.uv.ne2008.interaction.QUPC_IN043100UV01;
import org.marc.everest.rmim.uv.ne2008.interaction.QUPC_IN043200UV01;
import org.marc.everest.rmim.uv.ne2008.interaction.QUQI_IN000003UV01;
import org.marc.everest.rmim.uv.ne2008.interaction.REPC_IN004110UV01;
import org.openmrs.api.context.Context;
import org.openmrs.module.shr.ihe.pcc.actors.ClinicalDataSourceService;
import org.openmrs.module.shr.ihe.pcc.api.QueryForExistingDataService;
import org.openmrs.module.shr.ihe.pcc.api.Hl7QueryService;
import org.openmrs.module.shr.ihe.pcc.query.Hl7QueryInfo;
import org.openmrs.module.shr.ihe.pcc.util.TransportWrapperUtil;
import org.openmrs.util.OpenmrsConstants;
import org.springframework.stereotype.Service;

/**
 * The ClinicalDataSource service implementation. 
 * 
 * Note: This part takes care of the transport wrapper and sets up the OpenMRS context
 * and then calls down to the service to construct the summary  
 */
@Service
public class ClinicalDataSourceServiceImpl implements ClinicalDataSourceService {
	
	// Get the clinical statement service
	protected final Log m_log = LogFactory.getLog(this.getClass());

	/**
	 * Start an OpenMRS Session
	 */
	private void startSession() {
	    // TODO: Move this to a config parameter web services user
		Context.openSession();
		Context.authenticate("admin", "test");
		this.m_log.error(OpenmrsConstants.DATABASE_NAME);
    }


	/**
	 * Get care record summary from the openmrs
	 * @see org.openmrs.module.shr.ihe.pcc.actors.ClinicalDataSourceService#getCareRecordProfileQuery(org.marc.everest.rmim.uv.ne2008.interaction.QUPC_IN043100UV01)
	 */
	@Override
	public QUPC_IN043200UV01 getCareRecordProfileQuery(QUPC_IN043100UV01 request) {

		// Response message
		QUPC_IN043200UV01 retVal= new QUPC_IN043200UV01();
		retVal.setInteractionId(QUPC_IN043200UV01.defaultInteractionId());
		TransportWrapperUtil.populateTransportWrapper(retVal, request.getSender());
		
		try
		{
			this.startSession();
			QueryForExistingDataService queryExistingDataService = Context.getService(QueryForExistingDataService.class);
			return retVal;
		}
		catch(Exception e)
		{
			
		}
		finally
		{
			Context.closeSession();
		}
	}
	
	@Override
	public QUPC_IN043200UV01 generalQueryActivateContinue(QUQI_IN000003UV01 request) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public QUPC_IN043200UV01 generalQueryActivateCancel(QUQI_IN000003UV01 request) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
