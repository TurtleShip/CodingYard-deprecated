package com.codingyard.manager;

import com.codingyard.api.entity.contest.Contest;
import com.codingyard.api.entity.contest.Language;
import com.codingyard.api.entity.contest.uva.UVaSolution;
import com.codingyard.api.entity.user.CodingyardUser;
import com.codingyard.dao.UVaSolutionDAO;
import com.google.common.base.Optional;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Date;
import java.util.List;

public class UVaSolutionManager extends BasicEntityManager<UVaSolution> {

    private final UVaSolutionDAO uvaDAO;
    private final Path root;

    public UVaSolutionManager(UVaSolutionDAO uvaDAO, Path root) {
        super(uvaDAO);
        this.uvaDAO = uvaDAO;
        this.root = root;
    }

    public Path save(final CodingyardUser author,
                     final String content,
                     final long problemId,
                     final Language language,
                     final Date date) throws IOException {

        final Path path = createPath(problemId, author, language, date);
        Files.createDirectories(path.getParent());
        Files.deleteIfExists(path);
        Files.createFile(path);

        try (BufferedWriter writer = Files.newBufferedWriter(path, StandardOpenOption.WRITE)) {
            writer.append(content);
        }

        return path;
    }

    public List<String> load(final CodingyardUser author,
                             final long problemId,
                             final Language language,
                             final Date date) throws IOException {
        return Files.readAllLines(createPath(problemId, author, language, date));
    }

    public List<String> load(final UVaSolution solution) throws IOException {
        return load(solution.getAuthor(),
            solution.getProblemNumber(),
            solution.getLanguage(),
            solution.getSubmissionDate());
    }

    public List<UVaSolution> findAll(Optional<Long> problemNumber,
                                     Optional<Language> language,
                                     Optional<Long> userId) {
        return uvaDAO.findAll(problemNumber, language, userId);
    }


    /**
     * Given info on a solution, creates a path under which the solution should be saved.
     *
     * @param author    The author of a solution.
     * @param language  The language in which a solution is written.
     * @param problemId The problem id of a solution.
     * @return The path to where a solution should
     * @throws IOException
     */
    private Path createPath(final long problemId,
                            final CodingyardUser author,
                            final Language language,
                            final Date createdDate) throws IOException {
        return Paths.get(root.toString(),
            Contest.UVA_ONLINE_JUDGE.toString(),
            Long.toString(problemId),
            String.format("%s-%d.%s", author.getUsername(), createdDate.getTime(), language.getExtension()));
    }
}
