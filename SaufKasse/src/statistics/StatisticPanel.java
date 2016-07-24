package statistics;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.text.DecimalFormat;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.PieSectionLabelGenerator;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import model.Model;
import utilities.Variables;

public class StatisticPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Model model;

	private DecimalFormat format;

	private JTabbedPane tabbedPane;
	private JTextField txfTotalSum;
	private JTextField txfTotalSumProductSale;
	private JTextField txfTotalSumVouchers;
	private JTextField txfTotalSumDeposit;
	private JTextField txfSoldProductsWithoutDepositWithoutVouchers;
	private JTextField txfSoldVouchersWithoutProductWithoutDeposit;
	private JTextField txfSoldVouchersWithoutDepositWithProducts;
	private JTextField txfSoldMinusReturnDeposit;

	private ArrayList<JComponent> uiRepo;
	private ArrayList<ChartPanel> chartRepo;

	public StatisticPanel(Model model) {
		super();

		this.model = model;
		uiRepo = new ArrayList<>();
		chartRepo = new ArrayList<>();
		format = new DecimalFormat("#0.00 €");

		setLayout(new CardLayout());

		add(initChartTabbedPane(), BorderLayout.CENTER);
	}

	// unused
	private JPanel initDatePickerPanel() {
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Datum und Zeit eingrenzen",
				TitledBorder.LEADING, TitledBorder.TOP, null, new Color(128, 128, 128)));
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[] { 137, 137, 137, 0 };
		gbl_panel.rowHeights = new int[] { 74, 74, 74, 0, 0 };
		gbl_panel.columnWeights = new double[] { 0.0, 0.0, 0.0, Double.MIN_VALUE };
		gbl_panel.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
		panel.setLayout(gbl_panel);

		JLabel lblVon = new JLabel("Von");
		GridBagConstraints gbc_lblVon = new GridBagConstraints();
		gbc_lblVon.fill = GridBagConstraints.BOTH;
		gbc_lblVon.insets = new Insets(0, 0, 5, 5);
		gbc_lblVon.gridx = 0;
		gbc_lblVon.gridy = 0;
		panel.add(lblVon, gbc_lblVon);

		JTextField dummyDatePicker1 = new JTextField();
		dummyDatePicker1.setHorizontalAlignment(SwingConstants.CENTER);
		dummyDatePicker1.setText("22-07-2016");
		GridBagConstraints gbc_textField = new GridBagConstraints();
		gbc_textField.fill = GridBagConstraints.BOTH;
		gbc_textField.insets = new Insets(0, 0, 5, 5);
		gbc_textField.gridx = 1;
		gbc_textField.gridy = 0;
		panel.add(dummyDatePicker1, gbc_textField);
		dummyDatePicker1.setColumns(10);

		JSpinner spinner = new JSpinner();
		GridBagConstraints gbc_spinner = new GridBagConstraints();
		gbc_spinner.fill = GridBagConstraints.BOTH;
		gbc_spinner.insets = new Insets(0, 0, 5, 0);
		gbc_spinner.gridx = 2;
		gbc_spinner.gridy = 0;
		panel.add(spinner, gbc_spinner);

		JLabel lblBis = new JLabel("Bis");
		GridBagConstraints gbc_lblBis = new GridBagConstraints();
		gbc_lblBis.fill = GridBagConstraints.BOTH;
		gbc_lblBis.insets = new Insets(0, 0, 5, 5);
		gbc_lblBis.gridx = 0;
		gbc_lblBis.gridy = 1;
		panel.add(lblBis, gbc_lblBis);

		JTextField dummyDatePicker2 = new JTextField();
		dummyDatePicker2.setHorizontalAlignment(SwingConstants.CENTER);
		dummyDatePicker2.setText("23-07-2016");
		GridBagConstraints gbc_textField_1 = new GridBagConstraints();
		gbc_textField_1.fill = GridBagConstraints.BOTH;
		gbc_textField_1.insets = new Insets(0, 0, 5, 5);
		gbc_textField_1.gridx = 1;
		gbc_textField_1.gridy = 1;
		panel.add(dummyDatePicker2, gbc_textField_1);
		dummyDatePicker2.setColumns(10);

		JSpinner spinner_1 = new JSpinner();
		GridBagConstraints gbc_spinner_1 = new GridBagConstraints();
		gbc_spinner_1.fill = GridBagConstraints.BOTH;
		gbc_spinner_1.insets = new Insets(0, 0, 5, 0);
		gbc_spinner_1.gridx = 2;
		gbc_spinner_1.gridy = 1;
		panel.add(spinner_1, gbc_spinner_1);

		JButton btnAktualisiere = new JButton("Aktualisieren");
		GridBagConstraints gbc_btnAktualisiere = new GridBagConstraints();
		gbc_btnAktualisiere.insets = new Insets(0, 0, 5, 0);
		gbc_btnAktualisiere.gridwidth = 3;
		gbc_btnAktualisiere.fill = GridBagConstraints.BOTH;
		gbc_btnAktualisiere.gridx = 0;
		gbc_btnAktualisiere.gridy = 2;
		panel.add(btnAktualisiere, gbc_btnAktualisiere);

		JLabel lblDieseZahlenStellen = new JLabel(
				"Aktuell dargestellter Zeitraum: 18-07-2016 12:00 Uhr bis 19-07-2016 12:00 Uhr.");
		lblDieseZahlenStellen.setFont(new Font("Tahoma", Font.PLAIN, 10));
		GridBagConstraints gbc_lblDieseZahlenStellen = new GridBagConstraints();
		gbc_lblDieseZahlenStellen.gridwidth = 3;
		gbc_lblDieseZahlenStellen.insets = new Insets(0, 0, 0, 5);
		gbc_lblDieseZahlenStellen.gridx = 0;
		gbc_lblDieseZahlenStellen.gridy = 3;
		panel.add(lblDieseZahlenStellen, gbc_lblDieseZahlenStellen);

		return panel;
	}

	private JTabbedPane initChartTabbedPane() {

		tabbedPane = new JTabbedPane();

		setSize(new Dimension(1024, 768));
		setVisible(true);

		// JFreeChart chart2 = ChartFactory.createTimeSeriesChart("Umsatz",
		// "Zeit", "Umsatz", model.getSalesTimeSeries(), true, true, false);
		// chart2.setBackgroundPaint(Color.WHITE);
		//
		// final XYPlot plot = chart2.getXYPlot();
		// plot.setBackgroundPaint(Color.lightGray);
		// plot.setDomainGridlinePaint(Color.white);
		// plot.setRangeGridlinePaint(Color.white);
		// plot.setDomainCrosshairVisible(true);
		// plot.setRangeCrosshairVisible(false);
		//
		// final XYItemRenderer renderer = plot.getRenderer();
		// if (renderer instanceof StandardXYItemRenderer) {
		// final StandardXYItemRenderer rr = (StandardXYItemRenderer) renderer;
		// rr.setBaseShapesVisible(true);
		// renderer.setSeriesStroke(0, new BasicStroke(2.0f));
		// renderer.setSeriesStroke(1, new BasicStroke(2.0f));
		// }
		//
		// final DateAxis axis = (DateAxis) plot.getDomainAxis();
		// axis.setDateFormatOverride(new SimpleDateFormat("yyyy.MM.dd '-'
		// HH:mm"));
		//
		// ChartPanel chartpanel2 = new ChartPanel(chart2);
		// panel2.removeAll();
		// panel2.add(chartpanel2, BorderLayout.CENTER);

		tabbedPane.add("Verkaufte Märkchen",
				generateBarChartPanel("Verkaufte Märkchen Gesamt", model.getSoldVoucherDatasetWithoutDeposit()));
		tabbedPane.add("Verkaufte Produkte", generateBarChartPanel("Verkaufte Produkte (Ohne Märkchendirektbuchung)",
				model.getSoldProductsWithoutDeposit()));
		tabbedPane.add("Pfand", generatePieChartPanel("Pfand", model.getDepositPieData()));
		tabbedPane.add("Zahlen", initCounterStatisticPanel());

		return tabbedPane;
	}
	
	public void updateCharts() {

		removeAll();
		
		uiRepo = new ArrayList<>();
		chartRepo = new ArrayList<>();
		add(initChartTabbedPane(), BorderLayout.CENTER);
		
		
		repaint();
		revalidate();
		
	}
	
	private JPanel generatePieChartPanel(String graphname, DefaultPieDataset dataset) {
		JPanel panel = new JPanel(new BorderLayout());

		JFreeChart chart = ChartFactory.createPieChart(graphname, dataset, true, true, false);

		PiePlot plot = (PiePlot) chart.getPlot();
		plot.setLabelFont(Variables.buttonAndComboFont);
		plot.setNoDataMessage("No data available");
		plot.setCircular(false);
		plot.setLabelGap(0.02);
		
		PieSectionLabelGenerator gen = new StandardPieSectionLabelGenerator(
	            "{0}: {1} ({2})", new DecimalFormat("0"), new DecimalFormat("0%"));
	        plot.setLabelGenerator(gen);

		ChartPanel chartpanel = new ChartPanel(chart);
		chartRepo.add(chartpanel);
		panel.add(chartpanel, BorderLayout.CENTER);
		return panel;
	}

	private JPanel generateBarChartPanel(String graphName, DefaultCategoryDataset dataset) {
		JPanel panel = new JPanel(new BorderLayout());
		
		JFreeChart chart = ChartFactory.createBarChart(graphName, "", "", dataset, PlotOrientation.HORIZONTAL, true,
				true, false);
		CategoryPlot catplot = chart.getCategoryPlot();
		catplot.setRangeGridlinePaint(Color.BLACK);
		
		ChartPanel chartpanel = new ChartPanel(chart);
		chartRepo.add(chartpanel);
		panel.removeAll();
		panel.add(chartpanel, BorderLayout.CENTER);

		return panel;
	}

	private JPanel initCounterStatisticPanel() {

		JPanel gridPanel = new JPanel();
		gridPanel.setLayout(new GridLayout(0, 2, 0, 0));

		JPanel sumPanel = new JPanel();
		sumPanel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Umsatz Statistik",
				TitledBorder.LEADING, TitledBorder.TOP, null, new Color(128, 128, 128)));
		gridPanel.add(sumPanel);
		sumPanel.setLayout(new GridLayout(0, 2, 5, 5));

		JLabel lblTotalSumProductSale = new JLabel("Umsatz durch Produktverkauf");
		sumPanel.add(lblTotalSumProductSale);
		uiRepo.add(lblTotalSumProductSale);

		txfTotalSumProductSale = new JTextField();
		txfTotalSumProductSale.setHorizontalAlignment(SwingConstants.CENTER);
		txfTotalSumProductSale.setText("2000,00 \u20AC");
		sumPanel.add(txfTotalSumProductSale);
		uiRepo.add(txfTotalSumProductSale);
		txfTotalSumProductSale.setColumns(10);

		JLabel lblTotalSumVouchers = new JLabel("Umsatz durch M\u00E4rkchenverkauf");
		sumPanel.add(lblTotalSumVouchers);
		uiRepo.add(lblTotalSumVouchers);

		txfTotalSumVouchers = new JTextField();
		txfTotalSumVouchers.setHorizontalAlignment(SwingConstants.CENTER);
		txfTotalSumVouchers.setText("1300,50 \u20AC");
		sumPanel.add(txfTotalSumVouchers);
		uiRepo.add(txfTotalSumVouchers);
		txfTotalSumVouchers.setColumns(10);

		JLabel lblTotalSumDeposit = new JLabel(
				"<html>Umsatz durch Pfand<br>(Ausgegebenes - Zur\u00FCckgegebenes Pfand)</html>");
		sumPanel.add(lblTotalSumDeposit);
		uiRepo.add(lblTotalSumDeposit);

		txfTotalSumDeposit = new JTextField();
		sumPanel.add(txfTotalSumDeposit);
		uiRepo.add(txfTotalSumDeposit);
		txfTotalSumDeposit.setHorizontalAlignment(SwingConstants.CENTER);
		txfTotalSumDeposit.setText("20,00 \u20AC");
		txfTotalSumDeposit.setColumns(10);

		JLabel lblTotalSum = new JLabel("Gesamtumsatz (in \u20AC)");
		lblTotalSum.setHorizontalAlignment(SwingConstants.CENTER);
		sumPanel.add(lblTotalSum);
		uiRepo.add(lblTotalSum);

		txfTotalSum = new JTextField();
		txfTotalSum.setHorizontalAlignment(SwingConstants.CENTER);
		sumPanel.add(txfTotalSum);
		uiRepo.add(txfTotalSum);
		txfTotalSum.setText("3300,50 \u20AC");
		txfTotalSum.setColumns(10);

		JPanel saleAnalysisPanel = new JPanel();
		saleAnalysisPanel.setBorder(
				new TitledBorder(null, "Absatz Statistik", TitledBorder.LEADING, TitledBorder.TOP, null, Color.GRAY));
		gridPanel.add(saleAnalysisPanel);
		saleAnalysisPanel.setLayout(new GridLayout(0, 2, 5, 5));

		JLabel lblSoldProductsWithoutDepositWithoutVouchers = new JLabel(
				"<html>Verkaufte Produkte<br>(ohne Pfand, ohne M\u00E4rkchenverkauf)<html>");
		lblSoldProductsWithoutDepositWithoutVouchers.setHorizontalAlignment(SwingConstants.CENTER);
		saleAnalysisPanel.add(lblSoldProductsWithoutDepositWithoutVouchers);
		uiRepo.add(lblSoldProductsWithoutDepositWithoutVouchers);

		txfSoldProductsWithoutDepositWithoutVouchers = new JTextField();
		txfSoldProductsWithoutDepositWithoutVouchers.setHorizontalAlignment(SwingConstants.CENTER);
		saleAnalysisPanel.add(txfSoldProductsWithoutDepositWithoutVouchers);
		uiRepo.add(txfSoldProductsWithoutDepositWithoutVouchers);
		txfSoldProductsWithoutDepositWithoutVouchers.setText("150");
		txfSoldProductsWithoutDepositWithoutVouchers.setColumns(10);

		JLabel lblSoldVouchersWithoutProductWithoutDeposit = new JLabel(
				"<html>Verkaufte M\u00E4rkchen <br> (Ohne Pfand, Ohne Produkte)<html>");
		saleAnalysisPanel.add(lblSoldVouchersWithoutProductWithoutDeposit);
		uiRepo.add(lblSoldVouchersWithoutProductWithoutDeposit);

		txfSoldVouchersWithoutProductWithoutDeposit = new JTextField();
		txfSoldVouchersWithoutProductWithoutDeposit.setHorizontalAlignment(SwingConstants.CENTER);
		txfSoldVouchersWithoutProductWithoutDeposit.setText("20");
		saleAnalysisPanel.add(txfSoldVouchersWithoutProductWithoutDeposit);
		uiRepo.add(txfSoldVouchersWithoutProductWithoutDeposit);
		txfSoldVouchersWithoutProductWithoutDeposit.setColumns(10);

		JLabel lblSoldVouchersWithoutDepositWithProducts = new JLabel(
				"<html>Verkaufte M\u00E4rkchen <br> (Ohne Pfand, Mit Produkten)<html>");
		saleAnalysisPanel.add(lblSoldVouchersWithoutDepositWithProducts);
		uiRepo.add(lblSoldVouchersWithoutDepositWithProducts);

		txfSoldVouchersWithoutDepositWithProducts = new JTextField();
		txfSoldVouchersWithoutDepositWithProducts.setHorizontalAlignment(SwingConstants.CENTER);
		txfSoldVouchersWithoutDepositWithProducts.setText("150");
		saleAnalysisPanel.add(txfSoldVouchersWithoutDepositWithProducts);
		uiRepo.add(txfSoldVouchersWithoutDepositWithProducts);
		txfSoldVouchersWithoutDepositWithProducts.setColumns(10);

		JLabel lblSoldMinusReturnDeposit = new JLabel("Differenz Pfandmarken");
		saleAnalysisPanel.add(lblSoldMinusReturnDeposit);
		uiRepo.add(lblSoldMinusReturnDeposit);

		txfSoldMinusReturnDeposit = new JTextField();
		txfSoldMinusReturnDeposit.setHorizontalAlignment(SwingConstants.CENTER);
		txfSoldMinusReturnDeposit.setText("20");
		saleAnalysisPanel.add(txfSoldMinusReturnDeposit);
		uiRepo.add(txfSoldMinusReturnDeposit);
		txfSoldMinusReturnDeposit.setColumns(10);

		changeFont(Variables.buttonAndComboFont);
		updateCountStatisticPanel();

		return gridPanel;
	}

	private void changeFont(Font font) {
		for (int i = 0; i < uiRepo.size(); i++) {
			uiRepo.get(i).setFont(font);
		}
	}

	private void updateCountStatisticPanel() {

		txfTotalSumProductSale.setText("" + format.format(model.getTotalProductSum()));
		txfTotalSumVouchers.setText("" + format.format(model.getTotalVoucherSum()));
		txfTotalSumDeposit.setText("" + format.format(model.getTotalDepositSum()));
		txfTotalSum.setText("" + format.format(model.getTotalSaleSum()));
		txfSoldProductsWithoutDepositWithoutVouchers.setText("" + model.getSoldProductWithoutVoucher());
		txfSoldVouchersWithoutProductWithoutDeposit.setText("" + model.getSoldVouchersWithoutDepositWithoutProducts());
		txfSoldVouchersWithoutDepositWithProducts.setText("" + model.getSoldVouchersWithoutDepositWithProducts());
		txfSoldMinusReturnDeposit.setText("" + model.getSoldAndRefundDepositDifference());
	}



}
