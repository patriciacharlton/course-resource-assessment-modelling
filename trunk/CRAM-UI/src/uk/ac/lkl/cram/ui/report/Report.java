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
package uk.ac.lkl.cram.ui.report;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.TableModel;
import org.docx4j.dml.wordprocessingDrawing.Inline;
import org.docx4j.jaxb.Context;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.exceptions.InvalidFormatException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.BinaryPartAbstractImage;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;
import org.docx4j.wml.BooleanDefaultTrue;
import org.docx4j.wml.CTBorder;
import org.docx4j.wml.Jc;
import org.docx4j.wml.JcEnumeration;
import org.docx4j.wml.ObjectFactory;
import org.docx4j.wml.P;
import org.docx4j.wml.PPr;
import org.docx4j.wml.R;
import org.docx4j.wml.RPr;
import org.docx4j.wml.STBorder;
import org.docx4j.wml.Tbl;
import org.docx4j.wml.TblBorders;
import org.docx4j.wml.TblPr;
import org.docx4j.wml.Tc;
import org.docx4j.wml.Text;
import org.docx4j.wml.Tr;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import uk.ac.lkl.cram.model.AELMTest;
import uk.ac.lkl.cram.model.Module;
import uk.ac.lkl.cram.model.StudentTeacherInteraction;
import uk.ac.lkl.cram.model.TLALineItem;
import uk.ac.lkl.cram.model.TLActivity;
import uk.ac.lkl.cram.ui.TutorCostTableModel;
import uk.ac.lkl.cram.ui.TutorHoursTableModel;
import uk.ac.lkl.cram.ui.chart.FeedbackChartMaker;
import uk.ac.lkl.cram.ui.chart.HoursChartMaker;
import uk.ac.lkl.cram.ui.chart.LearningExperienceChartMaker;
import uk.ac.lkl.cram.ui.chart.LearningTypeChartMaker;


/**
 * Class to create a report in the form of a Word Document.
 * @author Bernard Horan
 * @version $Revision$
 */
//$Date$
public class Report {
    private static final Logger LOGGER = Logger.getLogger(Report.class.getName());
    private static final NumberFormat PERCENT_FORMATTER = NumberFormat.getPercentInstance();
    private static final DecimalFormat FLOAT_FORMATTER = new DecimalFormat( "0.0" );
    private static final NumberFormat INTEGER_FORMATTER = NumberFormat.getIntegerInstance();
    private static final NumberFormat CURRENCY_FORMATTER = NumberFormat.getCurrencyInstance();


    /**
     * For testing purposes only
     * @param args (ignored)
     * @throws Docx4JException
     */
    public static void main(String[] args) throws Docx4JException {
        Report report = new Report(AELMTest.populateModule());
        String filename = System.getProperty("user.dir") + "/AELMTest.docx";
        report.save(new File(filename));
    }

    private final WordprocessingMLPackage wordMLPackage;
    private final ObjectFactory factory;
    private final Module module;
    
    /**
     * Create a new instance of a report from the module
     * @param module the module from which to create a report
     * @throws InvalidFormatException
     */
    public Report(Module module) throws InvalidFormatException {
        wordMLPackage = WordprocessingMLPackage.createPackage();
        factory = Context.getWmlObjectFactory();
        CURRENCY_FORMATTER.setMaximumFractionDigits(0);
        this.module = module;
        addModuleData();
        addLearningActivities();
        addLearningTypes();
        addLearningExperience();
        addLearnerFeedback();
        addTutorHours();
        addTutorCost();
        addSummary();
    }
    
