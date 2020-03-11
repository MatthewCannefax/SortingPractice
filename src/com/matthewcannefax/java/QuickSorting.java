package com.matthewcannefax.java;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

public class QuickSorting {
    public QuickSorting() throws InterruptedException {
        long start = System.currentTimeMillis();
        List<File> fileList = new ArrayList<>();

        fileList.add(new File(Paths.get("file1.txt").toString()));
        fileList.add(new File(Paths.get("file2.txt").toString()));
        fileList.add(new File(Paths.get("file3.txt").toString()));

        Thread thread1 = sortFile(fileList.get(0), 1);
        Thread thread2 = sortFile(fileList.get(1), 2);
        Thread thread3 = sortFile(fileList.get(2), 3);

        thread1.start();
        thread2.start();
        thread3.start();

        thread1.join();
        thread2.join();
        thread3.join();


        long end = System.currentTimeMillis();

        System.out.println(end - start);

        int num = 0;
    }

    private void createAndWriteFile(Object[] ints, int fileNumber){
        try {
            File file =  new File("sortedFile" + fileNumber + ".txt");
            if(file.createNewFile()){
                FileWriter writer = new FileWriter(file);
                BufferedWriter bw = new BufferedWriter(writer);
                for(Object i : ints){
                    bw.write(i + "\n");
                }
                bw.close();
                writer.close();
            }else{
                fileNumber++;
                createAndWriteFile(ints, fileNumber);
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private Thread sortFile(File file, int fileNumber){
        return new Thread(new Runnable() {
            @Override
            public void run() {
                List<Integer> ints = new LinkedList<>();

                try(Stream<String> stream = Files.lines(Paths.get(file.getPath()))){
                    int i = 0;
                    stream.forEach(line -> {
                        ints.add(Integer.parseInt(line.trim()));
                    });

                } catch (IOException e) {
                    e.printStackTrace();
                }

                Object[] intArray = ints.toArray();

                sort(intArray, 0, intArray.length - 1);

                createAndWriteFile(intArray, 1);
            }
        });
    }

    private int partition(Object arr[], int low, int high){
        int pivot = (int)arr[high];
        int i = low - 1;

        for(int j = low; j < high; j++){
            if((int)arr[j] < pivot){
                i++;

                int temp = (int)arr[i];
                arr[i] = arr[j];
                arr[j] = temp;
            }
        }

        int temp = (int)arr[i+1];
        arr[i+1] = arr[high];
        arr[high] = temp;

        return i+1;
    }

    private void sort(Object arr[], int low, int high){
        if(low < high){
            int pi = partition(arr, low, high);

            sort(arr, low, pi-1);
            sort(arr, pi+1, high);
        }
    }


}
