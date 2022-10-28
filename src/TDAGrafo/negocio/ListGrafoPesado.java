package TDAGrafo.negocio;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

/**
 *
 * @author HP
 * @param <E>
 */
public class ListGrafoPesado< E extends Comparable<E>> {

    private final List<E> listaDeVertices;
    private final List< List<AdyacenteConPeso>> listasDeAdyacencias;
    private final boolean esDirigido;    

    public ListGrafoPesado() {
        this(false);
    }

    public ListGrafoPesado(boolean tipo) {
        this.esDirigido = tipo;
        this.listasDeAdyacencias = new ArrayList<>();
        this.listaDeVertices = new ArrayList<>();
    }

    public boolean insertarVertice(E vertice) {
        if (this.existeVertice(vertice)) {
            return false;
        }
        listaDeVertices.add(vertice);
        List<AdyacenteConPeso> adyacenciasDelVertice = new ArrayList<>();
        listasDeAdyacencias.add(adyacenciasDelVertice);
        return true;
    }

    public boolean insertarArista(E verticeOrigen, E verticeDestino, double peso) {
        if (!this.existeVertice(verticeOrigen)
                || !this.existeVertice(verticeDestino)) {
            return false;
        }
        if (this.existeAdyacenciaEntre(verticeOrigen, verticeDestino)) {
            return false;
        }

        int posicionDelVerticeOrigen = this.getPosicionDelVertice(verticeOrigen);
        int posicionDelVerticeDestino = this.getPosicionDelVertice(verticeDestino);
        List<AdyacenteConPeso> adyacenciasDelVerticeOrigen = this.listasDeAdyacencias.get(posicionDelVerticeOrigen);
        adyacenciasDelVerticeOrigen.add(new AdyacenteConPeso(posicionDelVerticeDestino, peso));
        Collections.sort(adyacenciasDelVerticeOrigen);
        if (!this.esDirigido && !this.esLazo(verticeOrigen, verticeDestino)) {
            List<AdyacenteConPeso> adyacenciasDelVerticeDestino = this.listasDeAdyacencias.get(posicionDelVerticeDestino);
            adyacenciasDelVerticeDestino.add(new AdyacenteConPeso(posicionDelVerticeOrigen,peso));
            Collections.sort(adyacenciasDelVerticeDestino);
        }
        return true;
    }

    public boolean eliminarVertice(E vertice) {
        if (!this.existeVertice(vertice)) {
            return false;
        }
        int posicionDelVertice = this.getPosicionDelVertice(vertice);
        this.listaDeVertices.remove(posicionDelVertice);
        this.listasDeAdyacencias.remove(posicionDelVertice);

        for (List<AdyacenteConPeso> adyacenciasDeUnVertice : this.listasDeAdyacencias) {
            AdyacenteConPeso adyacenciaEnTurno = new AdyacenteConPeso(posicionDelVertice);
            if (adyacenciasDeUnVertice.contains(adyacenciaEnTurno)) {
                adyacenciasDeUnVertice.remove(adyacenciaEnTurno);
            }
            for (int i = 0; i < adyacenciasDeUnVertice.size(); i++) {
                AdyacenteConPeso adyacencia = adyacenciasDeUnVertice.get(i);
                int posicionDeVerticeAdyacente = adyacencia.getPosicionVertice();
                if (posicionDeVerticeAdyacente > posicionDelVertice) {
                    adyacencia.setPosicionVertice(posicionDelVertice - 1);
                    adyacenciasDeUnVertice.set(i, adyacencia);
                }
            }
        }
        return true;
    }

    public boolean eliminarArista(E verticeOrigen, E verticeDestino) {
        if (!this.existeVertice(verticeOrigen)
                || !this.existeVertice(verticeDestino)) {
            return false;
        }
        if (!this.existeAdyacenciaEntre(verticeOrigen, verticeDestino)) {
            return false;
        }
        int posicionDelVerticeOrigen = this.getPosicionDelVertice(verticeOrigen);
        int posicionDelVerticeDestino = this.getPosicionDelVertice(verticeDestino);
        List<AdyacenteConPeso> adyacenciasDelVerticeOrigen = this.listasDeAdyacencias.get(posicionDelVerticeOrigen);
        adyacenciasDelVerticeOrigen.remove(new AdyacenteConPeso(posicionDelVerticeDestino));

        if (!this.esDirigido) {
            List<AdyacenteConPeso> adyacenciasDelVerticeDestino = this.listasDeAdyacencias.get(posicionDelVerticeDestino);
            adyacenciasDelVerticeDestino.remove(new AdyacenteConPeso(posicionDelVerticeOrigen));
        }
        return true;
    }

