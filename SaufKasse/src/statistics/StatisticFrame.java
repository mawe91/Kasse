package statistics;

import java.awt.BasicStroke;
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
import java.text.SimpleDateFormat;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;

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

		JFreeChart chart1 = ChartFactory.createBarChart("Verkaufte Produkte", "", "", model.getSoldProductsDataset(),
				PlotOrientation.HORIZONTAL, true, true, false);
		CategoryPlot catplot = chart1.getCategoryPlot();
		catplot.setRangeGridlinePaint(Color.BLACK);

		ChartPanel chartpanel1 = new ChartPanel(chart1);
		panel1.removeAll();
		panel1.add(chartpanel1, BorderLayout.CENTER);

		JFreeChart chart2 = ChartFactory.createTimeSeriesChart("Umsatz", "Zeit", "Umsatz", model.getSalesTimeSeries(), true, true, false);
		chart2.setBackgroundPaint(Color.WHITE);
		
        final XYPlot plot = chart2.getXYPlot();
        plot.setBackgroundPaint(Color.lightGray);
        plot.setDomainGridlinePaint(Color.white);
        plot.setRangeGridlinePaint(Color.white);
        plot.setDomainCrosshairVisible(true);
        plot.setRangeCrosshairVisible(false);
        
        final XYItemRenderer renderer = plot.getRenderer();
        if (renderer instanceof StandardXYItemRenderer) {
            final StandardXYItemRenderer rr = (StandardXYItemRenderer) renderer;
            rr.setBaseShapesVisible(true);
            renderer.setSeriesStroke(0, new BasicStroke(2.0f));
            renderer.setSeriesStroke(1, new BasicStroke(2.0f));
           }
        
        final DateAxis axis = (DateAxis) plot.getDomainAxis();
        axis.setDateFormatOverride(new SimpleDateFormat("yyyy.MM.dd '-' HH:mm"));

		ChartPanel chartpanel2 = new ChartPanel(chart2);
		panel2.removeAll();
		panel2.add(chartpanel2, BorderLayout.CENTER);

		tabbedPane.add("Statistik 1", panel1);
		tabbedPane.add("Statistik 2", panel2);

		c.add(tabbedPane, BorderLayout.CENTER);
	}

}