    private void addModuleData() {
        MainDocumentPart mdp = wordMLPackage.getMainDocumentPart();
        mdp.addStyledParagraphOfText("Title", "Course Resource Appraisal Statistics Summary");
        mdp.addStyledParagraphOfText("BodyText", "Module name: " + module.getModuleName());
        mdp.addStyledParagraphOfText("BodyText", "Number of weeks: " + module.getWeekCount());
        mdp.addStyledParagraphOfText("BodyText", "Number of credit hours: " + module.getTotalCreditHourCount());
        mdp.addStyledParagraphOfText("BodyText", "Tutor group size: " + module.getTutorGroupSize());
        mdp.addStyledParagraphOfText("BodyText", "Estimated number of home students in first presentation: " + module.getModulePresentations().get(0).getHomeStudentCount());
        mdp.addStyledParagraphOfText("BodyText", "Estimated number of overseas students in first presentation: " + module.getModulePresentations().get(0).getOverseasStudentCount());
        mdp.addStyledParagraphOfText("BodyText", "Estimated home student teaching-related income in first presentation: " + module.getModulePresentations().get(0).getHomeFee());
        mdp.addStyledParagraphOfText("BodyText", "Estimated overseas student teaching-related income in first presentation: " + module.getModulePresentations().get(0).getOverseasFee());
    }
    
    private void addLearningActivities() {
        MainDocumentPart mdp = wordMLPackage.getMainDocumentPart();
        mdp.addStyledParagraphOfText("Heading1", "Learning Activities");
        float online_hours = 0f;
        float f2f_hours = 0f;
        Tbl table = factory.createTbl();
        Tr tableHead = factory.createTr();
        TableModel tableModel = new ModuleReportModel(module);
        for (int col = 0; col < tableModel.getColumnCount(); col++) {
	    addTableCell(tableHead, tableModel.getColumnName(col), JcEnumeration.CENTER, true);
	}
	table.getContent().add(tableHead);
        int columnCount = tableModel.getColumnCount();
        for (int row = 0; row < tableModel.getRowCount(); row++) {
	    boolean lastRow = row == tableModel.getRowCount() - 1;
            Tr tableRow = factory.createTr();
	    addTableCell(tableRow, tableModel.getValueAt(row, 0).toString(), JcEnumeration.LEFT, lastRow);
            for (int col = 1; col < columnCount; col++) {
                addTableCell(tableRow, FLOAT_FORMATTER.format(tableModel.getValueAt(row, col)), JcEnumeration.RIGHT, lastRow);
            }          
            table.getContent().add(tableRow);
        }
        addBorders(table);
        List<TLALineItem> lineItems = module.getTLALineItems();
        for (TLALineItem lineItem : lineItems) {
            TLActivity activity = lineItem.getActivity();
            float totalLearnerHourCount = lineItem.getTotalLearnerHourCount(module);
            StudentTeacherInteraction sti = activity.getStudentTeacherInteraction();
            if (sti.isOnline()) {
                online_hours += totalLearnerHourCount;
            }
            if (sti.isTutorSupported() && sti.isLocationSpecific()) {
                f2f_hours += totalLearnerHourCount;
            }
        }
        mdp.addStyledParagraphOfText("BodyText", "Number of learner hours online: " + (int) online_hours);
        mdp.addStyledParagraphOfText("BodyText", "Number of learner hours face-to-face: " + (int) f2f_hours);
        addBorders(table);
        mdp.addObject(table);
    }
    
    private void addLearningTypes() {
        MainDocumentPart mdp = wordMLPackage.getMainDocumentPart();
        mdp.addStyledParagraphOfText("Heading1", "Learning Types");
        LearningTypeChartMaker maker = new LearningTypeChartMaker(module);
        JFreeChart chart = maker.getChartPanel().getChart();
        DefaultPieDataset dataset = (DefaultPieDataset) maker.getDataset();
        Tbl table = factory.createTbl();
        //Tr tableHead = factory.createTr();
        //addTableCell(tableHead, "Activity", "Percentage Learning Type");
        //table.getContent().add(tableHead);
        double total = 0f;
        for (Object key : dataset.getKeys()) {
            total += (Double) dataset.getValue((Comparable)key);
        }
        for (Object key : dataset.getKeys()) {
            Tr tableRow = factory.createTr();
            double percent = (Double) dataset.getValue((Comparable)key) / total;
            addSimpleTableCell(tableRow, key.toString());
            addTableCell(tableRow, PERCENT_FORMATTER.format(percent), JcEnumeration.RIGHT, false);
            table.getContent().add(tableRow);
        }
        addBorders(table);
        /* Specify the height and width of the Pie Chart */
        int width=480; /* Width of the chart */
        int height=360; /* Height of the chart */
        addChart(chart, width, height);
        mdp.addObject(table);
    }
    