    public int cantidadDeVertices() {
        return listaDeVertices.size();
    }

    public int cantidadDeAristas() {
        int cantidad = 0;
        for (List<AdyacenteConPeso> adyancenciasDeUnVertice : this.listasDeAdyacencias) {
            cantidad += adyancenciasDeUnVertice.size();
        }

        if (!this.esDirigido) {
            return cantidad / 2;
        }

        return cantidad;
    }

    private boolean esLazo(E verticeOrigen, E verticeDestino) {
        return verticeOrigen.compareTo(verticeDestino) == 0;
    }

    /**
     * Retorna true si hay adyacencia entre los vertices. Pre: Los vertices
     * existen.
     *
     * @param verticeOrigen
     * @param verticeDestino
     * @return
     */
    private boolean existeAdyacenciaEntre(E verticeOrigen, E verticeDestino) {
        int posicionVerticeOrigen = this.getPosicionDelVertice(verticeOrigen);
        int posicionVerticeDestino = this.getPosicionDelVertice(verticeDestino);
        List<AdyacenteConPeso> adyacenciasDelVerticeOrigen = this.listasDeAdyacencias.get(posicionVerticeOrigen);
        return adyacenciasDelVerticeOrigen.contains(new AdyacenteConPeso(posicionVerticeDestino));
    }

    private boolean existeAdyacenciaEntre(int posicionVerticeOrigen, int posicionVerticeDestino) {
        List<AdyacenteConPeso> adyacenciasDelVerticeOrigen = this.listasDeAdyacencias.get(posicionVerticeOrigen);
        return adyacenciasDelVerticeOrigen.contains(new AdyacenteConPeso(posicionVerticeDestino));
    }

    private boolean existeVertice(E vertice) {
        return this.listaDeVertices.contains(vertice);
    }

    private int getPosicionDelVertice(E vertice) {
        return listaDeVertices.indexOf(vertice);
    }

    public Boolean hayVerticesEnGrafo() {
        return !this.listaDeVertices.isEmpty();
    }

    /**
     * recorrido en amplitud = por niveles. Retorna una lista con todos los
     * vertices que se puedan acceder desde el verticeDePartida
     *
     * @param verticeDePartida
     * @return
     */
    public List<E> bfs(E verticeDePartida) {
        List<E> recorrido = new LinkedList<>();
        if (!this.existeVertice(verticeDePartida)) {
            return recorrido;
        }
        Queue<E> colaDeVertices = new LinkedList<>();
        List<Boolean> listaDeMarcados = new ArrayList<>();
        this.ponerTodosEnDesmarcado(listaDeMarcados);
        colaDeVertices.offer(verticeDePartida);
        this.marcarVertice(listaDeMarcados, this.getPosicionDelVertice(verticeDePartida));
        do {
            E verticeDeTurno = colaDeVertices.poll();
            recorrido.add(verticeDeTurno);
            int posicionDeVerticeDeTurno = this.getPosicionDelVertice(verticeDeTurno);
            List<AdyacenteConPeso> adyacentesDelVerticeDeTurno = this.listasDeAdyacencias.get(posicionDeVerticeDeTurno);
            for (AdyacenteConPeso adyacenteDeUnVertice : adyacentesDelVerticeDeTurno) {
                if (!this.estaMarcadoElVertice(listaDeMarcados, adyacenteDeUnVertice.getPosicionVertice())) {
                    colaDeVertices.offer(this.listaDeVertices.get(adyacenteDeUnVertice.getPosicionVertice()));
                    this.marcarVertice(listaDeMarcados, adyacenteDeUnVertice.getPosicionVertice());
                }
            }
        } while (!colaDeVertices.isEmpty());
        return recorrido;
    }

