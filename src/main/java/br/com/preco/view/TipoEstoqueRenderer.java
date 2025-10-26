package br.com.preco.view;

import br.com.preco.enums.TipoEstoque;

import javax.swing.*;
import java.awt.*;

/**
 * Renderer customizado para o JComboBox de TipoEstoque.
 * Garante que a descrição amigável seja exibida.
 */
public class TipoEstoqueRenderer extends DefaultListCellRenderer {
    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        if (value instanceof TipoEstoque) {
            setText(((TipoEstoque) value).getDescricao());
        }
        return this;
    }
}