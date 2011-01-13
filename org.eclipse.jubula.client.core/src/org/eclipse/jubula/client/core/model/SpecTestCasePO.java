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

import java.util.List;

import javax.persistence.Basic;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;

import org.apache.commons.lang.Validate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.jubula.client.core.businessprocess.IParamNameMapper;
import org.eclipse.jubula.client.core.persistence.GeneralStorage;
import org.eclipse.jubula.client.core.persistence.HibernateUtil;
import org.eclipse.jubula.client.core.persistence.NodePM;
import org.eclipse.jubula.client.core.persistence.ProjectPM;
import org.eclipse.jubula.tools.exception.GDException;


/**
 * class only for specification data of testcase specificaton data are infos to
 * CapPO tripel like name of CapPO, name of component, action name and the fixed
 * value for each parameter of CapPO as far as set (static data) this static
 * part of testcase is only once existent and will be used as reference for one
 * or more ExecTestCases
 * 
 * @author BREDEX GmbH
 * @created 07.10.2004
 */
@Entity
@DiscriminatorValue(value = "S")
class SpecTestCasePO extends TestCasePO implements ISpecTestCasePO {
    
    /** The logger */
    private static final Log LOG = LogFactory.getLog(SpecTestCasePO.class);

    /**
     * <code>m_isInterfaceLocked</code> flag for lock of parameter(s)modification
     * reserved flag for user defined InterfaceLocked state contrary to isReused state
     */
    private Boolean m_isInterfaceLocked = false;
    
    /**
     * <code>m_isReused</code> flag for lock of parameter(s)modification
     */
    private transient Boolean m_isReused = null;


    /**
     * only for hibernate
     */
    SpecTestCasePO() {
        super();
    }

    /**
     * constructor
     * 
     * @param testCaseName
     *            name of testCase
     * @param isGenerated indicates whether this node has been generated
     */
    SpecTestCasePO(String testCaseName, boolean isGenerated) {
        super(testCaseName, isGenerated);
    }

    /**
     * constructor when GUID is already defined
     * 
     * @param testCaseName
     *            name of testCase
     * @param guid
     *            GUID of the testCase
     * @param isGenerated indicates whether this node has been generated
     */
    SpecTestCasePO(String testCaseName, String guid, boolean isGenerated) {
        super(testCaseName, guid, isGenerated);
    }
    
    /**
     * Adds a parameter to the parameter list to call for each parameter of
     * specTestCase.
     * 
     * @param type
     *            type of parameter (e.g. String)
     * @param name
     *            name of parameter
     * @param guid of parameter           
     * @param mapper mapper to resolve and persist param names
     * @return The new parameter description
     */
    public IParamDescriptionPO addParameter(String type, String name,
        String guid, IParamNameMapper mapper) {
        Validate.notEmpty(type, "Missing parameter type for Testcase " + //$NON-NLS-1$
            this.getName());
        Validate.notEmpty(name, "Missing name for parameter in " + //$NON-NLS-1$
            "testcase " + this.getName()); //$NON-NLS-1$
        
        IParamDescriptionPO desc = PoMaker.createTcParamDescriptionPO(type,
            name, guid, mapper);
        super.addParameter(desc);
        return desc;
    }
    
    
    /**
     * Adds a parameter to the node's list of parameters. if <code>always</code>
     * is <code>true</code>, the parameter will be added even if a parameter
     * with <code>userDefName</code> already exists.
     * 
     * @param type
     *            The parameter type
     * @param name
     *            name of the parameter
     * @param always
     *            If <code>true</code>, a parameter might be added several
     *            times, if <code>false</code>, it will not be added if the
     *            <code>userDefName</code> already exists
     * @param mapper mapper to resolve param names
     * @return the description for new created parameter or null
     */
    public IParamDescriptionPO addParameter(String type, String name, 
        boolean always, IParamNameMapper mapper) {
        IParamDescriptionPO desc = null;
        if (always || getParameterForName(name) == null) {
            desc = addParameter(type, name, mapper);
        }
        return desc;
    }
    
    /**
     * Removes the given parameter from the parameter list. The method also
     * removes the corresponding test data from the own test data manager and
     * from all associated test execution nodes with own (non-referenced) data
     * managers.
     * 
     * {@inheritDoc}
     * 
     * @param uniqueId
     *            the uniqueId of the parameter to be removed.
     */
    public void removeParameter(String uniqueId) {
        IParamDescriptionPO desc = getParameterForUniqueId(uniqueId);
        if (desc != null) {
            removeParameter(desc);
            ((TcParamDescriptionPO)desc).getParamNameMapper()
                .removeParamNamePO(desc.getUniqueId());
        }
        
    }
   
    /**
     * {@inheritDoc}
     */
    public final void moveParameter(String guId, int index) {
        final IParamDescriptionPO parameter = getParameterForUniqueId(guId);
        final List<IParamDescriptionPO> paramList = getHbmParameterList();
        final int currIdx = paramList.indexOf(parameter);
        paramList.remove(currIdx);
        paramList.add(index, parameter);
    }
    
    /**
     * {@inheritDoc}
     */
    @Transient
    public INodePO getParentNode() {
        INodePO parent = super.getParentNode();
        if (parent == null) {
            try {
                parent = ProjectPM.loadProjectById(getParentProjectId(), 
                    GeneralStorage.getInstance().getMasterSession());
            } catch (GDException e) {
                LOG.fatal("Could not find parent for TestCase: " + this //$NON-NLS-1$
                    + "; Returning null.",  //$NON-NLS-1$
                    e);
            }
        }
        return parent;
    }

    /**
     * {@inheritDoc}
     */
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        
        if (obj instanceof ISpecTestCasePO) {
            ISpecTestCasePO otherSpecTC = (ISpecTestCasePO)obj;
            return getGuid().equals(otherSpecTC.getGuid());
        }
        
        return false;
    }

    /**
     * {@inheritDoc}
     */
    public int hashCode() {
        return getGuid().hashCode();
    }
    
    
    /**
     * 
     * @return the isInterfaceLocked
     */
    @Basic
    public Boolean isInterfaceLocked() {
        return m_isInterfaceLocked;
    }

    /**
     * @param isInterfaceLocked the isInterfaceLocked to set
     */
    public void setInterfaceLocked(Boolean isInterfaceLocked) {
        m_isInterfaceLocked = isInterfaceLocked;
    }

    /**
     * @return the isInterfaceLocked
     */
    @Transient
    public Boolean isReused() {
        setIsReused(NodePM.isReused(this, 
            GeneralStorage.getInstance().getMasterSession()));
        return m_isReused;
    }
    
    /**
     * @param isReused the isReused to set
     */
    public void setIsReused(Boolean isReused) {
        m_isReused = isReused;
    }

    /**
     * Adds a parameter to the parameter list to call for each parameter of
     * specTestCase.
     * 
     * @param type
     *            type of parameter (e.g. String)
     * @param name
     *            name of parameter
     * @param mapper mapper to resolve and persist param names
     * @return The new parameter description
     */
    public IParamDescriptionPO addParameter(String type, String name, 
        IParamNameMapper mapper) {
        Validate.notEmpty(type, "Missing parameter type for Testcase " + //$NON-NLS-1$
            this.getName());
        Validate.notEmpty(name, "Missing name for parameter in " + //$NON-NLS-1$
            "testcase " + this.getName()); //$NON-NLS-1$
        
        IParamDescriptionPO desc = PoMaker.createTcParamDescriptionPO(type,
            name, HibernateUtil.generateGuid(), mapper);
        super.addParameter(desc);
        return desc;
    }
    
}