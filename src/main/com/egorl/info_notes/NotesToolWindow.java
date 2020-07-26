package main.com.egorl.info_notes;

import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowEP;
import main.com.egorl.info_notes.tools.TextNotesService;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class NotesToolWindow extends ToolWindowEP {

    private JTabbedPane tabbedPane1;
    private JPanel panel1;
    private JButton createButton;
    private JButton removeButton;
    private JCheckBox editableBox;
    private JList<String> notesList;
    private JEditorPane noteEditor;
    private JTextField noteNameField;
    private JButton cancelEditNote;
    private JButton okEditNote;

    private Map<String, Note> notes;
    private Note selectedNote;

    TextNotesService textNotesService = ServiceManager.getService(TextNotesService.class);

    DefaultListModel<String> notesData = new DefaultListModel<>();

    public NotesToolWindow(ToolWindow toolWindow) {
        getNotesFromDisk();
        fillNotesList();

        addListeners();
    }

    private void getNotesFromDisk() {
        notes = textNotesService.getNotesMap();
    }

    private void fillNotesList() {
        notes.values().stream()
                .sorted(Comparator.comparing(Note::getCreationDate).reversed())
                .forEach(note -> notesData.add(notesData.size(), note.getName()));

        notesList.setModel(notesData);
    }

    private void addListeners() {
        createButton.addActionListener(e -> createNewNote());
        removeButton.addActionListener(e -> removeNotes());
        editableBox.addActionListener(e -> setNoteEditable());

        okEditNote.addActionListener(e -> saveAlterNote());
        okEditNote.addActionListener(e -> setEditButtonsVisible(false));

        cancelEditNote.addActionListener(e -> cancelAlterNote());
        cancelEditNote.addActionListener(e -> setEditButtonsVisible(false));

        noteEditor.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                setEditButtonsVisible(true);
            }
        });
        noteNameField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                setEditButtonsVisible(true);
            }
        });

        notesList.addListSelectionListener(e -> {
            if (notesList.getSelectedValue() != null) {
                switchNote(notesList.getSelectedValue());
            }
            else {
                clearNoteEditor();
            }
        });
    }

    private void createNewNote() {
        Note note = new Note();

        note.setName(textNotesService.createNewNoteName());
        note.setContent("");
        note.setEditable(true);

        notesData.add(notesData.size(), note.getName());
        notes.put(note.getName(),note);
        textNotesService.addNote(note);
    }

    void clearNoteEditor() {
        selectedNote = null;
        noteEditor.setEditable(false);
        noteNameField.setEditable(false);
        noteEditor.setText("");
        noteNameField.setText("");
    }

    private void removeNotes() {
        List<String> selectedValuesList = notesList.getSelectedValuesList();
        selectedValuesList.forEach(noteName -> {
                notes.remove(noteName);
            textNotesService.removeNote(noteName);
            notesData.removeElement(noteName);
        });
    }

    private void setNoteEditable() {
        boolean editable = editableBox.isSelected();
        selectedNote.setEditable(editable);
        noteEditor.setEditable(editable);
        noteNameField.setEditable(editable);
        saveAlterNote();
    }

    private void saveAlterNote() {
        String oldNoteName = selectedNote.getName();

        selectedNote.setName(noteNameField.getText());
        selectedNote.setContent(noteEditor.getText());
        selectedNote.setEditable(editableBox.isSelected());

        textNotesService.alterNote(oldNoteName, selectedNote);

        notes.put(selectedNote.getName(), selectedNote);
        notesData.set(notesList.getSelectedIndex(), selectedNote.getName());
    }

    private void cancelAlterNote() {
        noteNameField.setText(selectedNote.getName());
        noteEditor.setText(selectedNote.getContent());
    }

    protected void switchNote(String noteName) {
        selectedNote = notes.get(noteName);

        editableBox.setSelected(selectedNote.isEditable());
        noteEditor.setEditable(selectedNote.isEditable());
        noteNameField.setEditable(selectedNote.isEditable());
        setEditButtonsVisible(false);
        noteNameField.setText(selectedNote.getName());
        noteEditor.setText(selectedNote.getContent());
    }

    public JPanel getContent() {
        return panel1;
    }

    private void setEditButtonsVisible(boolean visible) {
        okEditNote.setEnabled(visible);
        cancelEditNote.setEnabled(visible);
    }

}
