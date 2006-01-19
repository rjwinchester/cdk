/* $RCSfile$
 * $Author$
 * $Date$
 * $Revision$
 *
 * Copyright (C) 2002-2005  The Jmol Development Team
 *
 * Contact: cdk-devel@lists.sourceforge.net
 *
 *  This library is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public
 *  License as published by the Free Software Foundation; either
 *  version 2.1 of the License, or (at your option) any later version.
 *
 *  This library is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *  Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public
 *  License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 */
package org.openscience.cdk.io;

import java.util.Vector;

import org.openscience.cdk.interfaces.IChemObject;
import org.openscience.cdk.io.listener.IChemObjectIOListener;
import org.openscience.cdk.io.setting.IOSetting;

/**
 * Abstract class that ChemObjectReader's can implement to have it
 * take care of basic stuff, like managing the ReaderListeners.
 *
 * @cdk.module io
 */
public abstract class DefaultChemObjectWriter implements IChemObjectWriter {

    /**
     * Holder of reader event listeners.
     */
    private Vector listenerList = new Vector();

    public void addChemObjectIOListener(IChemObjectIOListener listener) {
        listenerList.addElement(listener);
    }

    public void removeChemObjectIOListener(IChemObjectIOListener listener) {
        listenerList.removeElement(listener);
    }

    /**
     * @deprecated
     */
    public boolean accepts(IChemObject object) {
        return accepts(object.getClass());
    }
    public boolean accepts(Class objectClass) {
        // leave it up the write(IChemObject) to decide by default
        return true;
    }
    
    protected void fireIOSettingQuestion(IOSetting setting) {
        for (int i = 0; i < listenerList.size(); ++i) {
            IChemObjectIOListener listener = (IChemObjectIOListener) listenerList.elementAt(i);
            listener.processIOSettingQuestion(setting);
        }
    }

    public IOSetting[] getIOSettings() {
        return new IOSetting[0];
    }
    
}
