package br.com.contato.table;

import br.com.contato.model.ContatoModel;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

/**
 * TableModel customizado para exibir objetos ContatoModel em um JTable.
 */
public class ContatoTableModel extends AbstractTableModel {

    private List<ContatoModel> contatos;
    private final String[] colunas = {"ID", "Telefone", "E-mail", "Endereço"};

    public ContatoTableModel() {
        this.contatos = new ArrayList<>();
    }

    @Override
    public int getRowCount() {
        return contatos.size();
    }

    @Override
    public int getColumnCount() {
        return colunas.length;
    }

    @Override
    public String getColumnName(int column) {
        return colunas[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        ContatoModel contato = contatos.get(rowIndex);

        switch (columnIndex) {
            case 0: return contato.getId();
            case 1: return contato.getTelefone();
            case 2: return contato.getEmail();
            case 3: return contato.getEndereco();
            default: return null;
        }
    }

    /**
     * Retorna o objeto ContatoModel na linha especificada.
     * @param rowIndex Índice da linha.
     * @return ContatoModel correspondente.
     */
    public ContatoModel getContatoAt(int rowIndex) {
        if (rowIndex >= 0 && rowIndex < contatos.size()) {
            return contatos.get(rowIndex);
        }
        return null;
    }

    /**
     * Atualiza a lista de contatos e notifica a tabela para redesenhar.
     * @param novosContatos Nova lista de contatos.
     */
    public void setContatos(List<ContatoModel> novosContatos) {
        this.contatos = novosContatos;
        fireTableDataChanged(); // Notifica a JTable que os dados mudaram
    }
}