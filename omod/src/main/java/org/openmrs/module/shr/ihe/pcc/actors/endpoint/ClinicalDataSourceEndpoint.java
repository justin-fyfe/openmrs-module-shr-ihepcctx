package org.openmrs.module.shr.ihe.pcc.actors.endpoint;

import java.util.UUID;

import org.marc.everest.rmim.uv.ne2008.interaction.QUPC_IN043100UV01;
import org.marc.everest.rmim.uv.ne2008.interaction.QUPC_IN043200UV01;
import org.marc.everest.rmim.uv.ne2008.interaction.QUQI_IN000003UV01;
import org.openmrs.module.shr.ihe.pcc.actors.ClinicalDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import org.springframework.ws.soap.addressing.server.annotation.Action;

@Endpoint
public class ClinicalDataSourceEndpoint {
	
	@Action(value = "ClinicalDataSource_QUPC_IN043100UV")
	@ResponsePayload
    public static QUPC_IN043200UV01 getCareRecordProfileQuery(@RequestPayload QUPC_IN043100UV01 request) {
		QUPC_IN043200UV01 retVal = new QUPC_IN043200UV01();
		retVal.setId(UUID.randomUUID());
		return retVal;
    }

	@Action(value = "ClinicalDataSource_QUQI_IN000003UV01_Continue")
	@ResponsePayload
    public static QUPC_IN043200UV01 generalQueryActivateContinue(@RequestPayload QUQI_IN000003UV01 request) {
	    // TODO Auto-generated method stub
	    return null;
    }

	@Action(value = "ClinicalDataSource_QUQI_IN000003UV01_Cancel")
	@ResponsePayload
    public static QUPC_IN043200UV01 generalQueryActivateCancel(@RequestPayload QUQI_IN000003UV01 request) {
	    // TODO Auto-generated method stub
	    return null;
    }
	
}
