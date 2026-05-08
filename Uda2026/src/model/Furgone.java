/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.time.LocalDate;

/**
 *
 * @author aless
 */
public class Furgone extends Veicolo {
    
    private int volume;
    private int autonomia;
    
    public Furgone(String targa, String marca, String modello, int anno,
            int km, LocalDate scadenzaAssicurazione, 
            LocalDate scadenzaRevisione, LocalDate scadenzaTagliando, 
            String note, String classeEnergetica, double consumo, int volume, int autonomia) {
        super(targa, marca, modello, anno, km, scadenzaAssicurazione, 
                scadenzaRevisione, scadenzaTagliando, note, classeEnergetica, consumo);
        _setVolume(volume);
        _setAutonomia(autonomia);
    }

    //getter
    public int getVolume() {
        return volume;
    }

    public int getAutonomia() {
        return autonomia;
    }

    //setter privv
    private void _setVolume(int volume) {
        if(volume<= 0)
            throw new IllegalArgumentException("volume del furgone non valido");
        this.volume = volume;
    }

    private void _setAutonomia(int autonomia) {
        if(autonomia<= 0)
            throw new IllegalArgumentException("autonomia del furgone non valida");
        this.autonomia = autonomia;
    }

    //setter pubblici
    public void setVolume(int volume) {
        this.volume = volume;
    }

    public void setAutonomia(int autonomia) {
        this.autonomia = autonomia;
    }

    @Override
    public String toString() {
        return "Furgone: " + super.toString()+ "volume=" + volume + ", autonomia=" + autonomia;
    }
    
    
    
}
