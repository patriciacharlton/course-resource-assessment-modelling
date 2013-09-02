package uk.ac.lkl.cram.ui;


import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
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
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import uk.ac.lkl.cram.model.Module;
import static uk.ac.lkl.cram.ui.ModuleOkCancelDialog.RET_OK;


/**
 * $Date$
 * @author bernard
 */
public class CRAMApplication {

    private static final Logger LOGGER = Logger.getLogger(CRAMApplication.class.getName());
    private StartupDialog startupDialog;
    private Set<JFrame> windows;
    int frameCount = 0;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        for (int i = 0; i < args.length; i++) {
            LOGGER.log(Level.INFO, "arg[{0}]: {1}", new Object[]{i, args[i]});
        }
        CRAMApplication application = new CRAMApplication();
        application.startUp();
    }

    CRAMApplication() {
        windows = new TreeSet<JFrame>(new Comparator<JFrame>() {
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
                    System.exit(1);
                }
            }
        });
        startupDialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (quitApplication(startupDialog)) {
                    System.exit(1);
                } 
            }
        });

    }

    private boolean addModule(Module m) {
        final ModuleFrame moduleFrame = new ModuleFrame(m);
        if (windows.contains(moduleFrame)) {
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
			System.exit(1);
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
        
    
        moduleFrame.setVisible(true);
        return true;
    }

    private boolean openModule() {
        JFileChooser jfc = new JFileChooser();
	jfc.setDialogTitle("Open CRAM Module");
	FileFilter filter = new FileNameExtensionFilter("CRAM Module File", "mam", "MAM");
	jfc.addChoosableFileFilter(filter);
	int returnVal = jfc.showOpenDialog(new JFrame());
	if (returnVal == JFileChooser.APPROVE_OPTION) {
	    File file = jfc.getSelectedFile();
	    Module importedModule = null;
	    try {
		FileInputStream inStream = new FileInputStream(file);
		ObjectInputStream inObject = new ObjectInputStream(inStream);
		importedModule = (Module) inObject.readObject();
		inObject.close();
		inStream.close();
	    } catch (IOException i) {
                LOGGER.log(Level.SEVERE, "Failed to open File", i);
                return false;
	    } catch (ClassNotFoundException c) {
		LOGGER.log(Level.SEVERE, "Class not found", c);
		return false;
	    }
	    return addModule(importedModule);
	} else {
            return false;
        }
    }
    
    private boolean createNewModule() {
	Module module = new Module();
        ModuleOkCancelDialog dialog = new ModuleOkCancelDialog(new javax.swing.JFrame(), true, module);
	dialog.setVisible(true);
	System.out.println(dialog.getReturnStatus());
	if (dialog.getReturnStatus() == RET_OK) {
	    return addModule(module);
	} else {
            return false;
        }
    }

    private boolean quitApplication(Window aWindow) {
        int confirm = JOptionPane.showConfirmDialog(aWindow,
                "Are you sure you want to quit?",
                "Quit Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        return (confirm == JOptionPane.YES_OPTION);
    }

    private void startUp() {
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
