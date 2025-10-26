package br.com.custo.view;

import br.com.custo.enums.TipoCusto;

import javax.swing.*;
import java.awt.*;

/**
 * Renderer customizado para o JComboBox de TipoCusto.
 * Garante que a descrição amigável seja exibida.
 */
public class TipoCustoRenderer extends DefaultListCellRenderer {
    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        if (value instanceof TipoCusto) {
            setText(((TipoCusto) value).getDescricao());
        }
        return this;
    }
}