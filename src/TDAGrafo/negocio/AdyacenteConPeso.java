/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TDAGrafo.negocio;

/**
 *
 * @author HP
 */
public class AdyacenteConPeso implements Comparable< AdyacenteConPeso >{
    
    private int posicionVertice;
    private double peso;

    public AdyacenteConPeso(int posicionVertice) {
        this.posicionVertice = posicionVertice;
    }

    public AdyacenteConPeso(int posicionVertice, double peso) {
        this.posicionVertice = posicionVertice;
        this.peso = peso;
    }

    public int getPosicionVertice() {
        return posicionVertice;
    }

    public void setPosicionVertice(int posicionVertice) {
        this.posicionVertice = posicionVertice;
    }

    public double getPeso() {
        return peso;
    }

    public void setPeso(double peso) {
        this.peso = peso;
    }
    
    @Override
    public int compareTo(AdyacenteConPeso vertice){
        Integer esteVertice = this.posicionVertice;
        Integer otroVertice = vertice.posicionVertice;
        return esteVertice.compareTo(otroVertice);
    }

    @Override
    public boolean equals(Object otro) {
        if(otro == null){
            return false;
        }
        if(otro.getClass() != this.getClass()){
            return false;
        }
        
        AdyacenteConPeso otroVertice = (AdyacenteConPeso) otro;
        return this.posicionVertice == otroVertice.posicionVertice;
    }
}
