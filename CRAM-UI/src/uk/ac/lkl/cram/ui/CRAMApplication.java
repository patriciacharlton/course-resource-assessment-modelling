/*
 * Copyright 2014 London Knowledge Lab, Institute of Education.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package uk.ac.lkl.cram.ui;


import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.ToolTipManager;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.bind.JAXBException;
import org.openide.util.Exceptions;
import uk.ac.lkl.cram.model.Module;
import uk.ac.lkl.cram.model.io.ModuleMarshaller;
import uk.ac.lkl.cram.model.io.ModuleUnmarshaller;


/**
 * This class represents the CRAM application and is responsible for managing
 * the windows (instances of ModuleFrame) and the menu items on those instances.
 * This class is the 'main' class in that the main() method in this class is the
 * one that starts the CRAM tool application.
 * @see ModuleFrame
 * @version $Revision$
 * @author Bernard Horan
 */
//$Date$
public class CRAMApplication {

    private static final Logger LOGGER = Logger.getLogger(CRAMApplication.class.getName());
    //Used to name the untitled new modules
    private static int MODULE_COUNT = 1;
    //Filename for the user guide, expected to be found in the resource package
    private static final String USER_GUIDE_FILENAME = "CRAMUserGuide";

    /**
     * Start the CRAM Tool
     * @param args ignored
     * @throws IOException ignored
     */
    public static void main(String[] args) throws IOException {
        // Get the global logger to configure it
        Logger logger = Logger.getLogger("uk.ac.lkl.cram");
        logger.setLevel(Level.ALL);
        //TODO
//        FileHandler txtHandler = new FileHandler("CRAMLog.txt");
//        txtHandler.setFormatter(new SimpleFormatter());
//        logger.addHandler(txtHandler);
                
        //Set the mac GUI options
	System.setProperty("apple.awt.graphics.EnableQ2DX", "true");
        System.setProperty("apple.laf.useScreenMenuBar", "true");
        System.setProperty("com.apple.mrj.application.apple.menu.about.name", "CRAM");
	//Tooltips
	ToolTipManager.sharedInstance().setInitialDelay(10);
	ToolTipManager.sharedInstance().setDismissDelay(3000);
	ToolTipManager.sharedInstance().setReshowDelay(1000);
	try {
            //Set System LAF
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (UnsupportedLookAndFeelException e) {
            LOGGER.log(Level.WARNING, "Could not set systemlook and feel", e);
        } catch (ClassNotFoundException e) {
            LOGGER.log(Level.SEVERE, "Failed to find class", e);
        } catch (InstantiationException ex) {
            LOGGER.log(Level.SEVERE, "Failed to instatiate", ex);
        } catch (IllegalAccessException ex) {
            LOGGER.log(Level.SEVERE, "Unknown exception", ex);
        }
	//Create a new instance of the application
	CRAMApplication application = new CRAMApplication();
        if (OSUtil.isMac()) {
            //We're on the Apple Mac platform, so set up some handlers

            try {
                Object app = Class.forName("com.apple.eawt.Application").getMethod("getApplication",
                        (Class[]) null).invoke(null, (Object[]) null);

                Object ql = Proxy.newProxyInstance(Class.forName("com.apple.eawt.QuitHandler")
                        .getClassLoader(), new Class[]{Class.forName("com.apple.eawt.QuitHandler")},
                        new QuitListener(application));
                app.getClass().getMethod("setQuitHandler", new Class[]{
                    Class.forName("com.apple.eawt.QuitHandler")}).invoke(app, new Object[]{ql});
            } catch (ClassNotFoundException | NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                LOGGER.log(Level.WARNING, null, e);
            }
        }
	//Start the application
        application.startUp();
    }

    
    //The offset for displaying subseqent instances of ModuleFrame
    private Point frameOffset = new Point();
    //The startup dialogue for the application
    private StartupDialog startupDialog;
    //The set of windows managed by the application
    private Set<JFrame> windows;
    //The user guide (a PDF)
    private File userGuideFile = null;

    CRAMApplication() {
        //Keep the set of windows sorted by the title of the frame
	windows = new TreeSet<>(new Comparator<JFrame>() {
            @Override
            public int compare(JFrame a, JFrame b) {
                return a.getTitle().compareTo(b.getTitle());
            }
        });

        initStartupDialog();
    }

    private void initStartupDialog() {
        startupDialog = new StartupDialog(new JFrame(), true);
	//Listen for clicks on new module button
        startupDialog.getNewModuleButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
		//Try to create a new module
                if (createNewModule()) {
		    //If a new module has been created, close the startup dialog
                    startupDialog.setVisible(false);
                }
            }
        });
	//Listen for clicks on the open module button
        startupDialog.getOpenModuleButtion().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
		//Try to open a module
                if (openModule()) {
		    //If a module is successfully opened, close the startup dialog
                    startupDialog.setVisible(false);
                }
            }
        });
	//Listen for clicks on the quit button
        startupDialog.getQuitButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
		//Confirm that the user wants to quit
                if (quitApplication(startupDialog)) {
		    //If the user wants to quit, exit Java
                    System.exit(0);
                }
            }
        });
	//Listen for the user closing the startup dialog
        startupDialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
		//Confirm that the user wants to quit
                if (quitApplication(startupDialog)) {
		    //If the user wants to quit, exit Java
                    System.exit(0);
                } 
            }
        });

    }

    /**
     * Add a module to the application
     * @param m the module to be added
     * @param title the title of the window to display the module
     * @return true if the module is added
     */
    private boolean addModule(Module m, String title) {
	//Create a new frame for the module
        final ModuleFrame moduleFrame = new ModuleFrame(m);
        moduleFrame.setTitle(title);
	//See if the set of windows already contains the module
        if (windows.contains(moduleFrame)) {
            moduleFrame.toFront();
            return false;
        }
        windows.add(moduleFrame);
        //Set up the window menu
	moduleFrame.getWindowMenu().addMenuListener(new WindowMenuListener());
        //Listen for the user closing the window
	moduleFrame.addWindowListener(new WindowAdapter() {
            
            @Override
            public void windowClosing(WindowEvent e) {
		//TODO check if need to save
		if (windows.size() == 1) {
		    //This is the last window
		    //Confirm that user wants to quit
		    if (quitApplication(moduleFrame)) {
			//If user wants to quit, exit Java
			System.exit(0);
		    }
		} else {
		    moduleFrame.setVisible(false);
		    windows.remove(moduleFrame);
		}
	    }
            
        });
	//Listen for the user selecting new module
        moduleFrame.getNewMenuItem().addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                createNewModule();
            }
        });
	//Listen for the user selecting open module
        moduleFrame.getOpenMenuItem().addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                openModule();
            }
        });
	//Listten for the user selecting quit
        moduleFrame.getQuitMenuItem().addActionListener(new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent ae) {
		//Confirm that the user wants to quit
		if (quitApplication(moduleFrame)) {
		    //If the user wants to quit, exit Java
		    System.exit(0);
		}
	    }
	});
	//Listen for the user selecting Save_as
        moduleFrame.getSaveAsMenuItem().addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                saveModule(moduleFrame);
            }
        });
	//Listen for the user selecting duplicate
        moduleFrame.getDuplicateMenuItem().addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                duplicateModule(moduleFrame);
            }
        });
	//Listen for the user opening the user guide
	moduleFrame.getOpenHelpMenuItem().addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent e) {
		openUserGuide();
	    }
	});
	//Set the location of the window
        moduleFrame.setLocation(frameOffset);
	
	//Update the window offset
        int offset = moduleFrame.getInsets().top;
        frameOffset.x += offset;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        if (frameOffset.x >= screenSize.width) {
            frameOffset.x = 0;
        }
        if (frameOffset.y >= screenSize.height) {
            frameOffset.y = 0;
        }
        frameOffset.y += offset;
	
	//Show the window
        moduleFrame.setVisible(true);
        return true;
    }

    /**
     * Open a module from a file
     * @return true if the module is successfully opened
     */
    private boolean openModule() {
	//Set up a file chooser
	JFileChooser jfc = new JFileChooser();
	jfc.setAcceptAllFileFilterUsed(false);
	jfc.setDialogTitle("Open CRAM Module");
	FileFilter filter = new FileNameExtensionFilter("CRAM Module File", "mamx");
	jfc.addChoosableFileFilter(filter);
	//Open the file chooser and get the return value
	int returnVal = jfc.showOpenDialog(new JFrame());
	if (returnVal == JFileChooser.APPROVE_OPTION) {
	    //Get the selected file to open
	    File file = jfc.getSelectedFile();
	    try {
		//Create a new unmarshaller on the file
		ModuleUnmarshaller unmarshaller = new ModuleUnmarshaller(file);
		//Unmarshall the module from the file
		Module importedModule = unmarshaller.unmarshallModule();
		//add the module and return the value if it's successful
		return addModule(importedModule, file.getName());
	    } catch (IOException | JAXBException i) {
		LOGGER.log(Level.SEVERE, "Failed to open file", i);
		return false;
	    }

	} else {
	    return false;
	}
    }
    
    /**
     * Save the contents of a module
     * @param moduleFrame the moduleFrame containing the module
     */
    private void saveModule(ModuleFrame moduleFrame) {
        Module module = moduleFrame.getModule();
	//Set up a file chooser
        JFileChooser jfc = new JFileChooser();
        jfc.setAcceptAllFileFilterUsed(false);
        jfc.setDialogTitle("Save CRAM Module");
        FileFilter filter = new FileNameExtensionFilter("CRAM Module File", "mamx");
        jfc.setFileFilter(filter);
        jfc.setSelectedFile(new File(module.getModuleName() + ".mamx"));
	//Open the dialog and wait for the user to provide a name for the file
        int returnVal = jfc.showSaveDialog(moduleFrame);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = jfc.getSelectedFile();
	    //Add the file extension
            if (!jfc.getSelectedFile().getAbsolutePath().endsWith(".mamx")) {
                file = new File(jfc.getSelectedFile() + ".mamx");
            }
            try {
		//Create a marshaller to marshall the module
                ModuleMarshaller marshaller = new ModuleMarshaller(file);
		//Marshall the module
                marshaller.marshallModule(module);
		//The name of the module may have changed, so update the list of windows
                windows.remove(moduleFrame);
                moduleFrame.setTitle(file.getName());
                windows.add(moduleFrame);
            } catch (JAXBException ioe) {
                LOGGER.log(Level.SEVERE, "Failed to save file", ioe);
            }
        }
    }
    
    /**
     * Duplicate the module of the frame
     * @param moduleFrame the frame whose module is to be duplicated
     */
    private void duplicateModule(ModuleFrame moduleFrame) {
        try {
            //Marshall the existing module to a file then read it in again
            //This avoids having to deal with cloning objects
            Module module = moduleFrame.getModule();
            String moduleName = module.getModuleName();
            File tempFile = File.createTempFile("CRAM", "mamx");
            tempFile.deleteOnExit();
            ModuleMarshaller marshaller = new ModuleMarshaller(tempFile);
            marshaller.marshallModule(module);
            ModuleUnmarshaller unmarshaller = new ModuleUnmarshaller(tempFile);
            Module duplicateModule = unmarshaller.unmarshallModule();
            String duplicateModuleName = moduleName + " (Copy)";
            duplicateModule.setModuleName(duplicateModuleName);
            addModule(duplicateModule, duplicateModuleName);
        } catch (IOException | JAXBException ex) {
            LOGGER.log(Level.SEVERE, "Unable to duplicate module", ex);
            JOptionPane.showMessageDialog(moduleFrame, "See log for details", "Unable to duplicate module", JOptionPane.ERROR_MESSAGE);
        }
        
    }
    
    /**
     * Create a new module
     * @return true if the frame containing the new module is added to the UI
     */
    @SuppressWarnings("ValueOfIncrementOrDecrementUsed")
    private boolean createNewModule() {
        Module module = new Module();
	//Create a dialog for the user to enter the details of the module
        ModuleOkCancelDialog dialog = new ModuleOkCancelDialog(new javax.swing.JFrame(), true, module);
        dialog.setSize(dialog.getPreferredSize());
        dialog.setVisible(true);
        if (dialog.getReturnStatus() == ModuleOkCancelDialog.RET_OK) {
            return addModule(module, "Untitled " + MODULE_COUNT++);
        } else {
            return false;
        }
    }

    private boolean quitApplication(Window aWindow) {
        int confirm = JOptionPane.showConfirmDialog(aWindow,
                "Are you sure you want to quit?",
                "Quit Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null);
        return (confirm == JOptionPane.YES_OPTION);
    }
    
    private void openUserGuide() {
        if (!Desktop.isDesktopSupported()) {
            LOGGER.info("Desktop not supported");
	    JOptionPane.showMessageDialog(null, "Cannot open user guide on this platform", "Platform not supported", JOptionPane.ERROR_MESSAGE);
            return;
        }
        File pdfFile;
        try {
            pdfFile = getUserGuideFile();
         } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, "Failed to create user guide file", ex);
            JOptionPane.showMessageDialog(null, ex.getLocalizedMessage(), "Cannot open user guide", JOptionPane.ERROR_MESSAGE);
            return;
        }   
        try {
            Desktop.getDesktop().open(pdfFile); 
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, "Failed to open user guide", ex);
            String message = ex.getLocalizedMessage();
            if (OSUtil.isWindows()) {
                message = "Ensure Adobe Reader is installed";
            }
            JOptionPane.showMessageDialog(null, message, "Cannot open user guide", JOptionPane.ERROR_MESSAGE);
        }
        
    }
    
    @SuppressWarnings({"ConvertToTryWithResources", "NestedAssignment"})
    private File getUserGuideFile() throws IOException {
	if (userGuideFile == null) {
	    InputStream is = getClass().getResourceAsStream("resource/" + USER_GUIDE_FILENAME + ".pdf");
	    userGuideFile = File.createTempFile(USER_GUIDE_FILENAME, ".pdf");
	    userGuideFile.deleteOnExit();
	    FileOutputStream fos = new FileOutputStream(userGuideFile);
	    byte[] buf = new byte[1024];
	    int len;
	    while ((len = is.read(buf)) > 0) {
		fos.write(buf, 0, len);
	    }
	    fos.flush();
	    fos.close();
	    is.close();
	}
	return userGuideFile;
    }

    /**
     * Put the dialog box in the centre of the screen
     */
    private void startUp() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dialogSize = startupDialog.getSize();
        int x = (screenSize.width - dialogSize.width) /2;
        int y = (screenSize.height - dialogSize.height) / 2;
        startupDialog.setLocation(x, y);
        startupDialog.setVisible(true);
    }

    /**
     * Populate the window menu with the ordered list of window titles
     * @param menu the menu to be populated
     */
    private void populateWindowMenu(JMenu menu) {
	//Clear out the menu
        menu.removeAll();
        for (final JFrame jFrame : windows) {
	    //Create a menu item for each window
            JMenuItem menuItem = new JMenuItem(jFrame.getTitle());
            menuItem.addActionListener(new ActionListener() {
		//When the user clicks on the menu item, bring the corresponding window to the front
                @Override
                public void actionPerformed(ActionEvent ae) {
                    jFrame.toFront();
                }
            });
            menu.add(menuItem);
        }
    }
    
    /**
     * Mac specific
     */
    private static class QuitListener implements InvocationHandler {
	private final CRAMApplication application;

	private QuitListener(CRAMApplication application) {
	    this.application = application;
	}

        @Override
	@SuppressWarnings("AccessingNonPublicFieldOfAnotherObject")
        public Object invoke(Object o, Method method, Object[] os) throws Throwable {
	    Object quitResponse = os[1];
	    Class qrClass = Class.forName("com.apple.eawt.QuitResponse");
	    @SuppressWarnings("unchecked")
	    Method cancelQuitMethod = qrClass.getDeclaredMethod("cancelQuit", (Class[]) null);
	    @SuppressWarnings("unchecked")
	    Method performQuitMethod = qrClass.getDeclaredMethod("performQuit", (Class[]) null);
	    if (application.quitApplication(application.startupDialog)) {
		performQuitMethod.invoke(quitResponse, (Object[]) null);
	    } else {
		cancelQuitMethod.invoke(quitResponse, (Object[])null);
	    }
            return null;
        }
    
    }
    
    /**
     * Class that keeps the window menu up to date
     */
    private class WindowMenuListener implements MenuListener {

        @Override
        public void menuCanceled(MenuEvent e) {
        }

        @Override
        public void menuDeselected(MenuEvent e) {
        }

        @Override
        public void menuSelected(MenuEvent e) {
            JMenu menu = (JMenu) e.getSource();
            populateWindowMenu(menu);
        }
    }
}
