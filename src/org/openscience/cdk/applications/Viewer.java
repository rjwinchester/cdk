/* $RCSfile$
 * $Author$
 * $Date$
 * $Revision$
 *
 * Copyright (C) 2002  The Chemistry Development Kit (CDK) project
 *
 * Contact: steinbeck@ice.mpg.de, gezelter@maul.chem.nd.edu, egonw@sci.kun.nl
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
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */

package org.openscience.cdk.applications;

import org.openscience.cdk.*;
import org.openscience.cdk.io.*;
import org.openscience.cdk.renderer.*;
import org.openscience.cdk.tools.*;
import org.openscience.cdk.geometry.*;
import org.openscience.cdk.smiles.*;
import org.openscience.cdk.layout.*;
import java.util.*;
import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.vecmath.*;

/**
 * Command line utility for viewing chemical information from files.
 *
 * @keyword command line util
 */
public class Viewer {

    private org.openscience.cdk.tools.LoggingTool logger;

    private boolean useJava3D;
    private boolean use3D;

    private void view(Molecule m, boolean use3D, boolean useJava3D) {
        JFrame frame = new JFrame("CDK Molecule Viewer");
        frame.getContentPane().setLayout(new BorderLayout());

        // use Accelerated viewer if 3D coords are available
        if (GeometryTools.has3DCoordinates(m) && use3D) {
            logger.info("Viewing with 3D viewer");

            boolean viewed = false;
            if (useJava3D) {
                logger.debug(".. trying Java3D viewer");
                try {
                    AtomContainer atomContainer = chemModel.getAllInOneContainer();
                    AcceleratedRenderer3D renderer = new AcceleratedRenderer3D(
                        new AcceleratedRenderer3DModel(atomContainer));

                    frame.getContentPane().add(renderer, BorderLayout.CENTER);
                    logger.debug(".. done");

                    viewed = true;
                } catch (Exception e) {
                    logger.error("Viewing did not succeed!");
                    logger.error(e.toString());
                }
            }

            // try to view it without Java3D
            if (!viewed) {
                logger.debug(".. trying non-Java3D viewer");
                MoleculeViewer3D mv = new MoleculeViewer3D(m);
                frame.getContentPane().add(mv, BorderLayout.CENTER);
                logger.debug(".. done");
            }
        } else if (GeometryTools.has2DCoordinates(m)) {
            logger.info("Viewing with 2D viewer");
            MoleculeViewer2D mv = new MoleculeViewer2D(m);
            frame.getContentPane().add(mv, BorderLayout.CENTER);
        } else {
            System.out.println("Molecule has no coordinates.");
            System.exit(1);
        }
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setSize(500,500);
        frame.setVisible(true);
        frame.addWindowListener(new AppCloser());       
    }

    // view a SMILES string
    public Viewer(String SMILES) {
        logger = new org.openscience.cdk.tools.LoggingTool(this.getClass().getName());
        logger.dumpSystemProperties();
        logger.dumpClasspath();
        
        SmilesParser sp = new SmilesParser();
        Molecule mol = null;
        try {
            mol = sp.parseSmiles(SMILES);
        } catch(Exception exc) {
            System.out.println("Problem parsing SMILES: " +  exc.toString());
        }
        if (mol != null) {
            StructureDiagramGenerator sdg = new StructureDiagramGenerator();
            try {
                sdg.setMolecule((Molecule)mol.clone());
                sdg.generateCoordinates(new Vector2d(0,1));
		view(sdg.getMolecule(), false, false);
            } catch(Exception exc) {
                System.out.println("*** Exit due to an unexpected error during coordinate generation ***");
                exc.printStackTrace();
            }
        }
    }

    // view a file
    public Viewer(String inFile, boolean useJava3D, boolean use3D) {
        this.useJava3D = useJava3D;
        this.use3D = use3D;

        logger = new org.openscience.cdk.tools.LoggingTool(this.getClass().getName());
        logger.dumpSystemProperties();
        logger.dumpClasspath();

        ChemFile chemFile = new ChemFile();
        try {
            ChemObjectReader reader;
            logger.info("Loading: " + inFile);
            if (inFile.endsWith(".xyz")) {
               reader = new XYZReader(new FileReader(inFile));
               logger.info("Expecting XYZ format...");
            } else if (inFile.endsWith(".cml")) {
                String url = "file:" + System.getProperty("user.dir") + "/" + inFile;
                reader = new CMLReader(url);
                logger.info("Expecting CML format...");
            } else if (inFile.endsWith(".pdb")) {
                reader = new PDBReader(new FileReader(inFile));
                logger.info("Expecting PDB format...");
            } else {
                reader = new MDLReader(new FileInputStream(inFile));
                logger.info("Expecting MDL MolFile format...");
            }
            chemFile = (ChemFile)reader.read((ChemObject)new ChemFile());
        } catch(Exception exc) {
            logger.error("Error while reading file");
            logger.error(exc.toString());
            exc.printStackTrace();            
            System.exit(1);
        }


        ChemSequence chemSequence;
        ChemModel chemModel;
        SetOfMolecules setOfMolecules;
        logger.info("  number of sequences: " + chemFile.getChemSequenceCount());
    	for (int sequence = 0; sequence < chemFile.getChemSequenceCount(); sequence++) {
          chemSequence = chemFile.getChemSequence(sequence);
          logger.info("  number of models in sequence " + sequence + ": " +
                             chemSequence.getChemModelCount());
          for (int model = 0; model < chemSequence.getChemModelCount(); model++) {
            chemModel = chemSequence.getChemModel(model);
            setOfMolecules = chemModel.getSetOfMolecules();
            logger.info("  number of molecules in model " + model + ": " +
                               setOfMolecules.getMoleculeCount());
            for (int i = 0; i < setOfMolecules.getMoleculeCount(); i++) {
                Molecule m = setOfMolecules.getMolecule(i);
                view(m, useJava3D, use3D);
            }
          }
        }
    }
    
    public static void main(String[] args) {

        boolean useJava3D = true;
        boolean use3D = true;

        boolean showSmiles = false;

        String filename = "";
        if (args.length == 1) {
            filename = args[0];
        } else if (args.length > 1) {
            // parse options
            for (int i=1; i<args.length; i++) {
                String opt = args[i-1];
                if ("--nojava3d".equalsIgnoreCase(opt)) {
                    useJava3D = false;
                } else if ("--no3d".equalsIgnoreCase(opt)) {
                    use3D = false;
                } else if ("--smiles".equalsIgnoreCase(opt)) {
                    // Filename is considered to be a smiles string
                    showSmiles = true;
                } else {
                    System.err.println("Unknown option: " + opt);
                    System.exit(1);
                }
            }

            filename = args[args.length -1];
        } else {
            System.out.println("Syntax : Viewer [options] <inputfile>");
            System.out.println();
            System.out.println("options: --nojava3D    Disable Java3D support");
            System.out.println("options: --no3D        View only 2D info");
            System.out.println();
            System.out.println("      or Viewer --smiles <SMILES>");
            System.exit(0);
        }

        if (showSmiles) {
            // Filename is considered to be a smiles string
            Viewer v = new Viewer(filename);
        } else {
            if (new File(filename).canRead()) {
                new Viewer(filename, useJava3D, use3D);
            } else {
                System.out.println("File " + filename + " does not exist!");
            }
        }
    }

    // Class to close program
    protected static final class AppCloser extends WindowAdapter {

        public void windowClosing(WindowEvent e) {
            System.exit(0);
        }
    }

}

