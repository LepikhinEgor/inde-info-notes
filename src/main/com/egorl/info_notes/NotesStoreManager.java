package main.com.egorl.info_notes;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

@State(name = "NotesStorageManager", storages = {@Storage("info-notes.xml")})
public class NotesStoreManager implements PersistentStateComponent<NotesStoreManager> {

    List<Note> notes;

    String state = "xyi";

    public NotesStoreManager() {
        notes = new ArrayList<>();
    }

    @Nullable
    @Override
    public NotesStoreManager getState() {
        return this;
    }

    @Override
    public void loadState(@NotNull NotesStoreManager notesStoreManager) {
        XmlSerializerUtil.copyBean(notesStoreManager, this);
        if (notes == null)
            notes = new ArrayList<>();
    }

    public List<Note> getNotes() {
        return notes;
    }

    public void setNotes(List<Note> notes) {
        this.notes = notes;
    }

}
