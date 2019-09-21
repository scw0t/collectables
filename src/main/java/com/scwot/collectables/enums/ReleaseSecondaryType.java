package com.scwot.collectables.enums;

public enum ReleaseSecondaryType {
    AUDIO_DRAMA("audio drama"),
    AUDIOBOOK("audio book"),
    COMPILATION("compilation"),
    DEMO("demo"),
    DJ_MIX("dj mix"),
    INTERVIEW("interview"),
    LIVE("live"),
    MIXTAPE_STREET("mixtape street"),
    REMIX("remix"),
    SOUNDTRACK("soundtrack"),
    SPOKENWORD("spokenword");

    private String text;

    ReleaseSecondaryType(String text) {
        this.text = text;
    }

    public String getText() {
        return this.text;
    }

    public static ReleaseSecondaryType fromString(String text) {
        for (ReleaseSecondaryType b : ReleaseSecondaryType.values()) {
            if (b.text.equalsIgnoreCase(text)) {
                return b;
            }
        }
        return null;
    }

}
