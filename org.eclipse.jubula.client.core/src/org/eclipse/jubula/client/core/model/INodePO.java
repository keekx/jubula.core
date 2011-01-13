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

import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/**
 * @author BREDEX GmbH
 * @created 19.12.2005
 */
public interface INodePO extends ITimestampPO {
    /**
     * @return The name of this node
     */
    public abstract String getName();

    /**
     * Sets the value of the m_name property.
     * @param name the name of this node
     */
    public abstract void setName(String name);
    
    /**
     * @return the current value of the m_parentNode property
     */
    public INodePO getParentNode();

    /**
     * @return the unmodifiable node list.
     */
    public abstract List getUnmodifiableNodeList();

    /**
     * @return Returns the m_comment.
     */
    public abstract String getComment();

    /**
     * @param comment The m_comment to set.
     */
    public abstract void setComment(String comment);
    
    /**
     * @return if Node respectively parentNode of Node is already reused
     */
    public abstract Boolean isReused();

    /**
     * adds a childnode to an existent node
     * creation of reference to the parent node
     * @param childNode
     *            reference to the childnode
     */
    public abstract void addNode(INodePO childNode);

    /**
     * adds a childnode to an existent node
     * creation of reference to the parent node
     * @param position the position to add the childnode.
     * @param childNode
     *            reference to the childnode
     */
    public abstract void addNode(int position, INodePO childNode);

    /**
     * deletes a node and resolves the
     * reference to the parent node
     * sign off as child node of the parent node
     * @param childNode reference to the childnode
     */
    public abstract void removeNode(INodePO childNode);

    /**
     * Removes all child nodes and sets the parent of the child nodes 
     * to <code>null</code>
     */
    public abstract void removeAllNodes();

    /**
     * Returns the index of the given node in the node list.
     * @param node the node whose index is want.
     * @return the index of the given node.
     */
    public abstract int indexOf(INodePO node);

    /**
     * {@inheritDoc}
     */
    public abstract int hashCode();

    /**
     * use this method instead of getNodeList()
     * use add-/removeNode-method for modification of list 
     * @return iterator for unmodifiable NodeList
     */
    public abstract Iterator<INodePO> getNodeListIterator();

    /**
     * @return size of nodeList
     */
    public abstract int getNodeListSize();

    /**
     * @param aut aut, for which to get the sumOMFlag
     * @return Returns the sumOMFlag or false, if the given AUT isn't contained
     * in map
     */
    public abstract boolean getSumOMFlag(IAUTMainPO aut);

    /**
     * @return <code>false</code> if this node or any of its children contain
     *         a reference to a non-existing SpecTestCase. Otherwise, 
     *         <code>true</code>.
     */
    public boolean getSumSpecTcFlag();

    /**
     * @param sumSpecTcFlag The sumSpecTcFlag to set.
     */
    public void setSumSpecTcFlag(boolean sumSpecTcFlag);

    /**
     * @param aut aut, for which to set the sumOMFlag
     * @param sumOMFlag The sumOMFlag to set.
     */
    public abstract void setSumOMFlag(IAUTMainPO aut, boolean sumOMFlag);

    /**
     * method to get the sumTdFlag for a given Locale
     * @param loc locale, for which to get the sumTdFlag
     * @return the state of sumTdFlag
     */
    public abstract boolean getSumTdFlag(Locale loc);

    /**
     * method to set the sumTdFlag for a given Locale
     * @param loc  locale, for which to set the sumTdFlag
     * @param flag the state of sumTdFlag to set
     */
    public abstract void setSumTdFlag(Locale loc, boolean flag);

    /**
     * {@inheritDoc}
     */
    public abstract String toString();

    /**
     * @return Returns the GUID.
     */
    public abstract String getGuid();

    /**
     * Checks for circular dependences with a potential parent.
     * @param parent the parent to check
     * @return true if there is a circular dependence, false otherwise.
     */
    public abstract boolean hasCircularDependences(INodePO parent);

    /**
     * Checks the equality of the given Object with this Object.
     * {@inheritDoc}
     * @param obj the object to check
     * @return if there is a database ID it returns true if the ID is equal.
     * If there is no ID it will be compared to identity.
     */
    public abstract boolean equals(Object obj);
    
    
    /**
     * @param parent to set
     */
    public abstract void setParentNode(INodePO parent);
    
    /**
     * @return the current toolkit level of this node.
     */
    public abstract String getToolkitLevel();

    /**
     * Sets the current toolkit level of this node.
     * @param toolkitLevel the toolkit level.
     */
    public abstract void setToolkitLevel(String toolkitLevel);
    
    /**
     * Returns the valis staus of the node.<br>
     * Normally all Nodes are valid. only CapPOs with an InvalidComponent
     * should return false.
     * @return true if the Node is valid, false otherwise. 
     */
    public boolean isValid();

    /**
     * 
     * @return all attribute types associated with this node.
     */
    public Set<IDocAttributeDescriptionPO> getDocAttributeTypes();
    
    /**
     * 
     * @param attributeType The type of the attribute for which to get the 
     *                      value.
     * @return The documentation attribute value for the given name, or 
     *         <code>null</code> if no such attribute exists.
     */
    public IDocAttributePO getDocAttribute(
            IDocAttributeDescriptionPO attributeType);

    /**
     * 
     * @param attributeType  The type of the attribute for which to set the 
     *                       value.
     * @param attribute The value to set.
     */
    public void setDocAttribute(IDocAttributeDescriptionPO attributeType, 
            IDocAttributePO attribute);
    
    /**
     * @return true, if node has been generated, false otherwise
     */
    public boolean isGenerated();
    
    /**
     * @param isGenerated flag for generation 
     */
    public void setGenerated(boolean isGenerated);
    
    /**
     * @param isActive the isActive to set
     */
    public void setActive(boolean isActive);

    /**
     * @return the isActive
     */
    public boolean isActive();
}