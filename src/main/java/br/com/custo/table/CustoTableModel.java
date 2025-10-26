package br.com.custo.table;

import br.com.custo.model.CustoModel;

import javax.swing.table.AbstractTableModel;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * TableModel customizado para exibir objetos CustoModel em um JTable.
 */
public class CustoTableModel extends AbstractTableModel {

    private List<CustoModel> custos;
    private final String[] colunas = {"ID", "Tipo de Custo", "Imposto (%)", "Custo Fixo (R$)", "Custo Variável (R$)", "Margem Lucro (%)", "Data Processamento"};
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final NumberFormat CURRENCY_FORMATTER = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));

    public CustoTableModel() {
        this.custos = new ArrayList<>();
    }

    @Override
    public int getRowCount() {
        return custos.size();
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
        CustoModel custo = custos.get(rowIndex);

        switch (columnIndex) {
            case 0: return custo.getId();
            case 1: return custo.getTipoCusto() != null ? custo.getTipoCusto().getDescricao() : "";
            case 2: return custo.getImposto(); // Exibe como número
            case 3: return custo.getCustoFixo() != null ? CURRENCY_FORMATTER.format(custo.getCustoFixo()) : "";
            case 4: return custo.getCustoVariavel() != null ? CURRENCY_FORMATTER.format(custo.getCustoVariavel()) : "";
            case 5: return custo.getMargemLucro(); // Exibe como número
            case 6: return custo.getDataProcessamento() != null ? custo.getDataProcessamento().format(DATE_FORMATTER) : "";
            default: return null;
        }
    }

    /**
     * Retorna o objeto CustoModel na linha especificada.
     * @param rowIndex Índice da linha.
     * @return CustoModel correspondente.
     */
    public CustoModel getCustoAt(int rowIndex) {
        if (rowIndex >= 0 && rowIndex < custos.size()) {
            return custos.get(rowIndex);
        }
        return null;
    }

    /**
     * Atualiza a lista de custos e notifica a tabela para redesenhar.
     * @param novosCustos Nova lista de custos.
     */
    public void setCustos(List<CustoModel> novosCustos) {
        this.custos = novosCustos;
        fireTableDataChanged(); // Notifica a JTable que os dados mudaram
    }
}