    /**
     * recorrido en profundidad Recursivo. Retorna una lista con todos los
     * vertices que se puedan acceder desde el verticeDePartida
     *
     * @param verticeDePartida
     * @return
     */
    public List<E> dfs(E verticeDePartida) {
        List<E> recorrido = new LinkedList<>();

        if (!this.existeVertice(verticeDePartida)) {
            return recorrido;
        }
        List<Boolean> listaDeMarcados = new ArrayList<>();
        ponerTodosEnDesmarcado(listaDeMarcados);
        dfsR(recorrido, listaDeMarcados, this.getPosicionDelVertice(verticeDePartida));
        return recorrido;
    }

    private void dfsR(List<E> recorrido, List<Boolean> listaDeMarcados, int posicionDeVertice) {
        recorrido.add(this.listaDeVertices.get(posicionDeVertice));
        marcarVertice(listaDeMarcados, posicionDeVertice);
        List<AdyacenteConPeso> adyancenciasDelVerticeEnTurno = this.listasDeAdyacencias.get(posicionDeVertice);
        for (AdyacenteConPeso adyacenteDeUnVertice : adyancenciasDelVerticeEnTurno) {
            if (!estaMarcadoElVertice(listaDeMarcados, adyacenteDeUnVertice.getPosicionVertice())) {
                this.dfsR(recorrido, listaDeMarcados, adyacenteDeUnVertice.getPosicionVertice());
            }
        }
    }

    /**
     * ** METODOS PARA IDENTIFICAR QUE VERTICES FUERON PROCESADOS POR LOS
     * RECORRIDOS BFS Y DFS ***
     */
    private void ponerTodosEnDesmarcado(List<Boolean> ListaDeMarcados) {
        for (int index = 0; index < this.cantidadDeVertices(); index++) {
            ListaDeMarcados.add(Boolean.FALSE);
        }
    }
    
    private void desmarcarATodos(List<Boolean> listaDeMarcados) {
        for (int index = 0; index < listaDeMarcados.size(); index++) {
            listaDeMarcados.set(index, Boolean.FALSE);
        }
    }
    private void marcarVertice(List<Boolean> ListaDeMarcados, int posicionDelVertice) {
        ListaDeMarcados.set(posicionDelVertice, Boolean.TRUE);
    }

    private boolean estaMarcadoElVertice(List<Boolean> ListaDeMarcados, int posicionDelVertice) {
        return ListaDeMarcados.get(posicionDelVertice);
    }

    private boolean estanTodosMarcados(List<Boolean> ListaDeMarcados) {
        for (Boolean estaMarcado : ListaDeMarcados) {
            if (!estaMarcado) {
                return false;
            }
        }
        return true;
    }
//    --------------------------------------------------------------------------
    /**
     * Retorna el costo de avanzar del V.Origen al V.Destino. Si no hay camino
     * entre los vertices retorna -Infinto.
     *
     * @param verticeOrigen
     * @param verticeDestino
     * @return
     */
    public double costo(E verticeOrigen, E verticeDestino) {
        if (this.existeAdyacenciaEntre(verticeOrigen, verticeDestino)) {
            int posicionDelVerticeOrigen = this.getPosicionDelVertice(verticeOrigen);
            List<AdyacenteConPeso> adyacenciasDelVerticeOrigen = this.listasDeAdyacencias.get(posicionDelVerticeOrigen);
            int posicionDeAdyacenciaDelVD = buscarEnListaDeAdyacenciasConPeso(adyacenciasDelVerticeOrigen, verticeDestino);
            return adyacenciasDelVerticeOrigen.get(posicionDeAdyacenciaDelVD).getPeso();
        }
        return Double.POSITIVE_INFINITY;
    }

    /**
     * Retorna la posicion que ocupa el vertice a buscar B ,en la lista de
     * adyacencias de un Vertice A. Pre: El vertice a buscar existe.
     *
     * @param adyacenciasDeUnVertice
     * @param verticeABuscar
     * @return
     */
    private int buscarEnListaDeAdyacenciasConPeso(List<AdyacenteConPeso> adyacenciasDeUnVertice, E verticeABuscar) {
        int posVerticeABuscarEnListaDeVertices = this.getPosicionDelVertice(verticeABuscar);
        AdyacenteConPeso adyacenteABuscar = new AdyacenteConPeso(posVerticeABuscarEnListaDeVertices);
        return adyacenciasDeUnVertice.indexOf(adyacenteABuscar);
    }

