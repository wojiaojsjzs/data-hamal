package com.striveonger.study.tools;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;

public class FileHashTest {

    @Test
    public void testFileHash() {
        String path1 = "/Users/striveonger/tmp/app1.sh";
        String path2 = "/Users/striveonger/tmp/app2.sh";
        File file1 = new File(path1), file2 = new File(path2);
        String code1 = FileHash.SHA512.code(file1);
        String code2 = FileHash.SHA512.code(file2);
        Assertions.assertEquals(code1, code2);
        System.out.println(code1);
        System.out.println(code2);
    }
}