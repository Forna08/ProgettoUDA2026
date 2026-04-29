/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.View;

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
    
    
    //setter

    private void _setPosti(int posti) {
        
        this.posti = posti;
    }

    private void _setTipo(String tipo) {
        this.tipo = tipo;
    }

    private void _setvMax(int vMax) {
        this.vMax = vMax;
    }

    private void _setColore(String colore) {
        this.colore = colore;
    }
    
}
