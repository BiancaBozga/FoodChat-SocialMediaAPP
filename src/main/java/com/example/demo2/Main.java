package com.example.demo2;


import com.example.demo2.factory.Factory;

public class Main {
    public static void main(String[] args) {
        var container = Factory.getInstance().build();
        container.getUi().run();
    }
}