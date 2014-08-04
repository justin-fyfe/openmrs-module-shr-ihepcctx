package org.openmrs.module.shr.ihe.pcc.actors.endpoint;

import java.util.UUID;

import org.marc.everest.rmim.uv.ne2008.interaction.QUPC_IN043100UV01;
import org.marc.everest.rmim.uv.ne2008.interaction.QUPC_IN043200UV01;
import org.marc.everest.rmim.uv.ne2008.interaction.QUQI_IN000003UV01;
import org.openmrs.module.shr.ihe.pcc.actors.ClinicalDataSourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import org.springframework.ws.soap.addressing.server.annotation.Action;
import org.springframework.ws.soap.server.endpoint.annotation.SoapAction;

@Endpoint
public class ClinicalDataSourceEndpoint {
	
	private final ClinicalDataSourceService m_qedService;

	@Autowired
    public ClinicalDataSourceEndpoint(ClinicalDataSourceService qedService) {
        this.m_qedService = qedService;
    }
	
	@Action("urn:hl7-org:v3:QUPC_IN043100UV01")
	@SoapAction("urn:hl7-org:v3:QUPC_IN043100UV01")
	@ResponsePayload
    public QUPC_IN043200UV01 getCareRecordProfileQuery(@RequestPayload QUPC_IN043100UV01 request) {
		return this.m_qedService.getCareRecordProfileQuery(request);
    }

	@Action("urn:hl7-org:v3:QUQI_IN000003UV01")
	@SoapAction("urn:hl7-org:v3:QUQI_IN000003UV01")
	@ResponsePayload
    public QUPC_IN043200UV01 generalQueryActivateContinue(@RequestPayload QUQI_IN000003UV01 request) {
		return this.m_qedService.generalQueryActivateContinue(request);
    }

	@Action("urn:hl7-org:v3:QUQI_IN000003UV01_Cancel")
	@SoapAction("urn:hl7-org:v3:QUQI_IN000003UV01_Cancel")
	@ResponsePayload
    public QUPC_IN043200UV01 generalQueryActivateCancel(@RequestPayload QUQI_IN000003UV01 request) {
		return this.m_qedService.generalQueryActivateCancel(request);
    }
	
}
