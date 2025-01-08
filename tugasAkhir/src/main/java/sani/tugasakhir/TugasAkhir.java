package sani.tugasakhir;
import java.io.*;
import java.text.ParseException;
import java.util.*;
import java.text.SimpleDateFormat;
import java.util.concurrent.TimeUnit;

public class TugasAkhir {
    public static ArrayList<Mobil> daftarMobil = new ArrayList<>();
    public static ArrayList<DataSewa> daftarSewa = new ArrayList<>();
    public static BufferedReader Masukan = new BufferedReader(new InputStreamReader(System.in));
    public static boolean lanjutMobil = true; 
    
    static class Mobil {
        int id;
        String namaMobil;
        double biaya;
        boolean tersedia;
        boolean tergantungTujuan;
        
        Mobil(int id, String namaMobil, double biaya, boolean tergantungTujuan) {
            this.id = id;
            this.namaMobil = namaMobil;
            this.biaya = biaya;
            this.tersedia = true;
            this.tergantungTujuan = tergantungTujuan;
        }
    }

    static class DataSewa {
        int idSewa;
        int idMobil;
        String nik;
        String nama;
        boolean supir;
        String tujuan;
        double totalBiaya;
        Date tanggalBerangkat;
        Date tanggalKembali;
        
        DataSewa(int idSewa, int idMobil, String nik, String nama, boolean supir, 
                String tujuan, double totalBiaya, Date tanggalBerangkat, Date tanggalKembali) {
            this.idSewa = idSewa;
            this.idMobil = idMobil;
            this.nik = nik;
            this.nama = nama;
            this.supir = supir;
            this.tujuan = tujuan;
            this.totalBiaya = totalBiaya;
            this.tanggalBerangkat = tanggalBerangkat;
            this.tanggalKembali = tanggalKembali;
        }
    }

    public static Mobil dapatkanMobilDariId(int idMobil) {
        for(Mobil mobil : daftarMobil) {
            if(mobil.id == idMobil) {
                return mobil;
            }
        }
        return null;  
    }

    private static void inisialisasiContohDataSewa() {
        try {
            SimpleDateFormat formatTanggal = new SimpleDateFormat("dd-MM-yyyy");
            daftarSewa.add(new DataSewa(
                1,                             
                1,                            
                "3525015201882",           
                "John Doe",                     
                true,                           
                "Surabaya",                     
                800000,                        
                formatTanggal.parse("01-01-2025"),      
                formatTanggal.parse("03-01-2025")        
            ));
        } catch (ParseException e) {
            System.out.println("Error initializing sample data: " + e.getMessage());
        }
    }

    public static void tampilkanSewa() {
        SimpleDateFormat formatTanggal = new SimpleDateFormat("dd-MM-yyyy");
        System.out.println("All Rental Data");
        System.out.println("======================================================================================================================================================");
        System.out.printf("|%-4s|%-15s|%-15s|%-18s|%-8s|%-12s|%-15s|%-12s|%-23s|%-16s|\n",
                "No.",
                "Nama",
                "NIK",
                "Mobil",
                "Supir",
                "Tujuan",
                "Total Biaya",
                "Durasi (hari)",
                "Jadwal Keberangkatan",
                "Jadwal Kembali");
        System.out.println("======================================================================================================================================================");
        if(daftarSewa.isEmpty()){
            System.out.println("Tidak Ada Data");
        }
        for(int i = 0; i < daftarSewa.size(); i++) {
            DataSewa sewa = daftarSewa.get(i);
            Mobil mobil = dapatkanMobilDariId(sewa.idMobil);
            String namaMobil = mobil != null ? mobil.namaMobil : "Unknown";
            long selisihMiliDetik = sewa.tanggalKembali.getTime() - sewa.tanggalBerangkat.getTime();
            long selisihHari = TimeUnit.DAYS.convert(selisihMiliDetik, TimeUnit.MILLISECONDS);
            System.out.printf("|%-4d|%-15s|%-15s|%-18s|%-8s|%-12s|Rp %-12.0f|%-13d|%-23s|%-16s|\n",
                i + 1,
                sewa.nama,
                sewa.nik,
                namaMobil,
                sewa.supir ? "Ya" : "Tidak",
                sewa.tujuan,
                sewa.totalBiaya,
                selisihHari,
                formatTanggal.format(sewa.tanggalBerangkat),
                formatTanggal.format(sewa.tanggalKembali));
        }
        System.out.println("======================================================================================================================================================");
    }