    private void addLearningExperience() {
        MainDocumentPart mdp = wordMLPackage.getMainDocumentPart();
        mdp.addStyledParagraphOfText("Heading1", "Learning Experience");
        LearningExperienceChartMaker maker = new LearningExperienceChartMaker(module);
        JFreeChart chart = maker.getChartPanel().getChart();
        DefaultCategoryDataset dataset = (DefaultCategoryDataset) maker.getDataset();
        Tbl table = factory.createTbl();
        //Tr tableHead = factory.createTr();
        //addTableCell(tableHead, "Activity", "Percentage Learning Experience");
        //table.getContent().add(tableHead);
        double total = 0f;
        Comparable columnKey = (Comparable) dataset.getColumnKeys().get(0);
        for (Object key : dataset.getRowKeys()) {
            total += (Double) dataset.getValue((Comparable)key, columnKey);
        }
        for (Object key : dataset.getRowKeys()) {
            Tr tableRow = factory.createTr();
            double percent = (Double) dataset.getValue((Comparable)key, columnKey) / total;
            addSimpleTableCell(tableRow, key.toString());
            addTableCell(tableRow, PERCENT_FORMATTER.format(percent), JcEnumeration.RIGHT, false);
            table.getContent().add(tableRow);
        }
        addBorders(table);
        mdp.addObject(table);
        /* Specify the height and width of the Stacked Bar Chart */
        int width=480; /* Width of the chart */
        int height=180; /* Height of the chart */
        addChart(chart, width, height);
        
    }
    
    private void addLearnerFeedback() {
        MainDocumentPart mdp = wordMLPackage.getMainDocumentPart();
        mdp.addStyledParagraphOfText("Heading1", "Source of Learner Feedback");
        FeedbackChartMaker maker = new FeedbackChartMaker(module);
        JFreeChart chart = maker.getChartPanel().getChart();
        DefaultCategoryDataset dataset = (DefaultCategoryDataset) maker.getDataset();
        Tbl table = factory.createTbl();
        //Tr tableHead = factory.createTr();
        //addTableCell(tableHead, "Activity", "Learner Feedback (Hours per student/group)");
        //table.getContent().add(tableHead);
        Comparable columnKey = (Comparable) dataset.getColumnKeys().get(0);
        for (Object key : dataset.getRowKeys()) {
            Tr tableRow = factory.createTr();
            double value = (Double) dataset.getValue((Comparable)key, columnKey);
            addSimpleTableCell(tableRow, key.toString());
            addTableCell(tableRow, FLOAT_FORMATTER.format(value) + " hours", JcEnumeration.RIGHT, false);
            table.getContent().add(tableRow);
        }
        addBorders(table);
        mdp.addObject(table);
        /* Specify the height and width of the Bar Chart */
        int width=480; /* Width of the chart */
        int height=360; /* Height of the chart */
        addChart(chart, width, height);
    }
    
    private void addTutorHours() {
	MainDocumentPart mdp = wordMLPackage.getMainDocumentPart();
        mdp.addStyledParagraphOfText("Heading1", "Tutor Hours");
        HoursChartMaker maker = new HoursChartMaker(module);
        JFreeChart chart = maker.getChartPanel().getChart();
         /* Specify the height and width of the Bar Chart */
        int width=480; /* Width of the chart */
        int height=360; /* Height of the chart */
        addChart(chart, width, height);
        addTutorPreparationHours();
	addTutorSupportHours();
    }
    
