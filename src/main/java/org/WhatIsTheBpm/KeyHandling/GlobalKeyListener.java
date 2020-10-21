package org.WhatIsTheBpm.KeyHandling;

import org.WhatIsTheBpm.Controllers.SceneController;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

public class GlobalKeyListener implements NativeKeyListener {
    private SceneController controller;

    //this hook is cool as fuck thanks to the dev
    public void nativeKeyPressed(NativeKeyEvent e) {
        controller.down(e.getKeyText(e.getKeyCode()));
    }
    public void addController(SceneController controller) {
        this.controller = controller;
    }

    public void nativeKeyReleased(NativeKeyEvent e) {
        controller.up(e.getKeyText(e.getKeyCode()));
    }

    public void nativeKeyTyped(NativeKeyEvent e) {
        //
    }
}