package com.codingyard.api.entity.contest;

public enum Language {
    JAVA("java"), CPP("cpp"), C("c"), PYTHON("py"), RUBY("rb"), OTHER("txt");

    private final String extension;

    Language(final String extension) {
        this.extension = extension;
    }

    public String getExtension() {
        return extension;
    }
}
