package br.com.estoque.table;

import br.com.estoque.model.EstoqueModel;

import javax.swing.table.AbstractTableModel;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * TableModel customizado para exibir objetos EstoqueModel em um JTable.
 */
public class EstoqueTableModel extends AbstractTableModel {

    private List<EstoqueModel> estoques;
    private final String[] colunas = {"ID", "Tipo", "Quantidade (L)", "Tanque", "Endereço", "Lote", "Validade"};
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public EstoqueTableModel() {
        this.estoques = new ArrayList<>();
    }

    @Override
    public int getRowCount() {
        return estoques.size();
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
        EstoqueModel estoque = estoques.get(rowIndex);

        switch (columnIndex) {
            case 0: return estoque.getId();
            case 1: return estoque.getTipoEstoque() != null ? estoque.getTipoEstoque().getDescricao() : "";
            case 2: return estoque.getQuantidade(); // BigDecimal é bem representado por padrão
            case 3: return estoque.getLocalTanque();
            case 4: return estoque.getLocalEndereco();
            case 5: return estoque.getLoteFabricacao();
            case 6: return estoque.getDataValidade() != null ? estoque.getDataValidade().format(DATE_FORMATTER) : "";
            default: return null;
        }
    }

    /**
     * Retorna o objeto EstoqueModel na linha especificada.
     * @param rowIndex Índice da linha.
     * @return EstoqueModel correspondente.
     */
    public EstoqueModel getEstoqueAt(int rowIndex) {
        if (rowIndex >= 0 && rowIndex < estoques.size()) {
            return estoques.get(rowIndex);
        }
        return null;
    }

    /**
     * Atualiza a lista de estoques e notifica a tabela para redesenhar.
     * @param novosEstoques Nova lista de estoques.
     */
    public void setEstoques(List<EstoqueModel> novosEstoques) {
        this.estoques = novosEstoques;
        fireTableDataChanged(); // Notifica a JTable que os dados mudaram
    }
}