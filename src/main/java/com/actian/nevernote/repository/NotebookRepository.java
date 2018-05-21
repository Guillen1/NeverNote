package com.actian.nevernote.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import com.actian.nevernote.model.Note;
import com.actian.nevernote.model.Notebook;

import java.util.List;


public interface NotebookRepository extends CrudRepository<Notebook, Long>, Repository<Notebook, Long> {

	/**
	 * Find list of Notes by Notebook Id and Tag
	 * @param id The Notebook Id to select Notes
	 * @param tag The Tag to filter by
     * @return
     */
	@Query("SELECT n FROM Notebook nb INNER JOIN nb.notes n INNER JOIN n.tags t WHERE UPPER(t) = :tag AND nb.id = :id")
	List<Note> findNotesByTag(@Param("id") Long id, @Param("tag") String tag);
}


