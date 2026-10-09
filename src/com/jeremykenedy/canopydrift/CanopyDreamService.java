package com.jeremykenedy.canopydrift;

import android.service.dreams.DreamService;

public final class CanopyDreamService extends DreamService {
    private CanopySceneView scene;

    @Override public void onAttachedToWindow() {
        super.onAttachedToWindow();
        setInteractive(false);
        setFullscreen(true);
        scene = new CanopySceneView(this);
        setContentView(scene);
    }

    @Override public void onDreamingStarted() {
        super.onDreamingStarted();
        if (scene != null) scene.start();
    }

    @Override public void onDreamingStopped() {
        if (scene != null) scene.stop();
        super.onDreamingStopped();
    }

    @Override public void onDetachedFromWindow() {
        if (scene != null) scene.stop();
        scene = null;
        super.onDetachedFromWindow();
    }
}
