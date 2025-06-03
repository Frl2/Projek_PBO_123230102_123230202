package view;

import controller.BarangController;
import model.Barang;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.util.List;

public class BarangView extends JFrame {
    private final BarangController barangController = new BarangController();
    private final JTable table = new JTable();
    private final DefaultTableModel model = new DefaultTableModel();

    private final JTextField tfNama = new JTextField();
    private final JTextField tfJumlah = new JTextField();
    private final JTextField tfLokasi = new JTextField();

    private final JButton btnTambah = new JButton("Tambah");
    private final JButton btnUpdate = new JButton("Update");
    private final JButton btnHapus = new JButton("Hapus");
    private final JButton btnRefresh = new JButton("Tampilkan Data");

    // Komponen Search (pindah ke atas tabel)
    private final JTextField tfSearch = new JTextField();
    private final JButton btnSearch = new JButton("Search");
    private final JComboBox<String> cbKategori = new JComboBox<>(new String[]{"Nama Barang", "Jumlah", "Lokasi"});

    public BarangView() {
        setTitle("Kelola Barang");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(700, 460);
        setLocationRelativeTo(null);
        setLayout(null);

        model.addColumn("ID");
        model.addColumn("Nama Barang");
        model.addColumn("Jumlah");
        model.addColumn("Lokasi");

        // Pindahkan komponen Search ke atas tabel
        JLabel lblSearch = new JLabel("Cari berdasarkan:");
        lblSearch.setBounds(20, 10, 120, 25);
        add(lblSearch);

        cbKategori.setBounds(140, 10, 120, 25);
        add(cbKategori);

        tfSearch.setBounds(270, 10, 180, 25);
        add(tfSearch);

        btnSearch.setBounds(460, 10, 90, 25);
        add(btnSearch);

        // Table & scrollpane di bawah search
        table.setModel(model);
        JScrollPane sp = new JScrollPane(table);
        sp.setBounds(20, 40, 650, 180);
        add(sp);

        // Form input data barang di bawah tabel
        JLabel lblNama = new JLabel("Nama Barang:");
        lblNama.setBounds(20, 230, 100, 25);
        add(lblNama);

        tfNama.setBounds(120, 230, 150, 25);
        add(tfNama);

        JLabel lblJumlah = new JLabel("Jumlah:");
        lblJumlah.setBounds(20, 260, 100, 25);
        add(lblJumlah);

        tfJumlah.setBounds(120, 260, 150, 25);
        add(tfJumlah);

        JLabel lblLokasi = new JLabel("Lokasi:");
        lblLokasi.setBounds(20, 290, 100, 25);
        add(lblLokasi);

        tfLokasi.setBounds(120, 290, 150, 25);
        add(tfLokasi);

        btnTambah.setBounds(300, 230, 120, 25);
        add(btnTambah);

        btnUpdate.setBounds(300, 260, 120, 25);
        add(btnUpdate);

        btnHapus.setBounds(300, 290, 120, 25);
        add(btnHapus);

        btnRefresh.setBounds(300, 320, 150, 25);
        add(btnRefresh);

        tampilkanData(); // menampilkan data awal

        btnTambah.addActionListener(e -> {
            try {
                String nama = tfNama.getText().trim();
                int jumlah = Integer.parseInt(tfJumlah.getText().trim());
                String lokasi = tfLokasi.getText().trim();

                if (nama.isEmpty() || lokasi.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Nama dan Lokasi tidak boleh kosong.");
                    return;
                }

                Barang barang = new Barang(0, nama, jumlah, lokasi);
                barangController.insertBarang(barang);
                tampilkanData();
                clearForm();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Jumlah harus berupa angka.");
            }
        });

        btnUpdate.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                try {
                    int id = Integer.parseInt(model.getValueAt(selectedRow, 0).toString());
                    String nama = tfNama.getText().trim();
                    int jumlah = Integer.parseInt(tfJumlah.getText().trim());
                    String lokasi = tfLokasi.getText().trim();

                    if (nama.isEmpty() || lokasi.isEmpty()) {
                        JOptionPane.showMessageDialog(this, "Nama dan Lokasi tidak boleh kosong.");
                        return;
                    }

                    Barang barang = new Barang(id, nama, jumlah, lokasi);
                    barangController.updateBarang(barang);
                    tampilkanData();
                    clearForm();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Jumlah harus berupa angka.");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Pilih data yang ingin diubah.");
            }
        });

        btnHapus.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                int id = Integer.parseInt(model.getValueAt(selectedRow, 0).toString());
                barangController.deleteBarang(id);
                tampilkanData();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Pilih data yang ingin dihapus.");
            }
        });

        btnRefresh.addActionListener(e -> tampilkanData());

        // Listener Search button dengan kategori
        btnSearch.addActionListener(e -> {
            String keyword = tfSearch.getText().trim().toLowerCase();
            if (keyword.isEmpty()) {
                tampilkanData();
            } else {
                String kategori = (String) cbKategori.getSelectedItem();
                cariData(keyword, kategori);
            }
        });

        // Klik baris pada tabel, data dimasukkan ke form
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = table.getSelectedRow();
                tfNama.setText(model.getValueAt(selectedRow, 1).toString());
                tfJumlah.setText(model.getValueAt(selectedRow, 2).toString());
                tfLokasi.setText(model.getValueAt(selectedRow, 3).toString());
            }
        });
    }

    private void tampilkanData() {
        model.setRowCount(0);
        List<Barang> list = barangController.getAllBarang();
        for (Barang b : list) {
            model.addRow(new Object[]{b.getId(), b.getNama(), b.getJumlah(), b.getLokasi()});
        }
    }

    // Search berdasarkan kategori yang dipilih di dropdown
    private void cariData(String keyword, String kategori) {
        model.setRowCount(0);
        List<Barang> list = barangController.getAllBarang();

        for (Barang b : list) {
            boolean match = false;
            switch (kategori) {
                case "Nama Barang":
                    match = b.getNama().toLowerCase().contains(keyword);
                    break;
                case "Jumlah":
                    match = String.valueOf(b.getJumlah()).contains(keyword);
                    break;
                case "Lokasi":
                    match = b.getLokasi().toLowerCase().contains(keyword);
                    break;
            }
            if (match) {
                model.addRow(new Object[]{b.getId(), b.getNama(), b.getJumlah(), b.getLokasi()});
            }
        }
    }

    private void clearForm() {
        tfNama.setText("");
        tfJumlah.setText("");
        tfLokasi.setText("");
    }
}
