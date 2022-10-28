/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TDAGrafo.principal;

import TDAGrafo.negocio.ExceptionGrafoTipoInvalido;
import TDAGrafo.negocio.ListGrafo;

/**
 *
 * @author HP
 */
public class programa {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws ExceptionGrafoTipoInvalido {
        
//        ListGrafo<Character> g = new ListGrafo<>(true);
//        g.insertarVertice('M');//0
//        g.insertarVertice('I');//1
//        g.insertarVertice('Y');//2
//        g.insertarVertice('X');//3
//        g.insertarVertice('Z');//4
//        
//        g.insertarArista('M', 'Y');
//        g.insertarArista('M', 'Z');
//        g.insertarArista('I', 'Y');
//        g.insertarArista('I', 'X');
//        g.insertarArista('Y', 'X');
//        g.insertarArista('X', 'M');
//        g.insertarArista('X', 'Z');
//        g.insertarArista('Z', 'M');
//        g.insertarArista('I', 'I');
        
        
//        Boolean[][] matriz = g.getMatrizDeAdyancencia();
//        String cad = "{";
//        for (int indexFila = 0; indexFila < matriz.length; indexFila++){
//            cad = cad + "(";
//            for( int indexColumna = 0; indexColumna < matriz.length; indexColumna++){
//                if ( matriz[indexFila][indexColumna] == true){
//                   cad = cad + "1 ,"; 
//                }else{
//                    cad = cad + "0 ,";
//                }
//            }
//            cad = cad + ")";
//        }
//        System.out.println(cad + "}");

//            System.out.println(g.esDebilmenteConexo());
            

        /*Ordenamiento topologico*/
//        ListGrafo<Integer> g2 = new ListGrafo<>(true);
//        g2.insertarVertice(1);
//        g2.insertarVertice(2);
//        g2.insertarVertice(3);
//        g2.insertarVertice(4);
//        g2.insertarVertice(5);
//        g2.insertarVertice(6);
//        g2.insertarVertice(7);
//        
//        g2.insertarArista(1, 2);
//        g2.insertarArista(1, 3);
//        g2.insertarArista(1, 5);
//        g2.insertarArista(1, 4);
//        g2.insertarArista(2, 4);
//        g2.insertarArista(4, 5);
//        g2.insertarArista(3, 2);
//        g2.insertarArista(3, 5);
//        g2.insertarArista(6, 7);
//        System.out.println(g2.ordenamientoTopologico());
        
        /* es fuertemente conexo */
        
        ListGrafo<Integer> g = new ListGrafo<>();
        
        g.insertarVertice(1);
        g.insertarVertice(2);
        g.insertarVertice(3);
        g.insertarVertice(3);
        g.insertarVertice(4);
        g.insertarVertice(5);
        
        g.insertarArista(1, 2);
        g.insertarArista(2, 3);
        g.insertarArista(3, 4);
        g.insertarArista(3, 5);
//        g.insertarArista(2, 5);
//        g.insertarArista(2, 1);
//        g.insertarArista(5, 1);
            
        System.out.println(g.hayCiclos());
        
    }

    
}
