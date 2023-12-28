package sku.sw.views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Calendar;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import com.ibm.icu.impl.duration.TimeUnit;
import com.slack.api.Slack;
import com.slack.api.methods.MethodsClient;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.request.chat.ChatPostMessageRequest;
import com.slack.api.methods.response.chat.ChatPostMessageResponse;

import lombok.extern.slf4j.Slf4j;
import sku.sw.dao.DiaryDao;
import sku.sw.dao.ScheduleDao;
import sku.sw.model.Schedule;

@Slf4j
public class CalendarView extends JFrame {
	private static CalendarView instance;
	ScheduleDao scheduleDao;
	
    private JLabel monthYearLabel;
    private JPanel calendarPanel;
    private Calendar currentCalendar;
    private JButton diaryButton;
    private JButton prevButton;
    private JButton nextButton;
	Long selectedEvent = 0L;
	int year;
	int month;
	LocalDate now;
	
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
        
        now = LocalDate.now();
    }
    
	// 싱글톤 패턴으로 CalendarView의 인스턴스 반환
	public static CalendarView getInstance() {
		if (instance == null) {
			instance = new CalendarView();
		}
		return instance;
	}

    public void addActionListener() {
    	// 이전 달 캘린더로 이동
    	prevButton.addActionListener(new ActionListener() { 
            @Override
            public void actionPerformed(ActionEvent e) {
            	now = now.minusMonths(1);
                updateCalendar();
            }
        });

    	// 다음 달 캘린더로 이동
        nextButton.addActionListener(new ActionListener() { 
            @Override
            public void actionPerformed(ActionEvent e) {
                now = now.plusMonths(1);
                updateCalendar();
            }
        });

        // 다이어리 목록 창 띄우기
        diaryButton.addActionListener(new ActionListener() { 
            @Override
            public void actionPerformed(ActionEvent e) {
                DiaryDao diaryDao = DiaryDao.getInstance();
                updateCalendar();
                new DiaryList(CalendarView.getInstance(), diaryDao, now.getYear(), now.getMonthValue());
            }
        });
    }

    public void updateCalendar() {
    	// 현재 달력 패널을 지우고 새로 고침
        calendarPanel.removeAll();
        calendarPanel.revalidate();
        calendarPanel.repaint();

        // 연도와 월 표시 라벨 업데이트
        year = now.getYear();
        month = now.getMonthValue();
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
        
        // 첫 주 시작일 전까지 빈 라벨 추가하여 간격 맞추기
        for (int i = 1; i < firstDayOfWeek; i++) { 
        	JLabel emptyLabel = new JLabel("");
            calendarPanel.add(emptyLabel);
        }
     
        daysInMonth = YearMonth.of(year, month).atEndOfMonth().getDayOfMonth();
        
        // 해당 월의 날짜 수만큼 반복하여 달력에 날짜 표시
        for (int i = 1; i <= daysInMonth; i++) { 
            DatePanel datePanel = new DatePanel(year, month, i);
            datePanel.setBackground(Color.WHITE);
        	Border blackline = BorderFactory.createEtchedBorder();
            datePanel.setBorder(blackline);
            JLabel dateLabel = new JLabel(String.valueOf(i), SwingConstants.CENTER);
            datePanel.add(dateLabel, BorderLayout.NORTH);

            java.util.List<Schedule> list = scheduleDao.findByDate(year, month, i);
            datePanel.addEvent(list);
            
            calendarPanel.add(datePanel); 
            datePanel.updatePanel();
        }
    }
    
    public Long getSelectedEvent() {
		return selectedEvent;
	}

	public void setSelectedEvent(Long selectedEvent) {
		this.selectedEvent = selectedEvent;
	}

}
