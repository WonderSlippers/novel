package com.homework.web.repository;

import com.homework.web.pojo.Collection;
import com.homework.web.pojo.History;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface HistoryRepository extends JpaRepository<History, Integer> {
	@Query(value = "select * from history where user_id = :user_id and novel_id=:novel_id", nativeQuery = true)
	Collection selectByUser_idNovel_id(@Param(value = "user_id") Integer user_id,
			@Param(value = "novel_id") Integer novel_id);

	@Query(value = "select * from history where novel_id=:novel_id", nativeQuery = true)
	List<Collection> selectByNovel_id(@Param(value = "novel_id") Integer novel_id);

	@Query(value = "select * from history where user_id=:user_id", nativeQuery = true)
	List<Collection> selectByUser_id(@Param(value = "user_id") Integer user_id);


}
