package sku.sw.components;

import javax.swing.*;
import java.awt.*;

public class Button extends JButton {
    public Button(String text, Color bgColor, Color txtColor) {
    	super(text);
        setBackground(bgColor);
        setForeground(txtColor);
        setFocusPainted(false);
        setOpaque(true);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }
}
