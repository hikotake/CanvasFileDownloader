package com.canvas.ui;

import javax.swing.JFileChooser;

public class DirectorySelector {
    private String selectedPath;

    public DirectorySelector() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int returnVal = chooser.showOpenDialog(chooser);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            this.selectedPath = chooser.getSelectedFile().getPath();
        }
    }

    public String getSelectedPath() {
        return this.selectedPath;
    }
}
