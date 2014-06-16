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
package uk.ac.lkl.cram.ui.wizard;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import uk.ac.lkl.cram.model.LearnerFeedback;
import uk.ac.lkl.cram.model.StudentTeacherInteraction;
import uk.ac.lkl.cram.model.TLALibrary;
import uk.ac.lkl.cram.model.TLActivity;
import uk.ac.lkl.cram.model.UserTLALibrary;
import uk.ac.lkl.cram.ui.wizard.FilteredList.Filter;

/**
 * This class implements the visual aspects of the step in the wizard to select
 * an existing TLA. It presents some checkboxes and lists from which the user can
 * browse the TLAs. The TLAs are taken from the pre-defined set in addition to
 * the ones in the user's preferences.
 * @version $Revision$
 * @author Bernard Horan
 */
//$Date$
@SuppressWarnings("serial")
public class PredefinedVisualPanel extends JPanel {
    private static final Logger LOGGER = Logger.getLogger(PredefinedVisualPanel.class.getName());
    //The filtered list of predefined activities
    private FilteredList<TLActivity> predefinedFilteredList;
    //The filtered list of user's activities
    private FilteredList<TLActivity> userFilteredList;
    //the filter that selects the activities according to the user's choice of feedback
    private final Filter<TLActivity> feedbackFilter;
    //the filter that selects the activities according to the user's choice of interaction
    private final Filter<TLActivity> interactionFilter;

