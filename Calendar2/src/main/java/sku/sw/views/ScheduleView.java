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
public class ScheduleView extends JDialog {

	ScheduleDao scheduleDao;

	private String selectedDate;
	private JTextArea subjectTextArea;
	private JTextArea scheduleTextArea;

	public ScheduleView(JFrame parent, DatePanel datePanel, int year, int month, int date, Long id) {
		super(parent, "스케줄 입력", true);
		this.selectedDate = selectedDate;

		this.scheduleDao = ScheduleDao.getInstance();
		Schedule s = scheduleDao.find(id);

		JPanel schedulePanel = new JPanel(new BorderLayout());
		JPanel inputPanel = new JPanel(new BorderLayout());
		JLabel dateLabel = new JLabel("선택한 날짜: " + year + "/" + month + "/" + date);
		String[] selectableTime = { "00:00", "1:00", "2:00", "3:00", "4:00", "5:00", "6:00", "7:00", "8:00", "9:00",
				"10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00", "19:00", "20:00",
				"21:00", "22:00", "23:00" };

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("H:mm");
		String formattedLocalTime = s.getTime().format(formatter);

		JComboBox combo = new JComboBox(selectableTime);
		combo.setSelectedItem(formattedLocalTime);
		subjectTextArea = new JTextArea(1, 30);
		subjectTextArea.setText(s.getSubject());
		subjectTextArea.setBorder(new LineBorder(Color.BLACK));
		scheduleTextArea = new JTextArea(10, 30);
		scheduleTextArea.setText(s.getMemo());
		scheduleTextArea.setBorder(new LineBorder(Color.BLACK));
		JButton saveButton = new JButton("저장");

		saveButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String subject = subjectTextArea.getText();
				String schedule = scheduleTextArea.getText();

				String selectedTime = (String) combo.getSelectedItem();

				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("H:mm");
				LocalTime time = LocalTime.parse(selectedTime, formatter);

				Schedule s = Schedule.builder().id(id).date(LocalDate.of(year, month, date)).time(time).subject(subject)
						.memo(schedule).build();

				scheduleDao.updateSchedule(s);

				datePanel.removeEvent(id);
				datePanel.addEvent(s);
				datePanel.updatePanel();
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
