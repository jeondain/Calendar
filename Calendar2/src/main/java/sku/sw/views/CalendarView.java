package sku.sw.views;

import javax.swing.*;
import javax.swing.border.Border;


import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.Calendar;

import lombok.extern.slf4j.Slf4j;
import sku.sw.components.Button;
import sku.sw.dao.ScheduleDao;
import sku.sw.model.Schedule;

@Slf4j
//@Component
public class CalendarView extends JFrame {
//	@Autowired
//	ScheduleController scheduleController;
	
	ScheduleDao scheduleDao;
	
    private JLabel monthYearLabel;
    private JPanel calendarPanel;
    private Calendar currentCalendar;
    private JButton diaryButton;
    private JButton prevButton;
    private JButton nextButton;
    
    public CalendarView() {
        setTitle("캘린더");
        setSize(850, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(Color.WHITE);
        prevButton = new Button("<", Color.WHITE, Color.BLACK);
        nextButton = new Button(">", Color.WHITE, Color.BLACK);
        diaryButton = new Button("Diary", Color.WHITE, Color.BLACK);
        diaryButton.setFont(new Font("Arial", Font.BOLD, 15));
        monthYearLabel = new JLabel("", SwingConstants.CENTER);

        prevButton.addActionListener(new ActionListener() { // 이전 달 캘린더로 이동
            @Override
            public void actionPerformed(ActionEvent e) {
                currentCalendar.add(Calendar.MONTH, -1);
                updateCalendar();
            }
        });

        nextButton.addActionListener(new ActionListener() { // 다음 달 캘린더로 이동
            @Override
            public void actionPerformed(ActionEvent e) {
                currentCalendar.add(Calendar.MONTH, 1);
                updateCalendar();
            }
        });

        diaryButton.addActionListener(new ActionListener() { // 다이어리 모음 창으로 이동
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

        headerPanel.add(prevButton);
        headerPanel.add(monthYearLabel);
        headerPanel.add(nextButton);
        headerPanel.add(diaryButton);
        add(headerPanel, BorderLayout.NORTH);

        calendarPanel = new JPanel(new GridLayout(0, 7));
        calendarPanel.setBackground(Color.WHITE);
        add(calendarPanel, BorderLayout.CENTER);

        currentCalendar = Calendar.getInstance();
        
        
        scheduleDao = ScheduleDao.getInstance();
        
        //updateCalendar();

        //log.info("hi {}", this.getClass());
    }

    public void updateCalendar() {
        // 현재 달력 패널을 지우고 새로 고침
        calendarPanel.removeAll();
        calendarPanel.revalidate();
        calendarPanel.repaint();

        // 현재 연도와 월 가져오기
        int year = currentCalendar.get(Calendar.YEAR);
        int month = currentCalendar.get(Calendar.MONTH) + 1;
        // 연도와 월 표시 라벨 업데이트
        monthYearLabel.setText(year + "년 " + month + "월");
        monthYearLabel.setFont(new Font("맑은 고딕", Font.BOLD, 25));

        // 달의 첫 날의 요일과 해당 월의 총 날짜 수 계산
        Calendar cal = (Calendar) currentCalendar.clone();
        cal.set(Calendar.DAY_OF_MONTH, 1);
        int firstDayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
        int daysInMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH);

        String[] daysOfWeek = {"일", "월", "화", "수", "목", "금", "토"};
        for (String day : daysOfWeek) {
            JLabel daysOfWeekLabel = new JLabel(day, SwingConstants.CENTER);
            calendarPanel.add(daysOfWeekLabel);
        }
        
        for (int i = 1; i < firstDayOfWeek; i++) { // 첫 주 시작일 전까지 빈 라벨 추가하여 간격 맞추기
            calendarPanel.add(new JLabel(""));
        }
        
//        Schedule s1 = Schedule.builder().date(LocalDate.now()).time(LocalTime.now()).subject("test122").memo("memo122").build();
//        scheduleController.save(s1);

        daysInMonth = YearMonth.of(year, month).atEndOfMonth().getDayOfMonth();
        
        for (int i = 1; i <= daysInMonth; i++) { // 해당 월의 날짜 수만큼 반복하여 달력에 날짜 표시
//            JPanel dayPanel = new JPanel(new BorderLayout()); // 날짜 패널
            DatePanel dayPanel = new DatePanel(year, month, i);
            dayPanel.setBackground(Color.WHITE);
            Border blackline = BorderFactory.createMatteBorder(1, 1, 0, 0, Color.black);
            dayPanel.setBorder(blackline);
            JLabel dayLabel = new JLabel(String.valueOf(i), SwingConstants.CENTER);
            dayPanel.add(dayLabel, BorderLayout.NORTH);
            //Schedule s1 = scheduleController.read(1);

            java.util.List<Schedule> list = scheduleDao.findByDate(year, month, i);
            
            dayPanel.addEvent(list);
            
            int day = i;
            dayPanel.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    String selectedDate = year + "/" + month + "/" + day ;
                    openOptionsDialog(dayPanel, year, month, day); // 선택 날짜 다이얼로그  열기
                }
            });
            calendarPanel.add(dayPanel); // 날짜 패널을 달력 패널에 추가
            dayPanel.updatePanel();
        }
    }

    private void openOptionsDialog(DatePanel dayPanel, int year, int month, int day) {
        String[] options = {"스케줄 입력", "다이어리 입력"};
        int choice = JOptionPane.showOptionDialog(
                calendarPanel,
                null,
                "Options",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                options,
                options[0]);

        if (choice == 0) {
            new ScheduleDialog(this, dayPanel, year, month, day);

        } else if (choice == 1) {
            new DiaryDialog(this, year, month, day);
        }
        

    }
}
