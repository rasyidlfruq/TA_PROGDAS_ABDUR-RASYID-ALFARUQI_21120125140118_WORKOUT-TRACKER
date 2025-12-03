package tugasakhir2;

// [Modul 5] Class & Object: Ini adalah Class yang berfungsi sebagai cetakan (blueprint)
// untuk membuat objek latihan. Satu class ini bisa menghasilkan banyak data latihan berbeda.
public class AngkatBeban {

    // [Modul 6] Encapsulation (Pembungkusan Data):
    // Atribut dibuat 'private' agar tidak bisa diakses atau diubah sembarangan oleh class lain.
    // Ini menjaga keamanan dan integritas data (Information Hiding).

    // [Modul 1] Variabel & Tipe Data:
    // Kita menggunakan tipe data yang tepat: String untuk teks, double untuk bilangan desimal, int untuk bulat.
    private String namaGerakan;
    private double beban; // dalam kg
    private int set;
    private int repetisi;

    // [Modul 5] Constructor:
    // Ini adalah method khusus yang namanya SAMA dengan nama Class.
    // Dijalankan otomatis saat kita menulis kode 'new AngkatBeban(...)'.
    // Fungsinya untuk memberikan nilai awal (inisialisasi) pada atribut objek saat pertama kali dibuat.
    public AngkatBeban(String namaGerakan, double beban, int set, int repetisi) {
        // Kata kunci 'this' merujuk pada variabel milik class ini sendiri.
        this.namaGerakan = namaGerakan;
        this.beban = beban;
        this.set = set;
        this.repetisi = repetisi;
    }

    // [Modul 6] Getter dan Setter:
    // Karena variabel di atas 'private', kita butuh "pintu" publik untuk mengaksesnya.

    // [Modul 4] Method Return Type: Getter mengembalikan nilai (return).
    public String getNamaGerakan() {
        return namaGerakan;
    }

    // [Modul 4] Method Void: Setter melakukan aksi mengubah nilai tanpa mengembalikan apa-apa.
    public void setNamaGerakan(String namaGerakan) {
        this.namaGerakan = namaGerakan;
    }

    public double getBeban() { return beban; }
    public void setBeban(double beban) { this.beban = beban; }

    public int getSet() { return set; }
    public void setSet(int set) { this.set = set; }

    public int getRepetisi() { return repetisi; }
    public void setRepetisi(int repetisi) { this.repetisi = repetisi; }

    // [Modul 4] Method Logic:
    // Method kustom untuk menghitung total volume latihan.
    // Rumus: Beban x Set x Repetisi.
    // [Modul 1] Operator Aritmatika: Menggunakan perkalian (*).
    public double hitungVolume() {
        return beban * set * repetisi;
    }
}