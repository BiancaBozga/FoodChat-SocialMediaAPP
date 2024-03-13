package com.example.demo2;

public interface Observable {
    void addObserver(Observer o);
    void removeObsrver(Observer o);
    void notifyObservers();

}
