/* $RCSfile$    
 * $Author$    
 * $Date$    
 * $Revision$
 * 
 * Copyright (C) 2005-2006  The Chemistry Development Kit (CDK) project
 * 
 * Contact: cdk-devel@lists.sourceforge.net
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 2.1
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA. 
 */
package org.openscience.cdk.test.config.atomtypes;

import java.io.StringReader;
import java.util.Vector;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.openscience.cdk.ChemObject;
import org.openscience.cdk.CDKConstants;
import org.openscience.cdk.config.atomtypes.AtomTypeReader;
import org.openscience.cdk.interfaces.IAtomType;
import org.openscience.cdk.test.CDKTestCase;

/**
 * Checks the funcitonality of the AtomTypeReader.
 *
 * @cdk.module test-extra
 */
public class AtomTypeReaderTest extends CDKTestCase {
     
	public AtomTypeReaderTest(String name) {
		super(name);
	}
	
	public void setUp() {}
	
	public static Test suite() {
		return new TestSuite(AtomTypeReaderTest.class);
	}

    public void testAtomTypeReader_Reader() {
        AtomTypeReader reader = new AtomTypeReader(
            new StringReader("")
        );
        assertNotNull(reader);
    }
    
    public void testReadAtomTypes_IChemObjectBuilder() {
        AtomTypeReader reader = new AtomTypeReader(
            new StringReader(
            "<atomTypeList xmlns=\"http://www.xml-cml.org/schema/cml2/core\"                              " +
            "  xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"                                    " +
            "  xsi:schemaLocation=\"http://www.xml-cml.org/schema/cml2/core ../../io/cml/data/cmlAll.xsd\"" +
            "  id=\"mol2\" title=\"MOL2 AtomTypes\">                                                      " +
            "                                                                                             " +
            "  <metadataList>                                                                             " +
            "    <metadata name=\"cvs:last-change-by\" content=\"$Author$\"/>                     " +
            "    <metadata name=\"cvs:date\" content=\"$Date$\"/>                   " +
            "    <metadata name=\"cvs:revision\" content=\"$Revision$\"/>                           " +
            "  </metadataList>                                                                            " +
            "                                                                                             " +
            "  <atomType id=\"C.3\" title=\"1\">                                                          " +
            "    <atom elementType=\"C\"/>                                                                " +
            "    <scalar dataType=\"xsd:string\" dictRef=\"cdk:hybridization\">sp3</scalar>               " +
            "  </atomType>                                                                                " +
            "  <atomType id=\"C.2\" title=\"2\">                                                          " +
            "    <atom elementType=\"C\"/>                                                                " +
            "    <scalar dataType=\"xsd:string\" dictRef=\"cdk:hybridization\">sp2</scalar>               " +
            "  </atomType>                                                                                " +
            "</atomTypeList>")
        );
        assertNotNull(reader);
        Vector types = reader.readAtomTypes(new ChemObject().getBuilder());
        assertNotNull(types);
        assertEquals(2, types.size());
    }
    
    public void testReadAtomTypes2() {
        String data = 
            "<atomTypeList xmlns=\"http://www.xml-cml.org/schema/cml2/core\"                              " +
            "  xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"                                    " +
            "  xsi:schemaLocation=\"http://www.xml-cml.org/schema/cml2/core ../../io/cml/data/cmlAll.xsd\"" +
            "  id=\"mol2\" title=\"MOL2 AtomTypes\">                                                      " +
            "                                                                                             " +
            "  <metadataList>                                                                             " +
            "    <metadata name=\"cvs:last-change-by\" content=\"$Author$\"/>                     " +
            "    <metadata name=\"cvs:date\" content=\"$Date$\"/>                   " +
            "    <metadata name=\"cvs:revision\" content=\"$Revision$\"/>                           " +
            "  </metadataList>                                                                            " +
            "                                                                                             " +
            "  <atomType id=\"C.3\" title=\"1\">                                                          " +
            "    <atom elementType=\"C\"/>                                                                " +
            "    <scalar dataType=\"xsd:string\" dictRef=\"cdk:hybridization\">sp3</scalar>               " +
            "  </atomType>                                                                                " +
            "  <atomType id=\"C.2\" title=\"2\">                                                          " +
            "    <atom elementType=\"C\"/>                                                                " +
            "    <scalar dataType=\"xsd:string\" dictRef=\"cdk:hybridization\">sp2</scalar>               " +
            "  </atomType>                                                                                " +
            "</atomTypeList>";
        
        AtomTypeReader reader = new AtomTypeReader(
            new StringReader(data)
        );
        assertNotNull(reader);
        Vector types = reader.readAtomTypes(new ChemObject().getBuilder());
        assertNotNull(types);
        assertEquals(2, types.size());
    }
    
