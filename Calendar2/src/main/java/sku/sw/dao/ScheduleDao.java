package sku.sw.dao;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import sku.sw.model.Schedule;

public class ScheduleDao {

	private EntityManager entityManager;

	private static ScheduleDao instance;

	// 싱글톤 패턴으로 ScheduleDao의 인스턴스 반환
	public static ScheduleDao getInstance() {
		if (instance == null) {
			instance = new ScheduleDao();

			EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("schedule");

			instance.entityManager = entityManagerFactory.createEntityManager();
		}
		return instance;
	}

	public void save(Schedule s) {	
		EntityTransaction tx = entityManager.getTransaction();
		try {
			tx.begin();
			entityManager.persist(s);
			tx.commit();
		} catch (RuntimeException e) {
			tx.rollback();
			throw e;
		}
	}

	public void save(List<Schedule> schedules) {
		EntityTransaction tx = entityManager.getTransaction();
		try {
			tx.begin();
			for (Schedule s : schedules) {
				entityManager.persist(s);
			}
			tx.commit();
		} catch (RuntimeException e) {
			tx.rollback();
			throw e;
		}
	}

	public int delete(Long id) {
		int updatedCount = 0;
		EntityTransaction tx = entityManager.getTransaction();
		try {
			tx.begin();

			updatedCount = entityManager.createQuery("delete from Schedule where id = :id").setParameter("id", id).executeUpdate();

			tx.commit();
		} catch (RuntimeException e) {
			tx.rollback();
			throw e;
		}

		return updatedCount;
	}

	public int delete(Schedule s) {
		
			int updatedCount = 0;
			EntityTransaction tx = entityManager.getTransaction();
			try {
				tx.begin();

				entityManager.remove(s);

				tx.commit();
			} catch (RuntimeException e) {
				tx.rollback();
				throw e;
			}

			return updatedCount;
	}

	public Schedule find(Long id) {
		return entityManager.find(Schedule.class, id);
	}

	public List<Schedule> findAll() {
		TypedQuery<Schedule> query = entityManager.createQuery("SELECT s FROM Schedule s", Schedule.class);
		return query.getResultList();
	}

	public List<Schedule> findAllByMonth(int year, int month) {
		int lastDate = YearMonth.of(year, month).atEndOfMonth().getDayOfMonth();

		LocalDate from = LocalDate.of(year, month, 1);
		LocalDate to = LocalDate.of(year, month, lastDate);

		TypedQuery<Schedule> query = entityManager
				.createQuery("select s from Schedule s where s.date between :fromdate and  :todate", Schedule.class);

		List<Schedule> list = query.setParameter("fromdate", from).setParameter("todate", to).getResultList();

		return list;
	}

	public List<Schedule> findByDate(int year, int month, int date) {
		LocalDate dt = LocalDate.of(year, month, date);

		TypedQuery<Schedule> query = entityManager.createQuery("select s from Schedule s where s.date = :dt",
				Schedule.class);

		List<Schedule> list = query.setParameter("dt", dt).getResultList();

		return list;
	}

	public void updateSchedule(Schedule s) {
		EntityTransaction tx = entityManager.getTransaction();
		try {
			tx.begin();
			entityManager.merge(s);
			tx.commit();
		} catch (RuntimeException e) {
			tx.rollback();
			throw e;
		}
		
	}
}
