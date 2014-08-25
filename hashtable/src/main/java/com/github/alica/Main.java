package com.github.alica;
import com.github.alica.hashtable.Dictionary;
import java.io.File;

public class Main {
    public static void main(String[] args) {
        Dictionary dictionary = new Dictionary(new File("src/main/resources/file.txt"));
        System.out.print(dictionary.find("вор"));
    }
}