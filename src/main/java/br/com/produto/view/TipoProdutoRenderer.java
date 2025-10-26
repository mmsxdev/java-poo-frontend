package br.com.produto.view;

import br.com.produto.enums.TipoProduto;

import javax.swing.*;
import java.awt.*;

/**
 * Renderer customizado para o JComboBox de TipoProduto.
 * Garante que a descrição amigável seja exibida.
 */
public class TipoProdutoRenderer extends DefaultListCellRenderer {
    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        if (value instanceof TipoProduto) {
            setText(((TipoProduto) value).getDescricao());
        }
        return this;
    }
}