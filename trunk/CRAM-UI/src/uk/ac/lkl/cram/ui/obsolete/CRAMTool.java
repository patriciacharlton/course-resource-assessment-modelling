package uk.ac.lkl.cram.ui.obsolete;

import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JFileChooser;
import javax.swing.KeyStroke;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import uk.ac.lkl.cram.model.Module;
import uk.ac.lkl.cram.ui.ModuleFrame;
import uk.ac.lkl.cram.ui.ModuleOkCancelDialog;
import static uk.ac.lkl.cram.ui.ModuleOkCancelDialog.RET_OK;

/**
 * $Date$
 * @author bernard
 */
public class CRAMTool extends javax.swing.JFrame {
    private Map<String, ModuleFrame> moduleMap = new HashMap<String, ModuleFrame>();

    /**
     * Creates new form CRAMTool
     */
    public CRAMTool() {
        initComponents();
	newMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
	openMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
	saveMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        desktop = new javax.swing.JDesktopPane();
        windowMenuBar = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        newMenuItem = new javax.swing.JMenuItem();
        openMenuItem = new javax.swing.JMenuItem();
        saveMenuItem = new javax.swing.JMenuItem();
        editMenu = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("CRAM Tool");
        setSize(new java.awt.Dimension(582, 456));

        fileMenu.setText("File");

        newMenuItem.setText("New Module...");
        newMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(newMenuItem);

        openMenuItem.setText("Open Module...");
        openMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(openMenuItem);

        saveMenuItem.setText("Save Module...");
        fileMenu.add(saveMenuItem);

        windowMenuBar.add(fileMenu);

        editMenu.setText("Edit");
        windowMenuBar.add(editMenu);

        setJMenuBar(windowMenuBar);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(desktop)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(desktop)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void newMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newMenuItemActionPerformed
	Module m = new Module();
	ModuleOkCancelDialog dialog = new ModuleOkCancelDialog(new javax.swing.JFrame(), true, m);
	dialog.setVisible(true);
	System.out.println(dialog.getReturnStatus());
	if (dialog.getReturnStatus() == RET_OK) {
	    addModule(m);
	}

    }//GEN-LAST:event_newMenuItemActionPerformed

    private void openMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openMenuItemActionPerformed
	JFileChooser jfc = new JFileChooser();
	jfc.setDialogTitle("Open CRAM Module");
	FileFilter filter = new FileNameExtensionFilter("CRAM Module File", "mam", "MAM");
	jfc.addChoosableFileFilter(filter);
	int returnVal = jfc.showOpenDialog(this);
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
		i.printStackTrace();
		return;
	    } catch (ClassNotFoundException c) {
		System.out.println("Template class not found");
		c.printStackTrace();
		return;
	    }
	    addModule(importedModule);

	}
    }//GEN-LAST:event_openMenuItemActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        final CRAMTool cramTool = new CRAMTool();

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                cramTool.setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JDesktopPane desktop;
    private javax.swing.JMenu editMenu;
    private javax.swing.JMenu fileMenu;
    private javax.swing.JMenuItem newMenuItem;
    private javax.swing.JMenuItem openMenuItem;
    private javax.swing.JMenuItem saveMenuItem;
    private javax.swing.JMenuBar windowMenuBar;
    // End of variables declaration//GEN-END:variables

    private void addModule(Module module) {
        ModuleFrame mif = moduleMap.get(module.getModuleName());
        if (mif == null) {
            mif = new ModuleFrame(module);
            moduleMap.put(module.getModuleName(), mif);
	    mif.setVisible(true);
	    desktop.add(mif);
        } 
	mif.toFront();
	
        
    }
}
