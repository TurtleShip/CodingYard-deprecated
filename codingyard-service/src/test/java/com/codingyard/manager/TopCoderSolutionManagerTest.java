package com.codingyard.manager;

import com.codingyard.dao.TopCoderSolutionDAO;
import com.codingyard.api.entity.contest.Language;
import com.codingyard.api.entity.contest.topcoder.TopCoderDifficulty;
import com.codingyard.api.entity.contest.topcoder.TopCoderDivision;
import com.codingyard.api.entity.user.CodingyardUser;
import com.google.common.collect.Lists;
import com.google.common.io.Files;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.nio.file.Path;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class TopCoderSolutionManagerTest {

    @Mock
    private TopCoderSolutionDAO tcDAO;


    private TopCoderSolutionManager manager;
    private Path tempDir;

    @Before
    public void setup() {
        tempDir = Files.createTempDir().toPath();
        manager = new TopCoderSolutionManager(tcDAO, tempDir);
    }

    @Test
    public void shouldSaveContentAndRetrieve() throws Exception {
        final CodingyardUser author = new CodingyardUser.Builder("Seulgi", "Kim").build();
        final TopCoderDivision division = TopCoderDivision.DIV2;
        final TopCoderDifficulty difficulty = TopCoderDifficulty.HARD;
        final long problemId = 123;
        final Language language = Language.OTHER;
        final String firstSentence = "Hello, it is a beautiful day! :)";
        final String secondSentence = "Would you like a cup of coffee?";
        final String newLine = System.getProperty("line.separator");
        final String content = firstSentence + newLine + secondSentence;

        manager.save(author, content, division, difficulty, problemId, language);

        final List<String> loaded = manager.load(author, division, difficulty, problemId, language);
        assertThat(loaded.size()).isEqualTo(2);
        assertThat(loaded.get(0)).isEqualTo(firstSentence);
        assertThat(loaded.get(1)).isEqualTo(secondSentence);
    }
}
