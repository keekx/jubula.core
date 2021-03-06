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
package org.eclipse.jubula.client.core.businessprocess.treeoperations;

import org.eclipse.jubula.client.core.model.ICapPO;
import org.eclipse.jubula.client.core.model.ICompNamesPairPO;
import org.eclipse.jubula.client.core.model.IExecTestCasePO;
import org.eclipse.jubula.client.core.model.INodePO;
import org.eclipse.jubula.client.core.utils.AbstractNonPostOperatingTreeNodeOperation;
import org.eclipse.jubula.client.core.utils.ITreeTraverserContext;

/**
 * Operation for checking whether a specific Component Name is being reused.
 * This is often more performant than just check whether the reuse set is 
 * empty, as the operation halts as soon as a reuse instance is found.
 *
 * @author BREDEX GmbH
 * @created Mar 6, 2009
 */
public class CheckIfComponentNameIsReusedOp 
    extends AbstractNonPostOperatingTreeNodeOperation<INodePO> {

    /** GUID of Component Name to use for this operation */
    private String m_compNameGuid;
    
    /** any reuses found? */
    private boolean m_isReused = false;
    
    /** the operation used to find instances of reuse */

    /**
     * Constructor
     * 
     * @param compNameGuid The GUID of the Component Name for which to check
     *                     the reuse.
     */
    public CheckIfComponentNameIsReusedOp(String compNameGuid) {
        m_compNameGuid = compNameGuid;
    }
    
    /**
     * {@inheritDoc}
     */
    public boolean operate(
            ITreeTraverserContext<INodePO> ctx, INodePO parent, INodePO node, 
            boolean alreadyVisited) {
        
//        if (Persistor.isPoSubclass(node, ICapPO.class)) {
        if (node instanceof ICapPO) {
            ICapPO cap = (ICapPO)node;
            if (cap.getComponentName() != null 
                    && cap.getComponentName().equals(m_compNameGuid)) {
                m_isReused = true;
                ctx.setContinued(false);
                return false;
            }
//        } else if (Persistor.isPoSubclass(node, IExecTestCasePO.class)) {
        } else if (node instanceof IExecTestCasePO) {
            IExecTestCasePO execTc = (IExecTestCasePO)node;
            for (ICompNamesPairPO pair : execTc.getCompNamesPairs()) {
                if (pair.getFirstName().equals(m_compNameGuid)
                        || pair.getSecondName().equals(m_compNameGuid)) {
                    m_isReused = true;
                    ctx.setContinued(false);
                    return false;
                }
            }
        }

        return !alreadyVisited;
    }

    /**
     * 
     * @return <code>true</code> if at least one reuse instance has been found.
     *         Otherwise, <code>false</code>.
     */
    public boolean hasFoundReuse() {
        return m_isReused;
    }
}
