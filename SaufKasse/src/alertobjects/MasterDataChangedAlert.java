package alertobjects;

import java.util.ArrayList;

import beans.Product;
import beans.Voucher;

public class MasterDataChangedAlert {

	private ArrayList<Product> productList;
	private ArrayList<Voucher> voucherList;
	
	public MasterDataChangedAlert(ArrayList<Product> pList, ArrayList<Voucher> vList) {
		
		productList = pList;
		voucherList = vList;
	}

	public ArrayList<Product> getProductList() {
		return productList;
	}

	public ArrayList<Voucher> getVoucherList() {
		return voucherList;
	}
	
	

}