    public double costo(int posicionOrigen, int posicionDestino) {
        if (this.existeAdyacenciaEntre(posicionOrigen, posicionDestino)) {
            List<AdyacenteConPeso> adyacenciasDelVerticeOrigen = this.listasDeAdyacencias.get(posicionOrigen);
            int posicionDeAdyacenciaDelVD = buscarEnListaDeAdyacenciasConPeso(adyacenciasDelVerticeOrigen,this.listaDeVertices.get(posicionDestino));
            return adyacenciasDelVerticeOrigen.get(posicionDeAdyacenciaDelVD).getPeso();
        }
        return Double.POSITIVE_INFINITY;
    }
    
    
    /**
     * ********-----DIRIGIDOS Y NO DIRIGIDOS------------*************
     */
    /**
     * Retorna True si al menos hay un ciclo en el Grafo. Retorna False si no
     * hay ningun ciclo en el Grafo o si no hay ningun vertice. El grafo puede
     * ser o no dirigido.
     *
     * @return
     */
    public Boolean hayCiclos() {
        List<Boolean> listaDeMarcados = new ArrayList<>();
        ponerTodosEnDesmarcado(listaDeMarcados);
        for (int posicionV = 0; posicionV < this.cantidadDeVertices(); posicionV++) {
            if (buscarCicloConDfsR(listaDeMarcados, posicionV, posicionV)) {
                return true;
            }
            desmarcarATodos(listaDeMarcados);
        }
        return false;
    }

    /**
     * Recorrido en profundidad desde un vertice de la posicion indicada.
     *
     * @param listaDeMarcados
     * @param posicionDeVertice
     */
    private boolean buscarCicloConDfsR(List<Boolean> listaDeMarcados, int posicionDeVertice, int posicionOrigen) {
        this.marcarVertice(listaDeMarcados, posicionDeVertice);
        List<AdyacenteConPeso> adyacenciasDeTurno = this.listasDeAdyacencias.get(posicionDeVertice);
        for (AdyacenteConPeso adyacente : adyacenciasDeTurno) {
            int posicionDeAdyacente = adyacente.getPosicionVertice();
            if (estaMarcadoElVertice(listaDeMarcados,posicionDeAdyacente )) {
                if (posicionOrigen == posicionDeAdyacente
                        && cantidadDeMarcados(listaDeMarcados) > 2) {
                    return true;
                }
            } else {
                if (buscarCicloConDfsR(listaDeMarcados,posicionDeAdyacente, posicionOrigen)) {
                    return true;
                }
            }
        }
        return false;
    }

    private int cantidadDeMarcados(List<Boolean> listaDeMarcados) {
        int cantidad = 0;
        for (boolean valor : listaDeMarcados) {
            if(valor) cantidad++;
        }
        return cantidad;
    }
    
    /* Retorna TRUE si el Grafo NO DIRIGIDO es Conexo. Retorna FALSE si el Grafo
     * no es conexo o no Hay Vertices en el Grafo.
     *
     * @return
     * @throws TDAGrafo.negocio.ExceptionGrafoTipoInvalido
     */
    
    public boolean esConexo() throws ExceptionGrafoTipoInvalido {
        if (this.esDirigido) {
            throw new ExceptionGrafoTipoInvalido();
        }

        if (!this.hayVerticesEnGrafo()) {
            return false;
        }
        List<E> recorridoDeGrafo = this.bfs(this.listaDeVertices.get(0));
        return recorridoDeGrafo.size() == this.listaDeVertices.size();
    }
    
    
     /**
     *____________________METODOS PARA LA MATRIZ DE PESOS Y ADYACENCIAS____________________________
     */
    /**
     * Retorna una matriz con el costo de llegar de un vertice a otro segun su posicion.
     * Si no hay adyacencia entre dos vertices, el costo sera INFINITO.
     * Para diGrafos.
     * @return
     */
    public double[][] getMatrizDePesosYAdyacencias() {
        final int dimension = this.cantidadDeVertices();
        double[][] matrizDeAdyacencia = new double[dimension][dimension];
        for (int indexOrigen = 0; indexOrigen < dimension; indexOrigen++) {
            for (int indexDestino = 0; indexDestino < dimension; indexDestino++) {
                matrizDeAdyacencia[indexOrigen][indexDestino] = costo(indexOrigen, indexDestino);
            }
        }
        return matrizDeAdyacencia;
    }

