package br.com.view;

import br.com.model.PessoaDTO;
import br.com.enums.TipoPessoa;
import br.com.service.IPessoaService;
import br.com.service.PessoaApiClient;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class PessoaFrame extends JFrame {

    private final IPessoaService pessoaService;
    private final DefaultTableModel tableModel;
    private final JTable table;
    private final JTextField idField = new JTextField(5);
    private final JTextField nomeField = new JTextField(20);
    private final JTextField cpfCnpjField = new JTextField(15);
    private final JTextField ctpsField = new JTextField(10);
    private final JTextField dataNascimentoField = new JTextField(10);
    private final JComboBox<TipoPessoa> tipoPessoaComboBox = new JComboBox<>(TipoPessoa.values());

    public PessoaFrame() {
        // Conectando ao cliente da API real
        this.pessoaService = new PessoaApiClient();

        setTitle("Cadastro de Pessoas");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Fecha apenas esta janela
        setLocationRelativeTo(null);

        // Tabela
        String[] columnNames = {"ID", "Nome Completo", "CPF/CNPJ", "CTPS", "Data Nasc.", "Tipo"};
        tableModel = new DefaultTableModel(columnNames, 0);
        table = new JTable(tableModel);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Painel de formulário
        JPanel formPanel = new JPanel(new GridLayout(0, 2, 5, 5));
        idField.setEditable(false);
        formPanel.add(new JLabel("ID:"));
        formPanel.add(idField);
        formPanel.add(new JLabel("Nome Completo:"));
        formPanel.add(nomeField);
        formPanel.add(new JLabel("CPF/CNPJ:"));
        formPanel.add(cpfCnpjField);
        formPanel.add(new JLabel("Nº CTPS:"));
        formPanel.add(ctpsField);
        formPanel.add(new JLabel("Data Nascimento (yyyy-MM-dd):"));
        formPanel.add(dataNascimentoField);
        formPanel.add(new JLabel("Tipo Pessoa:"));
        formPanel.add(tipoPessoaComboBox);

        // Painel de botões
        JPanel buttonPanel = new JPanel();
        JButton novoButton = new JButton("Novo");
        JButton salvarButton = new JButton("Salvar");
        JButton deletarButton = new JButton("Deletar");
        JButton limparButton = new JButton("Limpar");

        buttonPanel.add(novoButton);
        buttonPanel.add(salvarButton);
        buttonPanel.add(deletarButton);
        buttonPanel.add(limparButton);

        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.add(formPanel, BorderLayout.CENTER);
        southPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(southPanel, BorderLayout.SOUTH);

        // Listeners
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() != -1) {
                preencherFormularioComLinhaSelecionada();
            }
        });

        novoButton.addActionListener(e -> limparFormulario());
        limparButton.addActionListener(e -> limparFormulario());
        salvarButton.addActionListener(e -> salvarPessoa());
        deletarButton.addActionListener(e -> deletarPessoa());

        // Carregar dados iniciais
        atualizarTabela();
    }

    private void preencherFormularioComLinhaSelecionada() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) return;

        // Função auxiliar para obter valor da tabela de forma segura
        Object idValue = tableModel.getValueAt(selectedRow, 0);
        Object nomeValue = tableModel.getValueAt(selectedRow, 1);
        Object cpfCnpjValue = tableModel.getValueAt(selectedRow, 2);
        Object ctpsValue = tableModel.getValueAt(selectedRow, 3);
        Object dataNascValue = tableModel.getValueAt(selectedRow, 4);
        Object tipoPessoaValue = tableModel.getValueAt(selectedRow, 5);

        idField.setText(idValue != null ? idValue.toString() : "");
        nomeField.setText(nomeValue != null ? nomeValue.toString() : "");
        cpfCnpjField.setText(cpfCnpjValue != null ? cpfCnpjValue.toString() : "");
        ctpsField.setText(ctpsValue != null ? ctpsValue.toString() : "");
        dataNascimentoField.setText(dataNascValue != null ? dataNascValue.toString() : "");
        tipoPessoaComboBox.setSelectedItem(tipoPessoaValue);
    }

    private void limparFormulario() {
        idField.setText("");
        nomeField.setText("");
        cpfCnpjField.setText("");
        ctpsField.setText("");
        dataNascimentoField.setText("");
        tipoPessoaComboBox.setSelectedIndex(0);
        table.clearSelection();
    }

    private void atualizarTabela() {
        try {
            List<PessoaDTO> pessoas = pessoaService.listarTodos();
            tableModel.setRowCount(0); // Limpa a tabela
            for (PessoaDTO p : pessoas) {
                tableModel.addRow(new Object[]{
                        p.getId(),
                        p.getNomeCompleto(),
                        p.getCpfCnpj(),
                        p.getNumeroCtps(),
                        p.getDataNascimento().format(DateTimeFormatter.ISO_LOCAL_DATE),
                        p.getTipoPessoa()
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao buscar pessoas: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void salvarPessoa() {
        try {
            String nome = nomeField.getText();
            String cpfCnpj = cpfCnpjField.getText();
            Long ctps = ctpsField.getText().isEmpty() ? null : Long.parseLong(ctpsField.getText());
            LocalDate dataNascimento;
            try {
                dataNascimento = LocalDate.parse(dataNascimentoField.getText(), DateTimeFormatter.ISO_LOCAL_DATE);
            } catch (DateTimeParseException e) {
                JOptionPane.showMessageDialog(this, "Formato de data inválido. Use yyyy-MM-dd.", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
                return;
            }

            TipoPessoa tipoPessoa = (TipoPessoa) tipoPessoaComboBox.getSelectedItem();

            PessoaDTO pessoaDTO = new PessoaDTO(null, nome, cpfCnpj, ctps, dataNascimento, tipoPessoa);

            String idText = idField.getText();
            if (idText.isEmpty()) { // Criar nova pessoa
                pessoaService.salvar(pessoaDTO);
                JOptionPane.showMessageDialog(this, "Pessoa criada com sucesso!");
            } else { // Atualizar pessoa existente
                Long id = Long.parseLong(idText);
                pessoaDTO.setId(id);
                pessoaService.atualizar(id, pessoaDTO);
                JOptionPane.showMessageDialog(this, "Pessoa atualizada com sucesso!");
            }

            limparFormulario();
            atualizarTabela();

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Erro de formato numérico (CTPS).", "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar pessoa: " + e.getMessage(), "Erro de API", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deletarPessoa() {
        String idText = idField.getText();
        if (idText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Selecione uma pessoa para deletar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Tem certeza que deseja deletar esta pessoa?", "Confirmação", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            Long id = Long.parseLong(idText);
            pessoaService.excluir(id);
            JOptionPane.showMessageDialog(this, "Pessoa deletada com sucesso!");
            limparFormulario();
            atualizarTabela();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "ID inválido.", "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao deletar pessoa: " + e.getMessage(), "Erro de API", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        // Garante que a UI seja executada na Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(() -> {
            // Tenta usar um Look and Feel mais moderno se disponível
            try {

                for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                    if ("Nimbus".equals(info.getName())) {
                        UIManager.setLookAndFeel(info.getClassName());
                        break;
                    }
                }
            } catch (Exception e) {
                // Se o Nimbus não estiver disponível, usa o padrão do sistema
            }

            PessoaFrame frame = new PessoaFrame();
            frame.setVisible(true);
        });
    }
}