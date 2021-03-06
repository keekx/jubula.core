/*******************************************************************************
 * Copyright (c) 2004, 2013 BREDEX GmbH.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     BREDEX GmbH - initial API and implementation and/or initial documentation
 *******************************************************************************/
package org.eclipse.jubula.toolkit.common;

import java.util.Map;

import org.eclipse.swt.widgets.Composite;

/**
 * @author BREDEX GmbH
 */
public abstract class AbstractToolkitProvider implements IToolKitProvider {
    /** {@inheritDoc} */
    public Composite getAutConfigDialog(Composite parent, int style,
        Map<String, String> autConfig, String autName) {
        return null;
    }
}
