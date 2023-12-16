package sku.sw.views;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;


import lombok.extern.slf4j.Slf4j;
import sku.sw.dao.ScheduleDao;
import sku.sw.model.Schedule;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
public class ScheduleDialog extends JDialog {

	ScheduleDao scheduleDao;
	
	
    private String selectedDate;
    private JTextArea subjectTextArea;
    private JTextArea scheduleTextArea;

    public ScheduleDialog(JFrame parent, DatePanel dayPanel, int year, int month, int day) {
        super(parent, "스케줄 입력", true);
        this.selectedDate = selectedDate;
        
        this.scheduleDao = ScheduleDao.getInstance();
        JPanel schedulePanel = new JPanel(new BorderLayout());
        JPanel inputPanel = new JPanel(new BorderLayout());
        JLabel dateLabel = new JLabel("선택한 날짜: " + year + "/" + month + "/" + day);
        String [] selectableTime= {"00:00","1:00","2:00","3:00","4:00","5:00","6:00","7:00","8:00","9:00","10:00","11:00","12:00",
                "13:00","14:00","15:00","16:00","17:00","18:00","19:00","20:00","21:00","22:00","23:00"};
        JComboBox combo = new JComboBox(selectableTime); 
        subjectTextArea = new JTextArea(1, 30);
        subjectTextArea.setBorder(new LineBorder(Color.BLACK)); 
        scheduleTextArea = new JTextArea(10, 30);
        scheduleTextArea.setBorder(new LineBorder(Color.BLACK)); 
        JButton saveButton = new JButton("저장");

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String subject = subjectTextArea.getText();
//        		LocalDate date = LocalDate.of(year, month, day);
                String schedule = scheduleTextArea.getText();
                
                String selectedTime = (String)combo.getSelectedItem();
//                String[] hm = selectedTime.split(":");
//                int hour = Integer.parseInt(hm[0]);
//                int min = Integer.parseInt(hm[1]);

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("H:mm");
                LocalTime time = LocalTime.parse(selectedTime, formatter);
                
//                LocalTime time = LocalTime.of(hour, min);
                
              Schedule s1 = Schedule.builder().date(LocalDate.of(year, month, day)).time(time).subject(subject).memo(schedule).build();

              scheduleDao.save(s1);
              
              
              List<Schedule> list = scheduleDao.findAllByMonth(year, month);

	      		for(Schedule a:list) {
	      			log.info("{} {} {} [{}]", a.getId(), a.getDate(), a.getTime(), a.getSubject());
	      		}
                // 여기서 스케줄을 저장하거나 처리
                // 데이터베이스에 저장하거나 다른 곳에 보관
	      		
	      		dayPanel.addEvent(s1);
	      		dayPanel.updatePanel();
                dispose();
                
                
            }
        });
        
        inputPanel.add(subjectTextArea, BorderLayout.NORTH);
        inputPanel.add(combo, BorderLayout.CENTER);
        inputPanel.add(scheduleTextArea, BorderLayout.SOUTH);

        schedulePanel.add(dateLabel, BorderLayout.NORTH);
        schedulePanel.add(inputPanel, BorderLayout.CENTER);
        schedulePanel.add(saveButton, BorderLayout.SOUTH);

        getContentPane().add(schedulePanel);
        pack(); // 컴포넌트 크기에 맞게 다이얼로그 크기 조정
        setLocationRelativeTo(parent); // 부모 프레임을 기준으로 다이얼로그 위치 조정
        setVisible(true);
    }
}
