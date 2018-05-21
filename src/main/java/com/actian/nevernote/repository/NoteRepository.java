package com.actian.nevernote.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import com.actian.nevernote.model.Note;


public interface NoteRepository extends CrudRepository<Note, Long>, Repository<Note, Long> {

	/**
	 * Find list of Notes by Tag
	 * @param tag The Tag to filter by
	 * @return
	 */
	@Query("SELECT n FROM Note n INNER JOIN n.tags t where UPPER(t) = :tag")
	List<Note> findByTag(@Param("tag") String tag);

}
