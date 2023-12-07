package sku.sw;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;

import lombok.extern.slf4j.Slf4j;
import sku.sw.controller.ScheduleController;
import sku.sw.model.Schedule;
import sku.sw.repository.ScheduleRepository;


@Slf4j
@SpringBootTest
class CalendarApplicationTests {

	static {
		System.setProperty("java.awt.headless", "false");
	}
	@Autowired
	ScheduleController sController;
	
	@Autowired
	ScheduleRepository scheduleRepository;
	
	@Test
	void contextLoads() {
	}

	
	@Test
	void testInsertSchedule() {
		int year = 2023;
		int month = 11;
		
		int lastDate = YearMonth.of(year, month).atEndOfMonth().getDayOfMonth();
				
		for (int i=1; i<=lastDate; i++) {
			Schedule s1 = Schedule.builder().date(LocalDate.of(year, month, i)).time(LocalTime.of(9, 0)).subject("test1 " + month + "/" + i).memo("memo1 - " + month + "/" + i).build();
			scheduleRepository.save(s1);
		}
	}
	
	
	@Test
	void testH2() {
//		Schedule s1 = Schedule.builder().date(LocalDate.now()).subject("test1").memo("memo1").build();
//		scheduleRepository.save(s1);
		
		List<Schedule> list = scheduleRepository.findAll();
		for(Schedule a:list) {
			log.info("{} {} {}", a.getId(), a.getDate(), a.getSubject());
		}
		
		Optional<Schedule> result = scheduleRepository.findById(5L);
		Schedule s3 = result.get();
		
		log.info("{},{}", s3.getId(), s3.getSubject());
		
		int year = 2023;
		int month = 11;
		int date = 25;
		
		int lastDate = YearMonth.of(year, month).atEndOfMonth().getDayOfMonth();
		
		LocalDate from = LocalDate.of(year, month, 1);
		LocalDate to = LocalDate.of(year, month, lastDate);
		
		List<Schedule> listByMonth = scheduleRepository.findAllByMonth(from, to);
		for(Schedule a:list) {
			log.info("monthly - {} {} {}", a.getId(), a.getDate(), a.getSubject());
		}
		
	}
	@Test
	void testScheduleController() {
		int year = 2023;
		int month = 10;
		List<Schedule> list = sController.findAllByMonth(year, month);

		for(Schedule a:list) {
			log.info("{} {} {} [{}]", a.getId(), a.getDate(), a.getTime(), a.getSubject());
		}
	}
	
	@Test
	void testDeleteSchedule() {
		int year = 2023;
		int month = 11;
		sController.delete(Long.valueOf(15));

		List<Schedule> list = sController.findAllByMonth(year, month);

		for(Schedule a:list) {
			log.info("{} {} {} [{}]", a.getId(), a.getDate(), a.getTime(), a.getSubject());
		}
	}
	
	@Test
	void testSchedulefindByDate() {
		int year = 2023;
		int month = 12;
		int day = 1;
		List<Schedule> list = sController.findByDate(year, month, day);

		for(Schedule a:list) {
			log.info("*{} {} {} [{}]", a.getId(), a.getDate(), a.getTime(), a.getSubject());
		}
	}
	
	
}
