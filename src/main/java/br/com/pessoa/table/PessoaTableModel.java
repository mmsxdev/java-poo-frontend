package br.com.pessoa.table;

import br.com.pessoa.model.PessoaModel;

import javax.swing.table.AbstractTableModel;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * TableModel customizado para exibir objetos PessoaModel em um JTable.
 */
public class PessoaTableModel extends AbstractTableModel {

    private List<PessoaModel> pessoas;
    private String[] colunas = {"ID", "Nome Completo", "CPF/CNPJ", "CTPS", "Data Nasc.", "Tipo"};
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public PessoaTableModel() {
        this.pessoas = new ArrayList<>();
    }

    public PessoaTableModel(List<PessoaModel> pessoas) {
        this.pessoas = pessoas;
    }

    @Override
    public int getRowCount() {
        return pessoas.size();
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
        PessoaModel pessoa = pessoas.get(rowIndex);

        switch (columnIndex) {
            case 0: return pessoa.getId();
            case 1: return pessoa.getNomeCompleto();
            case 2: return pessoa.getCpfCnpj();
            case 3: return pessoa.getNumeroCtps();
            case 4: return pessoa.getDataNascimento() != null ? pessoa.getDataNascimento().format(DATE_FORMATTER) : "";
            case 5: return pessoa.getTipoPessoa() != null ? pessoa.getTipoPessoa().getDescricao() : "";
            default: return null;
        }
    }

    /**
     * Retorna o objeto PessoaModel na linha especificada.
     * @param rowIndex Índice da linha.
     * @return PessoaModel correspondente.
     */
    public PessoaModel getPessoaAt(int rowIndex) {
        return pessoas.get(rowIndex);
    }

    /**
     * Atualiza a lista de pessoas e notifica a tabela para redesenhar.
     * @param novasPessoas Nova lista de pessoas.
     */
    public void setPessoas(List<PessoaModel> novasPessoas) {
        this.pessoas = novasPessoas;
        fireTableDataChanged(); // Notifica a JTable que os dados mudaram
    }

    /**
     * Adiciona uma pessoa à lista e notifica a tabela.
     * @param pessoa Pessoa a ser adicionada.
     */
    public void addPessoa(PessoaModel pessoa) {
        this.pessoas.add(pessoa);
        fireTableRowsInserted(pessoas.size() - 1, pessoas.size() - 1);
    }

    // Métodos para remover e atualizar podem ser adicionados conforme a necessidade
}