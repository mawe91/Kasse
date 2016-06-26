package ui.view;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

import beans.Product;
import beans.Voucher;
import controller.Controller;
import mdf.panel.ProductDetailPanel;
import mdf.panel.VoucherDetailPanel;
import utilities.Variables;

public class MasterDataFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	JComboBox<String> jcb;
	JPanel dataButtonPanel;
	GridBagConstraints gbc;
	Controller controller;

	private VoucherDetailPanel voucherContentPanel;
	private ProductDetailPanel productDetailPanel;
	
	private JPanel contentPanel;
	
	private ArrayList<Voucher> voucherCacheMDF;

	public MasterDataFrame(ArrayList<Voucher> voucherArray, Controller contr) {
		super();

		this.controller = contr;

		setTitle("Bons");
		setSize(800, 640);
		setVisible(true);

		setLayout(new BorderLayout());

		JPanel naviPanel = new JPanel();
		naviPanel.setLayout(new BorderLayout());
		naviPanel.setBackground(Color.GRAY);

		jcb = new JComboBox<String>();
		jcb.addItem("Märkle");
		jcb.addItem("Speisen");
		jcb.addItem("Getränke");
		jcb.setFont(Variables.buttonAndComboFont);
		jcb.setActionCommand("MasterData.ComboBoxChanged");
		jcb.addItemListener(controller);
		naviPanel.add(jcb, BorderLayout.NORTH);

		dataButtonPanel = new JPanel();
		dataButtonPanel.setBackground(Color.GRAY);
		dataButtonPanel.setLayout(new GridBagLayout());
		
		naviPanel.add(dataButtonPanel, BorderLayout.CENTER);

		JButton jb = new JButton("Neu");
		jb.setActionCommand("MasterData.Neu");
		jb.addActionListener(controller);
		jb.setFont(Variables.buttonAndComboFont);
		naviPanel.add(jb, BorderLayout.SOUTH);
		
		contentPanel = new JPanel();
		contentPanel.setLayout(new CardLayout());
		
		JPanel emptyPanel = new JPanel();
		contentPanel.add(emptyPanel, "empty");

		voucherContentPanel = new VoucherDetailPanel(contr);
		voucherContentPanel.setVisible(false);
		contentPanel.add(voucherContentPanel, "Voucher");

		productDetailPanel = new ProductDetailPanel(contr);
		productDetailPanel.setVisible(false);
		contentPanel.add(productDetailPanel, "Product");
		
		updateButtons(voucherArray);
		voucherCacheMDF = voucherArray;

		JSplitPane sp = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		sp.setResizeWeight(0.3);
		sp.setEnabled(false); // Notwendig??
		sp.setDividerSize(4);

		sp.add(naviPanel);
		sp.add(contentPanel);
		add(sp, BorderLayout.CENTER);

	}

	public void updateButtons(ArrayList<?> allProductsOrVouchers) {

		dataButtonPanel.removeAll();
		dataButtonPanel.repaint();
		dataButtonPanel.revalidate();

		gbc = new GridBagConstraints();
		gbc.gridheight = 1;
		gbc.gridwidth = 1;
		gbc.weightx = 0.1;
		gbc.insets = new Insets(3, 3, 3, 3);
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = 0;

		int buttonsAdded = 0;

		for (int i = 0; i < allProductsOrVouchers.size(); i++) {

			JButton button;
			Product p = null;
			Voucher v = null;

			if (allProductsOrVouchers.get(0).getClass() == Product.class) {
				p = (Product) allProductsOrVouchers.get(i);
				button = new JButton(p.getName());
				button.setActionCommand("MasterData." + p.getName());
			} else {
				voucherCacheMDF = (ArrayList<Voucher>) allProductsOrVouchers;
				v = voucherCacheMDF.get(i);
				button = new JButton(v.getDescription());
				button.setActionCommand("MasterData." + v.getDescription());
			}

			button.addActionListener(controller);
			button.setFont(Variables.buttonAndComboFont);
			gbc.gridy = buttonsAdded;

			if (p != null && !p.isDrink() && jcb.getSelectedItem() == "Speisen") {
				buttonsAdded++;
				dataButtonPanel.add(button, gbc);
			} else if (p != null && p.isDrink() && jcb.getSelectedItem() == "Getränke") {
				buttonsAdded++;
				dataButtonPanel.add(button, gbc);
			} else if (v != null && jcb.getSelectedItem() == "Märkle") {
				buttonsAdded++;
				dataButtonPanel.add(button, gbc);
			}
		}
		
		updateDetailPageArea();

		dataButtonPanel.repaint();
		dataButtonPanel.revalidate();

	}
	
	private void showVoucherPage(){
		CardLayout cl = (CardLayout)(contentPanel.getLayout());
        cl.show(contentPanel, "Voucher");
		
	}

	public void updateDetailPageArea(Voucher voucher, boolean deletable) {

		showVoucherPage();

		voucherContentPanel.updateValues(voucher, deletable);

		revalidate();
		repaint();
	}

	public void updateDetailPageArea(Product product) {
		
		CardLayout cl = (CardLayout)(contentPanel.getLayout());
        cl.show(contentPanel, "Product");


		productDetailPanel.updateValues(product, voucherCacheMDF);

		revalidate();
		repaint();
	}
	
	public void updateDetailPageArea() {

		CardLayout cl = (CardLayout)(contentPanel.getLayout());
        cl.show(contentPanel, "empty");
        
		revalidate();
		repaint();        
		
	}

	public void showNewVoucherForm() {
		showVoucherPage();
		voucherContentPanel.showNewVoucherForm();
		revalidate();
		repaint();
		
	}

	public Voucher getNewVoucher() {
		// TODO Auto-generated method stub
		return voucherContentPanel.getNewVoucher();
	}
		

}
