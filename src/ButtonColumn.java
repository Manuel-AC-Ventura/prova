import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.EventObject;

public class ButtonColumn extends AbstractCellEditor implements TableCellRenderer, TableCellEditor {

    private JButton editButton;
    private JButton deleteButton;
    private JTable table;
    private Action editAction;
    private Action deleteAction;

    public ButtonColumn(JTable table, Action editAction, Action deleteAction, int editColumn, int deleteColumn) {
        this.table = table;
        this.editAction = editAction;
        this.deleteAction = deleteAction;

        editButton = new JButton("Editar");
        editButton.setFocusPainted(false);
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = table.convertRowIndexToModel(table.getEditingRow());
                editAction.actionPerformed(new ActionEvent(table, ActionEvent.ACTION_PERFORMED, "Edit:" + row));
                fireEditingStopped();
            }
        });

        deleteButton = new JButton("Apagar");
        deleteButton.setFocusPainted(false);
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = table.convertRowIndexToModel(table.getEditingRow());
                deleteAction.actionPerformed(new ActionEvent(table, ActionEvent.ACTION_PERFORMED, "Delete:" + row));
                fireEditingStopped();
            }
        });

        TableColumnModel columnModel = table.getColumnModel();
        columnModel.getColumn(editColumn).setCellRenderer(this);
        columnModel.getColumn(editColumn).setCellEditor(this);

        columnModel.getColumn(deleteColumn).setCellRenderer(this);
        columnModel.getColumn(deleteColumn).setCellEditor(this);
    }

    @Override
    public Object getCellEditorValue() {
        return "";
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        if (column == table.getColumn("Editar").getModelIndex()) {
            return editButton;
        } else if (column == table.getColumn("Apagar").getModelIndex()) {
            return deleteButton;
        }
        return null;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        if (column == table.getColumn("Editar").getModelIndex()) {
            return editButton;
        } else if (column == table.getColumn("Apagar").getModelIndex()) {
            return deleteButton;
        }
        return null;
    }

    @Override
    public boolean isCellEditable(EventObject anEvent) {
        return true;
    }
}
