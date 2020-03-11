package com.matthewcannefax.java;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.PriorityQueue;
import java.util.stream.Stream;

public class QueueSorting {
    public QueueSorting(){
        File file1 = new File(Paths.get("file1.txt").toString());
        File file2 = new File(Paths.get("file2.txt").toString());
        File file3 = new File(Paths.get("file3.txt").toString());

        long start = System.currentTimeMillis();

//        sortFile("file1.txt", 1);
//        sortFile("file2.txt", 2);
//        sortFile("file3.txt", 3);

        Thread thread1 = threadSorting("file1.txt", 1);
        Thread thread2 = threadSorting("file2.txt", 2);
        Thread thread3 = threadSorting("file3.txt", 3);


            long end = System.currentTimeMillis();
            System.out.println(end - start);




    }

    private Thread threadSorting(String fileName, int fileNum){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                sortFile(fileName, fileNum);
            }
        });
        thread.start();
        return thread;
    }

    private void sortFile(String fileName, int fileNum){
        PriorityQueue<Integer> queue = new PriorityQueue<>();
        try(Stream<String> stream = Files.lines(Paths.get(fileName))){
            stream.forEach(line -> {
                queue.add(Integer.parseInt(line.trim()));
            });

            createAndWriteFile(queue, fileNum);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private void createAndWriteFile(PriorityQueue<Integer> queue, int fileNum){
        try {
            File newFile = new File("sortedFile" + fileNum + ".txt");
            if(newFile.createNewFile()){
                FileWriter fileWriter = new FileWriter(newFile);
                BufferedWriter bw = new BufferedWriter(fileWriter);
                while(!queue.isEmpty()){
                    Integer integer = queue.poll();
                    bw.write(integer + "\n");
                }
                bw.close();
                fileWriter.close();
            }else {
                fileNum++;
                createAndWriteFile(queue, fileNum);
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
