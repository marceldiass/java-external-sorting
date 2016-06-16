package com.marceldias.externalsorting;

import com.marceldias.externalsorting.exception.ExternalSortingException;
import org.hamcrest.core.Is;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileReaderTest {

    private FileReader fileReader;
    private FileSplitter fileSplitter;
    private FileWriter fileWriter;

    @Before
    public void setUp() {
        fileSplitter = new FileSplitter();
        fileReader = new FileReader(fileSplitter);
        fileWriter = new FileWriter(fileSplitter);
    }

    @After
    public void tearDown() throws IOException {
        String[] testFiles = {"a.txt", "ab.txt"};
        for (String file : testFiles) {
            Files.deleteIfExists(Paths.get(ExternalSortingProperties.TEMP_FILES_DIR.value(), file));
        }
    }

    @Test
    public void testRun() {
        writeFile();
        String filename = ExternalSortingProperties.TEMP_FILES_DIR.value() + "/a.txt";
        System.setProperty(ExternalSortingProperties.FILENAME.getLabel(), filename);
        fileReader.run();

        Assert.assertThat(fileSplitter.getLinesQueue().size(), Is.is(3));
        Assert.assertThat(fileSplitter.isReaderDone(), Is.is(Boolean.TRUE));
    }

    @Test(expected = ExternalSortingException.class)
    public void testFileNotFound() {
        String filename = ExternalSortingProperties.TEMP_FILES_DIR.value() + "/fileNotFound.txt";
        System.setProperty(ExternalSortingProperties.FILENAME.getLabel(), filename);
        fileReader.run();
    }

    private void writeFile() {
        String fileContent = "abcdefghijklmnoprstuvxyz abcdefghijklmnoprstuvxyz \n" +
                "abc defghi jklmnopr stuvxyz abcdefghijklmnoprstuvxyz \n" +
                "abc zyxut defghijklmnoprstuvxyz abcdefghijklmnoprstuvxyz";

        fileWriter.proccessLine(fileContent);
    }
}