    public static void tambahSewa() throws IOException, ParseException {
        SimpleDateFormat formatTanggal = new SimpleDateFormat("dd-MM-yyyy");
        boolean lanjutPelanggan = true;
        int idSewa = daftarSewa.size() + 1;
        while(lanjutPelanggan) {
            System.out.print("Masukkan NIK: ");
            String nik = Masukan.readLine();
            System.out.print("Masukkan Nama: ");
            String nama = Masukan.readLine();
            boolean lanjutSewa = true;
            while(lanjutSewa && !daftarMobil.isEmpty()) {
                tampilkanSemuaMobil();
                System.out.print("Pilih nomor mobil (1-" + daftarMobil.size() + "): ");
                int indeksMobil = Integer.parseInt(Masukan.readLine()) - 1;
                if(indeksMobil >= 0 && indeksMobil < daftarMobil.size()) {
                    Mobil mobilDipilih = daftarMobil.get(indeksMobil);
                    if(!mobilDipilih.tersedia) {
                        System.out.println("Mobil tidak tersedia untuk disewa!");
                        continue;
                    }
                    System.out.print("Menggunakan Supir? (y/t): ");
                    boolean gunakanSupir = Masukan.readLine().equalsIgnoreCase("y");
                    System.out.print("Masukkan Tanggal Berangkat (dd-MM-yyyy): ");
                    Date tanggalBerangkat = formatTanggal.parse(Masukan.readLine());
                    System.out.print("Masukkan Tanggal Kembali (dd-MM-yyyy): ");
                    Date tanggalKembali = formatTanggal.parse(Masukan.readLine());
                    long selisihMiliDetik = tanggalKembali.getTime() - tanggalBerangkat.getTime();
                    long selisihHari = TimeUnit.DAYS.convert(selisihMiliDetik, TimeUnit.MILLISECONDS);
                    double totalBiaya;
                    String tujuan = "-";
                    if(mobilDipilih.tergantungTujuan) {
                        System.out.print("Masukkan Tujuan: ");
                        tujuan = Masukan.readLine();
                        System.out.print("Masukkan Biaya Sesuai Tujuan: ");
                        totalBiaya = Double.parseDouble(Masukan.readLine());
                    } else {
                        System.out.print("Masukkan Tujuan: ");
                        tujuan = Masukan.readLine();
                        totalBiaya = mobilDipilih.biaya * selisihHari;
                    }
                    if(gunakanSupir) {
                        totalBiaya += 500000 * selisihHari;
                    }
                    daftarSewa.add(new DataSewa(idSewa, mobilDipilih.id, nik, nama, gunakanSupir, 
                                             tujuan, totalBiaya, tanggalBerangkat, tanggalKembali));
                    mobilDipilih.tersedia = false;
                    System.out.println("Rental berhasil ditambahkan!");
                    System.out.println("Total Biaya: Rp " + String.format("%,.0f", totalBiaya));
                    System.out.print("Sewa mobil lain untuk pelanggan yang sama? (y/t): ");
                    if(Masukan.readLine().equalsIgnoreCase("t")) {
                        lanjutSewa = false;
                    }
                    idSewa++;
                } else {
                    System.out.println("Nomor mobil tidak valid!");
                }
            }
            System.out.print("Tambah pelanggan baru? (y/t): ");
            if(Masukan.readLine().equalsIgnoreCase("t")) {
                lanjutPelanggan = false;
            }
        }
    }

    public static void tampilkanMenu(){
        System.out.println("1. Tampilkan semua mobil");
        System.out.println("2. Tambah mobil");
        System.out.println("3. Hapus mobil");
        System.out.println("4. Edit mobil");
        System.out.println("5. Tampilkan semua data rental");
        System.out.println("6. Masukan data rental");
        System.out.println("7. Edit data rental");
        System.out.println("8. Hapus data rental");
        System.out.println("9. Cetak laporan rental");
        System.out.println("0. Exit");
    }

