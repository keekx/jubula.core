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
package org.eclipse.jubula.tools.xml.businessprocess;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jubula.tools.xml.businessmodell.Profile;

/**
 * This class contains methods for reading the configuration file and for 
 * mapping the configuration file to java objects.
 *
 * @author BREDEX GmbH
 * @created 08.07.2004
 */
public class ProfileBuilder {

    /**
     * The System of components.
     */
    private static List profiles = null;
    
    /** 
     * Default constructor
     */
    private ProfileBuilder() {
        super();
    }
    
    /**
     * Returns a List of all profiles
     * @return List
     */
    public static List getProfiles() {
        if (profiles == null) {
            profiles = new ArrayList();

            Profile profile = new Profile("Standard", 0.60, 0.30, 0.10, 0.85); //$NON-NLS-1$
            profiles.add(profile);

            profile = new Profile("Strict", 0.60, 0.30, 0.10, 1.00); //$NON-NLS-1$
            profiles.add(profile);

            profile = new Profile("Given names", 1.00, 0.00, 0.00, 1.00); //$NON-NLS-1$
            profiles.add(profile);

        }
        return profiles;
    }

    /**
     * Returns a List of all profiles
     * @return String Array
     */
    public static String[] getProfileNames() {
        Iterator iter = getProfiles().iterator();
        String[] names = new String[getProfiles().size()];
        int index = 0;
        while (iter.hasNext()) {
            names[index] = ((Profile) iter.next()).getName();
            index++;
        }
        return names;
    }

    /**
     * @param name
     *      String
     * @return
     *      Profile
     */
    public static Profile getProfile(String name) {
        Iterator iter = getProfiles().iterator();
        while (iter.hasNext()) {
            Profile prof = ((Profile) iter.next());
            if (prof.getName().equals(name)) {
                return prof;
            }
        }
        return null;
    }

    /**
     * @return the default object mapping profile.
     */
    public static Profile getDefaultProfile() {
        return (Profile)ProfileBuilder.getProfiles().get(0);
    }
}
