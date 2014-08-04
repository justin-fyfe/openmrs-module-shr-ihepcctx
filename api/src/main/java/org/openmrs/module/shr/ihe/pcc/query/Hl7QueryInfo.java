package org.openmrs.module.shr.ihe.pcc.query;

import java.util.Date;
import java.util.List;

/**
 * Query information
 */
public class Hl7QueryInfo {
	
	// Unique identifier for the query
	protected String m_queryId;
	// The list of items which match the query parameters provided in the quer
	protected List<Hl7QueryResult> m_queryResult;
	// The date the query was create
	protected Date m_createdDate;
	// The date the query was updated (last retrieved)
	protected Date m_updatedDate;
	// The date the query cancelled
	protected Date m_cancellationDate;
	
}
