package TDAGrafo.negocio;

import java.util.Objects;

/**
 *
 * @author HP
 */
public class AristaConPeso implements Comparable<AristaConPeso>{
        private int posicionDestino;
        private int posicionOrigen;
        private double peso;

        public AristaConPeso() {
        }

        public AristaConPeso( int posicionOrigen,int posicionDestino, double peso) {
            this.posicionOrigen = posicionOrigen;
            this.posicionDestino = posicionDestino;            
            this.peso = peso;
        }

        public int getPosicionDestino() {
            return posicionDestino;
        }

        public void setPosicionDestino(int posicionDestino) {
            this.posicionDestino = posicionDestino;
        }

        public int getPosicionOrigen() {
            return posicionOrigen;
        }

        public void setPosicionOrigen(int posicionOrigen) {
            this.posicionOrigen = posicionOrigen;
        }

        public double getPeso() {
            return peso;
        }

        public void setPeso(double peso) {
            this.peso = peso;
        }
        
        @Override
        public int compareTo(AristaConPeso other){
            Double pesoEstaArista = this.getPeso();
            Double pesoOtraArista = other.getPeso();
            return pesoEstaArista.compareTo(pesoOtraArista);
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final AristaConPeso other = (AristaConPeso) obj;           
            if (this.posicionOrigen == other.getPosicionOrigen() &&
                    this.posicionDestino == other.getPosicionDestino() &&
                    this.peso == other.getPeso())
                    return true;
            return false;
        }

    @Override
    public String toString() {
        return "["+ posicionDestino + ", " + posicionOrigen + ", " + peso + ']';
    }

    
    
    public static void main(String[] args){
        AristaConPeso u = new AristaConPeso(5,1,4.0);
        AristaConPeso v = new AristaConPeso(5,1,4.0);
        
        System.out.println(u.equals(v));
    }
}
