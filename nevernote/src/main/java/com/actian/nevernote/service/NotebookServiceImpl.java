package com.actian.nevernote.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.actian.nevernote.model.Note;
import com.actian.nevernote.model.Notebook;
import com.actian.nevernote.repository.NotebookRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class NotebookServiceImpl implements NotebookService{
	
	@Autowired
	private NotebookRepository notebookRepository;

	/**
	 * Get all Notebooks stored.
	 * @return
	 */
	public List<Notebook> getAllNotebooks(){
		List<Notebook> notebooks = new ArrayList<>();
		notebookRepository.findAll().forEach(notebooks::add);
		return notebooks;
	}

	/**
	 * Get Notebook by Id
	 * @param id The ID of the Notebook to get.
	 * @return
	 */
	public Notebook getNotebook(Long id){
		return notebookRepository.findById(id).get();
	}

	/**
	 * Add new notebook
	 * @param notebook The Notebook to be added
	 * @return
	 */
	public Notebook addNotebook(Notebook notebook){
		return notebookRepository.save(notebook);
	}

	/**
	 * Update Notebook passed by Id
	 * @param notebook The Notebook to be updated
	 * @param id The ID of the Notebook to be updated
	 * @return
	 */
	public void updateNotebook(Notebook notebook, Long id){
		notebook.setId(id);
		notebookRepository.save(notebook);
	}

	/**
	 * Delete Notebook by Id
	 * @param id The Id of the Notebook to be deleted
	 * @return
	 */
	public void deleteNotebook(Long id){
		notebookRepository.deleteById(id);;
	}

	/**
	 * Get all Notes in the Notebook passed.
	 * @param id The Notebook Id to select Notes
	 * @return
	 */
	public List<Note> getNotebookNotes(Long id){
		return getNotebook(id).getNotes();
	}

	/**
	 * Get all Notes in the Notebook passed by Note Tag
	 * @param id The Notebook Id to select Notes
	 * @param tag The Tag to filter Notes (Optional)
	 * @return
	 */
	public List<Note> getNotebookNotesByTag(Long id, String tag){
		return notebookRepository.findNotesByTag(id, tag.toUpperCase());
	}

	/**
	 * Validate Notebook Id exist
	 * @param id The Notebook Id to validate
	 * @return
     */
	public Boolean notebookExists(Long id){
		return notebookRepository.existsById(id);
	}
	
	
}
