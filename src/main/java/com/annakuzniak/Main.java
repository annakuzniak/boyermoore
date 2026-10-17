package com.annakuzniak;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {

        DataSetLoader dataSetLoader = new DataSetLoader();

        try {
            String text = dataSetLoader.loadBook();

            System.out.println("Book loaded succesfully");
            System.out.println("Number of characters: " + text.length());

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
