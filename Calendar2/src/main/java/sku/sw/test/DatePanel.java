package sku.sw.test;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;

import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import sku.sw.model.Schedule;

@Slf4j
public class DatePanel extends JPanel{
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
		
		setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
		setLayout(new BorderLayout());
		
		JLabel dateLabel = new JLabel(String.valueOf(date));
		dateLabel.setHorizontalAlignment(JLabel.CENTER);
		dateLabel.setPreferredSize(new Dimension(width,heightOfDate));
		this.add(dateLabel, BorderLayout.PAGE_START);
		this.add(eventPanel, BorderLayout.CENTER);
		
		eventPanel.setPreferredSize(new Dimension(width,heightOfEvent));
		eventPanel.setBackground(Color.CYAN);

		eventPanel.setLayout(new BoxLayout(eventPanel, BoxLayout.PAGE_AXIS));
		
		eventPanel.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent mouseEvent) {
				log.info("{}/{}/{} clicked.", year, month, date);
				
				// 선택된 날짜에 스케쥴 이벤트를 추가
				addEvent(Schedule.builder().id(Long.valueOf(events.size()+1)).subject("test " + (events.size()+1)).build());
				
				updatePanel();
				//eventPanel.updateUI();
			}
		});
		
		addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent mouseEvent) {
				log.info("{}/{}/{} date selected.", year, month, date);

			}
		});
	}
	public void addEvent(List<Schedule> s) {
		events.addAll(s);
	}
	public void addEvent(Schedule s) {
		events.add(s);
	}
	public void removeEvent(Schedule s) {
		events.remove(s);
	}
	
	public void setSelection(Long id) {
		for(Schedule s: events) {
			if(s.getId() == id) {
				
			}
		}
	}
	
	public void updatePanel() {
		eventPanel.removeAll();
		
		for(Schedule s: events) {
			EventLabel l = new EventLabel(s);
			eventPanel.add(l);
			

			l.addMouseListener(new MouseAdapter() {
				@Override
				public void mousePressed(MouseEvent mouseEvent) {
					log.info("{} {} clicked.", s.getId(), s.getSubject());
					
					selectedEvent = s.getId();
					
					// selected 된 스케쥴 이벤트의 Border 표시
					for(Component c : eventPanel.getComponents()) {
						if(c instanceof EventLabel) {
							EventLabel a = (EventLabel)c;
							if(a.getS().getId() == selectedEvent) {
								Border blackline = BorderFactory.createMatteBorder(1, 1, 0, 0, Color.black);
										//.createLineBorder(Color.black);
								
								
								a.setBorder(blackline);
							}
							else {
								a.setBorder(null);
							}
						}
					}
					
					eventPanel.revalidate();
					eventPanel.repaint();
//					l.revalidate();
//					l.repaint();
//					l.updateUI();
				}
			});
		}
		
		eventPanel.revalidate();
		eventPanel.repaint();
	}
}