    private void addTutorPreparationHours() {
	MainDocumentPart mdp = wordMLPackage.getMainDocumentPart();
	mdp.addStyledParagraphOfText("Heading2", "Tutor Preparation Hours");
	Tbl table = factory.createTbl();
	Tr tableHead = factory.createTr();
	TableModel tableModel = new TutorHoursTableModel(module);
	addTableCell(tableHead, tableModel.getColumnName(0), JcEnumeration.CENTER, true);
	for (int col = 1; col < 4; col++) {
	    addTableCell(tableHead, tableModel.getColumnName(col), JcEnumeration.CENTER, true);
	}
	table.getContent().add(tableHead);
	for (int row = 0; row < tableModel.getRowCount(); row++) {
	    boolean lastRow = row == tableModel.getRowCount() - 1;
	    Tr tableRow = factory.createTr();
	    if (lastRow) {
		addTableCell(tableRow, tableModel.getValueAt(row, 0).toString(), JcEnumeration.LEFT, true);
	    } else {
		addSimpleTableCell(tableRow, tableModel.getValueAt(row, 0).toString());
	    }
	    for (int col = 1; col < 4; col++) {
		addTableCell(tableRow, FLOAT_FORMATTER.format(tableModel.getValueAt(row, col)), JcEnumeration.RIGHT, lastRow);
	    }          
	    table.getContent().add(tableRow);
	}
	addBorders(table);
	mdp.addObject(table);
    }
    
    private void addTutorSupportHours() {
	MainDocumentPart mdp = wordMLPackage.getMainDocumentPart();
	mdp.addStyledParagraphOfText("Heading2", "Tutor Support Hours");
	Tbl table = factory.createTbl();
	Tr tableHead = factory.createTr();
	TableModel tableModel = new TutorHoursTableModel(module);
	addTableCell(tableHead, tableModel.getColumnName(0), JcEnumeration.CENTER, true);
	for (int col = 4; col < 7; col++) {
	    addTableCell(tableHead, tableModel.getColumnName(col), JcEnumeration.CENTER, true);
	}
	table.getContent().add(tableHead);
	for (int row = 0; row < tableModel.getRowCount(); row++) {
	    boolean lastRow = row == tableModel.getRowCount() - 1;
	    Tr tableRow = factory.createTr();
	    if (lastRow) {
		addTableCell(tableRow, tableModel.getValueAt(row, 0).toString(), JcEnumeration.LEFT, true);
	    } else {
		addSimpleTableCell(tableRow, tableModel.getValueAt(row, 0).toString());
	    }
	    for (int col = 4; col < 7; col++) {
		addTableCell(tableRow, FLOAT_FORMATTER.format(tableModel.getValueAt(row, col)), JcEnumeration.RIGHT, lastRow);
	    }          
	    table.getContent().add(tableRow);
	}
	addBorders(table);
	mdp.addObject(table);
    }
    
    private void addTutorCost() {
        MainDocumentPart mdp = wordMLPackage.getMainDocumentPart();
        mdp.addStyledParagraphOfText("Heading1", "Cost of Teaching Time");
        addTutorPreparationCost();
	addTutorSupportCost();
    }
    
