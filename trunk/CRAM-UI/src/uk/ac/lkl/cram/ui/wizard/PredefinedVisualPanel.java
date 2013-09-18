package uk.ac.lkl.cram.ui.wizard;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import uk.ac.lkl.cram.model.AELMTest;
import uk.ac.lkl.cram.model.LearnerFeedback;
import uk.ac.lkl.cram.model.Module;
import uk.ac.lkl.cram.model.StudentTeacherInteraction;
import uk.ac.lkl.cram.model.TLALineItem;
import uk.ac.lkl.cram.model.TLActivity;
import uk.ac.lkl.cram.ui.wizard.FilteredList.Filter;

/**
 * $Date$
 * $Revision$
 * @author Bernard Horan
 */
@SuppressWarnings("serial")
public class PredefinedVisualPanel extends JPanel {
    private FilteredList<TLActivity> filteredList;
    private InteractionMatcher interactionMatcher;
    private LearnerFeedback matchingFeedback;
    private static final Logger LOGGER = Logger.getLogger(PredefinedVisualPanel.class.getName());

    /**
     * Creates new form PredefinedVisualPanel
     */
    public PredefinedVisualPanel() {
	initComponents();
	interactionMatcher = InteractionMatcher.TUTOR_PRESENT;
	matchingFeedback = LearnerFeedback.NONE;
	List<TLActivity> predefinedList = getPredefinedList();
	filteredList = new FilteredList<TLActivity>(predefinedList);
	filteredList.setComparator(new Comparator<TLActivity>() {

	    @Override
	    public int compare(TLActivity t, TLActivity t1) {
		return t.getName().compareToIgnoreCase(t1.getName());
	    }
	});
	applyFilter();
	PredefinedListModel<TLActivity> listModel = new PredefinedListModel<TLActivity>(filteredList);
	activityList.setModel(listModel);
	listModel.addListDataListener(new ListDataListener() {

	    @Override
	    public void intervalAdded(ListDataEvent lde) {
		//No op
	    }

	    @Override
	    public void intervalRemoved(ListDataEvent lde) {
		//No op
	    }

	    @Override
	    public void contentsChanged(ListDataEvent lde) {
		activityList.clearSelection();
	    }
	});
	activityList.setCellRenderer(new TLActivityRenderer());
	
	//Interaction Radio Buttons	
	ActionListener interactionListener = new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent ae) {
		String actionCommand = ae.getActionCommand();
		interactionMatcher = InteractionMatcher.valueOf(actionCommand);
		applyFilter();
	    }
	};
	tutorPresentRB.addActionListener(interactionListener);
	tutorPresentRB.setActionCommand(InteractionMatcher.TUTOR_PRESENT.name());
	onlineRB.addActionListener(interactionListener);
	onlineRB.setActionCommand(InteractionMatcher.ONLINE.name());
	locationSpecificRB.addActionListener(interactionListener);
	locationSpecificRB.setActionCommand(InteractionMatcher.LOCATION_SPECIFIC.name());
	timeSpecificRB.addActionListener(interactionListener);
	timeSpecificRB.setActionCommand(InteractionMatcher.TIME_SPECIFIC.name());
	switch (interactionMatcher) {
	    case TUTOR_PRESENT: {
		tutorPresentRB.setSelected(true);
		break;
	    }
	    case ONLINE: {
		onlineRB.setSelected(true);
		break;
	    }
	    case LOCATION_SPECIFIC: {
		locationSpecificRB.setSelected(true);
		break;
	    }
	    case TIME_SPECIFIC: {
		timeSpecificRB.setSelected(true);
		break;
	    }
	}
	
	//Feedback Radio Buttons	
	ActionListener feedbackListener = new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent ae) {
		String actionCommand = ae.getActionCommand();
		matchingFeedback = LearnerFeedback.valueOf(actionCommand);
		applyFilter();
	    }
	};
	teacherFeedbackRB.addActionListener(feedbackListener);
	teacherFeedbackRB.setActionCommand(LearnerFeedback.TUTOR.name());
	peerFeedbackRB.addActionListener(feedbackListener);
	peerFeedbackRB.setActionCommand(LearnerFeedback.PEER_ONLY.name());
	telFeedbackRB.addActionListener(feedbackListener);
	telFeedbackRB.setActionCommand(LearnerFeedback.TEL.name());
	noFeedbackRB.addActionListener(feedbackListener);
	noFeedbackRB.setActionCommand(LearnerFeedback.NONE.name());
	switch (matchingFeedback) {
	    case TUTOR: {
		teacherFeedbackRB.setSelected(true);
		break;
	    }
	    case PEER_ONLY: {
		peerFeedbackRB.setSelected(true);
		break;
	    }
	    case TEL: {
		telFeedbackRB.setSelected(true);
		break;
	    }
	    case NONE: {
		noFeedbackRB.setSelected(true);
		break;
	    }
	}
    }

    @Override
    public String getName() {
	return "Select Predefined Activity";
    }
    
    JList getActivityList() {
	return activityList;
    }
    
    private List<TLActivity> getPredefinedList() {
	List<TLActivity> predefinedList = new ArrayList<TLActivity>();
	Module m = AELMTest.populateModule();
	for (TLALineItem lineItem : m.getTLALineItems()) {
	    predefinedList.add(lineItem.getActivity());
	}
	return predefinedList;
    }
    
    private void applyFilter() {
	Filter<TLActivity> filter = new Filter<TLActivity>() {
	    @Override
	    public boolean isMatched(TLActivity tla) {
		boolean matchedInteraction = false;
		StudentTeacherInteraction sti = tla.getStudentTeacherInteraction();
		switch (interactionMatcher) {
		    case TUTOR_PRESENT: {
			matchedInteraction = sti.isTutorSupported();
			break;
		    }
		    case ONLINE: {
			matchedInteraction = sti.isOnline();
			break;
		    }
		    case LOCATION_SPECIFIC: {
			matchedInteraction = sti.isLocationSpecific();
			break;
		    }
		    case TIME_SPECIFIC: {
			matchedInteraction = sti.isTimeSpecific();
			break;
		    }
		}
		boolean matchedFeedback = tla.getLearnerFeedback() == matchingFeedback;
		return matchedFeedback && matchedInteraction;
	    }
	};
	filteredList.setFilter(filter);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        studentFeedbackBG = new javax.swing.ButtonGroup();
        studentInteractionBG = new javax.swing.ButtonGroup();
        jScrollPane1 = new javax.swing.JScrollPane();
        activityList = new javax.swing.JList();
        panelTitleLabel = new javax.swing.JLabel();
        feedbackPanel = new javax.swing.JPanel();
        teacherFeedbackRB = new javax.swing.JRadioButton();
        peerFeedbackRB = new javax.swing.JRadioButton();
        telFeedbackRB = new javax.swing.JRadioButton();
        noFeedbackRB = new javax.swing.JRadioButton();
        studentInteractionPanel = new javax.swing.JPanel();
        tutorPresentRB = new javax.swing.JRadioButton();
        onlineRB = new javax.swing.JRadioButton();
        locationSpecificRB = new javax.swing.JRadioButton();
        timeSpecificRB = new javax.swing.JRadioButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        tlaTextArea = new javax.swing.JTextArea();

        activityList.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        activityList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane1.setViewportView(activityList);

        org.openide.awt.Mnemonics.setLocalizedText(panelTitleLabel, org.openide.util.NbBundle.getMessage(PredefinedVisualPanel.class, "PredefinedVisualPanel.panelTitleLabel.text")); // NOI18N

        feedbackPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(PredefinedVisualPanel.class, "PredefinedVisualPanel.feedbackPanel.border.title"))); // NOI18N

        studentFeedbackBG.add(teacherFeedbackRB);
        org.openide.awt.Mnemonics.setLocalizedText(teacherFeedbackRB, org.openide.util.NbBundle.getMessage(PredefinedVisualPanel.class, "PredefinedVisualPanel.teacherFeedbackRB.text")); // NOI18N

        studentFeedbackBG.add(peerFeedbackRB);
        org.openide.awt.Mnemonics.setLocalizedText(peerFeedbackRB, org.openide.util.NbBundle.getMessage(PredefinedVisualPanel.class, "PredefinedVisualPanel.peerFeedbackRB.text")); // NOI18N

        studentFeedbackBG.add(telFeedbackRB);
        org.openide.awt.Mnemonics.setLocalizedText(telFeedbackRB, org.openide.util.NbBundle.getMessage(PredefinedVisualPanel.class, "PredefinedVisualPanel.telFeedbackRB.text")); // NOI18N

        studentFeedbackBG.add(noFeedbackRB);
        org.openide.awt.Mnemonics.setLocalizedText(noFeedbackRB, org.openide.util.NbBundle.getMessage(PredefinedVisualPanel.class, "PredefinedVisualPanel.noFeedbackRB.text")); // NOI18N

        javax.swing.GroupLayout feedbackPanelLayout = new javax.swing.GroupLayout(feedbackPanel);
        feedbackPanel.setLayout(feedbackPanelLayout);
        feedbackPanelLayout.setHorizontalGroup(
            feedbackPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(feedbackPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(feedbackPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(teacherFeedbackRB)
                    .addComponent(peerFeedbackRB)
                    .addComponent(telFeedbackRB)
                    .addComponent(noFeedbackRB))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        feedbackPanelLayout.setVerticalGroup(
            feedbackPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(feedbackPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(teacherFeedbackRB)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(peerFeedbackRB)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(telFeedbackRB)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(noFeedbackRB))
        );

        studentInteractionPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(PredefinedVisualPanel.class, "PredefinedVisualPanel.studentInteractionPanel.border.title"))); // NOI18N

        studentInteractionBG.add(tutorPresentRB);
        org.openide.awt.Mnemonics.setLocalizedText(tutorPresentRB, org.openide.util.NbBundle.getMessage(PredefinedVisualPanel.class, "PredefinedVisualPanel.tutorPresentRB.text")); // NOI18N

        studentInteractionBG.add(onlineRB);
        org.openide.awt.Mnemonics.setLocalizedText(onlineRB, org.openide.util.NbBundle.getMessage(PredefinedVisualPanel.class, "PredefinedVisualPanel.onlineRB.text")); // NOI18N

        studentInteractionBG.add(locationSpecificRB);
        org.openide.awt.Mnemonics.setLocalizedText(locationSpecificRB, org.openide.util.NbBundle.getMessage(PredefinedVisualPanel.class, "PredefinedVisualPanel.locationSpecificRB.text")); // NOI18N

        studentInteractionBG.add(timeSpecificRB);
        org.openide.awt.Mnemonics.setLocalizedText(timeSpecificRB, org.openide.util.NbBundle.getMessage(PredefinedVisualPanel.class, "PredefinedVisualPanel.timeSpecificRB.text")); // NOI18N

        javax.swing.GroupLayout studentInteractionPanelLayout = new javax.swing.GroupLayout(studentInteractionPanel);
        studentInteractionPanel.setLayout(studentInteractionPanelLayout);
        studentInteractionPanelLayout.setHorizontalGroup(
            studentInteractionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(studentInteractionPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(studentInteractionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tutorPresentRB)
                    .addComponent(onlineRB)
                    .addComponent(locationSpecificRB)
                    .addComponent(timeSpecificRB))
                .addContainerGap(56, Short.MAX_VALUE))
        );
        studentInteractionPanelLayout.setVerticalGroup(
            studentInteractionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(studentInteractionPanelLayout.createSequentialGroup()
                .addComponent(tutorPresentRB)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(onlineRB)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(locationSpecificRB)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(timeSpecificRB)
                .addGap(0, 6, Short.MAX_VALUE))
        );

        jScrollPane2.setEnabled(false);
        jScrollPane2.setFocusTraversalKeysEnabled(false);
        jScrollPane2.setFocusable(false);
        jScrollPane2.setRequestFocusEnabled(false);
        jScrollPane2.setVerifyInputWhenFocusTarget(false);
        jScrollPane2.setWheelScrollingEnabled(false);

        tlaTextArea.setColumns(20);
        tlaTextArea.setLineWrap(true);
        tlaTextArea.setRows(5);
        tlaTextArea.setWrapStyleWord(true);
        jScrollPane2.setViewportView(tlaTextArea);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(panelTitleLabel)
                .addContainerGap(145, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane1)
                    .addComponent(studentInteractionPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(feedbackPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(panelTitleLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(studentInteractionPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(feedbackPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 188, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JList activityList;
    private javax.swing.JPanel feedbackPanel;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JRadioButton locationSpecificRB;
    private javax.swing.JRadioButton noFeedbackRB;
    private javax.swing.JRadioButton onlineRB;
    private javax.swing.JLabel panelTitleLabel;
    private javax.swing.JRadioButton peerFeedbackRB;
    private javax.swing.ButtonGroup studentFeedbackBG;
    private javax.swing.ButtonGroup studentInteractionBG;
    private javax.swing.JPanel studentInteractionPanel;
    private javax.swing.JRadioButton teacherFeedbackRB;
    private javax.swing.JRadioButton telFeedbackRB;
    private javax.swing.JRadioButton timeSpecificRB;
    private javax.swing.JTextArea tlaTextArea;
    private javax.swing.JRadioButton tutorPresentRB;
    // End of variables declaration//GEN-END:variables

    void setTLAInfo(String string) {
	tlaTextArea.setText(string);
    }

    private class TLActivityRenderer extends JLabel implements ListCellRenderer {

	TLActivityRenderer() {
	    setOpaque(true);
	    setHorizontalAlignment(LEFT);
	    setVerticalAlignment(CENTER);
	}

	@Override
	public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
	    if (isSelected) {
		setBackground(list.getSelectionBackground());
		setForeground(list.getSelectionForeground());
	    } else {
		setBackground(list.getBackground());
		setForeground(list.getForeground());
	    }
	    TLActivity tla = (TLActivity) value;
	    setText(tla.getName());
	    return this;
	}
    }
    
    private enum InteractionMatcher  {

	TUTOR_PRESENT, ONLINE, LOCATION_SPECIFIC, TIME_SPECIFIC;
    }
    
    
}
