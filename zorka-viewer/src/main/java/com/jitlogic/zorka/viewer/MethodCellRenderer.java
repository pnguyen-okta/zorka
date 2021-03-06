/**
 * Copyright 2012 Rafal Lewczuk <rafal.lewczuk@jitlogic.com>
 * <p/>
 * This is free software. You can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * <p/>
 * This software is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * <p/>
 * You should have received a copy of the GNU General Public License
 * along with this software. If not, see <http://www.gnu.org/licenses/>.
 */

package com.jitlogic.zorka.viewer;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.util.Map;

public class MethodCellRenderer extends JLabel implements TableCellRenderer {

    public static final int SINGLE_LEVEL = 12;

    private ViewerTraceRecord record;
    private ImageIcon icnTreePlus, icnTreeMinus;


    public MethodCellRenderer() {
        setOpaque(true);
        icnTreePlus = ResourceManager.getIcon12x12("tree-plus");
        icnTreeMinus = ResourceManager.getIcon12x12("tree-minus");
    }


    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus,
                                                   int row, int column) {

        if (isSelected) {
            setForeground(table.getSelectionForeground());
            setBackground(table.getSelectionBackground());
        } else {
            setForeground(table.getForeground());
            setBackground(table.getBackground());
        }

        setFont(table.getFont());

        if (hasFocus) {
            setBorder(UIManager.getBorder("Table.focusCellHighlightBorder"));
            if (table.isCellEditable(row, column)) {
                setForeground(UIManager.getColor("Table.focusCellForeground"));
                setBackground(UIManager.getColor("Table.focusCellBackground"));
            }
        } else {
            setBorder(new EmptyBorder(1,2,1,2));
        }

        record = (ViewerTraceRecord)value;

        setSize(new Dimension(table.getTableHeader().getColumnModel().getColumn(column).getWidth(), 1000));

        int prefh = 16 + record.numAttrs() * 16;

        if (table.getRowHeight(row) != prefh) {
            table.setRowHeight(row, prefh);
        }

        return this;
    }


    public void paint(Graphics g) {

        super.paint(g);

        if (record == null) {
            return;
        }

        int offs = record.getLevel() * SINGLE_LEVEL + 16;

        if (0 != record.numChildren()) {
            ImageIcon icon = record.isExpanded() ? icnTreeMinus : icnTreePlus;
            g.drawImage(icon.getImage(), offs - 14, 2, null);
        }

        Graphics2D g2 = (Graphics2D)g;
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g.setFont(record.hasFlag(ViewerTraceRecord.TRACE_BEGIN) ? getFont().deriveFont(Font.BOLD) : getFont());
        g.setColor(record.hasError() ? Color.RED : Color.BLACK);
        g.drawString(record.prettyPrint(), offs, 13);

        if (record.numAttrs() > 0) {
            int line = 1;
            g.setFont(getFont().deriveFont(Font.BOLD));
            g.setColor(Color.BLUE);
            for (Map.Entry<?,?> e : record.getAttrs().entrySet()) {
                g.drawString(record.sym((Long)e.getKey()) + "=" + e.getValue(), offs + 8, 13 + line*16);
                line++;
            }
        }

    }
}
