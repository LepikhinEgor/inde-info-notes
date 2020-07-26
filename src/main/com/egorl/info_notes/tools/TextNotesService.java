package main.com.egorl.info_notes.tools;

import com.intellij.openapi.components.ServiceManager;
import main.com.egorl.info_notes.Note;
import main.com.egorl.info_notes.NotesStoreManager;

import java.util.Map;
import java.util.stream.Collectors;

public class TextNotesService {

    NotesStoreManager storeManager;
    ProjectProperties projectProperties;

    public TextNotesService() {
        storeManager = ServiceManager.getService(NotesStoreManager.class);
        projectProperties = ServiceManager.getService(ProjectProperties.class);
    }

    public void addNote(Note note) {
        storeManager.getNotes().add(note);
    }

    public void removeNote(String noteName) {
        storeManager.getNotes().removeIf(note -> note.getName() != null && note.getName().equals(noteName));
    }

    public void alterNote(String noteName, Note alteredNote) {
        storeManager.getNotes().forEach(note -> {
            if (note.getName().equals(noteName)) {
                note.setName(alteredNote.getName());
                note.setContent(alteredNote.getContent());
                note.setEditable(alteredNote.isEditable());
            }
        });
    }

    public String createNewNoteName() {
        String defaultNoteName = projectProperties.getLocalizedMessage("defaultNoteName");
        String newNoteName = defaultNoteName;

        int unicNum = 1;
        Map<String,Note> notes = getNotesMap();
        while (notes.containsKey(newNoteName)) {
            newNoteName = defaultNoteName + " " + unicNum;
            unicNum++;
        }

        return newNoteName;
    }

    public Map<String,Note> getNotesMap() {
        return storeManager.getNotes().stream()
                .collect(Collectors.toMap(Note::getName, note -> note));
    }
}
