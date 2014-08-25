package com.github.alica.hashtable;

import java.io.*;

public class Dictionary {
    private List[] dict;//массив списков
    private int N = 0;//количество пар в хэш-таблице
    private File file;

    public Dictionary(File file){
        dict = new List[2];
        this.file = file;
        readfile();
    }
    private static class ListElem {
        public String key; // ключ
        public int n = 1;
        public ListElem next; // ссылка на следующий элемент

        //конструктор для создания элементов списка
        public ListElem(String Key) {key = Key;}
    }

    private static class List {
        public ListElem Head;
        public List(String s) {Head = new ListElem(s);}
        public List(ListElem le) {Head = le;}
    }

    private static int hash(String s, int n) {
        int x = s.charAt(s.length() - 1);
        for(int i = s.length() - 2; i >= 0; i--)
            x = 33 * x + s.charAt(i);
        return Math.abs(x) % n;
    }

    public void add(String key) {
        key = key.toLowerCase();
        int index = hash(key, dict.length); // значение hash-функции
        if (dict[index] != null) {
            ListElem current = dict[index].Head; // текущий элемент списка
            while (true) {
                if (key.equals(current.key)) {
                    current.n++;
                    return;
                }
                if (current.next == null) break;
                current = current.next;
            }
            if (!rebuild(key)) {
                current.next = new ListElem(key);
                N++;
            }
        }
        else {
            if (!rebuild(key)) {
                dict[index] = new List(key);
                N++;
            }
        }
    }

    private boolean rebuild(String key) {
        if(N * 2 <= dict.length) return false;
        List[] dict1 = new List[2 * dict.length];
        for(int i = 0; i < dict.length; i++) if (dict[i] != null) {
            ListElem current = dict[i].Head; // текущий элемент списка
            do {
                ListElem current2 = current;
                current = current2.next;
                current2.next = null;
                //считываем элементы из i-го списка, считаем хэш-функцию, вставляем в новый массив списков
                int index = hash(current2.key, dict1.length); // значение hash-функции
                if (dict1[index] != null) {
                    ListElem current1 = dict1[index].Head; // текущий элемент списка
                    while (current1.next != null) current1 = current1.next;
                    current1.next = current2;
                }
                else dict1[index] = new List(current2);
            }
            while (current != null);
        }
        dict = dict1;
        //в новый массив вставляем новый элемент, к-й был подан на Add
        //не совпадал ни с одним словом из старого словаря
        int index = hash(key, dict.length); // значение hash-функции
        if (dict[index] != null) {
            ListElem current = dict[index].Head; // текущий элемент списка
            while (true) {
                if (current.next == null) break;
                current = current.next;
            }

            current.next = new ListElem(key);
            N++;
        }
        else {
            dict[index] = new List(key);
            N++;
        }
        return true;
    }
    //поиск слова в словаре
    public int find(String key) {
        key = key.toLowerCase();
        int index = hash(key, dict.length); // значение hash-функции
        if (dict[index] != null) {
            ListElem current = dict[index].Head; // текущий элемент списка
            do {
                if (key.equals(current.key)) return current.n;
                current = current.next;
            }
            while (current != null);
            return 0;
        }
        else return 0;
    }

    private void readfile(){
        String res, key = "";
        String s = " ,./?;:!-`()\'\"1234567890";
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            while ((res = in.readLine()) != null) {
                for(int i = 0; i < res.length(); i++){
                    int j=0;
                    while(j < s.length()){
                        if ((!key.equals("")) && (s.charAt(j) == res.charAt(i))){
                            add(key);
                            key = "";
                            break;
                        }else if(s.charAt(j) == res.charAt(i)) break;
                        j++;
                    }
                    if (j == s.length()) key = key + res.charAt(i);
                }
                if(!key.equals("")){
                    add(key);
                    key = "";
                }
            }
            in.close();
        } catch (IOException e) {
            System.out.print("Uncorrect path file");
            System.exit(1);
        }

    }
}
