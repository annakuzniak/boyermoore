package com.annakuzniak;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {

        DataSetDownloader downloader = new DataSetDownloader();

        try {
            downloader.downloadBook();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
