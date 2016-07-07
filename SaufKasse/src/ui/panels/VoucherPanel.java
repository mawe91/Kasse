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
	}

	//achtung fieser hack
	//Wil ein Märckchen 2 Voucher hat
	public void updateVoucherCount(Map<Integer, Integer> voucherMap) {
		
		
		for (int i = 0; i < voucherLabels.size(); i++) {
			Integer vIDCount = voucherMap.get(i + 1);
			if (vIDCount == null) {
				vIDCount = 0;
			}
			if (i == voucherLabels.size()-1){
				if (voucherMap.get(i+2) == null) {
					//nothing to add
				} else {
					vIDCount = vIDCount + voucherMap.get(i+2);
				}
				
			}
			voucherLabels.get(i).setText("" + vIDCount);
		}

	}

	public void initialize(ArrayList<Voucher> alv) {

		//exclude Mittag als extra märkchen
		for (int i = 0; i < alv.size()-1; i++) {
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

	public void changeFont(Font buttonAndComboFont) {
		for (int i = 0; i < voucherLabels.size(); i++) {
			voucherLabels.get(i).setFont(buttonAndComboFont);
		}
	}

}