    /**
     * borra el registro de los lazos en la matriz de pesos y adyacencias.
     *
     * @param matrizDeAdyacencia
     */
    private void borrarLazosDe(double[][] matrizDePesoAdyacencia) {
        for (int index = 0; index < matrizDePesoAdyacencia.length; index++) {
            matrizDePesoAdyacencia[index][index] = Double.POSITIVE_INFINITY;
        }
    }
    
    /*_________________________Algoritmo de FLOYD______________________________*/            
    /**
     * A partir la matriz de pesos y adyacencias del DiGrafo, devuelve la matriz de 
     * costos minimos entre cada par de vertices, además carga la matriz de predecesores.
     * Algoritmo de FLOYD.  
     *
     * @param predecesores
     * @return
     * @throws TDAGrafo.negocio.ExceptionGrafoTipoInvalido
     */
    public double[][] getMatrizDeCostosMinimos(int[][] predecesores) throws ExceptionGrafoTipoInvalido {
        if (!this.esDirigido) {
            throw new ExceptionGrafoTipoInvalido("El Grafo debe ser Dirigido");
        }
        double[][] matrizCM = this.getMatrizDePesosYAdyacencias();        
        borrarLazosDe(matrizCM);
        int dimension = matrizCM.length;
        for (int puente = 0; puente < dimension; puente++) {
            for (int origen = 0; origen < dimension; origen++) {
                for (int destino = 0; destino < dimension; destino++) {
                   if (matrizCM[origen][puente] + matrizCM[puente][destino] < matrizCM[origen][destino]){
                       matrizCM[origen][destino] = matrizCM[origen][puente] + matrizCM[puente][destino];
                         predecesores[origen][destino] = puente;
                   }
                }
            }
        }
        return matrizCM;
    }
    
    private void inicializarPredecesores(int[][] predecesores){
        for (int origen = 0; origen < predecesores.length; origen++) {
                for (int destino = 0; destino < predecesores.length; destino++) {
                    predecesores[origen][destino] = -1;
                }
        }        
    }
    /**
     * Imprime todos los caminos de costo minimo de todos los vertices.
     * Utiliza la matriz de costos minimos(Algoritmo de Floyd).
     * @throws ExceptionGrafoTipoInvalido 
     */
    public void imprimirTodosLosCaminosDeCostoMinimo()throws ExceptionGrafoTipoInvalido {
        if (!this.esDirigido) {
            throw new ExceptionGrafoTipoInvalido("El Grafo debe ser Dirigido");
        }
        int dimension = this.cantidadDeVertices();
        int[][] predecesores = new int[dimension][dimension];
        inicializarPredecesores(predecesores);
        double[][] costosMinimos = getMatrizDeCostosMinimos(predecesores);
        for(int i = 0; i < dimension; i++){
            System.out.println("todos los caminos de costo minimo de : " + this.listaDeVertices.get(i));
            for( int j = 0; j < dimension; j++){
                System.out.println(getCaminoDeCostoMinimo(predecesores, costosMinimos, i, j));
            }
        }
    }
    
    /**
     * Retorna el camino de costo minimo entre los vertices indicados dadas la 
     * matrices de pesos y predecesores(Del algoritmo de Floyd).
     * @param predecesores
     * @param posOrigen
     * @param posDestino
     * @param pesos 
     */
    private List<E> getCaminoDeCostoMinimo(int[][] predecesores,double[][] pesos,int posOrigen, int posDestino){   
        List<E> camino = new LinkedList<>();
        if (pesos[posOrigen][posDestino] == Double.POSITIVE_INFINITY ){
//            System.out.println("no hay camino entre los vertices");
            return camino;
        }
//        System.out.print(this.listaDeVertices.get(posOrigen)+ " ");
        camino.add(this.listaDeVertices.get(posOrigen));
        navegarEnVerticesIntermedios(camino,predecesores,posOrigen,posDestino);        
//        System.out.print(this.listaDeVertices.get(posDestino));
        camino.add(this.listaDeVertices.get(posDestino));
        System.out.println("costo : " + pesos[posOrigen][posDestino]);
        return camino;
    }
    
