package main.com.egorl.info_notes;

import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowEP;
import com.intellij.ui.JBColor;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

public class NotesToolWindow extends ToolWindowEP {

    private JTabbedPane tabbedPane1;
    private JPanel panel1;
    private JButton createButton;
    private JButton removeButton;
    private JCheckBox editableBox;
    private JList<String> notesList;
    private JEditorPane noteEditor;
    private JTextField textField1;
    private JTextField noteNameField;
    private JButton cancelEditNote;
    private JButton okEditNote;

    private HashMap<String, String> notesData;

    DefaultListModel<String> notes = new DefaultListModel<String>();

    public NotesToolWindow(ToolWindow toolWindow) {
        notesList.setModel(notes);
        createButton.addActionListener(e -> createNewNote());
    }

    private void createNewNote() {
        notes.add(notes.size(), "Заметка " + notes.size());
        notesList.addListSelectionListener(e ->
                switchNote(e.getFirstIndex()));
    }

    protected void switchNote(int index) {
        String noteName = notes.get(index);
        String noteContent = notesData.get(noteName);

        note
        noteEditor.setText(noteContent);
    }

    public JPanel getContent() {
        return panel1;
    }

}
