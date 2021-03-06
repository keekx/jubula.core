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
package org.eclipse.jubula.communication.message;

import java.util.Map;

import org.eclipse.jubula.tools.constants.CommandConstants;

/**
 * @author BREDEX GmbH
 * @created 07.09.2010
 */
public class GetMonitoringDataResponseMessage extends Message {
    /** The profiling agent id */
    private String m_monitoringId;

    /** Additonal Monitoring Values */
    private Map m_monitoringValues;

    /** {@inheritDoc} */
    public String getCommandClass() {
        return CommandConstants.GET_MONITORING_DATA_RESPONSE_COMMAND;
    }

    /** @return the coverageId */
    public String getMonitoringId() {
        return m_monitoringId;
    }

    /**
     * @param monitoringId
     *            the monitoringId to set
     */
    public void setMonitoringId(String monitoringId) {
        m_monitoringId = monitoringId;
    }

    /** @return Map containing additonal Monitroing values */
    public Map getMonitoringValues() {
        return m_monitoringValues;
    }

    /**
     * @param map
     *            The map to set
     */
    public void setMonitoringValues(Map map) {
        this.m_monitoringValues = map;
    }
}