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
package org.eclipse.jubula.client.ui.rcp.controllers.propertysources;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.fieldassist.ContentProposalAdapter;
import org.eclipse.jface.fieldassist.IContentProposalProvider;
import org.eclipse.jubula.client.core.businessprocess.IParamNameMapper;
import org.eclipse.jubula.client.core.events.DataEventDispatcher;
import org.eclipse.jubula.client.core.events.DataEventDispatcher.DataState;
import org.eclipse.jubula.client.core.events.DataEventDispatcher.UpdateState;
import org.eclipse.jubula.client.core.model.IExecTestCasePO;
import org.eclipse.jubula.client.core.model.IParamDescriptionPO;
import org.eclipse.jubula.client.core.model.IParamNodePO;
import org.eclipse.jubula.client.core.model.IParameterInterfacePO;
import org.eclipse.jubula.client.core.model.IProjectPO;
import org.eclipse.jubula.client.core.persistence.GeneralStorage;
import org.eclipse.jubula.client.ui.constants.IconConstants;
import org.eclipse.jubula.client.ui.rcp.contentassist.TestDataCubeRefContentProposalProvider;
import org.eclipse.jubula.client.ui.rcp.controllers.propertydescriptors.ContentAssistedTextPropertyDescriptor;
import org.eclipse.jubula.client.ui.rcp.controllers.propertydescriptors.JBPropertyDescriptor;
import org.eclipse.jubula.client.ui.rcp.controllers.propertydescriptors.ParamTextPropertyDescriptor;
import org.eclipse.jubula.client.ui.rcp.factory.TestDataControlFactory;
import org.eclipse.jubula.client.ui.rcp.i18n.Messages;
import org.eclipse.jubula.client.ui.rcp.provider.labelprovider.DisabledLabelProvider;
import org.eclipse.jubula.client.ui.rcp.provider.labelprovider.ParameterValueLabelProvider;
import org.eclipse.jubula.client.ui.rcp.validator.TestDataCubeReferenceValidator;
import org.eclipse.jubula.client.ui.rcp.widgets.CheckedText.IValidator;
import org.eclipse.jubula.tools.constants.StringConstants;
import org.eclipse.jubula.tools.i18n.I18n;
import org.eclipse.jubula.tools.xml.businessmodell.ParamValueSet;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.PropertyDescriptor;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;


/**
 * This class is the PropertySource of an ExecTestCase.
 * Its used to display and edit the properties in the Properties View.
 * @author BREDEX GmbH
 * @created 23.02.2005
 */
