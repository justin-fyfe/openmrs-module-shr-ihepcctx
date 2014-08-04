package org.openmrs.module.shr.ihe.pcc.actors;

import org.marc.everest.rmim.uv.ne2008.interaction.QUPC_IN043100UV01;
import org.marc.everest.rmim.uv.ne2008.interaction.QUPC_IN043200UV01;
import org.marc.everest.rmim.uv.ne2008.interaction.QUQI_IN000003UV01;

/**
 * Clinical Data Source service
 */
public interface ClinicalDataSourceService {
	
	/**
	 * PCC-1 
	 * Get Care Record Profile Query
	 */
	public QUPC_IN043200UV01 getCareRecordProfileQuery(QUPC_IN043100UV01 request);

	/**
	 * PCC-1
	 * Continue stored query
	 */
	public QUPC_IN043200UV01 generalQueryActivateContinue(QUQI_IN000003UV01 request);

	/**
	 * PCC-1
	 * Cancel stored query
	 */
	public QUPC_IN043200UV01 generalQueryActivateCancel(QUQI_IN000003UV01 request);

}
