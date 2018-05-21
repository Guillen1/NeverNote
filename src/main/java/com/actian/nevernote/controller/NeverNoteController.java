package com.actian.nevernote.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.actian.nevernote.model.Note;
import com.actian.nevernote.model.Notebook;
import com.actian.nevernote.service.NoteService;
import com.actian.nevernote.service.NotebookService;

import java.net.URI;
import java.util.List;
import java.util.Optional;


@RestController
public class NeverNoteController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(NeverNoteController.class);
	 
	@Autowired
	NotebookService notebookService;
	
	@Autowired
	NoteService noteService;
	
	@Value("${app.version}")
	private String appVersion;
	
	@GetMapping("/")
    public String sayHello() {
        LOGGER.info("Begin... sayHello, root url '/'");
        return "<h1>Hello Welcome to NeverNote!</h1><br/>Version:" + appVersion;
	}

	/**
	 * Get all Notebooks stored.
	 * @return
     */
	@GetMapping("/notebooks")
	public List<Notebook> getAllNotebooks(){
		return notebookService.getAllNotebooks();
	}

	/**
	 * Get Notebook by Id
	 * @param id The ID of the Notebook to get.
	 * @return
     */
	@GetMapping("/notebooks/{id}")
	public Notebook getNotebook(@PathVariable long id){
		return notebookService.getNotebook(id);
	}

	/**
	 * Add new notebook
	 * @param notebook The Notebook to be added
	 * @return
     */
	@PostMapping("/notebooks")	
	public ResponseEntity<Object> addNotebook(@RequestBody Notebook notebook){
		notebook = notebookService.addNotebook(notebook);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(notebook.getId()).toUri();
		return ResponseEntity.created(location).build();
	}

	/**
	 * Update Notebook passed by Id
	 * @param notebook The Notebook to be updated
	 * @param id The ID of the Notebook to be updated
     * @return
     */
	@PutMapping("/notebooks/{id}")
	public ResponseEntity<Object> updateNotebook(@RequestBody Notebook notebook, @PathVariable Long id){
		//Validate Notebook Id exists before updating.
		if(!notebookService.notebookExists(id))
			return ResponseEntity.notFound().build();
		notebookService.updateNotebook(notebook, id);
		return ResponseEntity.noContent().build();
	}

	/**
	 * Delete Notebook by Id
	 * @param id The Id of the Notebook to be deleted
	 * @return
     */
	@DeleteMapping("/notebooks/{id}")
	public ResponseEntity<Object> deleteNotebook(@PathVariable Long id){
		//Validate Notebook Id exists before deleting.
		if(!notebookService.notebookExists(id))
			return ResponseEntity.notFound().build();
		notebookService.deleteNotebook(id);
		return ResponseEntity.noContent().build();
	}

	/**
	 * Get all Notes in the Notebook passed.
	 * Filtering optional by note tag
	 * @param id The Notebook Id to select Notes
	 * @param tag The Tag to filter Notes (Optional)
     * @return
     */
	@GetMapping("/notebooks/{id}/notes")
	public List<Note> getNotebookNotes(@PathVariable long id, @RequestParam(value="tag", required=false) Optional<String> tag){
		//Return filtered notes if tag present
		if(tag.isPresent())
			return notebookService.getNotebookNotesByTag(id, tag.get());
		return notebookService.getNotebookNotes(id);
	}

	/**
	 * Get all Notes stored
	 * Filtering optional by note tag
	 * @param tag The Tag to filter Noted (Optional)
	 * @return
     */
	@GetMapping("/notes")
	public List<Note> getAllNotes(@RequestParam(value="tag", required=false) Optional<String> tag){
		//Return filtered notes if tag present
		if(tag.isPresent())
			return noteService.getNotesByTag(tag.get());
		return noteService.getAllNotes();
	}

	/**
	 * Get Note by Notebook Id and Note Id
	 * @param notebookId The Notebook Id to get the Note
	 * @param noteId The Note Id to get the Note
     * @return
     */
	@GetMapping("/notebooks/{notebookId}/notes/{noteId}")
	public Note getNote(@PathVariable(value="notebookId") Long notebookId, @PathVariable(value="noteId") Long noteId){
		return noteService.getNote(noteId);
	}

	/**
	 * Add new Note by Notebook Id
	 * @param note The Note to be added
	 * @param notebookId The Notebook Id to add Note
     * @return
     */
	@PostMapping("/notebooks/{notebookId}/notes")
	public ResponseEntity<Object> addNote(@RequestBody Note note, @PathVariable(value="notebookId") Long notebookId){
		//Validate Notebook Id exists before adding Note.
		if(!notebookService.notebookExists(notebookId))
			return ResponseEntity.notFound().build();
		note = noteService.addNote(note, notebookId);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(note.getNoteId()).toUri();
		return ResponseEntity.created(location).build();
	}

	/**
	 * Update Note by Notebook Id and Note Id
	 * @param note The Note to be updated
	 * @param notebookId The Notebook Id to add Note
	 * @param noteId The Note Id to add Note
     * @return
     */
	@PutMapping("/notebooks/{notebookId}/notes/{noteId}")
	public ResponseEntity<Object> updateNote(@RequestBody Note note, @PathVariable(value="notebookId") Long notebookId, @PathVariable(value="noteId") Long noteId){
		//Validate Notebook Id exists before updating.
		if(!notebookService.notebookExists(notebookId))
			return ResponseEntity.notFound().build();
		//Validate Note Id exists before updating.
		if(!noteService.noteExists(noteId))
			return ResponseEntity.notFound().build();
		noteService.updateNote(note, noteId);
		return ResponseEntity.noContent().build();
	}

	/**
	 * Delete Note by Notebook Id and Note Id
	 * @param notebookId The Notebook Id to delete Note
	 * @param noteId The Note Id to delete Note
     * @return
     */
	@DeleteMapping("/notebooks/{notebookId}/notes/{noteId}")
	public ResponseEntity<Object> deleteNote(@PathVariable(value="notebookId") Long notebookId, @PathVariable(value="noteId") Long noteId){
		//Validate Notebook Id exists before deleting.
		if(!notebookService.notebookExists(notebookId))
			return ResponseEntity.notFound().build();
		//Validate Note Id exists before deleting.
		if(!noteService.noteExists(noteId))
			return ResponseEntity.notFound().build();
		noteService.deleteNote(noteId, notebookId);
		return ResponseEntity.noContent().build();
	}	
	
}
