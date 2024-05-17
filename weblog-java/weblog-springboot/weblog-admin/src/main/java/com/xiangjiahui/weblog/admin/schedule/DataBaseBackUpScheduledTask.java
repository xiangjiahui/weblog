package com.xiangjiahui.weblog.admin.schedule;

import com.xiangjiahui.weblog.common.config.DataBackUpProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Component
@Slf4j
public class DataBaseBackUpScheduledTask {

    @Autowired
    private DataBackUpProperties properties;


    //表示每天5点，23点执行
    @Scheduled(cron = "0 0 5,23 * * ?")
    public void execute() {
        log.info("============> 开始执行备份数据库任务");

        String username;
        String password;
        String databaseName;
        String backupPath;
        String installPath;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String sqlName = "weblog-backup-first" + ".sql";

        username = properties.getUsername();
        password = properties.getPassword();
        databaseName = properties.getDatabaseName();
        backupPath = properties.getBackupPath();
        installPath = properties.getInstallPath();


        File saveFile = new File(backupPath);
        if (!saveFile.exists()) {
            saveFile.mkdirs();
        }

        if (!backupPath.endsWith(File.separator)) {
            backupPath = backupPath + File.separator;
        }

        PrintWriter printWriter = null;
        BufferedReader reader = null;
        try {
            // 根据日期来创建当天的备份文件夹
            String dateFilePath = backupPath + File.separator + format.format(new Date());
            File dateFile = new File(dateFilePath);
            if (!dateFile.exists()) {
                dateFile.mkdir();
            }

            // 查看当天文件夹里是否已经有了一份备份的sql文件
            String name = dateFilePath + File.separator + sqlName;
            File sqlFile = new File(name);
            if (sqlFile.exists()) {
                // 如果已经有了一份备份文件,那么改名备份第二份sql文件
                name = dateFilePath + File.separator + "weblog-backup-second" + ".sql";
            }

            printWriter = new PrintWriter(new OutputStreamWriter(new FileOutputStream(name), StandardCharsets.UTF_8));
            String command = installPath + File.separator + "mysqldump " + " -u " + username + " -p" + password + " --databases " + databaseName + " > " + backupPath + sqlName;
            log.info(command);
            Process process = Runtime.getRuntime().exec(command);
            InputStreamReader inputStreamReader = new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8);
            reader = new BufferedReader(inputStreamReader);
            String line;
            while ((line = reader.readLine()) != null) {
                printWriter.println(line);
            }
            printWriter.flush();

            List<File> files = Arrays.asList(Objects.requireNonNull(saveFile.listFiles()));
            if (files.size() >= 61) {
                files.get(files.size() - 1).delete();
            }

        } catch (
                IOException exception) {
            log.error("============> 备份数据库失败", exception);
        } finally {
            try {
                if (Objects.nonNull(reader)) {
                    reader.close();
                }
                if (Objects.nonNull(printWriter)) {
                    printWriter.close();
                }
            } catch (IOException exception) {
                log.error("============> 关闭流异常", exception);
            }
        }

        log.info("============> 备份数据库任务执行完毕");
    }
}
