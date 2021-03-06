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
package org.eclipse.jubula.client.core.businessprocess.importfilter;

import java.io.File;
import java.io.IOException;
import java.util.Locale;

import org.eclipse.jubula.client.core.businessprocess.importfilter.exceptions.DataReadException;
import org.eclipse.jubula.client.core.businessprocess.importfilter.exceptions.NoSupportForLocaleException;


/**
 * @author BREDEX GmbH
 * @created Nov 8, 2005
 */
public interface IDataImportFilter {

    /**
     * @return a String Array of supported file extensions
     * without "." {"xls", "xlt"} for example
     */
    public abstract String[] getFileExtensions();

    /**
     * parses a file and returns the data as DataTable structure
     * 
     * @param dataDir
     *      directory for data files
     * @param file
     *      data source File
     * @param locale
     *      data source locale
     * @return
     *      filled TestDataManager with new data
     * @throws IOException
     *      error occured while reading data source
     * @throws NoSupportForLocaleException
     *      no support for selected locale
     */
    public abstract DataTable parse(File dataDir, String file, Locale locale) 
        throws IOException, NoSupportForLocaleException, DataReadException;

}