    public static void tampilkanSemuaMobil() throws IOException {
        System.out.println("Data Mobil");
        System.out.println("=================================================================================");
        System.out.printf("|%-4s|%-15s|%-18s|%-15s|%-23s|",
                "No.",
                "Nama Mobil",
                "Harga Sewa",
                "Ketersediaan",
                "Keterangan");
        System.out.println();
        System.out.println("|====+===============+==================+===============+=======================|");
        if(daftarMobil.isEmpty()){
            System.out.println("Tidak Terdapat Data");
        }
        
        for(int i = 0; i < daftarMobil.size(); i++) {
            Mobil mobil = daftarMobil.get(i);
            String ketersediaan = mobil.tersedia ? "Tersedia" : "Tidak Tersedia";
            String keterangan = mobil.tergantungTujuan ? "Tergantung tujuan" : "Tidak Tergantung Tujuan";
            System.out.printf("|%-4d|%-15s|Rp %-,11.0f/day|%-15s|%-23s|",
                i + 1,
                mobil.namaMobil,
                mobil.biaya, 
                ketersediaan,
                keterangan);
            System.out.println();
        }
        System.out.println("=================================================================================");
        System.out.println("Keterangan : Jika Mobil tergolong ke dalam tergantung tujuan maka harga ditentukan sendiri sehingga di dalam tabel harga 0");
    }

    private static void inisialisasiContohData() {
        daftarMobil.add(new Mobil(1, "Avanza", 350000, false));
        daftarMobil.add(new Mobil(2, "Inova Reborn", 700000, false));
        daftarMobil.add(new Mobil(3, "Brio", 300000, false));
        daftarMobil.add(new Mobil(4, "Ertiga", 400000, false));
        daftarMobil.add(new Mobil(5, "Elf Long", 0, true));
        daftarMobil.add(new Mobil(6, "Hiace", 0, true));
    }

    public static void tambahMobil() throws IOException {
        lanjutMobil = true;
        String namaMobilBaru;
        int idMobilBaru = 1;
        double biayaBaru;
        boolean tergantungTujuanBaru;
        while(lanjutMobil) {
            System.out.print("Masukan Nama Mobil : ");
            namaMobilBaru = Masukan.readLine();
            System.out.print("Apakah tergantung tujuan [y/t] ? ");
            String tujuan = Masukan.readLine();
            if(tujuan.equalsIgnoreCase("y")){
                System.out.println("Mobil ini termasuk tergantung tujuan");
                biayaBaru = 0;
                tergantungTujuanBaru = true;
            } else {
                System.out.println("Mobil ini bukan termasuk tergantung tujuan");
                tergantungTujuanBaru = false;
                System.out.print("Masukan Biaya Per Hari : ");
                biayaBaru = Double.parseDouble(Masukan.readLine());
            }
            daftarMobil.add(new Mobil(idMobilBaru, namaMobilBaru, biayaBaru, tergantungTujuanBaru));
            System.out.print("Masukan data lagi [y/t] ? ");
            String dataLagi = Masukan.readLine();
            if(dataLagi.equalsIgnoreCase("t")){
                lanjutMobil = false;
            }
            idMobilBaru++;
        }
    }

    public static void hapusMobil() throws IOException {
        lanjutMobil = true;
        while(lanjutMobil) {
            if(daftarMobil.isEmpty()){
                System.out.println("Tidak Ada Data yang bisa di hapus");
            }
            tampilkanSemuaMobil();
            System.out.print("Masukan Nomer (1-" + daftarMobil.size() + "): ");
            int posisi = Integer.parseInt(Masukan.readLine());
            
            if (posisi >= 1 && posisi <= daftarMobil.size()) {
                daftarMobil.remove(posisi - 1);
                System.out.println("Mobil dihapus!");
            } else {
                System.out.println("Input tidak valid!");
            }
            
            System.out.print("Hapus Mobil yang lain ? [y/t] ");
            String hapus = Masukan.readLine();
            if(hapus.equalsIgnoreCase("t")) {
                lanjutMobil = false;
            }
        }
    }

