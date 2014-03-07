package uk.ac.lkl.cram.ui.wizard;

import java.awt.Dimension;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.WeakHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import uk.ac.lkl.cram.model.AbstractModuleTime;
import uk.ac.lkl.cram.model.Module;
import uk.ac.lkl.cram.model.ModulePresentation;
import uk.ac.lkl.cram.model.PreparationTime;
import uk.ac.lkl.cram.model.SupportTime;
import uk.ac.lkl.cram.model.TLALineItem;
import uk.ac.lkl.cram.model.TLActivity;
import uk.ac.lkl.cram.ui.FormattedTextFieldAdapter;
import uk.ac.lkl.cram.ui.SelectAllAdapter;

/**
 * $Date$
 * $Revision$
 * @author Bernard Horan
 */
@SuppressWarnings("serial")
public class LineItemsDetailVisualPanel extends javax.swing.JPanel {
    private static final Logger LOGGER = Logger.getLogger(LineItemsDetailVisualPanel.class.getName());
    public final static String PROP_VALID = "valid";
    private WeakHashMap<JFormattedTextField, Boolean> dirtyMap = new WeakHashMap<>();
    
    private final TLALineItem lineItem;
    private final Module module;

    /**
     * Creates new form LineItemsDetailVisualPanel
     * @param module
     * @param li  
     */
    public LineItemsDetailVisualPanel(Module module, TLALineItem li) {
        this.lineItem = li;
	this.module = module;
        initComponents();
        final PropertyChangeListener nameListener = new PropertyChangeListener() {

	    @Override
	    public void propertyChange(PropertyChangeEvent pce) {
		tlActivityNameChanged();
	    }
	};
	lineItem.getActivity().addPropertyChangeListener(TLActivity.PROP_NAME, nameListener);
        lineItem.addPropertyChangeListener(TLALineItem.PROP_ACTIVITY, new PropertyChangeListener() {
	    @Override
	    public void propertyChange(PropertyChangeEvent pce) {
		TLActivity oldActivity = (TLActivity) pce.getOldValue();
		if (oldActivity != null) {
		    oldActivity.removePropertyChangeListener(TLActivity.PROP_NAME, nameListener);
		}
		TLActivity newActivity = (TLActivity) pce.getNewValue();
		if (newActivity != null) {
		    newActivity.addPropertyChangeListener(TLActivity.PROP_NAME, nameListener);
		    tlActivityNameChanged();
		}
	    }
	});
        weeklyHoursField.setValue(lineItem.getWeeklyLearnerHourCount());
        weeklyHoursField.addFocusListener(new SelectAllAdapter());
	new FormattedTextFieldAdapter(weeklyHoursField) {

	    @Override
	    public void updateValue(Object value) {
		lineItem.setWeeklyLearnerHourCount((Float)value);
		checkValidity();
	    }
	};
	weekCountField.setValue(lineItem.getWeekCount(module));
	//TODO Focus listener to check for invalid data?
	new FormattedTextFieldAdapter(weekCountField) {
	    @Override
	    public void updateValue(Object value) {
		lineItem.setWeekCount((Integer) value);
		checkValidity();
	    }
	};
        nonWeeklyHoursField.setValue(lineItem.getNonWeeklyLearnerHourCount());
        nonWeeklyHoursField.addFocusListener(new SelectAllAdapter());
	new FormattedTextFieldAdapter(nonWeeklyHoursField) {
	    @Override
	    public void updateValue(Object value) {
		lineItem.setNonWeeklyLearnerHourCount((Float) value);
		checkValidity();
	    }
	};
		
        JFormattedTextField.AbstractFormatterFactory aff = new JFormattedTextField.AbstractFormatterFactory() {

            @Override
            public JFormattedTextField.AbstractFormatter getFormatter(JFormattedTextField jftf) {
                return new PercentFormatter();
            }
        };
        
        InputVerifier verifier = new PercentVerifier();	
	
	//Preparation Fields
	final JFormattedTextField[] weeklyPreparationFields = new JFormattedTextField[3];
	weeklyPreparationFields[0] = presentation1WeeklyPreparation;
	weeklyPreparationFields[1] = presentation2WeeklyPreparation;
	weeklyPreparationFields[2] = presentation3WeeklyPreparation;
	final JFormattedTextField[] nonWeeklyPreparationFields = new JFormattedTextField[3];
	nonWeeklyPreparationFields[0] = presentation1NonWeeklyPreparation;
	nonWeeklyPreparationFields[1] = presentation2NonWeeklyPreparation;
	nonWeeklyPreparationFields[2] = presentation3NonWeeklyPreparation;
	final JFormattedTextField[] higherCostPreparationFields = new JFormattedTextField[3];
	higherCostPreparationFields[0] = presentation1HigherCostPreparation;
	higherCostPreparationFields[1] = presentation2HigherCostPreparation;
	higherCostPreparationFields[2] = presentation3HigherCostPreparation;
	JFormattedTextField[] lowerCostPreparationFields = new JFormattedTextField[3];
	lowerCostPreparationFields[0] = presentation1LowerCostPreparation;
	lowerCostPreparationFields[1] = presentation2LowerCostPreparation;
	lowerCostPreparationFields[2] = presentation3LowerCostPreparation;
	int preparationIndex = 0;
	for (final ModulePresentation modulePresentation : module.getModulePresentations()) {
	    weeklyPreparationFields[preparationIndex].setValue(lineItem.getPreparationTime(modulePresentation).getWeekly());
	    weeklyPreparationFields[preparationIndex].addFocusListener(new SelectAllAdapter());
	    new FormattedTextFieldAdapter(weeklyPreparationFields[preparationIndex]) {
		@Override
		public void updateValue(Object value) {
		    lineItem.getPreparationTime(modulePresentation).setWeekly((Float)value);
		    makeFieldDirty(textField);
		}
	    };
	    
	    nonWeeklyPreparationFields[preparationIndex].setValue(lineItem.getPreparationTime(modulePresentation).getNonWeekly());
	    nonWeeklyPreparationFields[preparationIndex].addFocusListener(new SelectAllAdapter());
	    new FormattedTextFieldAdapter(nonWeeklyPreparationFields[preparationIndex]) {
		@Override
		public void updateValue(Object value) {
		    lineItem.getPreparationTime(modulePresentation).setNonWeekly((Float) value);
		    makeFieldDirty(textField);
		}
	    };
	    
	    higherCostPreparationFields[preparationIndex].setValue(lineItem.getPreparationTime(modulePresentation).getSeniorRate());
	    higherCostPreparationFields[preparationIndex].addFocusListener(new SelectAllAdapter());
	    new FormattedTextFieldAdapter(higherCostPreparationFields[preparationIndex]) {
		@Override
		public void updateValue(Object value) {
		    lineItem.getPreparationTime(modulePresentation).setSeniorRate((Integer) value);
		    makeFieldDirty(textField);
		}
	    };
	    higherCostPreparationFields[preparationIndex].setFormatterFactory(aff);
            higherCostPreparationFields[preparationIndex].setInputVerifier(verifier);
	    lineItem.getPreparationTime(modulePresentation).addPropertyChangeListener(AbstractModuleTime.PROP_SENIOR_RATE, new HigherCostPropertyListener(higherCostPreparationFields[preparationIndex]));
	    
	    lowerCostPreparationFields[preparationIndex].setValue(lineItem.getPreparationTime(modulePresentation).getJuniorRate());
	    lowerCostPreparationFields[preparationIndex].addFocusListener(new SelectAllAdapter());
	    new FormattedTextFieldAdapter(lowerCostPreparationFields[preparationIndex]) {
		@Override
		public void updateValue(Object value) {
		    lineItem.getPreparationTime(modulePresentation).setJuniorRate((Integer) value);
		    makeFieldDirty(textField);
		}
	    };
	    lowerCostPreparationFields[preparationIndex].setFormatterFactory(aff);
            lowerCostPreparationFields[preparationIndex].setInputVerifier(verifier);
	    lineItem.getPreparationTime(modulePresentation).addPropertyChangeListener(AbstractModuleTime.PROP_SENIOR_RATE, new LowerCostPropertyListener(lowerCostPreparationFields[preparationIndex]));
	    
	    preparationIndex++;
	}
	
	
	//Support Fields
	final JFormattedTextField[] weeklySupportFields = new JFormattedTextField[3];
	weeklySupportFields[0] = presentation1WeeklySupport;
	weeklySupportFields[1] = presentation2WeeklySupport;
	weeklySupportFields[2] = presentation3WeeklySupport;
	final JFormattedTextField[] nonWeeklySupportFields = new JFormattedTextField[3];
	nonWeeklySupportFields[0] = presentation1NonWeeklySupport;
	nonWeeklySupportFields[1] = presentation2NonWeeklySupport;
	nonWeeklySupportFields[2] = presentation3NonWeeklySupport;
	JFormattedTextField[] higherCostSupportFields = new JFormattedTextField[3];
	higherCostSupportFields[0] = presentation1HigherCostSupport;
	higherCostSupportFields[1] = presentation2HigherCostSupport;
	higherCostSupportFields[2] = presentation3HigherCostSupport;
	JFormattedTextField[] lowerCostSupportFields = new JFormattedTextField[3];
	lowerCostSupportFields[0] = presentation1LowerCostSupport;
	lowerCostSupportFields[1] = presentation2LowerCostSupport;
	lowerCostSupportFields[2] = presentation3LowerCostSupport;
	int supportIndex = 0;
	for (final ModulePresentation modulePresentation : module.getModulePresentations()) {
	    weeklySupportFields[supportIndex].setValue(lineItem.getSupportTime(modulePresentation).getWeekly());
	    weeklySupportFields[supportIndex].addFocusListener(new SelectAllAdapter());
	    new FormattedTextFieldAdapter(weeklySupportFields[supportIndex]) {
		@Override
		public void updateValue(Object value) {
		    lineItem.getSupportTime(modulePresentation).setWeekly((Float) value);
		    makeFieldDirty(textField);
		}
	    };
	    
	    nonWeeklySupportFields[supportIndex].setValue(lineItem.getSupportTime(modulePresentation).getNonWeekly());
	    nonWeeklySupportFields[supportIndex].addFocusListener(new SelectAllAdapter());
	    new FormattedTextFieldAdapter(nonWeeklySupportFields[supportIndex]) {
		@Override
		public void updateValue(Object value) {
		    lineItem.getSupportTime(modulePresentation).setNonWeekly((Float) value);
		    makeFieldDirty(textField);
		}
	    };
	    
	    higherCostSupportFields[supportIndex].setValue(lineItem.getSupportTime(modulePresentation).getSeniorRate());
	    higherCostSupportFields[supportIndex].addFocusListener(new SelectAllAdapter());
	    new FormattedTextFieldAdapter(higherCostSupportFields[supportIndex]) {
		@Override
		public void updateValue(Object value) {
		   lineItem.getSupportTime(modulePresentation).setSeniorRate((Integer) value);
		   makeFieldDirty(textField);
		}
	    };
	    higherCostSupportFields[supportIndex].setFormatterFactory(aff);
            higherCostSupportFields[supportIndex].setInputVerifier(verifier);
	    lineItem.getSupportTime(modulePresentation).addPropertyChangeListener(AbstractModuleTime.PROP_SENIOR_RATE, new HigherCostPropertyListener(higherCostSupportFields[supportIndex]));
	    
	    lowerCostSupportFields[supportIndex].setValue(lineItem.getSupportTime(modulePresentation).getJuniorRate());
	    lowerCostSupportFields[supportIndex].addFocusListener(new SelectAllAdapter());
	    new FormattedTextFieldAdapter(lowerCostSupportFields[supportIndex]) {
		@Override
		public void updateValue(Object value) {
		    lineItem.getSupportTime(modulePresentation).setJuniorRate((Integer) value);
		    makeFieldDirty(textField);
		}
	    };
	    lowerCostSupportFields[supportIndex].setFormatterFactory(aff);
	    lowerCostSupportFields[supportIndex].setInputVerifier(verifier);
	    lineItem.getSupportTime(modulePresentation).addPropertyChangeListener(AbstractModuleTime.PROP_SENIOR_RATE, new LowerCostPropertyListener(lowerCostSupportFields[supportIndex]));
            supportIndex++;
	}
	
	ModulePresentation mp1 = module.getModulePresentations().get(0);
	final PreparationTime pt1 = lineItem.getPreparationTime(mp1);
	pt1.addPropertyChangeListener(new PropertyChangeListener() {
	    @Override
	    public void propertyChange(PropertyChangeEvent pce) {
		String property = pce.getPropertyName();
                switch (property) {
                    case AbstractModuleTime.PROP_WEEKLY:
                        if (!isFieldDirty(weeklyPreparationFields[1])) {
                            float run2Weekly = pt1.getWeekly() / 10;
                            run2Weekly = (float) Math.ceil(run2Weekly);
                            weeklyPreparationFields[1].setValue(run2Weekly);
                            float run3Weekly = run2Weekly / 10;
                            run3Weekly = (float) Math.ceil(run3Weekly);
                            weeklyPreparationFields[2].setValue(run3Weekly);
                        }
                        break;
                    case AbstractModuleTime.PROP_NON_WEEKLY:
                        if (!isFieldDirty(nonWeeklyPreparationFields[1])) {
                            float run2NonWeekly = pt1.getNonWeekly() / 10;
                            run2NonWeekly = (float) Math.ceil(run2NonWeekly);
                            nonWeeklyPreparationFields[1].setValue(run2NonWeekly);
                            float run3NonWeekly = run2NonWeekly / 10;
                            run3NonWeekly = (float) Math.ceil(run3NonWeekly);
                            nonWeeklyPreparationFields[2].setValue(run3NonWeekly);
                        }
                        break;
                }
	    }
	});
	
	final SupportTime st1 = lineItem.getSupportTime(mp1);
	st1.addPropertyChangeListener(new PropertyChangeListener() {
	    @Override
	    public void propertyChange(PropertyChangeEvent pce) {
		String property = pce.getPropertyName();
                switch (property) {
                    case AbstractModuleTime.PROP_WEEKLY:
                        if (!isFieldDirty(weeklySupportFields[1])) {
                            weeklySupportFields[1].setValue(st1.getWeekly());
                            weeklySupportFields[2].setValue(st1.getWeekly());
                        }
                        break;
                    case AbstractModuleTime.PROP_NON_WEEKLY:
                        if (!isFieldDirty(nonWeeklySupportFields[1])) {
                            nonWeeklySupportFields[1].setValue(st1.getNonWeekly());
                            nonWeeklySupportFields[2].setValue(st1.getNonWeekly());
                        }
                        break;
                }
	    }
	});
	tlActivityNameChanged();
        //Preferred size seems to be wrong so override
        setPreferredSize(new Dimension(444,424));
    }
    
