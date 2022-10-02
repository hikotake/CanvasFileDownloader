package com.canvas;

import com.canvas.controllers.*;
import com.canvas.jsonManager.JsonController;
import com.canvas.ui.CheckboxModel;
import com.canvas.ui.ConsoleReader;
import com.canvas.ui.DirectorySelector;
import com.canvas.utils.structs.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

public class Main {
    static String canvasUrl;
    static String token;
    static Path downloadLocation;

    static Path whitelistJsonPath = Paths.get("/Users/shoma/Projects/canvas_file_downloader/whitelist.json");
    static Path tokenJsonPath = Paths.get("/Users/shoma/Projects/canvas_file_downloader/token.json");
    static Path canvasUrlJsonPath = Paths.get("/Users/shoma/Projects/canvas_file_downloader/canvasUrl.json");
    static Path downloadLocationJsonPath = Paths.get("/Users/shoma/Projects/canvas_file_downloader/downloadLocation.json");

    public static void main(String[] args) {
        new Main();
    }

    public Main() {
        CourseController courseController = new CourseController();
        FileController fileController = new FileController();
        FolderController folderController = new FolderController();
        JsonController jsonController = new JsonController();
        // スレッドセーフなリスト
        List<File> downloadingFiles = new CopyOnWriteArrayList<>();

        this.setInformation();

        if (!Files.exists(whitelistJsonPath)) {
            List<Course> courseList = courseController.getCourses(canvasUrl, token);
            CheckboxModel whitelistSelector = new CheckboxModel(courseList);

            // ブラックリスト選択画面が表示されている間にも次の処理に進んでしまうため，待機させる
            while (whitelistSelector.getIsRunning()) {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            jsonController.saveWhitelist(whitelistSelector.getList());
        }

        List<Course> whitelist = jsonController.readWhitelist();


        whitelist.parallelStream().forEach(course -> {
            List<Folder> folderList = folderController.getFolders(canvasUrl, token, course.getId());
            // 親ディレクトリを先に作成する必要があるため，fullNameが短い順に並べることでディレクトリの作成順を決める
            folderList = folderList.stream().sorted((f1, f2) -> f1.getFullName().length() - f2.getFullName().length()).collect(Collectors.toList());

            if (folderList != null) {
                folderList.forEach(folder -> {
                    // fullNameはフォルダのパスになっているが，"course files/~~~"のようになっているため，親ディレクトリをコース名で置き換える
                    folder.replaceFullName(course.getName());
                    this.createDirectory(Paths.get(downloadLocation.toString(), folder.getFullName()));

                    List<File> fileList = fileController.getFilesInFolder(folder.getFilesUrl(), token);

                    if (fileList != null) {
                        fileList.parallelStream().forEach(file -> {
                            Path path = Paths.get(downloadLocation.toString(), folder.getFullName(), file.getDisplayName());

                            if (!Files.exists(path)) {
                                file.setDownloadLocation(path);
                                downloadingFiles.add(file);
                            }
                        });
                    }
                });
            }
        });

        downloadingFiles.parallelStream().forEach(file -> {
            fileController.downloadFileAsync(file);
        });
    }

    private void createDirectory(Path path) {
        if (!Files.exists(path)) {
            // プログラムがディレクトリに書き込めるようにパーミッションを設定する
            Set<PosixFilePermission> permissions = PosixFilePermissions.fromString("rwxr--r--");
            FileAttribute<Set<PosixFilePermission>> fileAttributes = PosixFilePermissions.asFileAttribute(permissions);
            try {
                Files.createDirectory(path, fileAttributes);
                System.out.println("Directory created: " + path);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void setInformation() {
        ConsoleReader cr = new ConsoleReader();
        JsonController jc = new JsonController();

        if (!Files.exists(canvasUrlJsonPath)) {
            Main.canvasUrl = cr.readConsole("Canvas URL");
            jc.saveCanvasUrl(Main.canvasUrl);
        } else
            Main.canvasUrl = jc.readCanvasUrl();

        if (!Files.exists(tokenJsonPath)) {
            Main.token = cr.readConsole("token");
            jc.saveToken(Main.token);
        } else
            Main.token = jc.readToken();

        if (!Files.exists(downloadLocationJsonPath)) {
            DirectorySelector dc = new DirectorySelector();
            Main.downloadLocation = Paths.get(dc.getSelectedPath());
            jc.saveDownloadLocation(Main.downloadLocation.toString());
        } else
            Main.downloadLocation = Paths.get(jc.readDownloadLocation());
    }
}