    public static void editMobil() throws IOException {
        lanjutMobil = true;
        while(lanjutMobil) {
            if(daftarMobil.isEmpty()) {
                System.out.println("Tidak Ada Data Yang Bisa di Edit");
            }
            tampilkanSemuaMobil();
            System.out.println("Masukan Nomer (1-" + daftarMobil.size() + "): ");
            int posisi = Integer.parseInt(Masukan.readLine());
            if(posisi >= 1 && posisi <= daftarMobil.size()) {
                Mobil mobil = daftarMobil.get(posisi-1);
                System.out.println("Tekan Enter Untuk Mempertahankan Data Lama");
                System.out.print("Masukan Nama Mobil Baru [" + mobil.namaMobil + "] :");
                String namaBaru = Masukan.readLine();
                if(!namaBaru.isEmpty()) {
                    mobil.namaMobil = namaBaru;
                }
                System.out.print("Tergantung tujuan [y/t] [" + mobil.tergantungTujuan + "] :");
                String tujuan = Masukan.readLine();
                if(!tujuan.isEmpty()) {
                    if(tujuan.equalsIgnoreCase("y")) {
                        mobil.tergantungTujuan = true;
                        mobil.biaya = 0;
                        System.out.println("Mobil Ini Tergantung Tujuan");
                    } else {
                        mobil.tergantungTujuan = false;
                        System.out.print("Masukan Biaya Per Hari [" + mobil.biaya + "] : ");
                        String biayaBaru = Masukan.readLine();
                        if(!biayaBaru.isEmpty()) {
                            mobil.biaya = Double.parseDouble(biayaBaru);
                        }
                    }
                }
                System.out.println("Data Berhasil Di update");
            } else {
                System.out.println("Nomer Tidak Valid!!");
            }
            System.out.print("Ingin Mengubah data lagi [y/t] : ");
            String jawab = Masukan.readLine();
            if(jawab.equalsIgnoreCase("t")) {
                lanjutMobil = false;
            }
        }
    }

    public static void hapusSewa() throws IOException {
        boolean lanjutHapus = true;
        while(lanjutHapus) {
            if(daftarSewa.isEmpty()) {
                System.out.println("Tidak Ada Data Rental yang bisa dihapus");
                return;
            }
            tampilkanSewa();
            System.out.print("Masukan Nomor Rental yang akan dihapus (1-" + daftarSewa.size() + "): ");
            int posisi = Integer.parseInt(Masukan.readLine());
            if (posisi >= 1 && posisi <= daftarSewa.size()) {
                DataSewa sewaHapus = daftarSewa.get(posisi - 1);
                for(Mobil mobil : daftarMobil) {
                    if(mobil.id == sewaHapus.idMobil) {
                        mobil.tersedia = true;
                        break;
                    }
                }
                daftarSewa.remove(posisi - 1);
                System.out.println("Data Rental berhasil dihapus!");
            } else {
                System.out.println("Nomor Rental tidak valid!");
            }
            System.out.print("Hapus data rental lain? (y/t): ");
            String jawab = Masukan.readLine();
            if(jawab.equalsIgnoreCase("t")) {
                lanjutHapus = false;
            }
        }
    }

