package sku.sw.controller;

import lombok.extern.slf4j.Slf4j;
import sku.sw.model.Schedule;
import sku.sw.repository.ScheduleRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j // 로깅 프레임워크
@Service
public class ScheduleController{
	@Autowired
	ScheduleRepository scheduleRepository;
	
    public void save(Schedule s){
    	scheduleRepository.save(s);
    }
    public void save(List<Schedule> schedules){
    	scheduleRepository.saveAll(schedules);
    }
    
    public void delete(Long id) {
    	scheduleRepository.deleteById(id);
    }
    public void delete(Schedule s) {
    	scheduleRepository.deleteById(s.getId());
    }
    
    public Schedule find(Long id) {
    	return scheduleRepository.findById(id).get();
    }
    public List<Schedule> findAll() {
    	return scheduleRepository.findAll();
    }
    
    public List<Schedule> findAllByMonth(int year, int month){
		int lastDate = YearMonth.of(year, month).atEndOfMonth().getDayOfMonth();
		
		LocalDate from = LocalDate.of(year, month, 1);
		LocalDate to = LocalDate.of(year, month, lastDate);

		return scheduleRepository.findAllByMonth(from, to);
    }

    public void updateSchedule(Schedule s) {
    	Optional<Schedule> schedule = scheduleRepository.findById(s.getId());
    	if(schedule.isPresent()) {
    		schedule.get().setSubject(s.getSubject());
    		schedule.get().setMemo(s.getMemo());
    		schedule.get().setDate(s.getDate());
    		schedule.get().setTime(s.getTime());
    	}
    	else {
    		scheduleRepository.save(s);
    	}
    }

	public List<Schedule> findByDate(int year, int month, int day) {
		// TODO Auto-generated method stub
		return scheduleRepository.findByDate(LocalDate.of(year, month, day));
	}

}
