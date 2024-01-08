package com.example.studentsync.utils;

import com.example.studentsync.utils.enums.SceneIdentifier;
public class SceneController {

    public void changeScene(SceneIdentifier newScene, int width, int height){
        ApplicationHandler.getInstance().changeScene(newScene,width,height);
    }

    public void closeApplication(){
        ApplicationHandler.getInstance().closeApplication();
    }
}