    public void testReadAtomTypes_FF() {
        String data = 
            "<atomTypeList xmlns=\"http://www.xml-cml.org/schema/cml2/core\"                              \n" +
            "  xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"                                    \n" +
            "  xsi:schemaLocation=\"http://www.xml-cml.org/schema/cml2/core ../../io/cml/data/cmlAll.xsd\"\n" +
            "  id=\"mol2\" title=\"MOL2 AtomTypes\">                                                      \n" +
            "                                                                                             \n" +
            "  <metadataList>                                                                             \n" +
            "    <metadata name=\"cvs:last-change-by\" content=\"$Author$\"/>                     \n" +
            "    <metadata name=\"cvs:date\" content=\"$Date$\"/>                   \n" +
            "    <metadata name=\"cvs:revision\" content=\"$Revision$\"/>                           \n" +
            "  </metadataList>                                                                            \n" +
            "\n" +
            " <atomType id=\"C\">\n" +
            "	<!-- for example in CC-->\n" +
            "   <atom elementType=\"C\" formalCharge=\"0\">\n" +
            "     <scalar dataType=\"xsd:double\" dictRef=\"cdk:maxBondOrder\">1.0</scalar>\n" +
            "     <scalar dataType=\"xsd:double\" dictRef=\"cdk:bondOrderSum\">4.0</scalar>\n" +
            "     <scalar dataType=\"xsd:integer\" dictRef=\"cdk:formalNeighbourCount\">4</scalar>\n" +
            "     <scalar dataType=\"xsd:integer\" dictRef=\"cdk:valency\">4</scalar>\n" +
            "     <scalar dataType=\"xsd:string\" dictRef=\"cdk:hybridization\">sp3</scalar>\n" +
            "     <scalar dataType=\"xsd:string\" dictRef=\"cdk:DA\">-</scalar>\n" +
            "     <scalar dataType=\"xsd:string\" dictRef=\"cdk:sphericalMatcher\">[CSP]-[0-4][-]?+;</scalar>\n" +
            "     <scalar dataType=\"xsd:integer\" dictRef=\"cdk:ringSize\">3</scalar>\n" +
            "     <scalar dataType=\"xsd:integer\" dictRef=\"cdk:ringConstant\">3</scalar>\n" +
            "   </atom>\n" +
            " </atomType>\n" + 
            "</atomTypeList>\n";
        
        AtomTypeReader reader = new AtomTypeReader(
            new StringReader(data)
        );
        assertNotNull(reader);
        Vector types = reader.readAtomTypes(new ChemObject().getBuilder());
        assertNotNull(types);
        assertEquals(1, types.size());
        
        Object object = types.elementAt(0);
        assertNotNull(object);
        assertTrue(object instanceof IAtomType);
        IAtomType atomType = (IAtomType)object;
        
        assertEquals("[CSP]-[0-4][-]?+;", atomType.getProperty(CDKConstants.SPHERICAL_MATCHER));
        assertFalse(atomType.getFlag(CDKConstants.IS_HYDROGENBOND_ACCEPTOR));
        assertFalse(atomType.getFlag(CDKConstants.IS_HYDROGENBOND_DONOR));

        assertEquals(3, ((Integer)atomType.getProperty(CDKConstants.PART_OF_RING_OF_SIZE)).intValue());
        assertEquals(3, ((Integer)atomType.getProperty(CDKConstants.CHEMICAL_GROUP_CONSTANT)).intValue());
    }
}