    public static void editSewa() throws IOException, ParseException {
        SimpleDateFormat formatTanggal = new SimpleDateFormat("dd-MM-yyyy");
        boolean lanjutEdit = true;
        while(lanjutEdit) {
            if(daftarSewa.isEmpty()) {
                System.out.println("Tidak Ada Data Rental yang bisa diedit");
                return;
            }
            tampilkanSewa();
            System.out.print("Masukan Nomor Rental yang akan diedit (1-" + daftarSewa.size() + "): ");
            int posisi = Integer.parseInt(Masukan.readLine());
            if (posisi >= 1 && posisi <= daftarSewa.size()) {
                DataSewa sewa = daftarSewa.get(posisi - 1);
                Mobil mobilSekarang = dapatkanMobilDariId(sewa.idMobil);
                System.out.println("\nTekan Enter untuk mempertahankan data lama");
                System.out.print("Masukkan Nama Baru [" + sewa.nama + "]: ");
                String namaBaru = Masukan.readLine();
                if(!namaBaru.isEmpty()) {
                    sewa.nama = namaBaru;
                }
                System.out.print("Masukkan NIK Baru [" + sewa.nik + "]: ");
                String nikBaru = Masukan.readLine();
                if(!nikBaru.isEmpty()) {
                    sewa.nik = nikBaru;
                }
                System.out.print("Ganti Mobil? (y/t): ");
                if(Masukan.readLine().equalsIgnoreCase("y")) {
                    tampilkanSemuaMobil();
                    System.out.print("Pilih nomor mobil baru (1-" + daftarMobil.size() + "): ");
                    int indeksMobilBaru = Integer.parseInt(Masukan.readLine()) - 1;
                    
                    if(indeksMobilBaru >= 0 && indeksMobilBaru < daftarMobil.size()) {
                        Mobil mobilBaru = daftarMobil.get(indeksMobilBaru);
                        if(!mobilBaru.tersedia && mobilBaru.id != mobilSekarang.id) {
                            System.out.println("Mobil tidak tersedia untuk disewa!");
                        } else {
                            mobilSekarang.tersedia = true;
                            mobilBaru.tersedia = false;
                            sewa.idMobil = mobilBaru.id;
                        }
                    } else {
                        System.out.println("Nomor mobil tidak valid!");
                    }
                }
                System.out.print("Gunakan Supir? (y/t) [" + (sewa.supir ? "y" : "t") + "]: ");
                String supirBaru = Masukan.readLine();
                if(!supirBaru.isEmpty()) {
                    sewa.supir = supirBaru.equalsIgnoreCase("y");
                }
                System.out.print("Masukkan Tanggal Berangkat Baru (dd-MM-yyyy) [" + formatTanggal.format(sewa.tanggalBerangkat) + "]: ");
                String tanggalBerangkatBaru = Masukan.readLine();
                if(!tanggalBerangkatBaru.isEmpty()) {
                    sewa.tanggalBerangkat = formatTanggal.parse(tanggalBerangkatBaru);
                }
                System.out.print("Masukkan Tanggal Kembali Baru (dd-MM-yyyy) [" + formatTanggal.format(sewa.tanggalKembali) + "]: ");
                String tanggalKembaliBaru = Masukan.readLine();
                if(!tanggalKembaliBaru.isEmpty()) {
                    sewa.tanggalKembali = formatTanggal.parse(tanggalKembaliBaru);
                }
                Mobil mobilPilihan = dapatkanMobilDariId(sewa.idMobil);
                System.out.print("Masukkan Tujuan Baru [" + sewa.tujuan + "]: ");
                String tujuanBaru = Masukan.readLine();
                if(!tujuanBaru.isEmpty()) {
                    sewa.tujuan = tujuanBaru;
                }
                long selisihMiliDetik = sewa.tanggalKembali.getTime() - sewa.tanggalBerangkat.getTime();
                long selisihHari = TimeUnit.DAYS.convert(selisihMiliDetik, TimeUnit.MILLISECONDS);
                if(mobilPilihan.tergantungTujuan) {
                    System.out.print("Masukkan Biaya Sesuai Tujuan Baru: ");
                    String biayaBaru = Masukan.readLine();
                    if(!biayaBaru.isEmpty()) {
                        sewa.totalBiaya = Double.parseDouble(biayaBaru);
                    }
                } else {
                    sewa.totalBiaya = mobilPilihan.biaya * selisihHari;
                }
                
                if(sewa.supir) {
                    sewa.totalBiaya += 500000 * selisihHari;
                }
                System.out.println("Data Rental berhasil diupdate!");
                System.out.println("Total Biaya Baru: Rp " + String.format("%,.0f", sewa.totalBiaya));
            } else {
                System.out.println("Nomor Rental tidak valid!");
            }
            System.out.print("Edit data rental lain? (y/t): ");
            String jawab = Masukan.readLine();
            if(jawab.equalsIgnoreCase("t")) {
                lanjutEdit = false;
            }
        }
    }

