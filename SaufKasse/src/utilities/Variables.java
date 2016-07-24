package utilities;

import java.awt.Font;
import java.text.DecimalFormat;

public class Variables {

	
	public static Font buttonAndComboFont = new Font("Tahoma", Font.BOLD, 17); 
	public static Font menuFont = new Font("Tahoma", Font.PLAIN, 10); 
	
	public static final int productDepositID = 18;
	public static final int productDepositReturnID = 19;
	
	public static final int voucherDepositID = 8;
	public static final int voucherDepositReturnID = 9;
	
	public static final int secondVoucherMittag = 7;
	
	public static final DecimalFormat moneyFormatter = new DecimalFormat("#0.00 €");
}
