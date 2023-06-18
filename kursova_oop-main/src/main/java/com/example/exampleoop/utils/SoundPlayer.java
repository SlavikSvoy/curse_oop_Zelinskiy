package com.example.exampleoop.utils;

import javafx.concurrent.Task;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class SoundPlayer {
//        public static final String BACKGROUND_MUSIC_PATH = "src/main/resources/com/example/exampleoop/sounds/background_music.wav";
    public static final String ZOMBIE_SOUND_PATH = "src/main/resources/com/example/exampleoop/sounds/zombie_sound.wav";
    public static final String ZOMBIE_DIE_SOUND_PATH = "src/main/resources/com/example/exampleoop/sounds/zombie-die_sound.wav";

    public void playSound(String pathToFile, boolean isBackgroundMusic) {
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() {
                try {
                    File soundFile = new File(pathToFile);
                    AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);
                    Clip clip = AudioSystem.getClip();
                    clip.open(audioIn);

                    if (isBackgroundMusic) clip.loop(Clip.LOOP_CONTINUOUSLY);
                    else {
                        clip.start();
                        while (clip.getMicrosecondLength() != clip.getMicrosecondPosition()) {
                            Thread.sleep(10);
                        }
                        clip.stop();
                        clip.close();
                    }
                } catch (IOException | UnsupportedAudioFileException | LineUnavailableException |
                         InterruptedException e) {
                    e.printStackTrace();
                }
                return null;
            }
        };

        Thread thread = new Thread(task);
        thread.start();
    }
}
