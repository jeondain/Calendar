package sku.sw.test;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

import sku.sw.model.Schedule;

class Test1 extends JFrame{
    JPanel contentPane=new JPanel();
    String s;
    
    Test1(){
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        this.setContentPane(contentPane);
        
        // 특정 일자의 날자패널 추가
        DatePanel p = new DatePanel(2023, 11, 02);
        
        contentPane.add(p);
        
        this.setSize(300,300);
        this.setVisible(true);
        contentPane.requestFocus();
    }

    public static void main(String[] args) {
        new Test1();
    }
}
