package uk.ac.lkl.cram.ui;


import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.bind.JAXBException;
import uk.ac.lkl.cram.model.Module;
import uk.ac.lkl.cram.model.io.ModuleMarshaller;
import uk.ac.lkl.cram.model.io.ModuleUnmarshaller;


/**
 * $Date$
 * $Revision$
 * @author bernard
 */
public class CRAMApplication {

    private static final Logger LOGGER = Logger.getLogger(CRAMApplication.class.getName());
    private static int MODULE_COUNT = 1;

    public static void main(String[] args) throws IOException {
        // Get the global logger to configure it
        Logger logger = Logger.getLogger("uk.ac.lkl.cram");
        logger.setLevel(Level.ALL);
        //TODO
//        FileHandler txtHandler = new FileHandler("CRAMLog.txt");
//        txtHandler.setFormatter(new SimpleFormatter());
//        logger.addHandler(txtHandler);
                
        System.setProperty("apple.awt.graphics.EnableQ2DX", "true");
        System.setProperty("apple.laf.useScreenMenuBar", "true");
        System.setProperty("com.apple.mrj.application.apple.menu.about.name", "CRAM");
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
        for (int i = 0; i < args.length; i++) {
            LOGGER.log(Level.INFO, "arg[{0}]: {1}", new Object[]{i, args[i]});
        }
        CRAMApplication application = new CRAMApplication();
        if (System.getProperty("os.name").contains("Mac")) {
            LOGGER.info("mac");

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

        application.startUp();
    }

    
    private Point frameOffset = new Point();
    private StartupDialog startupDialog;
    private Set<JFrame> windows;

    CRAMApplication() {
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
        startupDialog.getNewModuleButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                if (createNewModule()) {
                    startupDialog.setVisible(false);
                }
            }
        });
        startupDialog.getOpenModuleButtion().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                if (openModule()) {
                    startupDialog.setVisible(false);
                }
            }
        });
        startupDialog.getQuitButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                if (quitApplication(startupDialog)) {
                    System.exit(0);
                }
            }
        });
        startupDialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (quitApplication(startupDialog)) {
                    System.exit(0);
                } 
            }
        });

    }

    private boolean addModule(Module m, String title) {
        final ModuleFrame moduleFrame = new ModuleFrame(m);
        moduleFrame.setTitle(title);
        if (windows.contains(moduleFrame)) {
            moduleFrame.toFront();
            return false;
        }
        windows.add(moduleFrame);
        moduleFrame.getWindowMenu().addMenuListener(new WindowMenuListener());
        moduleFrame.addWindowListener(new WindowAdapter() {
            
            @Override
            public void windowClosing(WindowEvent e) {
                //TODO check if need to save
                if (windows.size() == 1) {
                    //This is the last window
                    if (quitApplication(moduleFrame)) {
                        System.exit(0);
                    } 
                } else {
                moduleFrame.setVisible(false);
                windows.remove(moduleFrame);
            }
            }
            
        });
        moduleFrame.getNewMenuItem().addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                createNewModule();
            }
        });
        moduleFrame.getOpenMenuItem().addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                openModule();
            }
        });
        moduleFrame.getQuitMenuItem().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                if (quitApplication(moduleFrame)) {
                        System.exit(0);
                }
            }
        });
        moduleFrame.getSaveAsMenuItem().addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                saveModule(moduleFrame);
            }
        });
    
        moduleFrame.setLocation(frameOffset);
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
        moduleFrame.setVisible(true);
        return true;
    }

    private boolean openModule() {
        JFileChooser jfc = new JFileChooser();
        jfc.setAcceptAllFileFilterUsed(false);
        jfc.setDialogTitle("Open CRAM Module");
        FileFilter filter = new FileNameExtensionFilter("CRAM Module File", "mamx");
        jfc.addChoosableFileFilter(filter);
        int returnVal = jfc.showOpenDialog(new JFrame());
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = jfc.getSelectedFile();
            try {
                ModuleUnmarshaller unmarshaller = new ModuleUnmarshaller(file);
                Module  importedModule = unmarshaller.unmarshallModule();
                return addModule(importedModule, file.getName());
            } catch (    IOException | JAXBException i) {
                LOGGER.log(Level.SEVERE, "Failed to open file", i);
                return false;
            }
            
        } else {
            return false;
        }
    }
    
    private void saveModule(ModuleFrame moduleFrame) {
        Module module = moduleFrame.getModule();
        JFileChooser jfc = new JFileChooser();
        jfc.setAcceptAllFileFilterUsed(false);
        jfc.setDialogTitle("Save CRAM Module");
        FileFilter filter = new FileNameExtensionFilter("CRAM Module File", "mamx");
        jfc.setFileFilter(filter);
        jfc.setSelectedFile(new File(module.getModuleName() + ".mamx"));
        int returnVal = jfc.showSaveDialog(moduleFrame);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = jfc.getSelectedFile();
            if (!jfc.getSelectedFile().getAbsolutePath().endsWith(".mamx")) {
                file = new File(jfc.getSelectedFile() + ".mamx");
            }
            try {
                ModuleMarshaller marshaller = new ModuleMarshaller(file);
                marshaller.marshallModule(module);
                windows.remove(moduleFrame);
                moduleFrame.setTitle(file.getName());
                windows.add(moduleFrame);
            } catch (JAXBException ioe) {
                LOGGER.log(Level.SEVERE, "Failed to save file", ioe);
            }
        }
    }
    
    private boolean createNewModule() {
        Module module = new Module();
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

    private void startUp() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dialogSize = startupDialog.getSize();
        int x = (screenSize.width - dialogSize.width) /2;
        int y = (screenSize.height - dialogSize.height) / 2;
        startupDialog.setLocation(x, y);
        startupDialog.setVisible(true);
    }

    void populateWindowMenu(JMenu menu) {
        menu.removeAll();
        for (final JFrame jFrame : windows) {
            JMenuItem menuItem = new JMenuItem(jFrame.getTitle());
            menuItem.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent ae) {
                    jFrame.toFront();
                }
            });
            menu.add(menuItem);
        }
    }
    
    private static class QuitListener implements InvocationHandler {
	private final CRAMApplication application;

	private QuitListener(CRAMApplication application) {
	    this.application = application;
	}

        @Override
        public Object invoke(Object o, Method method, Object[] os) throws Throwable {
	    Object quitResponse = os[1];
	    Class qrClass = Class.forName("com.apple.eawt.QuitResponse");
	    Method cancelQuitMethod = qrClass.getDeclaredMethod("cancelQuit", (Class[]) null);
	    Method performQuitMethod = qrClass.getDeclaredMethod("performQuit", (Class[]) null);
	    if (application.quitApplication(application.startupDialog)) {
		performQuitMethod.invoke(quitResponse, (Object[]) null);
	    } else {
		cancelQuitMethod.invoke(quitResponse, (Object[])null);
	    }
            return null;
        }
    
    }
    
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
