package sku.sw.views;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import lombok.Getter;
import lombok.Setter;
import sku.sw.model.Schedule;

@Getter
@Setter
public class EventLabel extends JLabel{
	
	Schedule s;
	
	public EventLabel(Schedule s) {
		super(s.getSubject());
		setOpaque(true); 
		this.s = s;
		this.updateUI();
	}
}
