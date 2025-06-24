package gui.table;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

/**
 * Aligns both header and cell values of every column to CENTER.
 */
public class TableHeaderAlignment implements TableCellRenderer {

    private final TableCellRenderer headerR;
    private final TableCellRenderer objectR;
    private final TableCellRenderer numberR;

    public TableHeaderAlignment(JTable table) {
        // Capture the table’s default renderers:
        headerR = table.getTableHeader().getDefaultRenderer();
        objectR = table.getDefaultRenderer(Object.class);
        numberR = table.getDefaultRenderer(Number.class);

        // Override cell renderer for Objects:
        table.setDefaultRenderer(Object.class, (tbl, val, sel, focus, row, col) -> {
            JLabel lbl = (JLabel) objectR
                    .getTableCellRendererComponent(tbl, val, sel, focus, row, col);
            lbl.setHorizontalAlignment(SwingConstants.CENTER);
            return lbl;
        });

        // Override cell renderer for Numbers (Integer, Double, etc.):
        table.setDefaultRenderer(Number.class, (tbl, val, sel, focus, row, col) -> {
            JLabel lbl = (JLabel) numberR
                    .getTableCellRendererComponent(tbl, val, sel, focus, row, col);
            lbl.setHorizontalAlignment(SwingConstants.CENTER);
            return lbl;
        });
    }

    @Override
    public Component getTableCellRendererComponent(JTable table,
                                                   Object value,
                                                   boolean isSelected,
                                                   boolean hasFocus,
                                                   int row,
                                                   int column) {
        // Render the header cell, then center it:
        JLabel lbl = (JLabel) headerR
                .getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        lbl.setHorizontalAlignment(SwingConstants.CENTER);
        return lbl;
    }
}
