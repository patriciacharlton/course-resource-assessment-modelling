package uk.ac.lkl.cram.ui.wizard;

import java.awt.Dimension;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.WeakHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
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
	weekCountField.setValue(lineItem.getWeekCount());
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
	
	//Percent Formatter
	final JFormattedTextField.AbstractFormatter seniorRateFormatter = new JFormattedTextField.AbstractFormatter() {

            @Override
            public Object stringToValue(String string) {
		//LOGGER.info("String: " + string);
		String s = string.replace("%", ""); //To fix issue 16
		try {
		    Float seniorRate = Float.valueOf(s) / 100;
		    //LOGGER.info("Senior rate: " + seniorRate);
		    return seniorRate;
		} catch (NumberFormatException pe) {
		    LOGGER.log(Level.SEVERE, s, pe);
		    return 0;
		}
	    }

            @Override
            public String valueToString(Object f) {
		//LOGGER.info("value: " + f + " class: " + f.getClass());
		int seniorRate = (int) ((Float) f * 100);
                //LOGGER.info("seniorRate: " + seniorRate);
                return String.valueOf(seniorRate) + '%';
            }
        };
        JFormattedTextField.AbstractFormatterFactory aff = new JFormattedTextField.AbstractFormatterFactory() {

            @Override
            public JFormattedTextField.AbstractFormatter getFormatter(JFormattedTextField jftf) {
                return seniorRateFormatter;
            }
        };
	
	
	//Preparation Fields
	final JFormattedTextField[] weeklyPreparationFields = new JFormattedTextField[3];
	weeklyPreparationFields[0] = presentation1WeeklyPreparation;
	weeklyPreparationFields[1] = presentation2WeeklyPreparation;
	weeklyPreparationFields[2] = presentation3WeeklyPreparation;
	final JFormattedTextField[] nonWeeklyPreparationFields = new JFormattedTextField[3];
	nonWeeklyPreparationFields[0] = presentation1NonWeeklyPreparation;
	nonWeeklyPreparationFields[1] = presentation2NonWeeklyPreparation;
	nonWeeklyPreparationFields[2] = presentation3NonWeeklyPreparation;
	JFormattedTextField[] seniorPreparationFields = new JFormattedTextField[3];
	seniorPreparationFields[0] = presentation1SeniorPreparation;
	seniorPreparationFields[1] = presentation2SeniorPreparation;
	seniorPreparationFields[2] = presentation3SeniorPreparation;
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
	    seniorPreparationFields[preparationIndex].setValue(lineItem.getPreparationTime(modulePresentation).getSeniorRate());
	    seniorPreparationFields[preparationIndex].addFocusListener(new SelectAllAdapter());
	    new FormattedTextFieldAdapter(seniorPreparationFields[preparationIndex]) {
		@Override
		public void updateValue(Object value) {
		    lineItem.getPreparationTime(modulePresentation).setSeniorRate((Float) value);
		    makeFieldDirty(textField);
		}
	    };
	    seniorPreparationFields[preparationIndex].setFormatterFactory(aff);
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
	JFormattedTextField[] seniorSupportFields = new JFormattedTextField[3];
	seniorSupportFields[0] = presentation1SeniorSupport;
	seniorSupportFields[1] = presentation2SeniorSupport;
	seniorSupportFields[2] = presentation3SeniorSupport;
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
	    seniorSupportFields[supportIndex].setValue(lineItem.getSupportTime(modulePresentation).getSeniorRate());
	    seniorSupportFields[supportIndex].addFocusListener(new SelectAllAdapter());
	    new FormattedTextFieldAdapter(seniorSupportFields[supportIndex]) {
		@Override
		public void updateValue(Object value) {
		    lineItem.getSupportTime(modulePresentation).setSeniorRate((Float) value);
		    makeFieldDirty(textField);
		}
	    };
	    seniorSupportFields[supportIndex].setFormatterFactory(aff);
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
        jLabel3 = new javax.swing.JLabel();
        jXLabel2 = new org.jdesktop.swingx.JXLabel();
        jXLabel1 = new org.jdesktop.swingx.JXLabel();
        jLabel6 = new javax.swing.JLabel();
        presentation1Label = new javax.swing.JLabel();
        presentation1WeeklyPreparation = new javax.swing.JFormattedTextField();
        presentation1NonWeeklyPreparation = new javax.swing.JFormattedTextField();
        presentation1SeniorPreparation = new javax.swing.JFormattedTextField();
        presentation2Label = new javax.swing.JLabel();
        presentation2WeeklyPreparation = new javax.swing.JFormattedTextField();
        presentation2NonWeeklyPreparation = new javax.swing.JFormattedTextField();
        presentation2SeniorPreparation = new javax.swing.JFormattedTextField();
        jLabel5 = new javax.swing.JLabel();
        presentation3WeeklyPreparation = new javax.swing.JFormattedTextField();
        presentation3NonWeeklyPreparation = new javax.swing.JFormattedTextField();
        presentation3SeniorPreparation = new javax.swing.JFormattedTextField();
        supportPanel = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jXLabel3 = new org.jdesktop.swingx.JXLabel();
        jXLabel4 = new org.jdesktop.swingx.JXLabel();
        jLabel7 = new javax.swing.JLabel();
        presentation1Label1 = new javax.swing.JLabel();
        presentation1WeeklySupport = new javax.swing.JFormattedTextField();
        presentation1NonWeeklySupport = new javax.swing.JFormattedTextField();
        presentation1SeniorSupport = new javax.swing.JFormattedTextField();
        presentation2Label1 = new javax.swing.JLabel();
        presentation2WeeklySupport = new javax.swing.JFormattedTextField();
        presentation2NonWeeklySupport = new javax.swing.JFormattedTextField();
        presentation2SeniorSupport = new javax.swing.JFormattedTextField();
        jLabel8 = new javax.swing.JLabel();
        presentation3WeeklySupport = new javax.swing.JFormattedTextField();
        presentation3NonWeeklySupport = new javax.swing.JFormattedTextField();
        presentation3SeniorSupport = new javax.swing.JFormattedTextField();
        tlaNamePanel = new javax.swing.JPanel();
        tlaNameField = new javax.swing.JTextField();

        activityPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Learner Hours"));

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

        preparationPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Preparation Hours"));
        preparationPanel.setToolTipText(bundle.getString("PREPARATION_HOURS_PANEL")); // NOI18N
        preparationPanel.setLayout(new java.awt.GridLayout(4, 4));
        preparationPanel.add(jLabel3);

        jXLabel2.setText(bundle.getString("WEEKLY HOURS")); // NOI18N
        jXLabel2.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jXLabel2.setLineWrap(true);
        preparationPanel.add(jXLabel2);

        jXLabel1.setText(bundle.getString("NON-WEEKLY HOURS")); // NOI18N
        jXLabel1.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jXLabel1.setLineWrap(true);
        preparationPanel.add(jXLabel1);

        jLabel6.setText(bundle.getString("% SENIOR")); // NOI18N
        jLabel6.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        preparationPanel.add(jLabel6);

        presentation1Label.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        presentation1Label.setText(bundle.getString("PRESENTATION 1:")); // NOI18N
        preparationPanel.add(presentation1Label);

        presentation1WeeklyPreparation.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        preparationPanel.add(presentation1WeeklyPreparation);

        presentation1NonWeeklyPreparation.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        preparationPanel.add(presentation1NonWeeklyPreparation);

        presentation1SeniorPreparation.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        preparationPanel.add(presentation1SeniorPreparation);

        presentation2Label.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        presentation2Label.setText(bundle.getString("PRESENTATION 2:")); // NOI18N
        preparationPanel.add(presentation2Label);

        presentation2WeeklyPreparation.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        preparationPanel.add(presentation2WeeklyPreparation);

        presentation2NonWeeklyPreparation.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        preparationPanel.add(presentation2NonWeeklyPreparation);

        presentation2SeniorPreparation.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        preparationPanel.add(presentation2SeniorPreparation);

        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel5.setText(bundle.getString("PRESENTATION 3:")); // NOI18N
        preparationPanel.add(jLabel5);

        presentation3WeeklyPreparation.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        preparationPanel.add(presentation3WeeklyPreparation);

        presentation3NonWeeklyPreparation.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        preparationPanel.add(presentation3NonWeeklyPreparation);

        presentation3SeniorPreparation.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        preparationPanel.add(presentation3SeniorPreparation);

        supportPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Support Hours"));
        supportPanel.setToolTipText(bundle.getString("SUPPORT_HOURS_PANEL")); // NOI18N
        supportPanel.setLayout(new java.awt.GridLayout(4, 4));
        supportPanel.add(jLabel4);

        jXLabel3.setText(bundle.getString("WEEKLY HOURS")); // NOI18N
        jXLabel3.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jXLabel3.setLineWrap(true);
        supportPanel.add(jXLabel3);

        jXLabel4.setText(bundle.getString("NON-WEEKLY HOURS")); // NOI18N
        jXLabel4.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jXLabel4.setLineWrap(true);
        supportPanel.add(jXLabel4);

        jLabel7.setText(bundle.getString("% SENIOR")); // NOI18N
        jLabel7.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        supportPanel.add(jLabel7);

        presentation1Label1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        presentation1Label1.setText(bundle.getString("PRESENTATION 1:")); // NOI18N
        supportPanel.add(presentation1Label1);

        presentation1WeeklySupport.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        supportPanel.add(presentation1WeeklySupport);

        presentation1NonWeeklySupport.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        supportPanel.add(presentation1NonWeeklySupport);

        presentation1SeniorSupport.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        supportPanel.add(presentation1SeniorSupport);

        presentation2Label1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        presentation2Label1.setText(bundle.getString("PRESENTATION 2:")); // NOI18N
        supportPanel.add(presentation2Label1);

        presentation2WeeklySupport.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        supportPanel.add(presentation2WeeklySupport);

        presentation2NonWeeklySupport.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        supportPanel.add(presentation2NonWeeklySupport);

        presentation2SeniorSupport.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        supportPanel.add(presentation2SeniorSupport);

        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel8.setText(bundle.getString("PRESENTATION 3:")); // NOI18N
        supportPanel.add(jLabel8);

        presentation3WeeklySupport.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        supportPanel.add(presentation3WeeklySupport);

        presentation3NonWeeklySupport.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        supportPanel.add(presentation3NonWeeklySupport);

        presentation3SeniorSupport.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        supportPanel.add(presentation3SeniorSupport);

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
            .add(preparationPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .add(supportPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
                .add(supportPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel activityPanel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JSeparator jSeparator2;
    private org.jdesktop.swingx.JXLabel jXLabel1;
    private org.jdesktop.swingx.JXLabel jXLabel2;
    private org.jdesktop.swingx.JXLabel jXLabel3;
    private org.jdesktop.swingx.JXLabel jXLabel4;
    private javax.swing.JFormattedTextField nonWeeklyHoursField;
    private javax.swing.JPanel preparationPanel;
    private javax.swing.JLabel presentation1Label;
    private javax.swing.JLabel presentation1Label1;
    private javax.swing.JFormattedTextField presentation1NonWeeklyPreparation;
    private javax.swing.JFormattedTextField presentation1NonWeeklySupport;
    private javax.swing.JFormattedTextField presentation1SeniorPreparation;
    private javax.swing.JFormattedTextField presentation1SeniorSupport;
    private javax.swing.JFormattedTextField presentation1WeeklyPreparation;
    private javax.swing.JFormattedTextField presentation1WeeklySupport;
    private javax.swing.JLabel presentation2Label;
    private javax.swing.JLabel presentation2Label1;
    private javax.swing.JFormattedTextField presentation2NonWeeklyPreparation;
    private javax.swing.JFormattedTextField presentation2NonWeeklySupport;
    private javax.swing.JFormattedTextField presentation2SeniorPreparation;
    private javax.swing.JFormattedTextField presentation2SeniorSupport;
    private javax.swing.JFormattedTextField presentation2WeeklyPreparation;
    private javax.swing.JFormattedTextField presentation2WeeklySupport;
    private javax.swing.JFormattedTextField presentation3NonWeeklyPreparation;
    private javax.swing.JFormattedTextField presentation3NonWeeklySupport;
    private javax.swing.JFormattedTextField presentation3SeniorPreparation;
    private javax.swing.JFormattedTextField presentation3SeniorSupport;
    private javax.swing.JFormattedTextField presentation3WeeklyPreparation;
    private javax.swing.JFormattedTextField presentation3WeeklySupport;
    private javax.swing.JPanel supportPanel;
    private javax.swing.JTextField tlaNameField;
    private javax.swing.JPanel tlaNamePanel;
    private javax.swing.JFormattedTextField weekCountField;
    private javax.swing.JFormattedTextField weeklyHoursField;
    // End of variables declaration//GEN-END:variables

    public static void main(String args[]) {

        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            LOGGER.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
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

}
