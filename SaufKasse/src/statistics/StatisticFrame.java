package statistics;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Panel;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import model.Model;

public class StatisticFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Panel panel1;
	private Panel panel2;
	
	private Container c;
	private Model model;
	

	public StatisticFrame(Model model) {
		super("Statistiken");
		
		JTabbedPane tabbedPane = new JTabbedPane();

		this.model = model;
		panel1 = new Panel(new BorderLayout());
		panel2 = new Panel(new BorderLayout());

		c = getContentPane();
		c.setLayout(new CardLayout());

		setSize(new Dimension(1024, 768));
		setVisible(true);

		JFreeChart chart = ChartFactory.createBarChart("Verkaufte Produkte", "", "", model.getSoldProductsDataset());
		CategoryPlot catplot = chart.getCategoryPlot();
		catplot.setRangeGridlinePaint(Color.BLACK);

		ChartPanel chartpanel1 = new ChartPanel(chart);
		panel1.removeAll();
		panel1.add(chartpanel1, BorderLayout.CENTER);		
		
		JFreeChart ch = ChartFactory.createBarChart("a", "b", "c", model.getSoldProductsDataset(),
				PlotOrientation.HORIZONTAL, false, false, false);
		CategoryPlot chpl = ch.getCategoryPlot();
		chpl.setRangeGridlinePaint(Color.BLACK);		

		ChartPanel chartpanel2 = new ChartPanel(ch);
		panel2.removeAll();
		panel2.add(chartpanel2, BorderLayout.CENTER);

		tabbedPane.add("Statistik 1", panel1);
		tabbedPane.add("Statistik 2", panel2);
		
		c.add(tabbedPane, BorderLayout.CENTER);
	}

}
