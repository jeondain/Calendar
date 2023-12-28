package sku.sw.views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import sku.sw.dao.DiaryDao;
import sku.sw.model.Diary;

public class DiaryList extends JDialog {
    private DiaryDao diaryDao;
    
	DefaultTableModel model = null;
	
    public DiaryList(JFrame parent, DiaryDao diaryDao, int year, int month) {
    	
        super(parent, "다이어리 목록");
        this.diaryDao = diaryDao;
        
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("id");
        model.addColumn("날짜");
        model.addColumn("제목");

        List<Diary> diaryEntries = diaryDao.findAllByMonth(year, month);

        JPanel panel = new JPanel(new BorderLayout());
        JTable table = new JTable();

        table.setRowHeight(25); 
        table.setShowGrid(false); 

        JTableHeader header = table.getTableHeader();
        header.setBackground(Color.lightGray);
        header.setFont(new Font("맑은 고딕", Font.BOLD, 12));
        
        table.setSelectionBackground(Color.gray);
        table.setSelectionForeground(Color.white);
        
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    int selectedRow = table.getSelectedRow();
                    Long selectedEvent = (Long)table.getValueAt(selectedRow, 0);
                    LocalDate Date = (LocalDate)table.getValueAt(selectedRow, 1);
                    
                    new DiaryView(CalendarView.getInstance(), Date.getYear(), Date.getMonthValue(), Date.getDayOfMonth(), selectedEvent, model, selectedRow);
                }
            }
        });
        
        Collections.sort(diaryEntries, Comparator.comparing(Diary::getDate));
        Collections.reverse(diaryEntries);
        
        for (Diary entry : diaryEntries) {
            Object[] rowData = { entry.getId(), entry.getDate(), entry.getSubject() };
            model.addRow(rowData);
        }

        table.setModel(model);
        
        TableColumnModel columnModel = table.getColumnModel();
        columnModel.getColumn(0).setMinWidth(0);
        columnModel.getColumn(0).setMaxWidth(0);
        columnModel.getColumn(1).setMinWidth(100);
        columnModel.getColumn(1).setMaxWidth(200);

        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);

        getContentPane().add(panel);
        pack();
        setLocationRelativeTo(parent);
        setVisible(true);
    }

}