public class ExecTestCaseGUIPropertySource extends 
    SpecTestCaseGUIPropertySource {

    /** Image for original Data */ 
    public static final Image ORIGINAL_DATA_IMAGE = 
        IconConstants.ORIGINAL_DATA_IMAGE;
       
    /** Image for overwritten Data */ 
    public static final Image OVERWRITTEN_DATA_IMAGE = 
        IconConstants.OVERWRITTEN_DATA_IMAGE;
   
    /** Constant for the String Specification Name */
    public static final String P_SPECNAME_DISPLAY_NAME =
        Messages.ExecTestCaseGUIPropertySourceSpecificationName;
    
    /** Constant for the String Test Case Reference Name */
    public static final String P_REFERNCE_DISPLAY_NAME =
        Messages.ExecTestCaseGUIPropertySourceTestCaseReferenceName;

    /** cached property descriptor for name */
    private IPropertyDescriptor m_namePropDesc = null;
    
    /** cached property descriptor for name of referenced Test Case */
    private IPropertyDescriptor m_specNamePropDesc = null;
    
    /** cached property descriptor for comment */
    private IPropertyDescriptor m_commentPropDesc = null;

    /** cached property descriptor for external data file */
    private PropertyDescriptor m_extDataPropDesc = null;

    /** cached property descriptor for referenced Test Data Cube */
    private PropertyDescriptor m_referencedCubePropDesc = null;

    
    /** cached property descriptors for parameters */
    private List<IPropertyDescriptor> m_paramPropDescList = 
        new ArrayList<IPropertyDescriptor>();
    
    /**
     * Constructor
     * @param testCase the dependened TestCase.
     */
    public ExecTestCaseGUIPropertySource(IExecTestCasePO testCase) {
        super(testCase);
    }
       
    /**
     * 
     * {@inheritDoc}
     */
    protected void initPropDescriptor() {
        if (!getPropertyDescriptorList().isEmpty()) {
            clearPropertyDescriptors();
        }
        // TestCase Name
        if (m_namePropDesc == null) {
            m_namePropDesc = new TextPropertyDescriptor(
                    new ExecNameController(), P_REFERNCE_DISPLAY_NAME);
        }
        addPropertyDescriptor(m_namePropDesc);
        
        // Specification Name
        if (m_specNamePropDesc == null) {
            JBPropertyDescriptor propDes = new JBPropertyDescriptor(
                    new SpecNameController(), P_SPECNAME_DISPLAY_NAME);
            propDes.setLabelProvider(new DisabledLabelProvider());
            m_specNamePropDesc = propDes;
        }
        addPropertyDescriptor(m_specNamePropDesc);
        
        // Comment
        if (m_commentPropDesc == null) {
            m_commentPropDesc = new TextPropertyDescriptor(
                    new CommentController(), P_ELEMENT_DISPLAY_COMMENT);
        }
        addPropertyDescriptor(m_commentPropDesc);

        // Task ID
        if (getTaskIdPropDesc() == null) {
            JBPropertyDescriptor taskIdPropDesc = new JBPropertyDescriptor(
                new ReadOnlyTaskIdController(),
                Messages.AbstractGuiNodePropertySourceTaskId);
            taskIdPropDesc.setLabelProvider(new DisabledLabelProvider());
            setTaskIdPropDesc(taskIdPropDesc);
        }
        addPropertyDescriptor(getTaskIdPropDesc());
        
        // Data Source
        addPropertyDescriptor(getDataSourcePropertyDescr(
                new ExecTestCaseTestDataSourceController(this)));
        
        // External data file
        if (m_extDataPropDesc == null) {
            m_extDataPropDesc = new TextPropertyDescriptor(
                new ExternalDataController(this), P_ELEMENT_DISPLAY_DATEFILE);
            m_extDataPropDesc.setCategory(P_TESTDATA_CAT);
        }
        addPropertyDescriptor(m_extDataPropDesc);

        // Referenced Test Data Cube
        if (m_referencedCubePropDesc == null) {
            IProjectPO activeProject = 
                GeneralStorage.getInstance().getProject();
            IContentProposalProvider dataCubeRefProposalProvider = null;
            IValidator dataCubeRefValidator = null;
            if (activeProject != null) {
                dataCubeRefProposalProvider = 
                    new TestDataCubeRefContentProposalProvider(activeProject, 
                            (IParameterInterfacePO)getPoNode());
                dataCubeRefValidator =
                    new TestDataCubeReferenceValidator(activeProject);
            }
            
            m_referencedCubePropDesc = 
                new ContentAssistedTextPropertyDescriptor(
                        new ReferenceTestDataController(this), 
                        P_ELEMENT_DISPLAY_REFDATA,
                        dataCubeRefProposalProvider, dataCubeRefValidator,
                        ContentProposalAdapter.PROPOSAL_REPLACE);
            m_referencedCubePropDesc.setCategory(P_TESTDATA_CAT);
        }
        addPropertyDescriptor(m_referencedCubePropDesc);
        // Parameters
        addPropertyDescriptor(createParamDescriptors());
    }
    
    /**
     * 
     * @return a List of PropertyDescriptors of parameters.
     */
    protected List< IPropertyDescriptor > createParamDescriptors() {
        if (m_paramPropDescList.isEmpty()) {
            PropertyDescriptor propDes;
            //      init Parameters
            final IParamNodePO paramNodePO = (IParamNodePO)getPoNode();
            List <IParamDescriptionPO> paramList = 
                paramNodePO.getParameterList();
            IParamNameMapper activeParamNameMapper = getActiveParamNameMapper();
            for (IParamDescriptionPO paramDescr : paramList) {
                ParamValueSet valueSet = 
                    ParamTextPropertyDescriptor.getValuesSet(
                            paramNodePO, paramDescr.getUniqueId());
                propDes = TestDataControlFactory.createValuePropertyDescriptor(
                        new ParameterValueController(this, 
                                paramDescr, activeParamNameMapper), 
                                getParameterNameDescr(paramDescr), 
                                ParamTextPropertyDescriptor.getValues(valueSet),
                                valueSet != null 
                                    ? valueSet.isCombinable() : false);
                propDes.setCategory(P_PARAMETER_CAT);
                propDes.setLabelProvider(
                        new ParameterValueLabelProvider(INCOMPL_DATA_IMAGE));
                m_paramPropDescList.add(propDes);
            }
        }

        return m_paramPropDescList;
    }
    
    /**
     * Class to control the name of the depending SpecTestCasePO. 
     * @author BREDEX GmbH
     * @created 23.02.2005
     */
    protected class SpecNameController extends AbstractPropertyController {

        /**
         * {@inheritDoc}
         */
        public boolean setProperty(Object value) {
            // do nothing, read only
            return true;
        }                
        /**
         * {@inheritDoc}
         */
        public Object getProperty() {
            IExecTestCasePO exTc = (IExecTestCasePO) getPoNode();
            if (exTc.getSpecTestCase() != null) {
                return exTc.getSpecTestCase().getName(); 
            }
            
            // FIXME zeb Provide an appropriate display string
            return StringConstants.EMPTY;
        }
        
        /**
         * {@inheritDoc}
         */
        public Image getImage() {
            return READONLY_IMAGE;
        }
    }
    
    /**
     * Class to control the name. 
     * @author BREDEX GmbH
     * @created 23.02.2005
     */
    protected class ExecNameController extends ElementNameController {

        /**
         * {@inheritDoc}
         */
        public Object getProperty() {
            if (getPoNode() != null) {
                IExecTestCasePO exTc = (IExecTestCasePO) getPoNode();
                String name = exTc.getRealName();
                if (name != null) {
                    return name;
                }
            }
            return StringConstants.EMPTY;
        }
    }
    
    /**
     * Class to control the parameter values of the depending ExecTestCase.
     * @author BREDEX GmbH
     * @created 20.04.2005
     */
    public class ParameterValueController extends
        AbstractParamValueController {
        
        /**
         * The constructor.
         * @param paramDescr The parameter description.
         * @param s
         *      AbstractNodePropertySource
         * @param paramNameMapper the param name mapper
         */
        public ParameterValueController(AbstractNodePropertySource s, 
            IParamDescriptionPO paramDescr, IParamNameMapper paramNameMapper) {
            super(s, paramDescr, paramNameMapper);
        }
        
    }
    
    /**
     * Class to control the test data source for an exec test case
     * 
     * @author BREDEX GmbH
     * @created Aug 30, 2010
     */
    public class ExecTestCaseTestDataSourceController extends
            SpecTestCaseTestDataSourceController {
        /**
         * <code>DATA_SOURCE_SPEC</code>
         */
        protected static final String DATA_SOURCE_REFERENCED = "TestDataSource.spec"; //$NON-NLS-1$

        /**
         * Constructor
         * @param s AbstractNodePropertySource
         */
        public ExecTestCaseTestDataSourceController(
                AbstractNodePropertySource s) {
            super(s);
            getDataSource().add(DATA_SOURCE_REFERENCED);
        }

        /**
         * {@inheritDoc}
         */
        public Object getProperty() {
            getPropertySource().setReadOnly(true);
            if (getPoNode() instanceof IParamNodePO) {
                IParamNodePO node = (IParamNodePO)getPoNode();
                String dataSource = getDataSource(node);
                if (dataSource == UNKOWN_DATA_SOURCE
                        && node instanceof IExecTestCasePO) {
                    IExecTestCasePO exec = (IExecTestCasePO)node;
                    getPropertySource().setReadOnly(false);
                    if (exec.getHasReferencedTD()) {
                        return getDataSource().indexOf(DATA_SOURCE_REFERENCED);
                    }
                    return getDataSource().indexOf(DATA_SOURCE_LOCAL);
                }
                return getDataSource().indexOf(dataSource);
            }
            return getDataSource().indexOf(UNKOWN_DATA_SOURCE);
        }
        
        /**
         * @return the dataSources human readable
         */
        public String[] getUserChoosableDataSource() {
            if (getUserChoosableValues().isEmpty()) {
                getUserChoosableValues().add(
                        I18n.getString(DATA_SOURCE_LOCAL));
                getUserChoosableValues().add(
                        I18n.getString(DATA_SOURCE_REFERENCED));
            }
            return getUserChoosableValues().toArray(
                    new String[getUserChoosableValues().size()]);
        }
        
        /**
         * {@inheritDoc}
         */
        public boolean setProperty(Object value) {
            if (getPoNode() instanceof IParamNodePO) {
                IParamNodePO node = (IParamNodePO)getPoNode();
                if (node instanceof IExecTestCasePO) {
                    IExecTestCasePO exec = (IExecTestCasePO)node;
                    int index = Integer.valueOf(
                            String.valueOf(value));
                    if (index >= 0) {
                        final String newDataSource = 
                            getUserChoosableValues().get(index);
                        boolean shouldBeLocal = newDataSource 
                            == I18n.getString(DATA_SOURCE_LOCAL);
                        boolean isLocal = !exec.getHasReferencedTD();
                        if (shouldBeLocal == isLocal) {
                            return false;
                        } else if (shouldBeLocal) {
                            exec.resolveTDReference();
                        } else {
                            exec.setHasReferencedTD(true);
                        }
                        DataEventDispatcher.getInstance()
                            .fireDataChangedListener(
                                node, DataState.StructureModified, 
                                UpdateState.onlyInEditor);
                        return true;
                    }
                }
            }
            return false;
        }
    }
}