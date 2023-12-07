package sku.sw.views;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;

public class DiaryDialog extends JDialog {
    private String selectedDate;
    private JTextArea subjectTextArea;
    private JTextArea diaryTextArea;

    public DiaryDialog(JFrame parent, int year, int month, int day) {
        super(parent, "다이어리 입력", true);
        this.selectedDate = selectedDate;
        JPanel diaryPanel = new JPanel(new BorderLayout());
        JPanel inputPanel = new JPanel(new BorderLayout());
        JLabel dateLabel = new JLabel("선택한 날짜: " + selectedDate);
        JButton chooseFileButton = new JButton("파일 선택");
        subjectTextArea = new JTextArea(1, 30);
        subjectTextArea.setBorder(new LineBorder(Color.BLACK));
        diaryTextArea = new JTextArea(10, 30);
        diaryTextArea.setBorder(new LineBorder(Color.BLACK));
        JButton saveButton = new JButton("저장");

        chooseFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	LocalDate date = LocalDate.of(year, month, day);
                JFileChooser fileChooser = new JFileChooser();

                // 파일 선택 대화 상자를 열고 선택된 파일 가져오기
                int returnValue = fileChooser.showOpenDialog(null);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    java.io.File selectedFile = fileChooser.getSelectedFile();
                    String filePath = selectedFile.getAbsolutePath();
                }
            }
        });

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String diary = diaryTextArea.getText();
                // 여기서 다이어리 저장하거나 처리
                // 데이터베이스에 저장하거나 다른 곳에 보관
                dispose();
            }
        });

        inputPanel.add(subjectTextArea, BorderLayout.NORTH);
        inputPanel.add(chooseFileButton, BorderLayout.CENTER);
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
