/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.time.LocalDate;

/**
 *
 * @author bernia
 */
public class Auto extends Veicolo{
    private int posti;
    private String tipo;
    private int vMax;
    private String colore;
    
    public Auto(String targa, String marca, String modello, int anno, int km,
            LocalDate scadenzaAssicurazione, LocalDate scadenzaRevisione, 
            LocalDate scadenzaTagliando, String note, String classeEnergetica, double consumo, int posti, String tipo, int vMax, String colore) {
        super(targa, marca, modello, anno, km, scadenzaAssicurazione, scadenzaRevisione, scadenzaTagliando, note, classeEnergetica, consumo);
        _setPosti(posti);
        _setTipo(tipo);
        _setvMax(vMax);
        _setColore(colore);
    }
    
    //getter

    public int getPosti() {
        return posti;
    }

    public String getTipo() {
        return tipo;
    }

    public int getvMax() {
        return vMax;
    }

    public String getColore() {
        return colore;
    }
    
    
    //setter privv

    private void _setPosti(int posti) {
        if(posti < 2)
            throw new IllegalArgumentException("numero di posti errato");
        this.posti = posti;
    }

    private void _setTipo(String tipo) {
        if(tipo.equals("") || tipo.isEmpty())
            throw new IllegalArgumentException("tipologia non corretta");
        this.tipo = tipo;
    }

    private void _setvMax(int vMax) {
        if(vMax <= 0)
            throw new IllegalArgumentException("vel massima non corretta");
        this.vMax = vMax;
    }

    private void _setColore(String colore) {
        if(colore.equals("") || colore.isEmpty())
            throw new IllegalArgumentException("colorazione assente");
        this.colore = colore;
    }

    //setter pubbliche
    public void setPosti(int posti) {
        this.posti = posti;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public void setvMax(int vMax) {
        this.vMax = vMax;
    }

    public void setColore(String colore) {
        this.colore = colore;
    }

    @Override
    public String toString() {
        return "Auto: " +super.toString() + " posti=" + posti + ", tipo=" + 
                tipo + ", vMax=" + vMax + ", colore=" + colore;
    }
    
    
}
