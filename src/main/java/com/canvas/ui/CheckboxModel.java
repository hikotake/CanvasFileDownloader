package com.canvas.ui;

import java.awt.BorderLayout;

// http://www.java2s.com/Tutorials/Java/Swing_How_to/JList/Create_JList_of_CheckBox.htm

import java.awt.Component;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;

import com.canvas.utils.structs.*;

public class CheckboxModel {
    private DefaultListModel<Course> listModel;
    private Boolean isRunning = true;

    public CheckboxModel(List<Course> courseList) {
        JFrame frame = new JFrame("Select whitelist Course");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        listModel = new DefaultListModel<>();

        JButton okButton = new JButton("OK");
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                isRunning = false;
                frame.dispose();
                return;
            }
        });

        courseList.forEach(course -> {
            listModel.addElement(course);
        });

        JList<Course> list = new JList<>(listModel);
        list.setCellRenderer(new CheckListRenderer());
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setVisibleRowCount(15);
        list.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent event) {
                JList list = (JList) event.getSource();
                int index = list.locationToIndex(event.getPoint());// Get index of item
                                                                   // clicked
                Course item = (Course) list.getModel().getElementAt(index);
                item.setIswhitelisted(!item.getIswhitelisted()); // Toggle selected state
                list.repaint(list.getCellBounds(index, index));// Repaint cell
            }
        });
        frame.getContentPane().add(new JScrollPane(list));
        frame.pack();
        frame.add(okButton, BorderLayout.SOUTH);
        frame.setVisible(true);
    }

    public List<Course> getList() {
        List<Course> result = new ArrayList<>();

        for (int i = 0; i < listModel.size(); i++) {
            if (listModel.getElementAt(i).getIswhitelisted() == true) {
                result.add(listModel.getElementAt(i));
            }
        }

        return result;
    }

    public boolean getIsRunning() {
        return this.isRunning;
    }
}


class CheckListRenderer extends JCheckBox implements ListCellRenderer {
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean hasFocus) {
        setEnabled(list.isEnabled());
        setSelected(((Course) value).getIswhitelisted());
        setFont(list.getFont());
        setBackground(list.getBackground());
        setForeground(list.getForeground());
        setText(((Course) value).getName());
        return this;
    }
}
