package sku.sw.views;

import javax.swing.*;
import javax.swing.border.LineBorder;

import sku.sw.dao.DiaryDao;
import sku.sw.dao.ScheduleDao;
import sku.sw.model.Diary;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class DiaryDialog extends JDialog {
	
	DiaryDao diaryDao;

    private String selectedDate;
    private JTextArea subjectTextArea;
    private JTextArea diaryTextArea;

    public DiaryDialog(JFrame parent, DatePanel datePanel, int year, int month, int date) {
        super(parent, "다이어리 입력", true);
        this.selectedDate = selectedDate;
        
        this.diaryDao = DiaryDao.getInstance();
        JPanel diaryPanel = new JPanel(new BorderLayout());
        JPanel inputPanel = new JPanel(new BorderLayout());
        JLabel dateLabel = new JLabel("선택한 날짜: " + year + "/" + month  + "/" + date);
        subjectTextArea = new JTextArea(1, 30);
        subjectTextArea.setBorder(new LineBorder(Color.BLACK));
        diaryTextArea = new JTextArea(10, 30);
        diaryTextArea.setBorder(new LineBorder(Color.BLACK));
        JButton saveButton = new JButton("저장");

        saveButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String subject = subjectTextArea.getText();
				String diary = diaryTextArea.getText();


				Diary d = Diary.builder().date(LocalDate.of(year, month, date)).subject(subject)
						.memo(diary).build();

				diaryDao.save(d);
				System.out.println("다이어리 저장");

				datePanel.updatePanel();
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