    private void addTutorPreparationCost() {
	MainDocumentPart mdp = wordMLPackage.getMainDocumentPart();
        mdp.addStyledParagraphOfText("Heading2", "Cost of Preparation");
	Tbl table = factory.createTbl();
	Tr tableHead = factory.createTr();
	TableModel tableModel = new TutorCostTableModel(module);
	addTableCell(tableHead, tableModel.getColumnName(0), JcEnumeration.CENTER, true);
	for (int col = 1; col < 4; col++) {
	    addTableCell(tableHead, tableModel.getColumnName(col), JcEnumeration.CENTER, true);
	}
	table.getContent().add(tableHead);
	for (int row = 0; row < tableModel.getRowCount(); row++) {
	    boolean lastRow = row == tableModel.getRowCount() - 1;
	    Tr tableRow = factory.createTr();
	    if (lastRow) {
		addTableCell(tableRow, tableModel.getValueAt(row, 0).toString(), JcEnumeration.LEFT, true);
	    } else {
		addSimpleTableCell(tableRow, tableModel.getValueAt(row, 0).toString());
	    }
	    for (int col = 1; col < 4; col++) {
		addTableCell(tableRow, CURRENCY_FORMATTER.format(tableModel.getValueAt(row, col)), JcEnumeration.RIGHT, lastRow);
	    }          
	    table.getContent().add(tableRow);
	}
	addBorders(table);
	mdp.addObject(table);
    }
    
    private void addTutorSupportCost() {
	MainDocumentPart mdp = wordMLPackage.getMainDocumentPart();
        mdp.addStyledParagraphOfText("Heading2", "Cost of Support");
	Tbl table = factory.createTbl();
	Tr tableHead = factory.createTr();
	TableModel tableModel = new TutorCostTableModel(module);
	addTableCell(tableHead, tableModel.getColumnName(0), JcEnumeration.CENTER, true);
	for (int col = 4; col < 7; col++) {
	    addTableCell(tableHead, tableModel.getColumnName(col), JcEnumeration.CENTER, true);
	}
	table.getContent().add(tableHead);
	for (int row = 0; row < tableModel.getRowCount(); row++) {
	    boolean lastRow = row == tableModel.getRowCount() - 1;
	    Tr tableRow = factory.createTr();
	    if (lastRow) {
		addTableCell(tableRow, tableModel.getValueAt(row, 0).toString(), JcEnumeration.LEFT, true);
	    } else {
		addSimpleTableCell(tableRow, tableModel.getValueAt(row, 0).toString());
	    }
	    for (int col = 4; col < 7; col++) {
		addTableCell(tableRow, CURRENCY_FORMATTER.format(tableModel.getValueAt(row, col)), JcEnumeration.RIGHT, lastRow);
	    }          
	    table.getContent().add(tableRow);
	}
	addBorders(table);
	mdp.addObject(table);
    }
    
    private void addSummary() {
        MainDocumentPart mdp = wordMLPackage.getMainDocumentPart();
        mdp.addStyledParagraphOfText("Heading1", "Summary");
        Tbl table = factory.createTbl();
        Tr tableHead = factory.createTr();
        TableModel tableModel = new SummaryReportModel(module);
        int columnCount = tableModel.getColumnCount();
        for (int col = 0; col < columnCount; col++) {
            addTableCell(tableHead, tableModel.getColumnName(col), JcEnumeration.CENTER, true);
        }
        table.getContent().add(tableHead);
        for (int row = 0; row < tableModel.getRowCount(); row++) {
            boolean lastRow = row == tableModel.getRowCount() - 1;
	    Tr tableRow = factory.createTr();
	    if (lastRow) {
		addTableCell(tableRow, tableModel.getValueAt(row, 0).toString(), JcEnumeration.LEFT, true);
	    } else {
		addSimpleTableCell(tableRow, tableModel.getValueAt(row, 0).toString());
	    }
	    for (int col = 1; col < columnCount; col++) {
                if (row > 4) {
                    addTableCell(tableRow, CURRENCY_FORMATTER.format(tableModel.getValueAt(row, col)), JcEnumeration.RIGHT, lastRow);
                } else {
                    addTableCell(tableRow, INTEGER_FORMATTER.format(tableModel.getValueAt(row, col)), JcEnumeration.RIGHT, lastRow);
                }
            }          
            table.getContent().add(tableRow);
        }
        addBorders(table);
        mdp.addObject(table);
    }

