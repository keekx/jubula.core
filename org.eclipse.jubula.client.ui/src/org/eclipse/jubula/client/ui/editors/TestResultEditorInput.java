/*******************************************************************************
 * Copyright (c) 2004, 2010 BREDEX GmbH.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     BREDEX GmbH - initial API and implementation and/or initial documentation
 *******************************************************************************/
package org.eclipse.jubula.client.ui.editors;

import java.util.Date;

import org.apache.commons.lang.Validate;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jubula.client.core.model.ITestResultSummaryPO;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;


/**
 * Editor input containing the information necessary to construct a 
 * Test Result tree.
 *
 * @author BREDEX GmbH
 * @created May 11, 2010
 */
public class TestResultEditorInput implements IEditorInput {

    /** database ID of the corresponding Test Result Summary */
    private Long m_testResultSumaryId;

    /** name of the corresponding Test Suite */
    private String m_testSuiteName;
    
    /** start time of the corresponding Test Suite */
    private Date m_testSuiteStartTime;
    
    /** database ID of the Project associated with the test run */
    private Long m_parentProjectId;

    /**
     * Constructor
     * 
     * @param testResultSummary The Test Result Summary for which to retrieve 
     *                          the Test Result details.
     */
    public TestResultEditorInput(ITestResultSummaryPO testResultSummary) {
        Validate.notNull(testResultSummary);
        m_testResultSumaryId = testResultSummary.getId();
        m_testSuiteName = testResultSummary.getTestsuiteName();
        m_testSuiteStartTime = testResultSummary.getTestsuiteStartTime();
        m_parentProjectId = testResultSummary.getInternalProjectID();
    }

    /**
     * {@inheritDoc}
     */
    public boolean exists() {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    public ImageDescriptor getImageDescriptor() {
        return ImageDescriptor.getMissingImageDescriptor();
    }

    /**
     * {@inheritDoc}
     */
    public String getName() {
        return m_testSuiteName;
    }

    /**
     * {@inheritDoc}
     */
    public IPersistableElement getPersistable() {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public String getToolTipText() {
        return m_testSuiteName + " (" + m_testSuiteStartTime + ")";  //$NON-NLS-1$ //$NON-NLS-2$
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public Object getAdapter(Class adapter) {
        if (adapter == TestResultEditorInput.class) {
            return this;
        }
        return null;
    }

    /**
     * 
     * @return the database ID of the corresponding Test Result Summary.
     */
    public Long getTestResultSummaryId() {
        return m_testResultSumaryId;
    }

    /**
     * 
     * @return the database ID of the Project associated with the test run.
     */
    public Long getParentProjectId() {
        return m_parentProjectId;
    }
    
    /**
     * {@inheritDoc}
     */
    public boolean equals(Object obj) {
        if (!(obj instanceof TestResultEditorInput)) {
            return false;
        }
        
        TestResultEditorInput otherInput = (TestResultEditorInput)obj;
        
        return new EqualsBuilder()
            .append(getToolTipText(), otherInput.getToolTipText()).isEquals();
    }

    /**
     * {@inheritDoc}
     */
    public int hashCode() {
        return new HashCodeBuilder().append(getToolTipText()).toHashCode();
    }
}
