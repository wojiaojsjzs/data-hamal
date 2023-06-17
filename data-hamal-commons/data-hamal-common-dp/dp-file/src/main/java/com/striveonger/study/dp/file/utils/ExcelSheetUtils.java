package com.striveonger.study.dp.file.utils;

import cn.hutool.poi.excel.sax.Excel03SaxReader;
import com.striveonger.study.core.exception.CustomException;
import org.apache.poi.hssf.record.BoundSheetRecord;
import org.apache.poi.openxml4j.exceptions.NotOfficeXmlFileException;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Mr.Lee
 * @description:
 * @date 2023-06-15 10:39
 */
public class ExcelSheetUtils {
    private final static Logger log = LoggerFactory.getLogger(ExcelSheetUtils.class);

    public static List<String> getSheetNames(String path) {
        return getSheetNames(new File(path));
    }


    public static List<String> getSheetNames(File file) {
        try {
            // 先用07+版本尝试去解析
            return read07BySax(file);
        } catch (NotOfficeXmlFileException e) {
            // 后缀错误读03区
            log.error("error...");
        }
        // 07版本解析失败后, 继续尝试03版本
        return read03BySax(file);
    }

    private static List<String> read07BySax(File file) {
        List<String> result = new ArrayList<>();
        XSSFReader xssfReader;
        try {
            xssfReader = new XSSFReader(OPCPackage.open(file));
            XSSFReader.SheetIterator iter = (XSSFReader.SheetIterator) xssfReader.getSheetsData();
            while (iter.hasNext()) {
                iter.next();
                result.add(iter.getSheetName());
            }
        } catch (IOException | OpenXML4JException e) {
            e.printStackTrace();
        }
        return result;
    }

    private static List<String> read03BySax(File file) {
        List<String> result = new ArrayList<>();
        Excel03SaxReader reader = new Excel03SaxReader((sheetIndex, rowIndex, rowList) -> {
            throw new CustomException("do not need");
        });
        try {
            reader.read(file);
        } catch (CustomException e) {
            // do nothing
        }
        try {
            Field field = reader.getClass().getDeclaredField("field");
            field.setAccessible(true);
            List<BoundSheetRecord> records = (List<BoundSheetRecord>) field.get(reader);
            records.stream().map(BoundSheetRecord::getSheetname).forEach(result::add);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }
        return result;
    }

}
