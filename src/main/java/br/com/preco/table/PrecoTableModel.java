package br.com.preco.table;

import br.com.preco.model.PrecoModel;

import javax.swing.table.AbstractTableModel;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * TableModel customizado para exibir objetos PrecoModel em um JTable.
 */
public class PrecoTableModel extends AbstractTableModel {

    private List<PrecoModel> precos;
    private final String[] colunas = {"ID", "Tipo de Combustível", "Valor (R$)", "Data de Vigência"};
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final NumberFormat CURRENCY_FORMATTER = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));

    public PrecoTableModel() {
        this.precos = new ArrayList<>();
    }

    @Override
    public int getRowCount() {
        return precos.size();
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
        PrecoModel preco = precos.get(rowIndex);

        switch (columnIndex) {
            case 0: return preco.getId();
            case 1: return preco.getTipoEstoque() != null ? preco.getTipoEstoque().getDescricao() : "";
            case 2: return preco.getValor() != null ? CURRENCY_FORMATTER.format(preco.getValor()) : "";
            case 3: return preco.getDataVigencia() != null ? preco.getDataVigencia().format(DATE_FORMATTER) : "";
            default: return null;
        }
    }

    /**
     * Retorna o objeto PrecoModel na linha especificada.
     * @param rowIndex Índice da linha.
     * @return PrecoModel correspondente.
     */
    public PrecoModel getPrecoAt(int rowIndex) {
        if (rowIndex >= 0 && rowIndex < precos.size()) {
            return precos.get(rowIndex);
        }
        return null;
    }

    /**
     * Atualiza a lista de preços e notifica a tabela para redesenhar.
     * @param novosPrecos Nova lista de preços.
     */
    public void setPrecos(List<PrecoModel> novosPrecos) {
        this.precos = novosPrecos;
        fireTableDataChanged(); // Notifica a JTable que os dados mudaram
    }
}