    public static void cetakLaporan() throws IOException {
        SimpleDateFormat formatTanggal = new SimpleDateFormat("dd-MM-yyyy");
        final int ITEM_PER_HALAMAN = 3;
        int totalHalaman = (int) Math.ceil((double) daftarSewa.size() / ITEM_PER_HALAMAN);
        double totalKeseluruhan = 0;
        for (int halamanSaatIni = 1; halamanSaatIni <= totalHalaman; halamanSaatIni++) {
            System.out.print("\033[H\033[2J");
            System.out.flush();
            System.out.println("LAPORAN DATA RENTAL MOBIL");
            System.out.println("MAULANA TRANS");
            System.out.println("Halaman: " + halamanSaatIni + " dari " + totalHalaman);
            System.out.println("|======================================================================================================================================================|");
            System.out.printf("|%-4s|%-15s|%-15s|%-18s|%-8s|%-12s|%-17s|%-12s|%-23s|%-16s|\n",
                    "No.",
                    "Nama",
                    "NIK",
                    "Mobil",
                    "Supir",
                    "Tujuan",
                    "Total Biaya",
                    "Durasi (hari)",
                    "Jadwal Keberangkatan",
                    "Jadwal Kembali");
            System.out.println("|====|===============|===============|==================|========|============|=================|=============|=======================|================|");
            int indeksAwal = (halamanSaatIni - 1) * ITEM_PER_HALAMAN;
            int indeksAkhir = Math.min(indeksAwal + ITEM_PER_HALAMAN, daftarSewa.size());
            double subtotal = 0;
            for (int i = indeksAwal; i < indeksAkhir; i++) {
                DataSewa sewa = daftarSewa.get(i);
                Mobil mobil = dapatkanMobilDariId(sewa.idMobil);
                String namaMobil = mobil != null ? mobil.namaMobil : "Unknown";
                long selisihMiliDetik = sewa.tanggalKembali.getTime() - sewa.tanggalBerangkat.getTime();
                long selisihHari = TimeUnit.DAYS.convert(selisihMiliDetik, TimeUnit.MILLISECONDS);
                System.out.printf("|%-4d|%-15s|%-15s|%-18s|%-8s|%-12s|Rp %,-14.0f|%-13d|%-23s|%-16s|\n",
                    i + 1,
                    sewa.nama,
                    sewa.nik,
                    namaMobil,
                    sewa.supir ? "Ya" : "Tidak",
                    sewa.tujuan,
                    sewa.totalBiaya,
                    selisihHari,
                    formatTanggal.format(sewa.tanggalBerangkat),
                    formatTanggal.format(sewa.tanggalKembali));
                subtotal += sewa.totalBiaya;
                totalKeseluruhan += sewa.totalBiaya;
            }
            System.out.println("|------------------------------------------------------------------------------------------------------------------------------------------------------|");
            System.out.printf("|%-4s|%-31s|%-18s|%-8s|%-12s|Rp %,-14.0f|%-13s|%-23s|%-16s|\n",
                    " ",
                    "Subtotal Halaman Ini",
                    " ",
                    " ",
                    " ",
                    subtotal,
                    " ",
                    " ",
                    " ");
            System.out.println("========================================================================================================================================================");
            if (halamanSaatIni == totalHalaman) {
                 System.out.printf("|%-4s|%-31s|%-18s|%-8s|%-12s|Rp %,-14.0f|%-13s|%-23s|%-16s|\n",
                    " ",
                    "Grand Total",
                    " ",
                    " ",
                    " ",
                    totalKeseluruhan,
                    " ",
                    " ",
                    " ");
                System.out.println("========================================================================================================================================================");
                System.out.println("Data sudah habis. Tekan enter untuk selesai...");
                Masukan.readLine();
            } else {
                System.out.println("Tekan enter untuk halaman berikutnya...");
                Masukan.readLine();
            }
        }
        if (daftarSewa.isEmpty()) {
            System.out.println("Tidak ada data rental untuk dicetak.");
            System.out.print("Tekan enter untuk kembali ke menu...");
            Masukan.readLine();
        }
    }

    public static void main(String[] args) throws IOException, ParseException {
        inisialisasiContohData();
//        inisialisasiContohDataSewa();
        while(true) {
            System.out.println("Rental Mobil");
            System.out.println("24.240.0028 | Muhammad Ichsan");
            tampilkanMenu();
            int pilihan;
            System.out.print("Masukan Nomer Menu : ");
            pilihan = Integer.parseInt(Masukan.readLine());
            switch(pilihan) {
                case 0 -> System.exit(0);
                case 1 -> tampilkanSemuaMobil();
                case 2 -> tambahMobil();
                case 3 -> hapusMobil();
                case 4 -> editMobil();
                case 5 -> tampilkanSewa();
                case 6 -> tambahSewa();
                case 7 -> editSewa();
                case 8 -> hapusSewa();
                case 9 -> cetakLaporan();
                default -> System.out.println("Pilihan tidak valid");
            }
        }
    }
}
