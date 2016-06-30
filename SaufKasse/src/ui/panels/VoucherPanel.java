package ui.panels;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Map;

import javax.swing.JLabel;

import beans.Voucher;
import utilities.Variables;

public class VoucherPanel extends AbstractKassenPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	ArrayList<Voucher> voucherAl;
	ArrayList<JLabel> voucherLabels;

	public VoucherPanel() {
		super();
		voucherLabels = new ArrayList<JLabel>();

		// Implementation of Voucher
		voucherAl = new ArrayList<Voucher>();

		// setBorder(getNewTitledBorder(""));
	}

	public void updateVoucherCount(Map<Integer, Integer> voucherMap) {
		for (int i = 0; i < voucherLabels.size(); i++) {
			Integer vIDCount = voucherMap.get(i + 1);
			if (vIDCount == null) {
				vIDCount = 0;
			}
			voucherLabels.get(i).setText("" + vIDCount);
		}

	}

	public void initialize(ArrayList<Voucher> alv) {

		for (int i = 0; i < alv.size(); i++) {
			setConstraintSettings(0, i, 0.5, 0.5, 1, 1);
			JLabel jl = new JLabel("0");
			jl.setHorizontalAlignment(JLabel.CENTER);
			jl.setFont(Variables.buttonAndComboFont);
			jl.setOpaque(true);
			jl.setBackground(alv.get(i).getColor());
			jl.setForeground(Color.BLACK);
			voucherLabels.add(jl);
			add(jl, gbc);
		}

		repaint();
		revalidate();

	}

	public void changeToPayMode() {
		// TODO Auto-generated method stub

	}

	public void changeToSellingMode() {
		// TODO Auto-generated method stub

	}

	public void changeFont(Font buttonAndComboFont) {
		for (int i = 0; i < voucherLabels.size(); i++) {
			voucherLabels.get(i).setFont(buttonAndComboFont);
		}
	}

	public void repaintVoucherViewWithNewVoucherData(ArrayList<Voucher> voucherList) {
		removeAll();
		initialize(voucherList);
		repaint();
		revalidate();
	}

}
