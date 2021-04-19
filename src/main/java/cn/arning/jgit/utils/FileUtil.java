package cn.arning.jgit.utils;


import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.read.builder.ExcelReaderBuilder;
import com.alibaba.excel.read.metadata.ReadSheet;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author arning
 */
public class FileUtil {

    private static String GIT_PATH = ".git";

    private static String XLSX_SUFFIX = ".xlsx";

    private static String TXT_SUFFIX = ".txt";


    public static List<File> findLocalGitRepository(File project, List<File> localGitRepositories) {
        if (project.isFile()) {
            return localGitRepositories;
        }
        File[] projectSubPaths = project.listFiles();
        if (projectSubPaths.length == 0) {
            return localGitRepositories;
        }
        for (File projectSubPath : projectSubPaths) {
            if (projectSubPath.isFile()) {
                continue;
            }
            if (GIT_PATH.equals(projectSubPath.getName())) {
                localGitRepositories.add(projectSubPath);
                continue;
            }
            findLocalGitRepository(projectSubPath, localGitRepositories);
        }
        return localGitRepositories;
    }

    static List<String> urls = new ArrayList<>();


    public static List<String> findCloneUrl(File readFile) {
        String name = readFile.getName();
        String suffix = name.substring(name.lastIndexOf("."));
        urls.clear();
        Assert.isNotNull(readFile, "文件不存在");
        if (TXT_SUFFIX.equals(suffix)) {
            BufferedReader reader = null;
            try {
                reader = new BufferedReader(new FileReader(readFile));
                String tmpUrl;
                while ((tmpUrl = reader.readLine()) != null) {
                    urls.add(tmpUrl);
                }
            } catch (IOException e) {
                System.out.println(e);
            } finally {
                if (null != reader) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        System.out.println(e);
                    }
                }
            }
        }
        if (XLSX_SUFFIX.equals(suffix)) {
            ExcelReader excelReader = null;
            try {
                excelReader = EasyExcel.read(name, ExcelData.class, new ExcelDataListener()).build();
                ReadSheet build = EasyExcel.readSheet(0).build();
                excelReader.read(build);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (null != excelReader) {
                    excelReader.finish();
                }
            }
        }
        return urls;
    }


    static class ExcelDataListener extends AnalysisEventListener<ExcelData> {

        @Override
        public void invoke(ExcelData excelData, AnalysisContext analysisContext) {
            urls.add(excelData.getUrl());
        }

        @Override
        public void doAfterAllAnalysed(AnalysisContext analysisContext) {

        }
    }

}