    /**
     * Creates new form PredefinedVisualPanel
     */
    public PredefinedVisualPanel() {
	initComponents();
	//Create the feedback filter
        feedbackFilter = new FeedbackFilter();
	//Create the interaction filter
        interactionFilter = new InteractionFilter();
	//Create the name comparator
	Comparator<TLActivity> nameComparator = new NameComparator();
	//Get the lists of existing TLAs
	List<TLActivity> predefinedList = getPredefinedList();
	List<TLActivity> userList = getUserList();
	//Wrap them in filtered lists
	predefinedFilteredList = new FilteredList<>(predefinedList);
	userFilteredList = new FilteredList<>(userList);
	//Set the comparators for the filtered lists
	predefinedFilteredList.setComparator(nameComparator);
	userFilteredList.setComparator(nameComparator);
	//Create the list models and assign to JLists
	ListModel<TLActivity> predefinedListModel = new PredefinedListModel<>(predefinedFilteredList);
	predefinedActivityList.setModel(predefinedListModel);
	ListModel<TLActivity> userListModel = new PredefinedListModel<>(userFilteredList);
	userActivityList.setModel(userListModel);
	//Add a data listener to each JList
	predefinedListModel.addListDataListener(new ListDataListener() {

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
		predefinedActivityList.clearSelection();
	    }
	});
	userListModel.addListDataListener(new ListDataListener() {

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
		userActivityList.clearSelection();
	    }
	});
	//Set the cell renderers for each JList
	ListCellRenderer<TLActivity> renderer = new TLActivityRenderer();
	predefinedActivityList.setCellRenderer(renderer);
	userActivityList.setCellRenderer(renderer);
	//Wire lists together
	new ExclusiveListSelectionListener(predefinedActivityList, userActivityList);
	
    
	//Interaction Radio Buttons	
	ActionListener interactionListener = new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent ae) {
		applyFilter();
	    }
	};
	tutorPresentCB.addActionListener(interactionListener);
	onlineCB.addActionListener(interactionListener);
	locationSpecificCB.addActionListener(interactionListener);
	timeSpecificCB.addActionListener(interactionListener);
	
	
	//Feedback Radio Buttons	
        ActionListener feedbackListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                applyFilter();
            }
        };
	tutorFeedbackCB.addActionListener(feedbackListener);
	tutorFeedbackCB.setActionCommand(LearnerFeedback.TUTOR.name());
	peerFeedbackCB.addActionListener(feedbackListener);
	peerFeedbackCB.setActionCommand(LearnerFeedback.PEER_ONLY.name());
	telFeedbackCB.addActionListener(feedbackListener);
	telFeedbackCB.setActionCommand(LearnerFeedback.TEL.name());
	noFeedbackCB.addActionListener(feedbackListener);
	noFeedbackCB.setActionCommand(LearnerFeedback.NONE.name());
        
        applyFilter();
    }

    @Override
    public String getName() {
	return "Select Existing Activity";
    }
    
    JList<TLActivity> getPredefinedActivityList() {
	return predefinedActivityList;
    }
    
    JList<TLActivity> getUserActivityList() {
	return userActivityList;
    }
    
    private List<TLActivity> getPredefinedList() {
	List<TLActivity> predefinedList = new ArrayList<>();
	TLALibrary library = TLALibrary.getDefaultLibrary();
	for (TLActivity activity : library.getActivities()) {
	    //Mark each activity as immutable 
	    //we don't want users to be able to 
	    //edit the name of predefined activities
	    activity.markAsImmutable();
	    predefinedList.add(activity);
	}
	return predefinedList;
    }
    
    private List<TLActivity> getUserList() {
	List<TLActivity> userList = new ArrayList<>();
	UserTLALibrary library = UserTLALibrary.getDefaultLibrary();
	for (TLActivity activity : library.getActivities()) {
	    userList.add(activity);
	}
	return userList;
    }
    
    private void applyFilter() {
	Filter<TLActivity> filter = new Filter<TLActivity>() {
	    @Override
	    public boolean isMatched(TLActivity tla) {
		boolean matchedFeedback = feedbackFilter.isMatched(tla);
                boolean matchedInteraction = interactionFilter.isMatched(tla);
		return matchedFeedback && matchedInteraction;
	    }
	};
	predefinedFilteredList.setFilter(filter);
	userFilteredList.setFilter(filter);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        predefinedActivityList = new javax.swing.JList<TLActivity>();
        panelTitleLabel = new javax.swing.JLabel();
        feedbackPanel = new javax.swing.JPanel();
        tutorFeedbackCB = new javax.swing.JCheckBox();
        peerFeedbackCB = new javax.swing.JCheckBox();
        telFeedbackCB = new javax.swing.JCheckBox();
        noFeedbackCB = new javax.swing.JCheckBox();
        studentInteractionPanel = new javax.swing.JPanel();
        tutorPresentCB = new javax.swing.JCheckBox();
        onlineCB = new javax.swing.JCheckBox();
        locationSpecificCB = new javax.swing.JCheckBox();
        timeSpecificCB = new javax.swing.JCheckBox();
        jScrollPane2 = new javax.swing.JScrollPane();
        tlaTextArea = new javax.swing.JTextArea();
        jScrollPane3 = new javax.swing.JScrollPane();
        userActivityList = new javax.swing.JList<TLActivity>();

        predefinedActivityList.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(PredefinedVisualPanel.class, "PredefinedVisualPanel.predefinedActivityList.border.title"))); // NOI18N
        predefinedActivityList.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        predefinedActivityList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane1.setViewportView(predefinedActivityList);

        org.openide.awt.Mnemonics.setLocalizedText(panelTitleLabel, org.openide.util.NbBundle.getMessage(PredefinedVisualPanel.class, "PredefinedVisualPanel.panelTitleLabel.text")); // NOI18N

        feedbackPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(PredefinedVisualPanel.class, "PredefinedVisualPanel.feedbackPanel.border.title"))); // NOI18N

        tutorFeedbackCB.setSelected(true);
        org.openide.awt.Mnemonics.setLocalizedText(tutorFeedbackCB, org.openide.util.NbBundle.getMessage(PredefinedVisualPanel.class, "PredefinedVisualPanel.tutorFeedbackCB.text")); // NOI18N

        peerFeedbackCB.setSelected(true);
        org.openide.awt.Mnemonics.setLocalizedText(peerFeedbackCB, org.openide.util.NbBundle.getMessage(PredefinedVisualPanel.class, "PredefinedVisualPanel.peerFeedbackCB.text")); // NOI18N

        telFeedbackCB.setSelected(true);
        org.openide.awt.Mnemonics.setLocalizedText(telFeedbackCB, org.openide.util.NbBundle.getMessage(PredefinedVisualPanel.class, "PredefinedVisualPanel.telFeedbackCB.text")); // NOI18N

        noFeedbackCB.setSelected(true);
        org.openide.awt.Mnemonics.setLocalizedText(noFeedbackCB, org.openide.util.NbBundle.getMessage(PredefinedVisualPanel.class, "PredefinedVisualPanel.noFeedbackCB.text")); // NOI18N

        javax.swing.GroupLayout feedbackPanelLayout = new javax.swing.GroupLayout(feedbackPanel);
        feedbackPanel.setLayout(feedbackPanelLayout);
        feedbackPanelLayout.setHorizontalGroup(
            feedbackPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(feedbackPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(feedbackPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tutorFeedbackCB)
                    .addComponent(peerFeedbackCB)
                    .addComponent(telFeedbackCB)
                    .addComponent(noFeedbackCB))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        feedbackPanelLayout.setVerticalGroup(
            feedbackPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(feedbackPanelLayout.createSequentialGroup()
                .addComponent(tutorFeedbackCB)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(peerFeedbackCB)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(telFeedbackCB)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(noFeedbackCB)
                .addGap(0, 6, Short.MAX_VALUE))
        );

        studentInteractionPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(PredefinedVisualPanel.class, "PredefinedVisualPanel.studentInteractionPanel.border.title"))); // NOI18N

        tutorPresentCB.setSelected(true);
        org.openide.awt.Mnemonics.setLocalizedText(tutorPresentCB, org.openide.util.NbBundle.getMessage(PredefinedVisualPanel.class, "PredefinedVisualPanel.tutorPresentCB.text")); // NOI18N

        onlineCB.setSelected(true);
        org.openide.awt.Mnemonics.setLocalizedText(onlineCB, org.openide.util.NbBundle.getMessage(PredefinedVisualPanel.class, "PredefinedVisualPanel.onlineCB.text")); // NOI18N

        locationSpecificCB.setSelected(true);
        org.openide.awt.Mnemonics.setLocalizedText(locationSpecificCB, org.openide.util.NbBundle.getMessage(PredefinedVisualPanel.class, "PredefinedVisualPanel.locationSpecificCB.text")); // NOI18N

        timeSpecificCB.setSelected(true);
        org.openide.awt.Mnemonics.setLocalizedText(timeSpecificCB, org.openide.util.NbBundle.getMessage(PredefinedVisualPanel.class, "PredefinedVisualPanel.timeSpecificCB.text")); // NOI18N

        javax.swing.GroupLayout studentInteractionPanelLayout = new javax.swing.GroupLayout(studentInteractionPanel);
        studentInteractionPanel.setLayout(studentInteractionPanelLayout);
        studentInteractionPanelLayout.setHorizontalGroup(
            studentInteractionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(studentInteractionPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(studentInteractionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tutorPresentCB)
                    .addComponent(onlineCB)
                    .addComponent(locationSpecificCB)
                    .addComponent(timeSpecificCB))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        studentInteractionPanelLayout.setVerticalGroup(
            studentInteractionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(studentInteractionPanelLayout.createSequentialGroup()
                .addComponent(tutorPresentCB)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(onlineCB)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(locationSpecificCB)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(timeSpecificCB)
                .addContainerGap())
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

        userActivityList.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(PredefinedVisualPanel.class, "PredefinedVisualPanel.userActivityList.border.title"))); // NOI18N
        userActivityList.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        userActivityList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane3.setViewportView(userActivityList);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(studentInteractionPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 212, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(feedbackPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 211, Short.MAX_VALUE)))
                    .addComponent(panelTitleLabel)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelTitleLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(feedbackPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(studentInteractionPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 153, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {feedbackPanel, studentInteractionPanel});

    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel feedbackPanel;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JCheckBox locationSpecificCB;
    private javax.swing.JCheckBox noFeedbackCB;
    private javax.swing.JCheckBox onlineCB;
    private javax.swing.JLabel panelTitleLabel;
    private javax.swing.JCheckBox peerFeedbackCB;
    private javax.swing.JList<TLActivity> predefinedActivityList;
    private javax.swing.JPanel studentInteractionPanel;
    private javax.swing.JCheckBox telFeedbackCB;
    private javax.swing.JCheckBox timeSpecificCB;
    private javax.swing.JTextArea tlaTextArea;
    private javax.swing.JCheckBox tutorFeedbackCB;
    private javax.swing.JCheckBox tutorPresentCB;
    private javax.swing.JList<TLActivity> userActivityList;
    // End of variables declaration//GEN-END:variables

    void setTLAInfo(String string) {
	tlaTextArea.setText(string);
    }

    /**
     * Renderer for the lists
     */
    private class TLActivityRenderer extends JLabel implements ListCellRenderer<TLActivity> {

	TLActivityRenderer() {
	    setOpaque(true);
	    setHorizontalAlignment(LEFT);
	    setVerticalAlignment(CENTER);
	}

	@Override
	public Component getListCellRendererComponent(JList<? extends TLActivity> list, TLActivity tla, int index, boolean isSelected, boolean cellHasFocus) {
	    if (isSelected) {
		setBackground(list.getSelectionBackground());
		setForeground(list.getSelectionForeground());
	    } else {
		setBackground(list.getBackground());
		setForeground(list.getForeground());
	    }
	    setText(tla.getName());
	    return this;
	}
    }
    
    /**
     * Feedback filter that is used to select activities that meet
     * the user's selection via checkboxes
     */
    private class FeedbackFilter implements Filter<TLActivity> {
        private final Set<JCheckBox> feedbackCheckBoxes = new HashSet<>();

        private FeedbackFilter() {
            feedbackCheckBoxes.add(tutorFeedbackCB);
            feedbackCheckBoxes.add(peerFeedbackCB);
            feedbackCheckBoxes.add(telFeedbackCB);
            feedbackCheckBoxes.add(noFeedbackCB);
        }

        @Override
        public boolean isMatched(TLActivity tla) {
            for (JCheckBox jCheckBox : feedbackCheckBoxes) {
                if (jCheckBox.isSelected()) {
                    LearnerFeedback feedback = LearnerFeedback.valueOf(jCheckBox.getActionCommand());
                    if (tla.getLearnerFeedback() == feedback) {
                        return true;
                    }
                }
            }
            return false;
        }
    }
    
    /**
     * Filter that is used to select activities according to the user's
     * selected interaction types via checkboxes
     */
    private class InteractionFilter implements Filter<TLActivity> {
        
        @Override
        public boolean isMatched(TLActivity tla) {
            StudentTeacherInteraction sti = tla.getStudentTeacherInteraction();
            if (tutorPresentCB.isSelected() == sti.isTutorSupported()) {
                return true;
            }
            if (onlineCB.isSelected() == sti.isOnline()) {
                return true;
            }
            if (locationSpecificCB.isSelected() == sti.isLocationSpecific()) {
                return true;
            }
            if (timeSpecificCB.isSelected() == sti.isTimeSpecific()) {
                return true;
            }
            return false;       
        }
        
    }
    
    /**
     * Comparator that sorts according to name of activity
     */
    private class NameComparator implements Comparator<TLActivity> {

	@Override
	public int compare(TLActivity t, TLActivity t1) {
	    return t.getName().compareToIgnoreCase(t1.getName());
	}
    }
    
    /**
     * Class that will ensure that only one list has a selection
     */
    private class ExclusiveListSelectionListener implements ListSelectionListener {
	private JList<TLActivity> list1;
	private JList<TLActivity> list2;
	
	ExclusiveListSelectionListener(JList<TLActivity> l1, JList<TLActivity> l2) {
	    list1 = l1;
	    list2 = l2;
	    addListeners();
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
	    if (!e.getValueIsAdjusting()) {
		@SuppressWarnings("unchecked")
		JList<TLActivity> thisList = (JList<TLActivity>) e.getSource();
		if (!thisList.isSelectionEmpty()) {
		    JList<TLActivity> otherList = getOtherList(thisList);
		    otherList.clearSelection();
		}
	    }
	}

	private void addListeners() {
	    list1.addListSelectionListener(this);
	    list2.addListSelectionListener(this);
	}

	private JList<TLActivity> getOtherList(JList<TLActivity> thisList) {
	    if (thisList == list1) {
		return list2;
	    } else {
		return list1;
	    }
	}
	
    }
    
}
