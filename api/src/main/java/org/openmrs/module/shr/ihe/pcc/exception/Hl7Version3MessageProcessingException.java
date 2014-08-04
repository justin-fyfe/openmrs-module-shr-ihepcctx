package org.openmrs.module.shr.ihe.pcc.exception;

import java.util.List;

import org.marc.everest.interfaces.IGraphable;
import org.marc.everest.interfaces.IResultDetail;

/**
 * Represents an issue with processing the HL7v3 
 */
public class Hl7Version3MessageProcessingException extends Exception {
	
	// Issues
	private ProcessingIssueCollection m_issues = null;
	
	// Serial identifier
    private static final long serialVersionUID = -4052731832891085607L;
    
    // List of issues
    private final IGraphable m_target;
    
    /**
     * Constructs a new instance of the DocumentValidationException class
     */
    public Hl7Version3MessageProcessingException(IGraphable target, ProcessingIssueCollection issues) {
    	this.m_issues = issues;
    	this.m_target = target;
    }
    /**
     * Constructs a new instance of the DocumentValidationException class with
     * the specified message
     * 
     * @param msg The exception message
     */
	public Hl7Version3MessageProcessingException(String msg, IGraphable target, ProcessingIssueCollection issues) { 
		super(msg); 
    	this.m_issues = issues;
    	this.m_target = target;
	}
    /**
     * Constructs a new instance of the DocumentValidationException class with
     * the specified cause.
     * 
     * @param ex The cause of this exception
     */
	public Hl7Version3MessageProcessingException(Throwable ex, IGraphable target, ProcessingIssueCollection issues) { 
		super(ex); 
    	this.m_issues = issues;
    	this.m_target = target;
	}
    /**
     * Constructs a new instance of the DocumentValidationException class with 
     * the specified message and cause
     * 
     * @param msg The exception message
     * @param ex The cause of the exception
     */
	public Hl7Version3MessageProcessingException(String msg, Throwable ex, IGraphable target, ProcessingIssueCollection issues) {
		super(msg, ex);
    	this.m_issues = issues;
    	this.m_target = target;
	}

	/**
	 * Gets the validation issues which caused this exception to be thrown
	 */
	public ProcessingIssueCollection getProcessingIssues()
	{
		return this.m_issues;
	}
	
	/**
	 * Gets the target object that failed validation
	 */
	public IGraphable getTarget()
	{
		return this.m_target;
	}
		
	
	
}
