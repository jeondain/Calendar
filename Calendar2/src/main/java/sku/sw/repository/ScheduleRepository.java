package sku.sw.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import sku.sw.model.Schedule;

/*
Now we can use JpaRepository’s methods: save(), findOne(), findById(), findAll(), count(), delete(), deleteById()… without implementing these methods.

We also define custom finder methods:
– findByPublished(): returns all Tutorials with published having value as input published.
– findByTitleContaining(): returns all Tutorials which title contains input title.

The implementation is plugged in by Spring Data JPA automatically.
*/
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
	
	  @Query("select  from Schedule s where s.date = :date")
	  List<Schedule> findByDate(@Param("date") LocalDate date);
	  
	  //@Query("select id, date, subject, memo from Schedule a where a.date < :todate and a.date >= :fromdate")
	  @Query("select s from Schedule s where s.date between :fromdate and  :todate")
	  List<Schedule> findAllByMonth(@Param("fromdate") LocalDate from, @Param("todate") LocalDate to);

}
