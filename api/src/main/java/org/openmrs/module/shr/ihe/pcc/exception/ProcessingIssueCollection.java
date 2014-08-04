package org.openmrs.module.shr.ihe.pcc.exception;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.marc.everest.interfaces.IResultDetail;
import org.marc.everest.interfaces.ResultDetailType;
import org.marc.everest.resultdetails.ValidationResultDetail;

/**
 * Represents a collection of validation issues
 */
public class ProcessingIssueCollection implements Iterable<ValidationResultDetail> {
	
	// The internal collection of items
	private List<ValidationResultDetail> m_internalList = new ArrayList<ValidationResultDetail>();

	
	/**
	 * Adds an error to the collection
	 */
	public void error(String description)
	{
		this.m_internalList.add(new ValidationResultDetail(ResultDetailType.ERROR, description));
	}
	
	/**
	 * Adds a warning to the collection
	 */
	public void warn(String description)
	{
		this.m_internalList.add(new ValidationResultDetail(ResultDetailType.WARNING, description));
	}
	
	/**
	 * True if this collection contains errors
	 * @return
	 */
	public Boolean hasErrors()
	{
		for(IResultDetail dtl : this.m_internalList)
			if(dtl.getType() == ResultDetailType.ERROR)
				return true;
		return false;
	}

	/**
	 * Get the iterator for the collection
	 * @see java.lang.Iterable#iterator()
	 */
	@Override
    public Iterator<ValidationResultDetail> iterator() {
		return this.m_internalList.iterator();
    }
	
}
