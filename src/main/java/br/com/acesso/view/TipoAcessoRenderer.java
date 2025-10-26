package br.com.acesso.view;

import br.com.acesso.enums.TipoAcesso;

import javax.swing.*;
import java.awt.*;

/**
 * Renderer customizado para o JComboBox de TipoAcesso.
 * Garante que a descrição amigável seja exibida.
 */
public class TipoAcessoRenderer extends DefaultListCellRenderer {
    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        if (value instanceof TipoAcesso) {
            setText(((TipoAcesso) value).getDescricao());
        }
        return this;
    }
}