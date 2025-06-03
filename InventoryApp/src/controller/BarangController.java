package controller;

import db.DBConnection;
import model.Barang;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BarangController {

    // Ambil semua data barang
    public List<Barang> getAllBarang() {
        List<Barang> list = new ArrayList<>();
        String sql = "SELECT * FROM barang";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Barang b = new Barang(
                    rs.getInt("id_barang"),
                    rs.getString("nama_barang"),
                    rs.getInt("jumlah"),
                    rs.getString("lokasi")
                );
                list.add(b);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Cari barang berdasarkan nama
    public List<Barang> searchBarangByName(String keyword) {
        List<Barang> list = new ArrayList<>();
        String sql = "SELECT * FROM barang WHERE nama_barang LIKE ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, "%" + keyword + "%");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Barang b = new Barang(
                    rs.getInt("id_barang"),
                    rs.getString("nama_barang"),
                    rs.getInt("jumlah"),
                    rs.getString("lokasi")
                );
                list.add(b);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Tambah barang baru
    public void insertBarang(Barang barang) {
        String sql = "INSERT INTO barang (nama_barang, jumlah, lokasi) VALUES (?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, barang.getNama());
            ps.setInt(2, barang.getJumlah());
            ps.setString(3, barang.getLokasi());

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Update data barang
    public void updateBarang(Barang barang) {
        String sql = "UPDATE barang SET nama_barang = ?, jumlah = ?, lokasi = ? WHERE id_barang = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, barang.getNama());
            ps.setInt(2, barang.getJumlah());
            ps.setString(3, barang.getLokasi());
            ps.setInt(4, barang.getId());

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Hapus barang berdasarkan id
    public void deleteBarang(int id) {
        String sql = "DELETE FROM barang WHERE id_barang = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
