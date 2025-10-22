package br.com.view;

import br.com.enums.TipoEstoque;
import br.com.model.EstoqueDTO;
import br.com.service.EstoqueApiClient;
import br.com.service.IEstoqueService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class EstoqueFrame extends JFrame {

    private final IEstoqueService estoqueService;
    private final DefaultTableModel tableModel;
    private final JTable table;
    private final JTextField idField = new JTextField(5);
    private final JTextField quantidadeField = new JTextField(10);
    private final JComboBox<TipoEstoque> tipoEstoqueComboBox = new JComboBox<>(TipoEstoque.values());
    private final JTextField localTanqueField = new JTextField(20);
    private final JTextField localEnderecoField = new JTextField(20);
    private final JTextField loteFabricacaoField = new JTextField(15);
    private final JTextField dataValidadeField = new JTextField(10);
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE; // yyyy-MM-dd

    public EstoqueFrame() {
        // 1. Substituindo o Mock pelo cliente da API real
        this.estoqueService = new EstoqueApiClient();

        setTitle("Cadastro de Estoque");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Tabela
        String[] columnNames = {"ID", "Tipo", "Quantidade", "Local/Tanque", "Endereço", "Lote", "Data Validade"};
        tableModel = new DefaultTableModel(columnNames, 0);
        table = new JTable(tableModel);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Painel de formulário
        JPanel formPanel = new JPanel(new GridLayout(0, 2, 5, 5));
        idField.setEditable(false);
        formPanel.add(new JLabel("ID:"));
        formPanel.add(idField);
        formPanel.add(new JLabel("Quantidade:"));
        formPanel.add(quantidadeField);
        formPanel.add(new JLabel("Tipo de Estoque:"));
        formPanel.add(tipoEstoqueComboBox);
        formPanel.add(new JLabel("Local/Tanque:"));
        formPanel.add(localTanqueField);
        formPanel.add(new JLabel("Endereço:"));
        formPanel.add(localEnderecoField);
        formPanel.add(new JLabel("Lote Fabricação:"));
        formPanel.add(loteFabricacaoField);
        formPanel.add(new JLabel("Data Validade (yyyy-MM-dd):"));
        formPanel.add(dataValidadeField);

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
        salvarButton.addActionListener(e -> salvarEstoque());
        deletarButton.addActionListener(e -> deletarEstoque());

        // Carregar dados iniciais
        atualizarTabela();
    }

    private void preencherFormularioComLinhaSelecionada() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) return;

        idField.setText(tableModel.getValueAt(selectedRow, 0).toString());
        tipoEstoqueComboBox.setSelectedItem(tableModel.getValueAt(selectedRow, 1));
        quantidadeField.setText(tableModel.getValueAt(selectedRow, 2).toString());
        localTanqueField.setText(tableModel.getValueAt(selectedRow, 3).toString());
        localEnderecoField.setText(tableModel.getValueAt(selectedRow, 4).toString());
        loteFabricacaoField.setText(tableModel.getValueAt(selectedRow, 5).toString());
        dataValidadeField.setText(tableModel.getValueAt(selectedRow, 6).toString());
    }

    private void limparFormulario() {
        idField.setText("");
        quantidadeField.setText("");
        localTanqueField.setText("");
        localEnderecoField.setText("");
        loteFabricacaoField.setText("");
        dataValidadeField.setText("");
        tipoEstoqueComboBox.setSelectedIndex(0);
        table.clearSelection();
    }

    private void atualizarTabela() {
        try {
            // 2. O serviço agora retorna uma lista de EstoqueDTO
            List<EstoqueDTO> estoques = estoqueService.listarTodos();
            tableModel.setRowCount(0); // Limpa a tabela
            for (EstoqueDTO dto : estoques) {
                tableModel.addRow(new Object[]{
                        dto.getId(),
                        dto.getTipoEstoque(), // Adicionado tipo
                        dto.getQuantidade(),
                        dto.getLocalTanque(),
                        dto.getLocalEndereco(),
                        dto.getLoteFabricacao(),
                        dto.getDataValidade() != null ? dto.getDataValidade().format(dateFormatter) : ""
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao buscar estoques: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void salvarEstoque() {
        try {
            // Validações básicas
            if (quantidadeField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "O campo 'Quantidade' é obrigatório.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // 3. Coletando dados do formulário para criar o DTO
            BigDecimal quantidade = new BigDecimal(quantidadeField.getText());
            TipoEstoque tipoEstoque = (TipoEstoque) tipoEstoqueComboBox.getSelectedItem();
            String localTanque = localTanqueField.getText();
            String localEndereco = localEnderecoField.getText();
            String lote = loteFabricacaoField.getText();
            String dataValidadeTexto = dataValidadeField.getText().trim();
            LocalDate dataValidade = dataValidadeTexto.isEmpty() ? null : LocalDate.parse(dataValidadeTexto, dateFormatter);

            // 4. Criando o objeto DTO para ser enviado para a API
            EstoqueDTO estoqueDTO = new EstoqueDTO(null, tipoEstoque, quantidade, localTanque, localEndereco, lote, dataValidade);

            String idText = idField.getText();
            if (idText.isEmpty()) { // Criar novo
                estoqueService.salvar(estoqueDTO);
                JOptionPane.showMessageDialog(this, "Estoque criado com sucesso!");
            } else { // Atualizar existente
                Long id = Long.parseLong(idText);
                estoqueDTO.setId(id);
                estoqueService.atualizar(id, estoqueDTO);
                JOptionPane.showMessageDialog(this, "Estoque atualizado com sucesso!");
            }

            limparFormulario();
            atualizarTabela();

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Erro de formato numérico (Quantidade). Use '.' como separador decimal.", "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this, "Formato de data inválido. Use yyyy-MM-dd.", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar estoque: " + e.getMessage(), "Erro de API", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deletarEstoque() {
        String idText = idField.getText();
        if (idText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Selecione um item do estoque para deletar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Tem certeza que deseja deletar este item do estoque?", "Confirmação", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            Long id = Long.parseLong(idText);
            estoqueService.excluir(id);
            JOptionPane.showMessageDialog(this, "Item do estoque deletado com sucesso!");
            limparFormulario();
            atualizarTabela();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao deletar item do estoque: " + e.getMessage(), "Erro de API", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                    if ("Nimbus".equals(info.getName())) {
                        UIManager.setLookAndFeel(info.getClassName());
                        break;
                    }
                }
            } catch (Exception e) {
                // Usa o padrão
            }

            EstoqueFrame frame = new EstoqueFrame();
            frame.setVisible(true);
        });
    }
}