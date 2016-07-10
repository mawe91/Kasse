package controller;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import model.Model;
import statistics.StatisticFrame;
import ui.view.View;
import utilities.Variables;

public class Controller implements ActionListener, ListSelectionListener {

	private Model model;

	private View view;

	private int selectedInvoiceLineStart;
	private int selectedInvoiceLineEnd;

	public Controller(Model model) {
		super();

		this.model = model;

		selectedInvoiceLineStart = -1;
		selectedInvoiceLineEnd = -1;

	}

	public void addViewToController(View v) {
		view = v;
		model.addObserver(view);
		view.initialize(model.getAllProducts(), model.getAllVouchers());

	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		System.out.println(arg0.getActionCommand());

			// order products
			for (int i = 0; i < model.getAllProducts().size(); i++) {
				if (arg0.getActionCommand() == model.getAllProducts().get(i).getName()) {

					model.orderProduct(model.getAllProducts().get(i).getId());
					return;
				}
			}

			// order voucher
			for (int i = 0; i < model.getAllVouchers().size(); i++) {
				if (arg0.getActionCommand() == model.getAllVouchers().get(i).getDescription()) {
					model.orderVoucher(model.getAllVouchers().get(i).getId());
					return;
				}
			}

			// calc
			for (int i = 0; i < 10; i++) {
				try {
					if (Integer.parseInt(arg0.getActionCommand()) == i) {
						model.addCalculatorNumber(i);
						return;
					}
				} catch (NumberFormatException e) {
					
				}
			}
			if (arg0.getActionCommand() == ",") {
				model.addCalculatorComma();
			} else if (arg0.getActionCommand() == "Bar") {
				model.payUnroundSum();
				if (model.isPaidSumBiggerThanInvoiceSum()) {
					invoiceTermination();
				}
				return;
			} else if (arg0.getActionCommand() == "X") {
				model.multiplyCount();
				return;
			} else if (arg0.getActionCommand() == "del") {
				model.deleteCalcValue();
				return;
			} else if (arg0.getActionCommand() == "Bezahlen") {
				view.changeToPayMode();
				model.deleteCalcValue();
				return;
			} else if (arg0.getActionCommand() == "Weiter kassieren") {
				view.changeToSellingMode();
				model.deleteCalcValue();
				return;
			} else if (arg0.getActionCommand() == "Zeilenstorno") {
				model.deleteInvoiceLines(selectedInvoiceLineStart, selectedInvoiceLineEnd);
				return;
			} else if (arg0.getActionCommand() == "Bon abbrechen") {
				model.deleteAndInitNewInvoice();
				view.changeToSellingMode();
				return;
			} else if (arg0.getActionCommand() == "5 €") {
				model.paySum(5);
				if (model.isPaidSumBiggerThanInvoiceSum()) {
					invoiceTermination();
				}
				return;
			} else if (arg0.getActionCommand() == "10 €") {
				model.paySum(10);
				if (model.isPaidSumBiggerThanInvoiceSum()) {
					invoiceTermination();
				}
				return;
			} else if (arg0.getActionCommand() == "20 €") {
				model.paySum(20);
				if (model.isPaidSumBiggerThanInvoiceSum()) {
					invoiceTermination();
				}
				return;
			} else if (arg0.getActionCommand() == "50 €") {
				model.paySum(50);
				if (model.isPaidSumBiggerThanInvoiceSum()) {
					invoiceTermination();
				}
				return;
			} else if (arg0.getActionCommand() == "Zahlung korrigieren") {
				model.deletePaidSum();
				return;
			} else if (arg0.getActionCommand() == "Schrift +") {
				// Test Font change
				int oldFontSize = Variables.buttonAndComboFont.getSize();
				Variables.buttonAndComboFont = new Font("Arial", Font.BOLD, oldFontSize + 3);
				view.changeFont(Variables.buttonAndComboFont);
				return;
			} else if (arg0.getActionCommand() == "Schrift -") {
				int oldFontSize = Variables.buttonAndComboFont.getSize();
				if (Variables.buttonAndComboFont.getSize() > 10) {
					Variables.buttonAndComboFont = new Font("Arial", Font.BOLD, oldFontSize - 3);
					view.changeFont(Variables.buttonAndComboFont);
				}
				return;
			} else if (arg0.getActionCommand() == "Test"){
				StatisticFrame sf = new StatisticFrame(model);
			}

	}

	private void invoiceTermination() {
		int n = view.finishInvoiceFrame();

		// Bestätigen
		if (n == 0) {
			model.saveAndInitInvoice();
			view.changeToSellingMode();
			// Zahlung korrigieren
		} else if (n == 1) {
			model.deletePaidSum();

			// Bon abbrechen
		} else if (n == 2) {
			model.deleteAndInitNewInvoice();
			view.changeToSellingMode();
		}

	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		if (!e.getValueIsAdjusting()) {

			DefaultListSelectionModel sm = (DefaultListSelectionModel) e.getSource();

			// convert Line Count in Invoice Line Index
			int maxInvLineIndex = model.getCurrentInvoiceLineCount() - 1;

			// Convert and Save Min and Max Index Values of InvoiceLines
			selectedInvoiceLineEnd = maxInvLineIndex - sm.getMinSelectionIndex();
			selectedInvoiceLineStart = maxInvLineIndex - sm.getMaxSelectionIndex();

			// Enable or Disable Zeilenstorno Button
			if (sm.getMinSelectionIndex() != -1) {
				view.setZeilenstornoEnabled(true);
			} else {
				view.setZeilenstornoEnabled(false);
			}
		}
	}
}
