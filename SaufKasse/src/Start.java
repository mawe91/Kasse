import javax.swing.UIManager;

import controller.Controller;
import db.DBHandler;
import model.Model;
import ui.view.View;

public class Start {

	public static void main(String[] args) {

		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
		} catch (Exception e) {
			e.printStackTrace();
		}

		DBHandler dbh = new DBHandler();
		Model model = new Model(dbh);
		

		Controller controller = new Controller(model);
		
		View view = new View(controller);
		controller.addViewToController(view);

	}

}
