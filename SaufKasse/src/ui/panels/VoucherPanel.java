package ui.panels;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Map;

import javax.swing.JLabel;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;

import beans.Voucher;
import utilities.Variables;

public class VoucherPanel extends AbstractKassenPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	ArrayList<Voucher> voucherAl;
	ArrayList<JLabel> voucherCountLabels;
	ArrayList<JLabel> voucherNameLabels;

	public VoucherPanel() {
		super();
		voucherCountLabels = new ArrayList<JLabel>();
		voucherNameLabels = new ArrayList<JLabel>();

		// Implementation of Voucher
		voucherAl = new ArrayList<Voucher>();
	}

	// achtung fieser hack
	// Wil ein Märckchen 2 Voucher hat
	public void updateVoucherCount(Map<Integer, Integer> voucherMap) {
		
		for (int i = 0; i < voucherCountLabels.size(); i++) {
						
			Integer vIDCount = voucherMap.get(i + 1);
			if (vIDCount == null) {
				vIDCount = 0;
			}
			if (i == Variables.secondVoucherMittag - 2) {
				if (voucherMap.get(Variables.secondVoucherMittag) == null) {
					// nothing to add
				} else {
					vIDCount = vIDCount + voucherMap.get(Variables.secondVoucherMittag);
				}
			}
			voucherCountLabels.get(i).setText("" + vIDCount);
		}
		

	}

	public void initialize(ArrayList<Voucher> alv) {

		setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Märkchen", TitledBorder.LEADING,
				TitledBorder.TOP, null, new Color(128, 128, 128)));

		// exclude Mittag als extra märkchen
		for (int i = 0; i < alv.size(); i++) {

			setConstraintSettings(0, i, 0.5, 0.5, 1, 1);
			JLabel jl1 = new JLabel(alv.get(i).getDescription());
			jl1.setHorizontalAlignment(JLabel.CENTER);
			jl1.setFont(Variables.buttonAndComboFont);
			jl1.setOpaque(true);
			jl1.setBackground(alv.get(i).getColor());
			jl1.setForeground(Color.BLACK);
			voucherNameLabels.add(jl1);
			add(jl1, gbc);

			setConstraintSettings(1, i, 0.5, 0.5, 1, 1);
			JLabel jl2 = new JLabel("0");
			jl2.setHorizontalAlignment(JLabel.CENTER);
			jl2.setFont(Variables.buttonAndComboFont);
			jl2.setOpaque(true);
			jl2.setBackground(alv.get(i).getColor());
			jl2.setForeground(Color.BLACK);
			voucherCountLabels.add(jl2);
			add(jl2, gbc);

			if (alv.get(i).getId() != Variables.voucherDepositReturnID
					&& alv.get(i).getId() != Variables.voucherDepositID
					&& alv.get(i).getId() != Variables.secondVoucherMittag) {
				jl1.setVisible(true);
				jl2.setVisible(true);
			} else {
				jl1.setVisible(false);
				jl2.setVisible(false);
			}

		}

		repaint();
		revalidate();

	}

	public void changeFont(Font buttonAndComboFont) {
		for (int i = 0; i < voucherCountLabels.size(); i++) {
			voucherCountLabels.get(i).setFont(buttonAndComboFont);
			voucherNameLabels.get(i).setFont(buttonAndComboFont);
		}
	}

}
