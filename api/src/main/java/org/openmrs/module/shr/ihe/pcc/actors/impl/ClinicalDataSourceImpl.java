package org.openmrs.module.shr.ihe.pcc.actors.impl;

import org.marc.everest.rmim.uv.ne2008.interaction.QUPC_IN043100UV01;
import org.marc.everest.rmim.uv.ne2008.interaction.QUPC_IN043200UV01;
import org.marc.everest.rmim.uv.ne2008.interaction.QUQI_IN000003UV01;
import org.openmrs.module.shr.ihe.pcc.actors.ClinicalDataSource;
import org.springframework.stereotype.Service;

@Service
public class ClinicalDataSourceImpl implements ClinicalDataSource {
	
	@Override
	public QUPC_IN043200UV01 getCareRecordProfileQuery(QUPC_IN043100UV01 request) {
		// TODO Auto-generated method stub
		return null;
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
