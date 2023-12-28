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

import sku.sw.model.Diary;
import sku.sw.model.Schedule;

public class DiaryDao {

	private EntityManager entityManager;

	private static DiaryDao instance;

	// 싱글톤 패턴으로 DiaryDao의 인스턴스 반환
	public static DiaryDao getInstance() {
		if (instance == null) {
			instance = new DiaryDao();

			EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("diary");

			instance.entityManager = entityManagerFactory.createEntityManager();
		}
		return instance;
	}

	public void save(Diary d) {
		EntityTransaction tx = entityManager.getTransaction();
		try {
			tx.begin();
			entityManager.persist(d);
			tx.commit();
		} catch (RuntimeException e) {
			tx.rollback();
			throw e;
		}
	}

	public void save(List<Diary> diarys) {
		EntityTransaction tx = entityManager.getTransaction();
		try {
			tx.begin();
			for (Diary d : diarys) {
				entityManager.persist(d);
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

			updatedCount = entityManager.createQuery("delete from Diary where id = :id").setParameter("id", id)
					.executeUpdate();

			tx.commit();
		} catch (RuntimeException e) {
			tx.rollback();
			throw e;
		}

		return updatedCount;
	}

	public int delete(Diary d) {

		int updatedCount = 0;
		EntityTransaction tx = entityManager.getTransaction();
		try {
			tx.begin();

			entityManager.remove(d);

			tx.commit();
		} catch (RuntimeException e) {
			tx.rollback();
			throw e;
		}

		return updatedCount;
	}

	public Diary find(Long id) {
		return entityManager.find(Diary.class, id);
	}

	public List<Diary> findAllByMonth(int year, int month) {
		int lastDate = YearMonth.of(year, month).atEndOfMonth().getDayOfMonth();

		LocalDate from = LocalDate.of(year, month, 1);
		LocalDate to = LocalDate.of(year, month, lastDate);

		TypedQuery<Diary> query = entityManager
				.createQuery("select s from Diary s where s.date between :fromdate and  :todate", Diary.class);

		List<Diary> list = query.setParameter("fromdate", from).setParameter("todate", to).getResultList();

		return list;
	}
	
	public List<Diary> findAll() {
		TypedQuery<Diary> query = entityManager.createQuery("SELECT d FROM Diary d", Diary.class);
		return query.getResultList();
	}

	public void updateDiary(Diary d) {
		EntityTransaction tx = entityManager.getTransaction();
		try {
			tx.begin();
			entityManager.merge(d);
			tx.commit();
		} catch (RuntimeException e) {
			tx.rollback();
			throw e;
		}

	}
}