    /**
     * Carga los vertices intermedios para llegar de V.Origen a V.Destino dada
     * la matriz de predecesores.
     * Pre: recibe parametros validos.
     * @param camino
     * @param posOrigen
     * @param posDestino
     * @param predecesores 
     */
    private void navegarEnVerticesIntermedios(List<E> camino,int[][] predecesores,int posOrigen, int posDestino) {
        int intermedio = predecesores[posOrigen][ posDestino];
        if (intermedio == -1){
            return;  
        }
             navegarEnVerticesIntermedios(camino,predecesores,posOrigen,intermedio);
             camino.add(this.listaDeVertices.get(intermedio)) ;
             navegarEnVerticesIntermedios(camino,predecesores,intermedio,posDestino);        
    }
    
    //________________ALGORITMO_DE_DIJKSTRA_________________________//

    /**
     * Retorna una lista de vertices que se pueden recorrer a a partir del verticePartida para llegar 
     * al verticeDestino con el menor "Costo" posible.
     * Algoritmo de DIJKSTRA.
     * Si no existe camino entre los vertices retorna -1.?
     * @param verticePartida
     * @param verticeDestino
     * @return
     * @throws ExceptionGrafoTipoInvalido 
     */
    public List<E> caminoDeCostoMinimoEntre(E verticePartida, E verticeDestino) throws ExceptionGrafoTipoInvalido {
        if (!this.esDirigido) {
            throw new ExceptionGrafoTipoInvalido("El Grafo debe ser Dirigido");
        }
        List<E> camino = new LinkedList<>();
         if(!hayVerticesEnGrafo()){
            return camino;
        }
         
        double[] pesos = new double[this.cantidadDeVertices()];
        List<Boolean> marcados = new ArrayList<>();
        int[] predecesores = new int[this.cantidadDeVertices()];        
        inicializarPesos(verticePartida, pesos);
        ponerTodosEnDesmarcado(marcados);
        inicializarPredecesores(predecesores);

        int posicionVerticeDestino = this.getPosicionDelVertice(verticeDestino);
        int posicionVMP = verticeDeMenorPeso(marcados, pesos);
        marcarVertice(marcados, posicionVMP);
        while (!estaMarcadoElVertice(marcados, posicionVerticeDestino)) {
            List<AdyacenteConPeso> adyacentesDelVMP = this.listasDeAdyacencias.get(posicionVMP);
            for (int index = 0; index < adyacentesDelVMP.size(); index++) {
                int posicionVA = adyacentesDelVMP.get(index).getPosicionVertice();
                if (!estaMarcadoElVertice(marcados, posicionVA)) {
                    if (pesos[posicionVA] > (pesos[posicionVMP] + costo(posicionVMP, posicionVA))) {
                        pesos[posicionVA] = pesos[posicionVMP] + costo(posicionVMP, posicionVA);
                        predecesores[posicionVA] = posicionVMP;
                    }
                }
            }
            posicionVMP = verticeDeMenorPeso(marcados, pesos);
            if (posicionVMP == -1) {
                System.out.println("no hay camino entre " + verticePartida + " y " + verticeDestino);
                return camino;
            }
            marcarVertice(marcados, posicionVMP);
        }
        obtenerCamino(camino, predecesores, posicionVerticeDestino);
        return camino;
    }
    
