package com.music.view;

public interface IListener {
    void onKeyPressed(String note);

    void onNoteReleased(String note);
}
