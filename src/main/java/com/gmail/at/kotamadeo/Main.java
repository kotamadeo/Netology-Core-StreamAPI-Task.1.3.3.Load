package com.gmail.at.kotamadeo;

import com.gmail.at.kotamadeo.game.GameProgress;
import lombok.SneakyThrows;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class Main {
    private static int counterBeforeFileCreation;
    private static int counterAfterFileCreation;
    private static final Path gameDirectoryPath = Path.of("\\Module_1-Stream_API-Task_1.3.3-Load\\Games\\savegames");
    private static final String zipName = "\\savegames.zip";

    public static void main(String[] args) {
        List<GameProgress> gameProgresses = new ArrayList<>();
        gameProgresses.add(new GameProgress(10, 20, 30, 2000));
        gameProgresses.add(new GameProgress(20, 10, 10, 200));
        gameProgresses.add(new GameProgress(120, 6, 48, 6200.25));
        List<String> pathsSave = new ArrayList<>();
        for (GameProgress gameProgress : gameProgresses) {
            saveGame(gameDirectoryPath, gameProgress, pathsSave);
            toZip(gameDirectoryPath, pathsSave);
        }
        deleteFileByPath(pathsSave);
        fromZip(gameDirectoryPath);
        loadGame(pathsSave);
    }

    @SneakyThrows
    private static void saveGame(Path path, GameProgress data, List<String> pathSaves) {
        Files.createFile(Path.of(path + "\\save" + (++counterBeforeFileCreation) + ".dat"));
        String pathForSave = path + "\\save" + (++counterAfterFileCreation) + ".dat";
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(pathForSave))) {
            objectOutputStream.writeObject(data);
            pathSaves.add(pathForSave);
        }
    }

    @SneakyThrows
    private static void toZip(Path path, List<String> files) {
        try (ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(path + zipName))) {
            for (String file : files) {
                zipOutputStream.putNextEntry(new ZipEntry(new File(file).getName()));
                byte[] bytes = Files.readAllBytes(Paths.get(file));
                zipOutputStream.write(bytes, 0, bytes.length);
            }
        }
    }

    private static void deleteFileByPath(List<String> files) {
        for (String file : files) {
            try {
                Files.deleteIfExists(Path.of(file));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @SneakyThrows
    private static void fromZip(Path path) {
        try (ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(path + zipName))) {
            ZipEntry zipEntry;
            byte[] buffer = new byte[2048];
            while ((zipEntry = zipInputStream.getNextEntry()) != null) {
                Path pathFromZIP = gameDirectoryPath.resolve(zipEntry.getName());
                try (BufferedOutputStream bufferedOutputStream =
                             new BufferedOutputStream(new FileOutputStream(pathFromZIP.toString()), buffer.length)) {
                    int length;
                    while ((length = zipInputStream.read()) > 0) {
                        bufferedOutputStream.write(buffer, 0, length);
                    }
                }
            }
        }
        Files.deleteIfExists(Path.of(path + zipName));
    }

    @SneakyThrows
    private static void loadGame(List<String> files) {
        List<GameProgress> gameProgresses = new ArrayList<>();
        for (String file : files) {
            try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(file))) {
                gameProgresses.add((GameProgress) objectInputStream.readObject());
            }
        }
        gameProgresses.forEach(System.out::println);
    }
}