    private void addBorders(Tbl table) {
        table.setTblPr(new TblPr());
        CTBorder border = new CTBorder();
        border.setColor("auto");
        border.setSz(new BigInteger("4"));
        border.setSpace(new BigInteger("0"));
        border.setVal(STBorder.SINGLE);
        
        TblBorders borders = new TblBorders();
        borders.setBottom(border);
        borders.setLeft(border);
        borders.setRight(border);
        borders.setTop(border);
        borders.setInsideH(border);
        borders.setInsideV(border);
        table.getTblPr().setTblBorders(borders);
    }
    
    /**
     * Save the report to a file. The file will be in the Word docx format, so it
     * would be a good idea if the file extension is 'docx'. This restriction is not 
     * imposed.
     * @param file the file into which the report should be
     * @throws Docx4JException
     */
    public void save(File file) throws Docx4JException {
        wordMLPackage.save(file);
    }
    
    private void addSimpleTableCell(Tr tableRow, String string) {
        Tc tableCell = factory.createTc();
        tableCell.getContent().add(wordMLPackage.getMainDocumentPart().createStyledParagraphOfText("NoSpacing", string));
        tableRow.getContent().add(tableCell);
    }
    
    private void addTableCells(Tr tableRow, JcEnumeration alignment, boolean bold, String... text) {
        for (String string : text) {
            addTableCell(tableRow, string, alignment, bold);
        }
    }
    
    private void addTableCell(Tr tableRow, String string, JcEnumeration alignment, boolean bold) {
        //<w:p>
        //  <w:pPr>
        //      <w:jc w:val="right"/>
        //  </w:pPr>
        //  <w:r>
        //	<w:rPr>
        //	    <w:b/>
        //	</w:rPr>
        //      <w:t>TEXT</w:t>
        //  </w:r>
        //  </w:p>
        
        //alignment
        PPr ppr = factory.createPPr();
        Jc jc = factory.createJc();
        jc.setVal(alignment);
        //set the alignment of the ppr
        ppr.setJc(jc);
        //Create a run
        R r = factory.createR();
	if (bold) {
	    //Create an rpr
	    RPr rpr = factory.createRPr();
	    //Set its boldness
	    rpr.setB(new BooleanDefaultTrue());
	    //set the rpr of the run
	    r.setRPr(rpr);
	}
        //Create a text and set its content
        Text txt = factory.createText();
        txt.setValue(string);
        //Put the text in the run
        r.getContent().add(txt);

        P p = factory.createP();
        p.setPPr(ppr);
        p.getContent().add(r);
        
        Tc tableCell = factory.createTc();
        tableCell.getContent().add(p);
        tableRow.getContent().add(tableCell);
    }

    private void addChart(JFreeChart chart, int width, int height) {
        MainDocumentPart mdp = wordMLPackage.getMainDocumentPart();
            try {
            /* We don't want to create an intermediate file. So, we create a byte array output stream 
            /* Write chart as PNG to Output Stream */
            ByteArrayOutputStream chart_out = new ByteArrayOutputStream();          
            ChartUtilities.writeChartAsPNG(chart_out,chart,width,height);
            chart_out.close();
            byte[] bytes = chart_out.toByteArray();
            BinaryPartAbstractImage imagePart = BinaryPartAbstractImage.createImagePart(wordMLPackage, bytes);
            Inline inline = imagePart.createImageInline(null, null, 0, 1, false);
            // Now add the inline in w:p/w:r/w:drawing
            org.docx4j.wml.P  p = factory.createP();
            org.docx4j.wml.R  run = factory.createR();		
            p.getContent().add(run);        
            org.docx4j.wml.Drawing drawing = factory.createDrawing();		
            run.getContent().add(drawing);		
            drawing.getAnchorOrInline().add(inline);
            mdp.addObject(p);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Failed to add chart", ex);
        }
    }

}
