package com.codingyard.manager;

import com.codingyard.api.entity.contest.Language;
import com.codingyard.api.entity.contest.topcoder.TopCoderDifficulty;
import com.codingyard.api.entity.contest.topcoder.TopCoderDivision;
import com.codingyard.api.entity.contest.topcoder.TopCoderSolution;
import com.codingyard.api.entity.user.CodingyardUser;
import com.codingyard.dao.TopCoderSolutionDAO;
import com.google.common.base.Optional;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

public class TopCoderSolutionManager {

    private final TopCoderSolutionDAO tcDAO;
    private final Path root;

    /**
     * Constructor for TopCoderSolutionManager.
     *
     * @param tcDAO Data Access Object for TopCoder Solution.
     * @param root  Root folder under which solutions will be saved.
     */
    public TopCoderSolutionManager(final TopCoderSolutionDAO tcDAO,
                                   final Path root) {
        this.tcDAO = tcDAO;
        this.root = root;
    }

    /**
     * Given a solution and its related information, this method saves the solution
     * and returns the path to which it is saved.
     *
     * @param author     The author of {@param content}
     * @param content    The content of of this solution.
     * @param division   The division of this solution.
     * @param difficulty The difficulty of this solution.
     * @param problemId  The problem id of this solution.
     * @param language   The language in which this solution is written.
     * @return The path under which the this solution is saved.
     * @throws IOException
     */
    public Path save(final CodingyardUser author,
                     final String content,
                     final TopCoderDivision division,
                     final TopCoderDifficulty difficulty,
                     final long problemId,
                     final Language language) throws IOException {

        final Path path = createPath(division, difficulty, problemId, author, language);
        Files.createDirectories(path.getParent());
        Files.deleteIfExists(path);
        Files.createFile(path);

        try (BufferedWriter writer = Files.newBufferedWriter(path, StandardOpenOption.WRITE)) {
            writer.append(content);
        }

        return path;
    }

    /**
     * Given info on a solution, return the content of the solution.
     *
     * @param author     The author of this solution.
     * @param division   The division of this solution.
     * @param difficulty The difficulty of this solution.
     * @param problemId  The problem id of this solution.
     * @param language   The language in which this solution is written.
     * @return The content of the solution.
     * @throws IOException
     */
    public List<String> load(final CodingyardUser author,
                             final TopCoderDivision division,
                             final TopCoderDifficulty difficulty,
                             final long problemId,
                             final Language language) throws IOException {
        return Files.readAllLines(createPath(division, difficulty, problemId, author, language));
    }

    public List<String> load(final TopCoderSolution solution) throws IOException {
        return load(solution.getAuthor(),
            solution.getDivision(),
            solution.getDifficulty(),
            solution.getProblemNumber(),
            solution.getLanguage());
    }

    public Optional<TopCoderSolution> findById(Long id) {
        return tcDAO.findById(id);

    }

    public List<TopCoderSolution> findAll(Optional<TopCoderDivision> division,
                                          Optional<TopCoderDifficulty> difficulty,
                                          Optional<Long> problemNumber,
                                          Optional<Language> language,
                                          Optional<Long> userId) {
        return tcDAO.findAll(division, difficulty, problemNumber, language, userId);
    }

    /**
     * Given info on a solution, creates a path under which the solution should be saved.
     *
     * @param division   The division to which a solution belongs.
     * @param difficulty The difficulty of a solution.
     * @param author     The author of a solution.
     * @param language   The language in which a solution is written.
     * @param problemId  The problem id of a solution.
     * @return The path to where a solution should
     * @throws IOException
     */
    private Path createPath(final TopCoderDivision division,
                            final TopCoderDifficulty difficulty,
                            final long problemId,
                            final CodingyardUser author,
                            final Language language) throws IOException {
        return Paths.get(root.toString(),
            division.name(),
            difficulty.name(),
            Long.toString(problemId),
            String.format("%s.%s", author.getUsername(), language.getExtension()));
    }

}
