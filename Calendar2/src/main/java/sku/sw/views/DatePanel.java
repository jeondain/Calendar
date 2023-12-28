package sku.sw.views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.border.Border;

import com.slack.api.Slack;
import com.slack.api.methods.MethodsClient;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.request.chat.ChatPostMessageRequest;
import com.slack.api.methods.response.chat.ChatPostMessageResponse;

import lombok.extern.slf4j.Slf4j;
import sku.sw.dao.ScheduleDao;
import sku.sw.model.Schedule;
import sku.sw.util.SlackMessenger;

@Slf4j
public class DatePanel extends JPanel implements ActionListener {
	JPopupMenu popMenu;
	JPopupMenu popMenu2;
	
	ScheduleDao scheduleDao;
	
	JPanel eventPanel=new JPanel();
	List<Schedule> events = new ArrayList<Schedule>();
	int year;
	int month;
	int date;

	int width = 100;
	int heightOfEvent = 100;
	int heightOfDate = 20;
	
	Long selectedEvent = 0L;
	
	public DatePanel(int year, int month, int date){
		this.year = year;
		this.month = month;
		this.date = date;
		
		popMenu = new JPopupMenu();
		JMenuItem miSchedule = new JMenuItem("add Schedule");
		JMenuItem miDiary = new JMenuItem("add Diary");
		popMenu.add(miSchedule);
		popMenu.add(miDiary);
		miSchedule.addActionListener(this);
		miDiary.addActionListener(this);
		
		popMenu2 = new JPopupMenu();
		JMenuItem miViewSchedule = new JMenuItem("view");
		JMenuItem miDelSchedule = new JMenuItem("delete");
		popMenu2.add(miViewSchedule);
		popMenu2.add(miDelSchedule);
		miViewSchedule.addActionListener(this);
		miDelSchedule.addActionListener(this);
		
		
		setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
		setLayout(new BorderLayout());
		
		JLabel dateLabel = new JLabel(String.valueOf(date));
		dateLabel.setHorizontalAlignment(JLabel.CENTER);
		dateLabel.setPreferredSize(new Dimension(width,heightOfDate));
		this.add(dateLabel, BorderLayout.PAGE_START);
		this.add(eventPanel, BorderLayout.CENTER);
		
		eventPanel.setPreferredSize(new Dimension(width,heightOfEvent));
		eventPanel.setBackground(Color.WHITE);

		eventPanel.setLayout(new BoxLayout(eventPanel, BoxLayout.PAGE_AXIS));
		
		// 팝업 띄우기
		dateLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent mouseEvent) {
				log.info("{}/{}/{} clicked.", year, month, date);
				
				if (mouseEvent.getButton() == MouseEvent.BUTTON3) {
					popMenu.show(eventPanel, mouseEvent.getX(), mouseEvent.getY());
				}
			}
		});
		
		eventPanel.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent mouseEvent) {
				log.info("{}/{}/{} clicked.", year, month, date);
				
				if (mouseEvent.getButton() == MouseEvent.BUTTON3) {
					popMenu.show(eventPanel, mouseEvent.getX(), mouseEvent.getY());
				}
			}
		});
		
	}
	public void addEvent(List<Schedule> s) {
		events.addAll(s);
	}
	public void addEvent(Schedule s) {
		events.add(s);
	}
	public void removeEvent(Long id) {
		for(Schedule s: events) {
			if(s.getId() == id) {
				events.remove(s);
				break;
			}
		}
	}
	
	public void updatePanel() {
		eventPanel.removeAll();
		
		for (Schedule s : events) {
			EventLabel l = new EventLabel(s);
			l.setMaximumSize(new Dimension(120, 15));
			l.setBackground(Color.lightGray);
			eventPanel.add(l);
			eventPanel.add(Box.createRigidArea(new Dimension(0, 2)));
			
			if (s.getId() == CalendarView.getInstance().getSelectedEvent()) {
				Border blackline = BorderFactory.createLineBorder(Color.black);
				l.setBorder(blackline);

			} else {
				l.setBorder(null);
			}
			
			// 해당 날짜에 slack 전송
			if(LocalDate.now().isEqual(LocalDate.of(year, month, date))) {
				try {
					SlackMessenger.send(s.getTime() + " " + s.getSubject());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SlackApiException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			l.addMouseListener(new MouseAdapter() {
				@Override
				public void mousePressed(MouseEvent mouseEvent) {
					
					if (mouseEvent.getButton() == MouseEvent.BUTTON3) {
						popMenu2.show(l, mouseEvent.getX(), mouseEvent.getY());
					} else {
						log.info("{} {} clicked.", s.getId(), s.getSubject());	
	
						CalendarView.getInstance().setSelectedEvent(s.getId());
						
						eventPanel.revalidate();
						eventPanel.repaint();
					}
					CalendarView.getInstance().updateCalendar();
				}
			});
		}
		
		eventPanel.revalidate();
		eventPanel.repaint();
		this.updateUI();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		
		selectedEvent = CalendarView.getInstance().getSelectedEvent();
		
		if(cmd.equals("delete")) {
	        ScheduleDao.getInstance().delete(selectedEvent);
			System.out.println(cmd + " deleted");
			
			removeEvent(selectedEvent);
			CalendarView.getInstance().updateCalendar();
		}
		else if(cmd.equals("view")) {
			 new ScheduleView(CalendarView.getInstance(), this, year, month, date, selectedEvent);
		}
		else if(cmd.equals("add Schedule")) {
	        new ScheduleDialog(CalendarView.getInstance(), this, year, month, date);
		}
		else if(cmd.equals("add Diary")) {
            new DiaryDialog(CalendarView.getInstance(), this, year, month, date);
		}
	}

}