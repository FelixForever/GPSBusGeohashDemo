package com.example;

public class MyClass {
    public static void main(String[] args) {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        for (int i = 0; i < 10; i++) {
            sb.append("'").append(i).append("'");
            sb.append(",");
        }
        for (int i = 0; i < 26; i++) {
            sb.append("'").append(((char) (i + 'a'))).append("'");
            sb.append(",");
        }
        sb.append("}");
        System.out.print(sb.toString());
    }
}