    /**
     * Imprime todos los caminos posibles del verticePartida a los demas vertices, con el menor costo posible.
     * Algoritmo de DIJKSTRA.
     * Si no existe camino a un vertice imprime vacio [].
     * @param verticePartida
     * @throws ExceptionGrafoTipoInvalido 
     */
     public void imprimirTodosLosCaminosDeCostoMinimo(E verticePartida) throws ExceptionGrafoTipoInvalido {
        if (!this.esDirigido) {
            throw new ExceptionGrafoTipoInvalido("El Grafo debe ser Dirigido");
        }

        double[] pesos = new double[this.cantidadDeVertices()];
        List<Boolean> marcados = new ArrayList<>();
        int[] predecesores = new int[this.cantidadDeVertices()];
        List<E> camino = new LinkedList<>();
        inicializarPesos(verticePartida, pesos);
        ponerTodosEnDesmarcado(marcados);
        inicializarPredecesores(predecesores);

        int posicionVMP = verticeDeMenorPeso(marcados, pesos);
        marcarVertice(marcados, posicionVMP);
        while (!estanTodosMarcados(marcados)) {
            List<AdyacenteConPeso> adyacentesDelVMP = this.listasDeAdyacencias.get(posicionVMP);
            for (int index = 0; index < adyacentesDelVMP.size(); index++) {
                int posicionVA = adyacentesDelVMP.get(index).getPosicionVertice();
                if (!estaMarcadoElVertice(marcados, posicionVA)) {
                    if (pesos[posicionVA] > (pesos[posicionVMP] + costo(posicionVMP, posicionVA))) {
                        pesos[posicionVA] = pesos[posicionVMP] + costo(posicionVMP, posicionVA);
                        predecesores[posicionVA] = posicionVMP;
                    }
                }
            }
            posicionVMP = verticeDeMenorPeso(marcados, pesos);
            if (posicionVMP == -1) {
                System.out.println("no hay caminos posible");
                break;
            }
            marcarVertice(marcados, posicionVMP);
        }
        
        for(int pos = 0; pos < this.cantidadDeVertices(); pos++){             
            int posicionVerticeDestino = getPosicionDelVertice(this.listaDeVertices.get(pos));
            if (posicionVerticeDestino == this.getPosicionDelVertice(verticePartida)){
                continue;
            }
            obtenerCamino(camino, predecesores, posicionVerticeDestino);            
            System.out.println(camino);
            camino.clear();
        }       
    }
    
    
    /**
     * Calcula la secuencia de vertices que hay que recorrer dado el vector de predecesores
     * para llegar al verticeDestino, cargar la secuencia en la lista de Caminos.
     * @param camino
     * @param predecesores
     * @param posicionDestino 
     */
    private void obtenerCamino(List<E> camino, int[] predecesores, int posicionDestino) {
        Stack<Integer> pila = new Stack<>();
        pila.push(posicionDestino);
        for (int pos = 0; pos < predecesores.length; pos++) {
            if (predecesores[posicionDestino] == -1) {
                break;
            }
            pila.push(predecesores[posicionDestino]);
            posicionDestino = pila.peek();
        }
        while (!pila.isEmpty()) {
            int posicionVertice = pila.pop();
            camino.add(this.listaDeVertices.get(posicionVertice));
        }
    }

    /**
     * Retorna la posicion del vertice de menor peso no marcado de la lista de
     * pesos. si no encuentra la posicion del vertice de menor peso retorna -1.
     *
     * @param marcados
     * @param pesos
     * @return
     */
    private int verticeDeMenorPeso(List<Boolean> marcados, double[] pesos) {
        double menorPeso = Double.POSITIVE_INFINITY;
        int posicionDelVerticeDeMenorPeso = -1;
        for (int index = 0; index < pesos.length; index++) {
            if (!estaMarcadoElVertice(marcados, index)) {
                if (pesos[index] < menorPeso) {
                    menorPeso = pesos[index];
                    posicionDelVerticeDeMenorPeso = index;
                }
            }
        }
        return posicionDelVerticeDeMenorPeso;
    }

    private void inicializarPesos(E verticeDePartida, double[] pesos) {
        int posicionPartida = getPosicionDelVertice(verticeDePartida);
        for (int index = 0; index < pesos.length; index++) {
            pesos[index] = Double.POSITIVE_INFINITY;
        }
        pesos[posicionPartida] = 0.0;
    }

    private void inicializarPredecesores(int[] predecesores) {
        for (int index = 0; index < predecesores.length; index++) {
            predecesores[index] = -1;
        }
    }

    //________________ALGORITMO_DE_KRUSKAL________________________//
       
