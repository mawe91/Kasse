package ui.view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import javax.swing.*;

import alertobjects.CalcFieldAlert;
import alertobjects.InvoiceAlert;
import alertobjects.OpenSumAlert;
import beans.Product;
import beans.Voucher;
import controller.Controller;
import statistics.StatisticPanel;
import ui.panels.ScreenControlArea;
import ui.panels.ScreenInfoArea;
import utilities.Variables;

/**
 * @author Marian
 *
 */
public class View extends JFrame implements Observer {

	private static final long serialVersionUID = 1L;
	
	private JMenuBar menuBar;
	private JMenu menu;
	private JMenuItem menuItem;
	
	private ScreenInfoArea infoTopArea;
	private ScreenControlArea buttonButtomPanel;

	private Container c;

	private JTabbedPane tabbedMasterPane;

	public View(Controller controller) {
		super("Kasse");
		
		tabbedMasterPane = new JTabbedPane();
				
		c = getContentPane();
		c.setLayout(new BorderLayout());

		initJMenu(controller);
		
		tabbedMasterPane.add("Kasse", initCheckoutPanel(controller));
		tabbedMasterPane.add("Auswertung", new StatisticPanel(controller.getModel()));
		
		add(tabbedMasterPane, BorderLayout.CENTER);
	
		setSize(new Dimension(1024, 768));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}

	private JPanel initCheckoutPanel (Controller con){
		JPanel checkoutPanel = new JPanel(new GridLayout(2, 1));
		infoTopArea = new ScreenInfoArea(con);
		checkoutPanel.add(infoTopArea);

		buttonButtomPanel = new ScreenControlArea(con);
		checkoutPanel.add(buttonButtomPanel);

		return checkoutPanel;
	}

	private void initJMenu(Controller handler) {
		menuBar = new JMenuBar();
		
		menu = new JMenu("Datei");
		menu.getAccessibleContext().setAccessibleDescription("Nice Menu");
		menu.setFont(Variables.menuFont);
		menuBar.add(menu);
		
		menuItem = new JMenuItem("Schrift +");
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_PLUS, ActionEvent.CTRL_MASK));
		menuItem.getAccessibleContext().setAccessibleDescription("Nothing this code does");
		menuItem.addActionListener(handler);
		menuItem.setFont(Variables.menuFont);
		menu.add(menuItem);
		
		menuItem = new JMenuItem("Schrift -");
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_MINUS, ActionEvent.CTRL_MASK));
		menuItem.setFont(Variables.menuFont);
		menuItem.addActionListener(handler);
		menu.add(menuItem);
		
		this.setJMenuBar(menuBar);
	}

	public void updateUI() {

		infoTopArea.updateUI();
		buttonButtomPanel.updateUI();

	}

	public void initialize(ArrayList<Product> alp, ArrayList<Voucher> alv) {
		buttonButtomPanel.initialize(alp, alv);
		infoTopArea.initialize(alv);

	}

	public int finishInvoiceFrame() {
		String[] yesNoOptions = { "Bestätigen", "Zahlung korrigieren", "Bon Abbrechen" };

		
		return JOptionPane.showOptionDialog(null, "Rechnung abschließen?", "Rechnungsabschluss",
				JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, yesNoOptions, yesNoOptions[0]);


	}

	@Override
	public void update(Observable o, Object arg) {

		if (arg.getClass().equals(CalcFieldAlert.class)) {
			CalcFieldAlert cfa = (CalcFieldAlert) arg;
			buttonButtomPanel.updateCalcField(cfa);
		} else if (arg.getClass().equals(InvoiceAlert.class)) {
			InvoiceAlert ia = (InvoiceAlert) arg;
			infoTopArea.updateAfterInvoiceChange(ia);

			if (ia.getInvoice().getInvoiceLines().size() == 0) {
				buttonButtomPanel.setChangeToPaymodeEnabled(false);
			} else {
				buttonButtomPanel.setChangeToPaymodeEnabled(true);
			}

		} else if (arg.getClass().equals(OpenSumAlert.class)) {
			OpenSumAlert psa = (OpenSumAlert) arg;
			infoTopArea.updateAfterPaidSumChanged(psa);
		}

	}

	public void changeToPayMode() {
		buttonButtomPanel.changeToPayMode();
		repaint();
		revalidate();
	}

	public void changeToSellingMode() {
		buttonButtomPanel.changeToSellingMode();
		repaint();
		revalidate();
	}

	public int getSelectedTableRow() {
		return infoTopArea.getSelectedTableRow();
	}

	public void setZeilenstornoEnabled(boolean b) {
		buttonButtomPanel.setZeilenstornoEnabled(b);
	}

	public void changeFont(Font buttonAndComboFont) {
		buttonButtomPanel.changeFont(buttonAndComboFont);
		infoTopArea.changeFont(buttonAndComboFont);
		repaint();
		revalidate();		
	}

}