package sku.sw;

import javax.swing.SwingUtilities;

import sku.sw.views.CalendarView;

public class CalendarApp {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
        	CalendarView calendar = CalendarView.getInstance();
        	calendar.addActionListener();
        	calendar.updateCalendar();
        	calendar.setVisible(true);
	}

}
