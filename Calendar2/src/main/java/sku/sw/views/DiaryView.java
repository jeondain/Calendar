package sku.sw.views;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;

import sku.sw.dao.DiaryDao;
import sku.sw.dao.ScheduleDao;
import sku.sw.model.Diary;
import sku.sw.model.Schedule;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class DiaryView extends JDialog {
	
	DiaryDao diaryDao;

    private String selectedDate;
    private JTextArea subjectTextArea;
    private JTextArea diaryTextArea;

    public DiaryView(JFrame parent, int year, int month, int date, Long id, DefaultTableModel model, int selectedRow) {
        super(parent, "다이어리 입력", true);
        this.selectedDate = selectedDate;
        
        this.diaryDao = DiaryDao.getInstance();
        Diary d = diaryDao.find(id);
        
        JPanel diaryPanel = new JPanel(new BorderLayout());
        JPanel inputPanel = new JPanel(new BorderLayout());
        JLabel dateLabel = new JLabel("선택한 날짜: " + year + "/" + month + "/" + date);
        subjectTextArea = new JTextArea(1, 30);
        subjectTextArea.setBorder(new LineBorder(Color.BLACK));
		subjectTextArea.setText(d.getSubject());
        diaryTextArea = new JTextArea(10, 30);
        diaryTextArea.setBorder(new LineBorder(Color.BLACK));
        diaryTextArea.setText(d.getMemo());
        JButton saveButton = new JButton("저장");

        saveButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String subject = subjectTextArea.getText();
				String diary = diaryTextArea.getText();


				Diary d = Diary.builder().id(id).date(LocalDate.of(year, month, date)).subject(subject)
						.memo(diary).build();

				diaryDao.updateDiary(d);
				
				model.setValueAt(subject, selectedRow, 2);
				
				dispose();
			}
		});

        inputPanel.add(subjectTextArea, BorderLayout.NORTH);
        inputPanel.add(diaryTextArea, BorderLayout.SOUTH);

        diaryPanel.add(dateLabel, BorderLayout.NORTH);
        diaryPanel.add(inputPanel, BorderLayout.CENTER);
        diaryPanel.add(saveButton, BorderLayout.SOUTH);

        getContentPane().add(diaryPanel);
        pack(); // 컴포넌트 크기에 맞게 다이얼로그 크기 조정
        setLocationRelativeTo(parent); // 부모 프레임을 기준으로 다이얼로그 위치 조정
        setVisible(true);
    }
}


