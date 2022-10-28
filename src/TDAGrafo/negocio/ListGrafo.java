package TDAGrafo.negocio;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 *
 * @author HP
 * @param <E>
 */
public class ListGrafo< E extends Comparable> {

    private List<E> listaDeVertices;
    private List< List<Integer>> listasDeAdyacencias;
    boolean esDirigido;

    public ListGrafo() {
        this(false);
    }

    public ListGrafo(boolean dirigido) {
        listaDeVertices = new ArrayList<>();
        listasDeAdyacencias = new ArrayList<>();
        esDirigido = dirigido;
    }

    public boolean insertarVertice(E vertice) {
        if (this.existeVertice(vertice)) {
            return false;
        }
        listaDeVertices.add(vertice);
        List<Integer> adyacenciasDelVertice = new ArrayList<>();
        listasDeAdyacencias.add(adyacenciasDelVertice);
        return true;
    }

    public boolean insertarArista(E verticeOrigen, E verticeDestino) {
        if (!this.existeVertice(verticeOrigen)
                || !this.existeVertice(verticeDestino)) {
            return false;
        }
        if (this.existeAdyacenciaEntre(verticeOrigen, verticeDestino)) {
            return false;
        }

        int posicionDelVerticeOrigen = this.getPosicionDelVertice(verticeOrigen);
        int posicionDelVerticeDestino = this.getPosicionDelVertice(verticeDestino);
        List<Integer> adyacenciasDelVerticeOrigen = this.listasDeAdyacencias.get(posicionDelVerticeOrigen);
        adyacenciasDelVerticeOrigen.add(posicionDelVerticeDestino);
        Collections.sort(adyacenciasDelVerticeOrigen);
        if (!this.esDirigido && !this.esLazo(verticeOrigen, verticeDestino)) {
            List<Integer> adyacenciasDelVerticeDestino = this.listasDeAdyacencias.get(posicionDelVerticeDestino);
            adyacenciasDelVerticeDestino.add(posicionDelVerticeOrigen);
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

        for (List<Integer> adyacenciasDeUnVertice : this.listasDeAdyacencias) {
            if (adyacenciasDeUnVertice.contains(posicionDelVertice)) {
                adyacenciasDeUnVertice.remove((Integer) posicionDelVertice);
            }
            for (int i = 0; i < adyacenciasDeUnVertice.size(); i++) {
                int posicionDeVerticeAdyacente = adyacenciasDeUnVertice.get(i);
                if (posicionDeVerticeAdyacente > posicionDelVertice) {
                    adyacenciasDeUnVertice.set(i, posicionDeVerticeAdyacente - 1);
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
        List<Integer> adyacenciasDelVerticeOrigen = this.listasDeAdyacencias.get(posicionDelVerticeOrigen);
        adyacenciasDelVerticeOrigen.remove((Integer) posicionDelVerticeDestino);

        if (!this.esDirigido) {
            List<Integer> adyacenciasDelVerticeDestino = this.listasDeAdyacencias.get(posicionDelVerticeDestino);
            adyacenciasDelVerticeDestino.remove((Integer) posicionDelVerticeOrigen);
        }
        return true;
    }

    public int cantidadDeVertices() {
        return listaDeVertices.size();
    }

    public int cantidadDeAristas() {
        int cantidad = 0;
        for (List<Integer> adyancenciasDeUnVertice : this.listasDeAdyacencias) {
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
        List<Integer> adyacenciasDelVerticeOrigen = this.listasDeAdyacencias.get(posicionVerticeOrigen);
        return adyacenciasDelVerticeOrigen.contains(posicionVerticeDestino);
    }

    private boolean existeAdyacenciaEntre(int posicionVerticeOrigen, int posicionVerticeDestino) {
        List<Integer> adyacenciasDelVerticeOrigen = this.listasDeAdyacencias.get(posicionVerticeOrigen);
        return adyacenciasDelVerticeOrigen.contains((Integer) posicionVerticeDestino);
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
            List<Integer> adyacentesDelVerticeDeTurno = this.listasDeAdyacencias.get(posicionDeVerticeDeTurno);
            for (Integer posicionDeAdyacente : adyacentesDelVerticeDeTurno) {
                if (!this.estaMarcadoElVertice(listaDeMarcados, posicionDeAdyacente)) {
                    colaDeVertices.offer(this.listaDeVertices.get(posicionDeAdyacente));
                    this.marcarVertice(listaDeMarcados, posicionDeAdyacente);
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
        List<Integer> adyancenciasDelVerticeEnTurno = this.listasDeAdyacencias.get(posicionDeVertice);
        for (Integer adyacenteDeUnVertice : adyancenciasDelVerticeEnTurno) {
             if (!estaMarcadoElVertice(listaDeMarcados,adyacenteDeUnVertice )) {
                this.dfsR(recorrido, listaDeMarcados, adyacenteDeUnVertice);
            }
        }
    }

    /**
     * ** METODOS PARA IDENTIFICAR QUE VERTICES FUERON PROCESADOS POR LOS
     * RECORRIDOS BFS Y DFS ***
     */
    /**
     * Metodo que añade False a la listaDeMarcados por cada vertice.
     *
     * @param ListaDeMarcados
     */
    private void ponerTodosEnDesmarcado(List<Boolean> listaDeMarcados) {
        for (int index = 0; index < this.cantidadDeVertices(); index++) {
            listaDeMarcados.add(Boolean.FALSE);
        }
    }

    private void desmarcarATodos(List<Boolean> listaDeMarcados) {
        for (int index = 0; index < listaDeMarcados.size(); index++) {
            listaDeMarcados.set(index, Boolean.FALSE);
        }
    }

    private void marcarVertice(List<Boolean> listaDeMarcados, int posicionDelVertice) {
        listaDeMarcados.set(posicionDelVertice, Boolean.TRUE);
    }

    private boolean estaMarcadoElVertice(List<Boolean> listaDeMarcados, int posicionDelVertice) {
        return listaDeMarcados.get(posicionDelVertice);
    }

    private boolean estanTodosMarcados(List<Boolean> listaDeMarcados) {
        for (Boolean estaMarcado : listaDeMarcados) {
            if (!estaMarcado) {
                return false;
            }
        }
        return true;
    }

    /**
     * ** METODOS PARA LA MATRIZ DE CAMINOS ***
     */
    /**
     * Retorna una matriz booleana, que indica las adyancencias entre los
     * vertices segun sus posiciones.
     *
     * @return
     */
    public Boolean[][] getMatrizDeAdyancencia() {
        final int dimension = this.cantidadDeVertices();
        Boolean[][] matrizDeAdyacencia = new Boolean[dimension][dimension];
        for (int indexOrigen = 0; indexOrigen < dimension; indexOrigen++) {
            for (int indexDestino = 0; indexDestino < dimension; indexDestino++) {
                matrizDeAdyacencia[indexOrigen][indexDestino] = existeAdyacenciaEntre(indexOrigen, indexDestino);
            }
        }
        return matrizDeAdyacencia;
    }

    /**
     * borra el registro de los lazos en la matriz de adyacencia.
     *
     * @param matrizDeAdyacencia
     */
    private void borrarLazosDe(Boolean[][] matrizDeAdyacencia) {
        for (int index = 0; index < matrizDeAdyacencia.length; index++) {
            matrizDeAdyacencia[index][index] = false;
        }
    }

    /**
     * A partir la matriz de adyacencias del Grafo, devuelve la matriz de
     * caminos P , usando el Algoritmo de Warshall.
     *
     * @return
     */
    public Boolean[][] getMatrizDeCaminos() {
        Boolean[][] matrizDeCaminos = this.getMatrizDeAdyancencia();
        borrarLazosDe(matrizDeCaminos);
        int dimension = matrizDeCaminos.length;
        for (int puente = 0; puente < dimension; puente++) {
            for (int origen = 0; origen < dimension; origen++) {
                for (int destino = 0; destino < dimension; destino++) {
                    matrizDeCaminos[origen][destino] = (matrizDeCaminos[origen][destino])
                            || (matrizDeCaminos[origen][puente] && matrizDeCaminos[puente][destino]);
                }
            }
        }
        return matrizDeCaminos;
    }

    /**
     * ** METODOS PARA REVISAR LAS CARACTERISTICAS DEL GRAFO ***
     */
    //////---------------******** GRAFO NO DIRIGIDO -------------------------*********//////
    /**
     * Retorna TRUE si el Grafo NO DIRIGIDO es Conexo. Retorna FALSE si el Grafo
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
     * Retorna la cantidad de "islas" o componentes conexas hay en un Grafo No
     * Dirigido.
     *
     * @return
     * @throws TDAGrafo.negocio.ExceptionGrafoTipoInvalido
     */
    public int cantidadDeIslasEnGrafoNoDirigido() throws ExceptionGrafoTipoInvalido {
        if (this.esDirigido) {
            throw new ExceptionGrafoTipoInvalido("El Grafo Debe ser NO-DIRIGIDO");
        }
        if (!hayVerticesEnGrafo()) {
            return 0;
        }
        int Contador = 0;
        List<Boolean> listaDeMarcados = new ArrayList<>();
        ponerTodosEnDesmarcado(listaDeMarcados);
        while (!estanTodosMarcados(listaDeMarcados)) {
            int  posVerticeNoMarcado = getVerticeNoMarcado(listaDeMarcados);
            Contador++;
            this.dfsR(listaDeMarcados,posVerticeNoMarcado);
        }
        return Contador;
    }

    /**
     * Tiene fallas teoricas. Mejor usar arbol de expansion
     * @return
     * @throws ExceptionGrafoTipoInvalido 
     */
    public List<E> puntosDeArticulacion()throws ExceptionGrafoTipoInvalido {
        if (this.esDirigido) {
            throw new ExceptionGrafoTipoInvalido("El Grafo Debe ser NO-DIRIGIDO");
        }
        List<E> puntos = new LinkedList<>();
        List<E> recorrido = new LinkedList<>();
        List<Boolean> listaDeMarcados = new ArrayList<>();
            ponerTodosEnDesmarcado(listaDeMarcados);
        for (int index = 0; index < this.listaDeVertices.size(); index++) {
            
            marcarVertice(listaDeMarcados, index);
            int posicionVerticeNoMarcado = getVerticeNoMarcado(listaDeMarcados);
            if (posicionVerticeNoMarcado == -1){
                break;
            }
            dfsR(recorrido,listaDeMarcados, posicionVerticeNoMarcado);
            if (recorrido.size() < cantidadDeVertices() - 1){
                puntos.add(this.listaDeVertices.get(index));
            }
            recorrido.clear();
            desmarcarATodos(listaDeMarcados);
        }
        return puntos;
    }
    /**
     * ******************COMPONENTES CONEXAS****************************
     */
    /**
     * Retorna True si el Grafo es Conexo o Retorna False si no es conexo o no
     * hay vertices --------- Imprime todos los componenetes conexos de un Grafo
     * NO-DIRIGIDO. si el grafo es conexo imprime todos los elementos del grafo.
     *
     * @return
     * @throws TDAGrafo.negocio.ExceptionGrafoTipoInvalido
     */
    public boolean imprimirComponentesConexas() throws ExceptionGrafoTipoInvalido {
        if (this.esDirigido) {
            throw new ExceptionGrafoTipoInvalido("El Grafo Debe ser NO-DIRIGIDO");
        }
        if (!hayVerticesEnGrafo()) {
            return false;
        }
        List<Boolean> listaDeMarcados = new ArrayList<>();
        ponerTodosEnDesmarcado(listaDeMarcados);
        List<E> componenteActual = new LinkedList<>();
        E verticeInicio = this.listaDeVertices.get(0);
        dfsR(componenteActual, listaDeMarcados, this.getPosicionDelVertice(verticeInicio)); //marcar un Componete Conexo
        System.out.println(componenteActual);
        if (componenteActual.size() == this.cantidadDeVertices()) { // si estan todos marcados
            return true;
        }
        while (!estanTodosMarcados(listaDeMarcados)) {
            int posVerticeSinMarcar = this.getVerticeNoMarcado(listaDeMarcados);
            componenteActual.clear();
            dfsR(componenteActual, listaDeMarcados,posVerticeSinMarcar);  //marcar un Componete Conexo
            System.out.println(componenteActual);
        }
        return false;
    }

    //////********------------------ GRAFO DIRIGIDO---------------------- *********//////
    /**
     * metodo que devuelve una lista con los vertices de los cuales se puede
     * partir para llegar al verticeX. El grafo si o si debe ser dirigido.
     *
     * @param verticeX
     * @return
     * @throws ExceptionGrafoTipoInvalido
     */
    public List<E> partidasParaLlegarA(E verticeX) throws ExceptionGrafoTipoInvalido {
        if (!this.esDirigido) {
            throw new ExceptionGrafoTipoInvalido();
        }

        List<E> partidas = new LinkedList<>();
        if (!this.existeVertice(verticeX)) {
            return partidas;
        }
        ListGrafo auxiliar = new ListGrafo(true);
        copiarGrafoInvertido(auxiliar);
        partidas = auxiliar.bfs(verticeX);
        return partidas;
    }

    /**
     * Copia el grafo a otro llamado auxiliar de manera que el sentido de las
     * aristas sea invertido.
     *
     * @param auxiliar
     */
    private void copiarGrafoInvertido(ListGrafo auxiliar) {
        for (E verticeActual : this.listaDeVertices) {
            auxiliar.insertarVertice(verticeActual);
        }

        for (int index = 0; index < this.listasDeAdyacencias.size(); index++) {
            List<Integer> adyacencias = this.listasDeAdyacencias.get(index);
            E verticeDestino = this.listaDeVertices.get(index);
            for (Integer posicionPartida : adyacencias) {
                E verticePartida = this.listaDeVertices.get(posicionPartida);
                auxiliar.insertarArista(verticePartida, verticeDestino);
            }
        }
    }

    /**
     * devuelve True si el Grafo es fuertemente conexo . Debe ser si o si Grafo
     * Dirigido. Retorna False si no es fuertemente Conexo y si no hay vertices
     * en el Grafo.
     *
     * @return
     * @throws ExceptionGrafoTipoInvalido
     */
    public Boolean esFuertementeConexo() throws ExceptionGrafoTipoInvalido {
        if (!this.esDirigido) {
            throw new ExceptionGrafoTipoInvalido();
        }
        if (!this.hayVerticesEnGrafo()) {
            return false;
        }

        for (E verticeDeTurno : this.listaDeVertices) {
            List<E> recorrido = this.bfs(verticeDeTurno);
            if (recorrido.size() != this.cantidadDeVertices()) {
                return false;
            }
        }
        return true;
    }

    /**
     * ******************COMPONENTES FUERTEMENTE
     * CONEXAS****************************
     */
    /**
     * Retorna True si el Grafo es Conexo o Retorna False si no es conexo o no
     * hay vertices --------- Imprime todos los componenetes conexos de un Grafo
     * NO-DIRIGIDO. si el grafo es conexo imprime todos los elementos del grafo.
     *
     * @return
     * @throws ExceptionGrafoTipoInvalido
     */
    public boolean imprimirComponentesFuertementeConexas() throws ExceptionGrafoTipoInvalido {
        if (!this.esDirigido) {
            throw new ExceptionGrafoTipoInvalido();
        }
        if (!this.hayVerticesEnGrafo()) {
            return false;
        }
        ListGrafo invertido = new ListGrafo<>(true);
        this.copiarGrafoInvertido(invertido);

        List<Boolean> listaDeMarcadosA = new LinkedList<>();
        List<Boolean> listaDeMarcadosB = new LinkedList<>();
        ponerTodosEnDesmarcado(listaDeMarcadosA);
        ponerTodosEnDesmarcado(listaDeMarcadosB);

        List<E> descendientes = new LinkedList<>();
        List<E> ascendientes = new LinkedList<>();
        List<Boolean> listaDeMarcadosI = new LinkedList<>();
        ponerTodosEnDesmarcado(listaDeMarcadosI);

        this.dfsR(descendientes, listaDeMarcadosA, 0); //obtiene los descendientes desde el vertice inicial
        invertido.dfsR(ascendientes, listaDeMarcadosB, 0);  //obtiene los ascendientes del vertice inicial

        List<E> componente = interseccion(ascendientes, descendientes, listaDeMarcadosI);
        System.out.println(componente);
        if (estanTodosMarcados(listaDeMarcadosI)) { // si estan todos marcados (todos en un componente frtmte. conexo)
            return true; //es frtmnte. conexo.
        }
        do {
            desmarcarATodos(listaDeMarcadosA);
            desmarcarATodos(listaDeMarcadosB);
            descendientes.clear();
            ascendientes.clear();
            int posVerticeNoMarcado = this.getVerticeNoMarcado(listaDeMarcadosI);
            this.dfsR(descendientes, listaDeMarcadosA, posVerticeNoMarcado);
            invertido.dfsR(ascendientes, listaDeMarcadosB,posVerticeNoMarcado);
            componente = interseccion(ascendientes, descendientes, listaDeMarcadosI);
            System.out.println(componente);
        } while (!estanTodosMarcados(listaDeMarcadosI)); //mientras no todos esten en componentes frtmte. conexos

        return false;
    }

    private List<E> interseccion(List<E> listaA, List<E> listaB, List<Boolean> listaDeMarcadosI) {
        List<E> interseccion = new LinkedList<>();
        for (int index = 0; index < listaA.size(); index++) {
            E elemento = this.listaDeVertices.get(index);
            if (listaB.contains(elemento)) {
                interseccion.add(elemento);
                listaDeMarcadosI.set(index, Boolean.TRUE);
            }
        }
        return interseccion;
    }

    public List<E> getComponenteFuertementeConexoDe(E verticeX) throws ExceptionGrafoTipoInvalido {
        if (!this.esDirigido) {
            throw new ExceptionGrafoTipoInvalido();
        }
        List<E> componente = new LinkedList<>();
        if (!this.existeVertice(verticeX)) {
            return componente;
        }
        ListGrafo invertido = new ListGrafo<>(true);
        this.copiarGrafoInvertido(invertido);

        List<Boolean> listaDeMarcadosA = new LinkedList<>();
        List<Boolean> listaDeMarcadosB = new LinkedList<>();
        ponerTodosEnDesmarcado(listaDeMarcadosA);
        ponerTodosEnDesmarcado(listaDeMarcadosB);

        List<E> descendientes = new LinkedList<>();
        List<E> ascendientes = new LinkedList<>();
        this.dfsR(descendientes, listaDeMarcadosA, this.getPosicionDelVertice(verticeX));
        invertido.dfsR(ascendientes, listaDeMarcadosB, this.getPosicionDelVertice(verticeX));
        return interseccion(ascendientes, descendientes);
    }

    private List<E> interseccion(List<E> listaA, List<E> listaB) {
        List<E> interseccion = new LinkedList<>();
        for (E elemento : listaA) {
            if (listaB.contains(elemento)) {
                interseccion.add(elemento);
            }
        }
        return interseccion;
    }

    /**
     * Pre: El grafo no debe ser Fuertemente Conexo. Retorna True si el Grafo es
     * debilmente Conexo.
     *
     * @return
     * @throws ExceptionGrafoTipoInvalido
     */
    public Boolean esDebilmenteConexo() throws ExceptionGrafoTipoInvalido {
        if (!this.esDirigido) {
            throw new ExceptionGrafoTipoInvalido();
        }
        if (!this.hayVerticesEnGrafo()) {
            return false;
        }
        //validar si no es fuertemente conexo
        List<Boolean> listaDeMarcados = new ArrayList<>();
        ponerTodosEnDesmarcado(listaDeMarcados);
        E verticeInicio = this.listaDeVertices.get(0);
        dfsR(listaDeMarcados, this.getPosicionDelVertice(verticeInicio));
        while (!this.estanTodosMarcados(listaDeMarcados)) {
            int posVerticeConAdyacenteMarcado = this.getVerticeConAdyacenteMarcado(listaDeMarcados);
            if (posVerticeConAdyacenteMarcado == -1) {
                return false;
            }
            //marca los vertices  los que se pueda llegar desde verticeConAdyacenteMarcado
            dfsR(listaDeMarcados, posVerticeConAdyacenteMarcado);
        }
        return true;
    }

    /**
     * Recorrido en profundidad desde un vertice de la posicion indicada.
     *
     * @param listaDeMarcados
     * @param posicionDeVertice
     */
    private void dfsR(List<Boolean> listaDeMarcados, int posicionDeVertice) {
        this.marcarVertice(listaDeMarcados, posicionDeVertice);
        List<Integer> adyacenciasDeTurno = this.listasDeAdyacencias.get(posicionDeVertice);
        for (int posicionDeAdyacente : adyacenciasDeTurno) {
            if (!estaMarcadoElVertice(listaDeMarcados, posicionDeAdyacente)) {
                dfsR(listaDeMarcados, posicionDeAdyacente);
            }
        }
    }

    /**
     * Retorna la posicion de un vertice que no este marcado pero que tenga un adyacente
     * marcado.Si no lo encuentra retorna -1;
     *
     * @return
     */
    private int getVerticeConAdyacenteMarcado(List<Boolean> listaDeMarcados) {
        for (E verticeDeTurno : this.listaDeVertices) {
            int posicionDeVerticeDeTurno = this.getPosicionDelVertice(verticeDeTurno);
            if (!this.estaMarcadoElVertice(listaDeMarcados, posicionDeVerticeDeTurno)) {
                List<Integer> adyacenciasDeTurno = this.listasDeAdyacencias.get(posicionDeVerticeDeTurno);
                for (Integer posicionAdyacente : adyacenciasDeTurno) {
                    if (!estaMarcadoElVertice(listaDeMarcados, posicionAdyacente)) {
                        return posicionAdyacente;
                    }
                }
            }
        }
        return -1;
    }

    private static Object verticeVacio() {
        return null;
    }

    /**
     * retorna la posicion del primer vertice que no este marcado en la lista de vertices. si
     * no hay vertices marcados retorna vacio = null;
     *
     * @param listaDeMarcados
     * @return
     */
    private int getVerticeNoMarcado(List<Boolean> listaDeMarcados) {
        for (int index = 0; index < this.cantidadDeVertices(); index++) {
            if (!estaMarcadoElVertice(listaDeMarcados, index)) {
                return index;
            }
        }
        return -1;
    }

    /**
     * Retorna la cantidad de "islas" o componentes fuertemente conexas de un
     * Grafo Dirigido.
     *
     * @return
     * @throws ExceptionGrafoTipoInvalido
     */
    public int cantidadDeIslasEnDiGrafo() throws ExceptionGrafoTipoInvalido {
        if (!esDirigido) {
            throw new ExceptionGrafoTipoInvalido("El grafo debe ser Dirigido");
        }
        if (!hayVerticesEnGrafo()) {
            return 0;
        }
        int Contador = 1;
        List<Boolean> listaDeMarcados = new ArrayList<>();
        ponerTodosEnDesmarcado(listaDeMarcados);
        E verticeInicial = this.listaDeVertices.get(0);
        this.dfsR(listaDeMarcados, this.getPosicionDelVertice(verticeInicial));
        while (!estanTodosMarcados(listaDeMarcados)) {
            int posVerticeX =  this.getVerticeConAdyacenteMarcado(listaDeMarcados);
            if (posVerticeX == -1) {
                Contador++;
                posVerticeX = this.getVerticeNoMarcado(listaDeMarcados);
            }
            //marca todos los vertices a los que se pueda llegar desde verticeX
            this.dfsR(listaDeMarcados,posVerticeX);
        }
        return Contador;
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
        List<Integer> adyacenciasDeTurno = this.listasDeAdyacencias.get(posicionDeVertice);
        for (int posicionDeAdyacente : adyacenciasDeTurno) {
            if (estaMarcadoElVertice(listaDeMarcados, posicionDeAdyacente)) {
                if (posicionOrigen == posicionDeAdyacente                        
                        && cantidadDeMarcados(listaDeMarcados) > 2) {
                    return true;
                }
            } else {
                if (buscarCicloConDfsR(listaDeMarcados, posicionDeAdyacente, posicionOrigen)) {
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
    
    
    /**
     * ************************ORDENAMIENTO
     * TOPOLOGICO*******************************
     */
    /**
     * Metodo que de un grafo Dirigido - Conexo - Aciclico realiza un
     * ordenamiento Topologico
     *
     * @return
     * @throws ExceptionGrafoTipoInvalido
     */
    public List<E> ordenamientoTopologico() throws ExceptionGrafoTipoInvalido {
        if (!this.esDirigido) {
            throw new ExceptionGrafoTipoInvalido("El grafo debe ser Dirigido");
        }
        if (this.hayCiclos()) {
            throw new ExceptionGrafoTipoInvalido("El Grafo debe ser Aciclico");
        }
        if (!this.esFuertementeConexo()) {
            if (!this.esDebilmenteConexo()) {
                throw new ExceptionGrafoTipoInvalido("El Grafo debe ser Conexo");
            }
        }
        List<E> listaEnOrden = new LinkedList<>();
        List<Integer> listaDeGradosDeEntrada = this.gradosDeEntradaXVertice();
        Queue<E> colaDeVertices = new LinkedList<>();
        agregarVerticesFuente(colaDeVertices, listaDeGradosDeEntrada, listaEnOrden);

        while (!colaDeVertices.isEmpty()) {
            E verticeDeTurno = colaDeVertices.poll();
            listaEnOrden.add(verticeDeTurno);
            decrementarGradosDeEntradaAdyacentes(verticeDeTurno, listaDeGradosDeEntrada);
            agregarVerticesFuente(colaDeVertices, listaDeGradosDeEntrada, listaEnOrden);
        }
        return listaEnOrden;
    }

    /**
     * Devuelve una lista con los grados de entrada de cada Vertice. Las
     * posiciones en la listaDeGradosDeEntradaXVertice coinciden con las
     * posiciones en la ListaDeVertices.
     *
     * @return
     */
    private List<Integer> gradosDeEntradaXVertice() {
        List<Integer> gradosDeEntrada = new ArrayList<>();
        if (!this.hayVerticesEnGrafo()) {
            return gradosDeEntrada;
        }
        for (int index = 0; index < this.cantidadDeVertices(); index++) {
            gradosDeEntrada.add(0);
        }
        for (List<Integer> adyacentesDeUnVertice : this.listasDeAdyacencias) {
            for (Integer posicionDeAdyacente : adyacentesDeUnVertice) {
                gradosDeEntrada.set(posicionDeAdyacente, gradosDeEntrada.get(posicionDeAdyacente) + 1);
            }
        }
        return gradosDeEntrada;
    }

    /**
     * Agrega en la cola aquellos vertices que tienen gradoDeEntrada = 0
     * (vertices Fuente) solo si no fue agregado con anterioridad.
     *
     * @param colaDeVertices
     * @param listaDeGradosDeEntrada
     * @param listaDeAgregados
     */
    private void agregarVerticesFuente(Queue<E> colaDeVertices, List<Integer> listaDeGradosDeEntrada, List<E> listaDeAgregados) {
        for (int index = 0; index < listaDeGradosDeEntrada.size(); index++) {
            E verticeActual = this.listaDeVertices.get(index);
            if (!listaDeAgregados.contains(verticeActual)) {
                Integer gradoDeUnVertice = listaDeGradosDeEntrada.get(index);
                if (gradoDeUnVertice == 0) {
                    colaDeVertices.offer(verticeActual);
                }
            }
        }
    }

    /**
     * Decrementa en 1 el numero de GradosDeEntrada(en la
     * listaDeGradosDeEntrada) de cada Vertice adyacente al verticeActual.
     *
     * @param verticeActual
     * @param listaDeGradosDeEntrada
     */
    private void decrementarGradosDeEntradaAdyacentes(E verticeActual, List<Integer> listaDeGradosDeEntrada) {
        List<Integer> adyacentesDelVertice = this.listasDeAdyacencias.get(this.getPosicionDelVertice(verticeActual));
        for (Integer posicionDeAdyacente : adyacentesDelVertice) {
            listaDeGradosDeEntrada.set(posicionDeAdyacente, listaDeGradosDeEntrada.get(posicionDeAdyacente) - 1);
        }
    }

}
