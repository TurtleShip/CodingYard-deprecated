package com.codingyard.api.payload;

import com.codingyard.api.entity.contest.Language;
import com.codingyard.api.entity.contest.topcoder.TopCoderDifficulty;
import com.codingyard.api.entity.contest.topcoder.TopCoderDivision;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;

public class TopCoderSolutionUploadPayload {

    private final TopCoderDivision division;
    private final TopCoderDifficulty difficulty;
    private final Long problemId;
    private final Language language;
    private final List<String> content;

    @JsonCreator
    public TopCoderSolutionUploadPayload(@NotNull @JsonProperty("division") TopCoderDivision division,
                                         @NotNull @JsonProperty("difficulty") TopCoderDifficulty difficulty,
                                         @NotNull @JsonProperty("problem_id") Long problemId,
                                         @NotNull @JsonProperty("language") Language language,
                                         @NotNull @JsonProperty("content") List<String> content) {
        this.division = division;
        this.difficulty = difficulty;
        this.problemId = problemId;
        this.language = language;
        this.content = content;
    }

    @NotNull @JsonProperty("division")
    public TopCoderDivision getDivision() {
        return division;
    }

    @NotNull @JsonProperty("difficulty")
    public TopCoderDifficulty getDifficulty() {
        return difficulty;
    }

    @NotNull @JsonProperty("problem_id")
    public Long getProblemId() {
        return problemId;
    }

    @NotNull @JsonProperty("language")
    public Language getLanguage() {
        return language;
    }

    @NotNull @JsonProperty("content")
    public List<String> getContent() {
        return content;
    }

    @Override
    public int hashCode() {
        return Objects.hash(division, difficulty, language, content);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final TopCoderSolutionUploadPayload other = (TopCoderSolutionUploadPayload) obj;
        return Objects.equals(this.division, other.division)
            && Objects.equals(this.difficulty, other.difficulty)
            && Objects.equals(this.language, other.language)
            && Objects.equals(this.content, other.content);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(TopCoderSolutionUploadPayload.class)
            .add("division", division)
            .add("difficulty", difficulty)
            .add("language", language)
            .add("content", content)
            .toString();
    }
}
