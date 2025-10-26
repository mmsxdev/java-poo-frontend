package br.com.pessoa.view;

import br.com.pessoa.enums.TipoPessoa;

import javax.swing.*;
        import java.awt.*;

/**
 * Um Renderer customizado para o JComboBox de TipoPessoa.
 * Garante que a descrição amigável ("Pessoa Física") seja exibida,
 * enquanto o valor subjacente permanece o objeto enum.
 */
public class TipoPessoaRenderer extends DefaultListCellRenderer {
    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        if (value instanceof TipoPessoa) {
            setText(((TipoPessoa) value).getDescricao()); // Exibe "Pessoa Física" ou "Pessoa Jurídica"
        } else if (value == null) {
            setText(""); // Se o valor for nulo, exibe um texto vazio
        }
        return this;
    }
}