package br.com.produto.table;

import br.com.produto.model.ProdutoModel;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

/**
 * TableModel customizado para exibir objetos ProdutoModel em um JTable.
 */
public class ProdutoTableModel extends AbstractTableModel {

    private List<ProdutoModel> produtos;
    private final String[] colunas = {"ID", "Nome", "Referência", "Fornecedor", "Marca", "Categoria"};

    public ProdutoTableModel() {
        this.produtos = new ArrayList<>();
    }

    @Override
    public int getRowCount() {
        return produtos.size();
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
        ProdutoModel produto = produtos.get(rowIndex);

        switch (columnIndex) {
            case 0: return produto.getId();
            case 1: return produto.getNome();
            case 2: return produto.getReferencia();
            case 3: return produto.getFornecedor();
            case 4: return produto.getMarca();
            case 5: return produto.getCategoria() != null ? produto.getCategoria().getDescricao() : "";
            default: return null;
        }
    }

    /**
     * Retorna o objeto ProdutoModel na linha especificada.
     * @param rowIndex Índice da linha.
     * @return ProdutoModel correspondente.
     */
    public ProdutoModel getProdutoAt(int rowIndex) {
        if (rowIndex >= 0 && rowIndex < produtos.size()) {
            return produtos.get(rowIndex);
        }
        return null;
    }

    /**
     * Atualiza a lista de produtos e notifica a tabela para redesenhar.
     * @param novosProdutos Nova lista de produtos.
     */
    public void setProdutos(List<ProdutoModel> novosProdutos) {
        this.produtos = novosProdutos;
        fireTableDataChanged(); // Notifica a JTable que os dados mudaram
    }
}