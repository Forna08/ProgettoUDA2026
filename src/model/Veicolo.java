/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

import java.time.LocalDate;

/**
 *
 * @author bernia
 */
public abstract class Veicolo {
    protected String targa;
    protected String marca;
    protected String modello;
    protected int anno;
    protected int km;
    protected LocalDate scadenzaAssicurazione;
    protected LocalDate scadenzaRevisione;
    protected LocalDate scadenzaTagliando;
    protected String classeEnergetica;
    protected double consumo;

    //costruttore
    public Veicolo(String targa, String marca, String modello, int anno, int km, 
            LocalDate scadenzaAssicurazione, LocalDate scadenzaRevisione,
            LocalDate scadenzaTagliando, String note, String classeEnergetica, double consumo) {
        _setTarga(targa);
        _setMarca(marca);
        _setModello(modello);
        _setAnno(anno);
        _setKm(km);
        _setScadenzaAssicurazione(scadenzaAssicurazione);
        _setScadenzaRevisione(scadenzaRevisione);
        _setScadenzaTagliando(scadenzaTagliando);
        _setClasseEnergetica(classeEnergetica);
        _setConsumo(consumo);
        
    }
    
    
    //getters
    public String getTarga() {
        return targa;
    }

    public String getMarca() {
        return marca;
    }

    public String getModello() {
        return modello;
    }

    public int getAnno() {
        return anno;
    }

    public int getKm() {
        return km;
    }

    public LocalDate getScadenzaAssicurazione() {
        return scadenzaAssicurazione;
    }

    public LocalDate getScadenzaRevisione() {
        return scadenzaRevisione;
    }

    public LocalDate getScadenzaTagliando() {
        return scadenzaTagliando;
    }

    public String getClasseEnergetica() {
        return classeEnergetica;
    }

    public double getConsumo() {
        return consumo;
    }

    
    //setters
    private void _setTarga(String targa) {
        if(targa.equals("") || targa.isEmpty() || targa.length() != 7)
            throw new IllegalArgumentException("Targa non corretta");
        this.targa = targa;
    }

    private void _setMarca(String marca) {
        if(marca.equals("") || marca.isEmpty())
            throw new IllegalArgumentException("marca della macchina non corretta");
        this.marca = marca;
    }

    private void _setModello(String modello) {
        if(modello.equals("") || modello.isEmpty())
            throw new IllegalArgumentException("modello della macchina non corretto");
        this.modello = modello;
    }

    private void _setAnno(int anno) {
        if(anno <= 0 || anno > 2026)
            throw new IllegalArgumentException("anno della macchina errato");
        this.anno = anno;
    }

    private void _setKm(int km) {
        if(anno < 0)
            throw new IllegalArgumentException("kilometraggio della macchina errato");
        this.km = km;
    }

    private void _setScadenzaAssicurazione(LocalDate scadenzaAssicurazione) {
        if(scadenzaAssicurazione == null || scadenzaAssicurazione.isAfter(LocalDate.now()))
            throw new IllegalArgumentException("Data di scadenza dell'assicurazione della macchina errata");
        this.scadenzaAssicurazione = scadenzaAssicurazione;
    }

    private void _setScadenzaRevisione(LocalDate ScadenzaRevisione) {
        if(ScadenzaRevisione == null || ScadenzaRevisione.isAfter(LocalDate.now()))
            throw new IllegalArgumentException("Data di scadenza della revisione della macchina errata");
        this.scadenzaRevisione = ScadenzaRevisione;
    }

    private void _setScadenzaTagliando(LocalDate ScadenzaTagliando) {
        if(ScadenzaTagliando == null || ScadenzaTagliando.isAfter(LocalDate.now()))
            throw new IllegalArgumentException("Data di scadenza del tagliando della macchina errata");
        this.scadenzaTagliando = ScadenzaTagliando;
    }

    private void _setClasseEnergetica(String classeEnergetica) {
        if(classeEnergetica.equals("") || classeEnergetica.isEmpty())
            throw new IllegalArgumentException("classe energetica della macchina non corretta");
        this.classeEnergetica = classeEnergetica;
    }

    private void _setConsumo(double consumo) {
        if(consumo <= 0)
            throw new IllegalArgumentException("consumo della macchina errato");
        this.consumo = consumo;
    }

    @Override
    public String toString() {
        return  "targa=" + targa + ", marca=" + marca 
                + ", modello=" + modello + ", anno=" + anno + ", km=" 
                + km + ", scadenzaAssicurazione=" + scadenzaAssicurazione 
                + ", scadenzaRevisione=" + scadenzaRevisione + ", scadenzaTagliando=" 
                + scadenzaTagliando + ", note=" + ", classeEnergetica=" 
                + classeEnergetica + ", consumo=" + consumo;
    }
    
    public boolean equals(Veicolo v){
        if(v == null)
            return false;
        return this.targa.equals(v.targa);
    }
    
    
}
