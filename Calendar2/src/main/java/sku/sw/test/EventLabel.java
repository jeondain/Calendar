package sku.sw.test;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.border.Border;

import lombok.Getter;
import lombok.Setter;
import sku.sw.model.Schedule;

@Getter
@Setter
public class EventLabel extends JLabel {
	Schedule s;
	boolean selected;

	public EventLabel(Schedule s) {
		super(s.getSubject());
		this.setBackground(Color.white);
		this.s = s;
//		addMouseListener(this);

//		addMouseListener(new MouseAdapter() {
//			@Override
//			public void mousePressed(MouseEvent mouseEvent) {
//				System.out.println(s.getId() + "," + s.getSubject() + " clicked.");
//				
//				selected = !selected;
//				
//				revalidate();
//				repaint();
//				updateUI();
//			}
//		});
	}

//	@Override
//	protected void paintComponent(Graphics g) {
//		super.paintComponent(g);
//		
//		if(selected) {
//			Border blackline = BorderFactory.createLineBorder(Color.black);
//			this.setBorder(blackline);
//		}
//		else {
//			this.setBorder(null);
//		}
//		
//
//	}
}
