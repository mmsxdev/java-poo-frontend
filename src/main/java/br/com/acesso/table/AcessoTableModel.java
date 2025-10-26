package br.com.acesso.table;

import br.com.acesso.model.AcessoModel;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

/**
 * TableModel customizado para exibir objetos AcessoModel em um JTable.
 */
public class AcessoTableModel extends AbstractTableModel {

    private List<AcessoModel> acessos;
    private final String[] colunas = {"ID", "Usuário", "Tipo de Acesso"};

    public AcessoTableModel() {
        this.acessos = new ArrayList<>();
    }

    @Override
    public int getRowCount() {
        return acessos.size();
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
        AcessoModel acesso = acessos.get(rowIndex);

        switch (columnIndex) {
            case 0: return acesso.getId();
            case 1: return acesso.getUsuario();
            case 2: return acesso.getTipoAcesso() != null ? acesso.getTipoAcesso().getDescricao() : "";
            default: return null;
        }
    }

    /**
     * Retorna o objeto AcessoModel na linha especificada.
     * @param rowIndex Índice da linha.
     * @return AcessoModel correspondente.
     */
    public AcessoModel getAcessoAt(int rowIndex) {
        if (rowIndex >= 0 && rowIndex < acessos.size()) {
            return acessos.get(rowIndex);
        }
        return null;
    }

    /**
     * Atualiza a lista de acessos e notifica a tabela para redesenhar.
     * @param novosAcessos Nova lista de acessos.
     */
    public void setAcessos(List<AcessoModel> novosAcessos) {
        this.acessos = novosAcessos;
        fireTableDataChanged(); // Notifica a JTable que os dados mudaram
    }
}