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
package org.eclipse.jubula.client.core.model;

import java.util.Map;

import org.eclipse.jubula.tools.objects.IMonitoringValue;



/**
 * @author BREDEX GmbH
 * @created Jan 22, 2010
 */
public interface ITestResultSummaryPO extends ITestResultSummary {
    /**
     * <code>DEFAULT_NUMBER_OF_FAILED_TEST_STEPS</code>
     */
    public static final int DEFAULT_NUMBER_OF_FAILED_TEST_STEPS = -1;
    
    /** testrun state ok */
    public static final String STATE_OK = "OK"; //$NON-NLS-1$
    
    /** testrun state failed */
    public static final String STATE_FAILED = "FAILED"; //$NON-NLS-1$
    
    /** testrun state testing */
    public static final String STATE_STOPPED = "STOPPED"; //$NON-NLS-1$

    /**
     * only for hibernate
     * @return Returns the id.
     */
    public abstract Long getId();

    /** 
     * {@inheritDoc}
     */
    public abstract Integer getVersion();
    /**
     * @param monitoringValue the monitoringValue to set
     */
    public abstract void setMonitoringValues(
            Map<String, IMonitoringValue> monitoringValue);
    
    /**
     * @return the monitoringValue
     */
    public abstract Map<String, IMonitoringValue> getMonitoringValues();
    
}