    @Override
    public String getName() {
	return java.util.ResourceBundle.getBundle("uk/ac/lkl/cram/ui/wizard/Bundle").getString("LINE ITEM DETAILS");
    }
    
    private void checkValidity() {
	boolean weeklyValid =  !(lineItem.getWeeklyLearnerHourCount()<= 0);
	boolean nonWeeklyValid = !(lineItem.getNonWeeklyLearnerHourCount() <= 0);
	//TODO check validity
	//boolean weekCountValid = (lineItem.getWeekCount() <= module.getWeekCount());
	if (nonWeeklyValid || weeklyValid) {
	    firePropertyChange(PROP_VALID, false, true);
	} else {
	    firePropertyChange(PROP_VALID, true, false);
	}
    }
    
    private void tlActivityNameChanged() {
	tlaNameField.setText(lineItem.getActivity().getName());
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        activityPanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        weeklyHoursField = new javax.swing.JFormattedTextField();
        nonWeeklyHoursField = new javax.swing.JFormattedTextField();
        jLabel9 = new javax.swing.JLabel();
        weekCountField = new javax.swing.JFormattedTextField();
        jLabel10 = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        preparationPanel = new javax.swing.JPanel();
        blankPreparationLabel = new javax.swing.JLabel();
        hoursPerWeekPreparationLabel = new org.jdesktop.swingx.JXLabel();
        nonWeeklyHoursPreparationLabel = new org.jdesktop.swingx.JXLabel();
        higherCostPreparationLabel = new org.jdesktop.swingx.JXLabel();
        lowerCostPreparationLabel = new org.jdesktop.swingx.JXLabel();
        presentation1PreparationLabel = new javax.swing.JLabel();
        presentation1WeeklyPreparation = new javax.swing.JFormattedTextField();
        presentation1NonWeeklyPreparation = new javax.swing.JFormattedTextField();
        presentation1HigherCostPreparation = new javax.swing.JFormattedTextField();
        presentation1LowerCostPreparation = new javax.swing.JFormattedTextField();
        presentation2PreparationLabel = new javax.swing.JLabel();
        presentation2WeeklyPreparation = new javax.swing.JFormattedTextField();
        presentation2NonWeeklyPreparation = new javax.swing.JFormattedTextField();
        presentation2HigherCostPreparation = new javax.swing.JFormattedTextField();
        presentation2LowerCostPreparation = new javax.swing.JFormattedTextField();
        presentation3PreparationLabel = new javax.swing.JLabel();
        presentation3WeeklyPreparation = new javax.swing.JFormattedTextField();
        presentation3NonWeeklyPreparation = new javax.swing.JFormattedTextField();
        presentation3HigherCostPreparation = new javax.swing.JFormattedTextField();
        presentation3LowerCostPreparation = new javax.swing.JFormattedTextField();
        supportPanel = new javax.swing.JPanel();
        blankSupportLabel = new javax.swing.JLabel();
        hoursPerWeekSupportLabel = new org.jdesktop.swingx.JXLabel();
        nonWeeklyHoursSupportLabel = new org.jdesktop.swingx.JXLabel();
        jXLabel1 = new org.jdesktop.swingx.JXLabel();
        jXLabel2 = new org.jdesktop.swingx.JXLabel();
        presentation1Label1 = new javax.swing.JLabel();
        presentation1WeeklySupport = new javax.swing.JFormattedTextField();
        presentation1NonWeeklySupport = new javax.swing.JFormattedTextField();
        presentation1HigherCostSupport = new javax.swing.JFormattedTextField();
        presentation1LowerCostSupport = new javax.swing.JFormattedTextField();
        presentation2Label1 = new javax.swing.JLabel();
        presentation2WeeklySupport = new javax.swing.JFormattedTextField();
        presentation2NonWeeklySupport = new javax.swing.JFormattedTextField();
        presentation2HigherCostSupport = new javax.swing.JFormattedTextField();
        presentation2LowerCostSupport = new javax.swing.JFormattedTextField();
        jLabel8 = new javax.swing.JLabel();
        presentation3WeeklySupport = new javax.swing.JFormattedTextField();
        presentation3NonWeeklySupport = new javax.swing.JFormattedTextField();
        presentation3HigherCostSupport = new javax.swing.JFormattedTextField();
        presentation3LowerCostSupport = new javax.swing.JFormattedTextField();
        tlaNamePanel = new javax.swing.JPanel();
        tlaNameField = new javax.swing.JTextField();

        activityPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Learner' Total Hours"));

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("uk/ac/lkl/cram/ui/wizard/Bundle"); // NOI18N
        jLabel1.setText(bundle.getString("WEEKLY:")); // NOI18N

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel2.setText(bundle.getString("NON-WEEKLY:")); // NOI18N
        jLabel2.setToolTipText("Non-regular activities");

        weeklyHoursField.setColumns(2);
        weeklyHoursField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        nonWeeklyHoursField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        jLabel9.setText("of");

        weekCountField.setColumns(3);

        jLabel10.setText("hours");

        jSeparator2.setOrientation(javax.swing.SwingConstants.VERTICAL);

        org.jdesktop.layout.GroupLayout activityPanelLayout = new org.jdesktop.layout.GroupLayout(activityPanel);
        activityPanel.setLayout(activityPanelLayout);
        activityPanelLayout.setHorizontalGroup(
            activityPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(activityPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(jLabel1)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(weekCountField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 50, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jLabel9)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(weeklyHoursField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jLabel10)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(jSeparator2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(jLabel2)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(nonWeeklyHoursField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 55, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        activityPanelLayout.linkSize(new java.awt.Component[] {nonWeeklyHoursField, weekCountField, weeklyHoursField}, org.jdesktop.layout.GroupLayout.HORIZONTAL);

        activityPanelLayout.setVerticalGroup(
            activityPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(activityPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                .add(jLabel2)
                .add(nonWeeklyHoursField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
            .add(activityPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                .add(jLabel1)
                .add(weeklyHoursField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(jLabel9)
                .add(weekCountField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(jLabel10))
            .add(jSeparator2)
        );

        preparationPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Teaching Preparation Hours"));
        preparationPanel.setToolTipText(bundle.getString("PREPARATION_HOURS_PANEL")); // NOI18N
        preparationPanel.setLayout(new java.awt.GridLayout(4, 5));
        preparationPanel.add(blankPreparationLabel);

        hoursPerWeekPreparationLabel.setText(bundle.getString("WEEKLY HOURS")); // NOI18N
        hoursPerWeekPreparationLabel.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        hoursPerWeekPreparationLabel.setLineWrap(true);
        preparationPanel.add(hoursPerWeekPreparationLabel);

        nonWeeklyHoursPreparationLabel.setText(bundle.getString("NON-WEEKLY HOURS")); // NOI18N
        nonWeeklyHoursPreparationLabel.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        nonWeeklyHoursPreparationLabel.setLineWrap(true);
        preparationPanel.add(nonWeeklyHoursPreparationLabel);

        higherCostPreparationLabel.setText(org.openide.util.NbBundle.getMessage(LineItemsDetailVisualPanel.class, "HIGHER_COST")); // NOI18N
        higherCostPreparationLabel.setToolTipText(org.openide.util.NbBundle.getMessage(LineItemsDetailVisualPanel.class, "STAFF_COST_TOOLTIP")); // NOI18N
        higherCostPreparationLabel.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        higherCostPreparationLabel.setLineWrap(true);
        preparationPanel.add(higherCostPreparationLabel);

        lowerCostPreparationLabel.setText(org.openide.util.NbBundle.getMessage(LineItemsDetailVisualPanel.class, "LOWER_COST")); // NOI18N
        lowerCostPreparationLabel.setToolTipText(org.openide.util.NbBundle.getMessage(LineItemsDetailVisualPanel.class, "STAFF_COST_TOOLTIP")); // NOI18N
        lowerCostPreparationLabel.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        lowerCostPreparationLabel.setLineWrap(true);
        preparationPanel.add(lowerCostPreparationLabel);

        presentation1PreparationLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        presentation1PreparationLabel.setText(bundle.getString("PRESENTATION 1:")); // NOI18N
        preparationPanel.add(presentation1PreparationLabel);

        presentation1WeeklyPreparation.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        preparationPanel.add(presentation1WeeklyPreparation);

        presentation1NonWeeklyPreparation.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        preparationPanel.add(presentation1NonWeeklyPreparation);

        presentation1HigherCostPreparation.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(java.text.NumberFormat.getIntegerInstance())));
        presentation1HigherCostPreparation.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        preparationPanel.add(presentation1HigherCostPreparation);

        presentation1LowerCostPreparation.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(java.text.NumberFormat.getIntegerInstance())));
        presentation1LowerCostPreparation.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        preparationPanel.add(presentation1LowerCostPreparation);

        presentation2PreparationLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        presentation2PreparationLabel.setText(bundle.getString("PRESENTATION 2:")); // NOI18N
        preparationPanel.add(presentation2PreparationLabel);

        presentation2WeeklyPreparation.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        preparationPanel.add(presentation2WeeklyPreparation);

        presentation2NonWeeklyPreparation.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        preparationPanel.add(presentation2NonWeeklyPreparation);

        presentation2HigherCostPreparation.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(java.text.NumberFormat.getIntegerInstance())));
        presentation2HigherCostPreparation.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        preparationPanel.add(presentation2HigherCostPreparation);

        presentation2LowerCostPreparation.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(java.text.NumberFormat.getIntegerInstance())));
        presentation2LowerCostPreparation.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        preparationPanel.add(presentation2LowerCostPreparation);

        presentation3PreparationLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        presentation3PreparationLabel.setText(bundle.getString("PRESENTATION 3:")); // NOI18N
        preparationPanel.add(presentation3PreparationLabel);

        presentation3WeeklyPreparation.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        preparationPanel.add(presentation3WeeklyPreparation);

        presentation3NonWeeklyPreparation.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        preparationPanel.add(presentation3NonWeeklyPreparation);

        presentation3HigherCostPreparation.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(java.text.NumberFormat.getIntegerInstance())));
        presentation3HigherCostPreparation.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        preparationPanel.add(presentation3HigherCostPreparation);

        presentation3LowerCostPreparation.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(java.text.NumberFormat.getIntegerInstance())));
        presentation3LowerCostPreparation.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        preparationPanel.add(presentation3LowerCostPreparation);

        supportPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Teaching Support Hours"));
        supportPanel.setToolTipText(bundle.getString("SUPPORT_HOURS_PANEL")); // NOI18N
        supportPanel.setLayout(new java.awt.GridLayout(4, 5));
        supportPanel.add(blankSupportLabel);

        hoursPerWeekSupportLabel.setText(bundle.getString("WEEKLY HOURS")); // NOI18N
        hoursPerWeekSupportLabel.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        hoursPerWeekSupportLabel.setLineWrap(true);
        supportPanel.add(hoursPerWeekSupportLabel);

        nonWeeklyHoursSupportLabel.setText(bundle.getString("NON-WEEKLY HOURS")); // NOI18N
        nonWeeklyHoursSupportLabel.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        nonWeeklyHoursSupportLabel.setLineWrap(true);
        supportPanel.add(nonWeeklyHoursSupportLabel);

        jXLabel1.setText(org.openide.util.NbBundle.getMessage(LineItemsDetailVisualPanel.class, "HIGHER_COST")); // NOI18N
        jXLabel1.setToolTipText(org.openide.util.NbBundle.getMessage(LineItemsDetailVisualPanel.class, "SUPPORT_TOOLTIP")); // NOI18N
        jXLabel1.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jXLabel1.setLineWrap(true);
        supportPanel.add(jXLabel1);

        jXLabel2.setText(org.openide.util.NbBundle.getMessage(LineItemsDetailVisualPanel.class, "LOWER_COST")); // NOI18N
        jXLabel2.setToolTipText(org.openide.util.NbBundle.getMessage(LineItemsDetailVisualPanel.class, "SUPPORT_TOOLTIP")); // NOI18N
        jXLabel2.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jXLabel2.setLineWrap(true);
        supportPanel.add(jXLabel2);

        presentation1Label1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        presentation1Label1.setText(bundle.getString("PRESENTATION 1:")); // NOI18N
        supportPanel.add(presentation1Label1);

        presentation1WeeklySupport.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        supportPanel.add(presentation1WeeklySupport);

        presentation1NonWeeklySupport.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        supportPanel.add(presentation1NonWeeklySupport);

        presentation1HigherCostSupport.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        supportPanel.add(presentation1HigherCostSupport);

        presentation1LowerCostSupport.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        supportPanel.add(presentation1LowerCostSupport);

        presentation2Label1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        presentation2Label1.setText(bundle.getString("PRESENTATION 2:")); // NOI18N
        supportPanel.add(presentation2Label1);

        presentation2WeeklySupport.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        supportPanel.add(presentation2WeeklySupport);

        presentation2NonWeeklySupport.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        supportPanel.add(presentation2NonWeeklySupport);

        presentation2HigherCostSupport.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        supportPanel.add(presentation2HigherCostSupport);

        presentation2LowerCostSupport.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        supportPanel.add(presentation2LowerCostSupport);

        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel8.setText(bundle.getString("PRESENTATION 3:")); // NOI18N
        supportPanel.add(jLabel8);

        presentation3WeeklySupport.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        supportPanel.add(presentation3WeeklySupport);

        presentation3NonWeeklySupport.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        supportPanel.add(presentation3NonWeeklySupport);

        presentation3HigherCostSupport.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        supportPanel.add(presentation3HigherCostSupport);

        presentation3LowerCostSupport.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        supportPanel.add(presentation3LowerCostSupport);

        tlaNamePanel.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(LineItemsDetailVisualPanel.class, "TLAPropertiesVisualPanel.tlaNamePanel.border.title"))); // NOI18N

        tlaNameField.setEnabled(false);

        org.jdesktop.layout.GroupLayout tlaNamePanelLayout = new org.jdesktop.layout.GroupLayout(tlaNamePanel);
        tlaNamePanel.setLayout(tlaNamePanelLayout);
        tlaNamePanelLayout.setHorizontalGroup(
            tlaNamePanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(tlaNameField)
        );
        tlaNamePanelLayout.setVerticalGroup(
            tlaNamePanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(tlaNameField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
        );

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(activityPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .add(preparationPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
            .add(supportPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
            .add(tlaNamePanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .add(0, 0, 0)
                .add(tlaNamePanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(0, 0, 0)
                .add(activityPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(0, 0, 0)
                .add(preparationPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(0, 0, 0)
                .add(supportPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel activityPanel;
    private javax.swing.JLabel blankPreparationLabel;
    private javax.swing.JLabel blankSupportLabel;
    private org.jdesktop.swingx.JXLabel higherCostPreparationLabel;
    private org.jdesktop.swingx.JXLabel hoursPerWeekPreparationLabel;
    private org.jdesktop.swingx.JXLabel hoursPerWeekSupportLabel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JSeparator jSeparator2;
    private org.jdesktop.swingx.JXLabel jXLabel1;
    private org.jdesktop.swingx.JXLabel jXLabel2;
    private org.jdesktop.swingx.JXLabel lowerCostPreparationLabel;
    private javax.swing.JFormattedTextField nonWeeklyHoursField;
    private org.jdesktop.swingx.JXLabel nonWeeklyHoursPreparationLabel;
    private org.jdesktop.swingx.JXLabel nonWeeklyHoursSupportLabel;
    private javax.swing.JPanel preparationPanel;
    private javax.swing.JFormattedTextField presentation1HigherCostPreparation;
    private javax.swing.JFormattedTextField presentation1HigherCostSupport;
    private javax.swing.JLabel presentation1Label1;
    private javax.swing.JFormattedTextField presentation1LowerCostPreparation;
    private javax.swing.JFormattedTextField presentation1LowerCostSupport;
    private javax.swing.JFormattedTextField presentation1NonWeeklyPreparation;
    private javax.swing.JFormattedTextField presentation1NonWeeklySupport;
    private javax.swing.JLabel presentation1PreparationLabel;
    private javax.swing.JFormattedTextField presentation1WeeklyPreparation;
    private javax.swing.JFormattedTextField presentation1WeeklySupport;
    private javax.swing.JFormattedTextField presentation2HigherCostPreparation;
    private javax.swing.JFormattedTextField presentation2HigherCostSupport;
    private javax.swing.JLabel presentation2Label1;
    private javax.swing.JFormattedTextField presentation2LowerCostPreparation;
    private javax.swing.JFormattedTextField presentation2LowerCostSupport;
    private javax.swing.JFormattedTextField presentation2NonWeeklyPreparation;
    private javax.swing.JFormattedTextField presentation2NonWeeklySupport;
    private javax.swing.JLabel presentation2PreparationLabel;
    private javax.swing.JFormattedTextField presentation2WeeklyPreparation;
    private javax.swing.JFormattedTextField presentation2WeeklySupport;
    private javax.swing.JFormattedTextField presentation3HigherCostPreparation;
    private javax.swing.JFormattedTextField presentation3HigherCostSupport;
    private javax.swing.JFormattedTextField presentation3LowerCostPreparation;
    private javax.swing.JFormattedTextField presentation3LowerCostSupport;
    private javax.swing.JFormattedTextField presentation3NonWeeklyPreparation;
    private javax.swing.JFormattedTextField presentation3NonWeeklySupport;
    private javax.swing.JLabel presentation3PreparationLabel;
    private javax.swing.JFormattedTextField presentation3WeeklyPreparation;
    private javax.swing.JFormattedTextField presentation3WeeklySupport;
    private javax.swing.JPanel supportPanel;
    private javax.swing.JTextField tlaNameField;
    private javax.swing.JPanel tlaNamePanel;
    private javax.swing.JFormattedTextField weekCountField;
    private javax.swing.JFormattedTextField weeklyHoursField;
    // End of variables declaration//GEN-END:variables

    public static void main(String args[]) {

        final JFrame frame = new JFrame("Line Item Wizard Panel");
        //Module m = AELMTest.populateModule();
	Module m = new Module();
        //LineItem li = m.getTLALineItems().get(0);
	TLALineItem li = new TLALineItem();
        frame.add(new LineItemsDetailVisualPanel(m, li));

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                frame.setVisible(true);
            }
        });
    }

    private void makeFieldDirty(JFormattedTextField jFormattedTextField) {
	dirtyMap.put(jFormattedTextField, Boolean.TRUE);
    }
    
    private boolean isFieldDirty(JFormattedTextField jFormattedTextField) {
	Boolean isDirty = dirtyMap.get(jFormattedTextField);
	if (isDirty == null) {
	    //LOGGER.warning("no entry in dirtyMap for : " + jFormattedTextField);
	    isDirty = Boolean.FALSE;
	}
	return isDirty;
    }
    
    private abstract class CostPropertyListener implements PropertyChangeListener {
	protected JFormattedTextField field;
	
	CostPropertyListener(JFormattedTextField field) {
	    this.field = field;
	}
    }
    
    private class HigherCostPropertyListener extends CostPropertyListener {
	
	HigherCostPropertyListener(JFormattedTextField field) {
	    super(field);
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
	    //LOGGER.info("HCPL: change from " + evt.getSource() + "to new value: " + evt.getNewValue());
	    //LOGGER.info("HCPL: existing value of field: " + field.getValue());
	    Integer currentValue = (Integer) field.getValue();
	    Integer newValue = (Integer) evt.getNewValue();
	    //LOGGER.info("HCPL: values are equal: " + currentValue.equals(newValue));
	    if(!currentValue.equals(newValue)) {
		field.setValue((Integer) evt.getNewValue());
	    }
	}
	
    }
    
    private class LowerCostPropertyListener extends CostPropertyListener {
	
	LowerCostPropertyListener(JFormattedTextField field) {
	    super(field);
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
	    //LOGGER.info("LCPL: change from " + evt.getSource() + "to new value: " + evt.getNewValue());
	    //LOGGER.info("HCPL: existing value of field: " + field.getValue());
	    Integer newValue = (Integer) evt.getNewValue();
	    int i = 100 - newValue;
	    Integer currentValue = (Integer) field.getValue();
	    if(!currentValue.equals(i)) {
		field.setValue(i);
	    }
	}
	
    }
    
    //Percent Formatter
    private class PercentFormatter extends JFormattedTextField.AbstractFormatter {

	@Override
	public Object stringToValue(String string) {
	    //LOGGER.info("String: " + string);
	    String s = string.replace("%", ""); //To fix issue 16
	    try {
		Integer rate = Integer.valueOf(s);
		//LOGGER.info("PercentFormatter  rate: " + rate);
                setEditValid(true);
                if (rate > 100) {
                    setEditValid(false);
                    rate = 100;
                }
                if (rate < 0) {
                    setEditValid(false);
                    rate = 0;
                }
		return rate;
	    } catch (NumberFormatException pe) {
		LOGGER.log(Level.SEVERE, s, pe);
		return 0;
	    }
	}

	@Override
	public String valueToString(Object f) {
	    //LOGGER.info("value: " + f + " class: " + f.getClass());
	    int rate = (Integer) f;
	    //LOGGER.info("rate: " + rate);
	    return String.valueOf(rate) + '%';
	}
    }
    
    //Percent verifier
    private class PercentVerifier extends InputVerifier {

        @Override
        public boolean verify(JComponent input) {
            JFormattedTextField tf = (JFormattedTextField) input;
            return tf.isEditValid();
        }
        
    }
}
