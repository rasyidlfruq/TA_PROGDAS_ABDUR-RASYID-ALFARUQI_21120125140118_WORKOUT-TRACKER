package tugasakhir;

public class AngkatBeban {
    // Penerapan Encapsulation (Modul 6)
    private String namaGerakan;
    private double beban; // dalam kg
    private int set;
    private int repetisi;

    // Constructor (Modul 5)
    public AngkatBeban(String namaGerakan, double beban, int set, int repetisi) {
        this.namaGerakan = namaGerakan;
        this.beban = beban;
        this.set = set;
        this.repetisi = repetisi;
    }

    // Getter dan Setter (Modul 6)
    public String getNamaGerakan() { return namaGerakan; }
    public void setNamaGerakan(String namaGerakan) { this.namaGerakan = namaGerakan; }

    public double getBeban() { return beban; }
    public void setBeban(double beban) { this.beban = beban; }

    public int getSet() { return set; }
    public void setSet(int set) { this.set = set; }

    public int getRepetisi() { return repetisi; }
    public void setRepetisi(int repetisi) { this.repetisi = repetisi; }

    // Method Function untuk menghitung Volume (Modul 4)
    public double hitungVolume() {
        return beban * set * repetisi;
    }
}