package util;

import java.io.File;
import java.io.IOException;

public class DataFolderInitializer {
    private static final String DATA_DIR = "src/data";  // 修改为src/data
    private static final String[] DATA_FILES = {
        "teachers.txt",
        "students.txt",
        "courses.txt",
        "grades.txt",
        "feedbacks.txt",
        "classrooms.txt"
    };

    public static void initializeDataFolder() {
        // 创建data目录
        File dataDir = new File(DATA_DIR);
        if (!dataDir.exists()) {
            dataDir.mkdir();
        }

        // 创建所需的数据文件
        for (String fileName : DATA_FILES) {
            File file = new File(dataDir, fileName);
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    System.err.println("创建文件失败: " + fileName);
                    e.printStackTrace();
                }
            }
        }
    }
}