    public ListGrafoPesado<E> arbolDeExpansionDeCostoMinimoK()throws ExceptionGrafoTipoInvalido {
        if (this.esDirigido) {
            throw new ExceptionGrafoTipoInvalido("El Grafo debe ser No-Dirigido");
        }
        ListGrafoPesado<E> arbol = new ListGrafoPesado<>();
        if (!hayVerticesEnGrafo()){
            return arbol;
        }
       
        for (E verticeAInsertar : listaDeVertices){
            arbol.insertarVertice(verticeAInsertar);
        }
        List<AristaConPeso> listaDeAristas = new LinkedList<>();
        cargarAristasEn(listaDeAristas);
        Collections.sort(listaDeAristas);
        System.out.println(listaDeAristas);
        for(AristaConPeso aristaAInsertar : listaDeAristas){
            int posVerticeOrigen = aristaAInsertar.getPosicionOrigen();
            int posVerticeDestino = aristaAInsertar.getPosicionDestino();
            double pesoDeArista = aristaAInsertar.getPeso();
            arbol.insertarArista(listaDeVertices.get(posVerticeOrigen), listaDeVertices.get(posVerticeDestino), pesoDeArista);
            if (arbol.hayCiclos()){
                System.out.println("genera ciclo =" + aristaAInsertar);
                 arbol.eliminarArista(listaDeVertices.get(posVerticeOrigen), listaDeVertices.get(posVerticeDestino));
       }
        }
        return arbol;
    }

    private void cargarAristasEn(List<AristaConPeso> listaDeAristas) {
        for(int index = 0; index < cantidadDeVertices(); index++){
            List<AdyacenteConPeso> adyacencias = listasDeAdyacencias.get(index);
            for(AdyacenteConPeso adyacente : adyacencias){     
                AristaConPeso arista = new AristaConPeso(index,adyacente.getPosicionVertice(),adyacente.getPeso());
                AristaConPeso aristaInversa = new AristaConPeso(adyacente.getPosicionVertice(),index,adyacente.getPeso());
                if(!listaDeAristas.contains(arista) && !listaDeAristas.contains(aristaInversa)){
                    listaDeAristas.add(arista);
                }                
            }
        }
        System.out.println(listaDeAristas);
    }
    
    //________________ALGORITMO_DE_PRIM________________________//
    
    public ListGrafoPesado<E> arbolDeExpansionDeCostoMinimoP()throws ExceptionGrafoTipoInvalido {
        if (this.esDirigido) {
            throw new ExceptionGrafoTipoInvalido("El Grafo debe ser No-Dirigido");
        }
        ListGrafoPesado<E> arbol = new ListGrafoPesado<>();
        if (!hayVerticesEnGrafo()){
            return arbol;
        }
        arbol.insertarVertice(listaDeVertices.get(0));
        
        return arbol;
    }
    
    
    

    public static void main(String[] args) throws ExceptionGrafoTipoInvalido {
//        ListGrafoPesado<String> g = new ListGrafoPesado<>();
//        g.insertarVertice("V1");
//        g.insertarVertice("V2");
//        g.insertarVertice("V3");
//        g.insertarVertice("V4");
//        g.insertarVertice("V5");
//        g.insertarArista("V1", "V2", 1);
//        g.insertarArista("V2", "V5", 7);
//        g.insertarArista("V2", "V4", 4);
//        g.insertarArista("V3", "V2", 2);
//        g.insertarArista("V3", "V5", 4);
//        g.insertarArista("V4", "V1", 6);
//        g.insertarArista("V4", "V5", 2);
//        g.insertarArista("V5", "V4", 3);
//        g.imprimirTodosLosCaminosDeCostoMinimo();
         
        /* Algoritmo de kruskal */
          ListGrafoPesado<Integer> g = new ListGrafoPesado<>();
          g.insertarVertice(1);
          g.insertarVertice(2);
          g.insertarVertice(3);
          g.insertarVertice(4);
          g.insertarVertice(5);
          
          g.insertarArista(1, 2, 1);
          g.insertarArista(1, 3, 3);
          System.out.println(g.hayCiclos());
//          g.insertarArista(2, 4, 6);
//          g.insertarArista(2, 3, 3);
//          g.insertarArista(3, 5, 2);
//          g.insertarArista(3, 4, 4);
//          g.insertarArista(5, 4, 5);
          
//          System.out.println(g.esConexo());
//          ListGrafoPesado<Integer> g2 = g.arbolDeExpansionDeCostoMinimoK();
//          System.out.println(g2.hayCiclos());
//          System.out.println(g2.esConexo());
//          System.out.println( g2.dfs(5));
    }

    
}
