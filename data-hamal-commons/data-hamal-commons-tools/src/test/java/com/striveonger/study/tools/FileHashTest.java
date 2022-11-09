package com.striveonger.study.tools;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;

public class FileHashTest {

    @Test
    public void testFileHash() {
        String path1 = "/Users/striveonger/tmp/test1.zip";
        String path2 = "/Users/striveonger/tmp/test2.zip";
        File file1 = new File(path1), file2 = new File(path2);
        String code1 = FileHash.SHA512.code(file1);
        String code2 = FileHash.SHA512.code(file2);
        Assertions.assertEquals(code1, code2);
        System.out.println(code1);
        System.out.println(code2);
    }
}