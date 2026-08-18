package com.musicservice;

import com.musicservice.controller.Implementation.Controller;

public class Main {

    public static void main(String[] args) {
        Dependency dependency = new Dependency();
        Controller controller = dependency.getController();
        controller.start();
    }
}