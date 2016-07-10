package statistics;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Panel;

import javax.swing.JFrame;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.data.category.DefaultCategoryDataset;
import model.Model;


public class StatisticFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Panel panel;
	private Container c;
	private Model model;
	
	public StatisticFrame (Model model){
		super ("Statistiken");

		this.model = model;
		panel = new Panel();
		
		c = getContentPane();
		c.setLayout(new FlowLayout());

		setSize(new Dimension(1024, 768));
		setVisible(true);
		
		JFreeChart chart = ChartFactory.createBarChart("Verkaufte Produkte", "", "", model.getSoldProductsDataset());
		CategoryPlot catplot = chart.getCategoryPlot();
		catplot.setRangeGridlinePaint(Color.BLACK);
		
		ChartPanel chartpanel = new ChartPanel(chart);
		panel.removeAll();
		panel.add(chartpanel, BorderLayout.CENTER);
		
		c.add(panel);

	}

	
	
	

}
