package main.com.egorl.info_notes;

import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowEP;
import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

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

    private Map<String, Note> notes = new HashMap<>();
    private Note selectedNote;

    NotesStoreManager notesStoreManager = ServiceManager.getService(NotesStoreManager.class);

    DefaultListModel<String> notesData = new DefaultListModel<String>();

    public NotesToolWindow(ToolWindow toolWindow) {

        notes = notesStoreManager.getNotes().stream().collect(Collectors.toMap(Note::getName, note -> note));
        notes.values().stream().sorted(Comparator.comparing(Note::getCreationDate).reversed()).forEach(note -> notesData.add(notesData.size(), note.getName()));

        notesList.setModel(notesData);
        createButton.addActionListener(e -> createNewNote());
        okEditNote.addActionListener(e -> saveAlterNote());
        okEditNote.addActionListener(e -> setEditButtonsVisible(false));
        editableBox.addActionListener(e -> setNoteEditable());
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
        notesList.addListSelectionListener(e ->
                switchNote(notesList.getSelectedIndex()));
    }

    private void createNewNote() {
        Note note = new Note();
        String newNoteName = "Заметка";
        int unicNum = 1;
        while (notes.containsKey(newNoteName)) {
            newNoteName = "Заметка " + unicNum;
            unicNum++;
        }

        note.setName(newNoteName);
        note.setContent("");
        note.setEditable(true);

        notesData.add(notes.size(), note.getName());
        notes.put(note.getName(),note);
        notesStoreManager.getNotes().add(note);
    }

    private void setNoteEditable() {
        selectedNote.setEditable(editableBox.isSelected());
        saveAlterNote();
    }

    private void saveAlterNote() {
        notesStoreManager.getNotes().forEach(note -> {
            if (note.getName().equals(selectedNote.getName())) {
                note.setName(noteNameField.getText());
                note.setContent(noteEditor.getText());
                note.setEditable(editableBox.isSelected());
            }
        });
        selectedNote.setName(noteNameField.getText());
        selectedNote.setContent(noteEditor.getText());
        selectedNote.setEditable(editableBox.isSelected());


        notes.put(selectedNote.getName(), selectedNote);
        notesData.set(notesList.getSelectedIndex(), selectedNote.getName());
    }

    private void cancelAlterNote() {
        noteNameField.setText(selectedNote.getName());
        noteEditor.setText(selectedNote.getContent());
    }

    protected void switchNote(int index) {
        String noteName = notesData.get(index);
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
        okEditNote.setVisible(visible);
        cancelEditNote.setVisible(visible);
    }

}
