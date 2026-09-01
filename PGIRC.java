import java.util.*;

public class PGIRC {
  
    // 1. ÁRBOL BINARIO DE BÚSQUEDA
    // =====================================================

    static class Nodo {
        int valor;
        Nodo izquierdo;
        Nodo derecho;

        Nodo(int valor) {
            this.valor = valor;
        }
    }

    static class ArbolBinarioBusqueda {

        Nodo raiz;

        // Insertar un nuevo valor
        public void insertar(int valor) {
            raiz = insertarRecursivo(raiz, valor);
        }

        private Nodo insertarRecursivo(Nodo nodo, int valor) {

            if (nodo == null) {
                return new Nodo(valor);
            }

            if (valor < nodo.valor) {
                nodo.izquierdo = insertarRecursivo(nodo.izquierdo, valor);
            } else if (valor > nodo.valor) {
                nodo.derecho = insertarRecursivo(nodo.derecho, valor);
            }

            return nodo;
        }

        // Buscar un valor mostrando las comparaciones
        public boolean buscar(int valor) {

            Nodo actual = raiz;

            while (actual != null) {

                System.out.println(
                        "Comparando " + valor + " con " + actual.valor
                );

                if (valor == actual.valor) {
                    return true;
                }

                if (valor < actual.valor) {
                    actual = actual.izquierdo;
                } else {
                    actual = actual.derecho;
                }
            }

            return false;
        }

        // Recorrido Inorden
        public void inorden(Nodo nodo) {

            if (nodo != null) {

                inorden(nodo.izquierdo);

                System.out.print(nodo.valor + " ");

                inorden(nodo.derecho);
            }
        }

        // Recorrido Preorden
        public void preorden(Nodo nodo) {

            if (nodo != null) {

                System.out.print(nodo.valor + " ");

                preorden(nodo.izquierdo);

                preorden(nodo.derecho);
            }
        }

        // Recorrido Postorden
        public void postorden(Nodo nodo) {

            if (nodo != null) {

                postorden(nodo.izquierdo);

                postorden(nodo.derecho);

                System.out.print(nodo.valor + " ");
            }
        }

        // Eliminar un nodo
        public void eliminar(int valor) {
            raiz = eliminarRecursivo(raiz, valor);
        }

        private Nodo eliminarRecursivo(Nodo nodo, int valor) {

            if (nodo == null) {
                return null;
            }

            if (valor < nodo.valor) {

                nodo.izquierdo =
                        eliminarRecursivo(nodo.izquierdo, valor);

            } else if (valor > nodo.valor) {

                nodo.derecho =
                        eliminarRecursivo(nodo.derecho, valor);

            } else {

                // Caso 1: nodo sin hijos
                if (nodo.izquierdo == null &&
                        nodo.derecho == null) {

                    return null;
                }

                // Caso 2: solo tiene hijo derecho
                if (nodo.izquierdo == null) {
                    return nodo.derecho;
                }

                // Caso 2: solo tiene hijo izquierdo
                if (nodo.derecho == null) {
                    return nodo.izquierdo;
                }

                // Caso 3: tiene dos hijos
                int sucesor = encontrarMinimo(nodo.derecho);

                nodo.valor = sucesor;

                nodo.derecho =
                        eliminarRecursivo(
                                nodo.derecho,
                                sucesor
                        );
            }

            return nodo;
        }

        private int encontrarMinimo(Nodo nodo) {

            while (nodo.izquierdo != null) {
                nodo = nodo.izquierdo;
            }

            return nodo.valor;
        }
    }


    // 2. GRAFO
    // =====================================================

    static class Arista {

        String destino;

        int distancia;
        int tiempo;

        boolean bloqueada;

        Arista(
                String destino,
                int distancia,
                int tiempo
        ) {

            this.destino = destino;
            this.distancia = distancia;
            this.tiempo = tiempo;
            this.bloqueada = false;
        }
    }


    static class Grafo {

        Map<String, List<Arista>> adyacencia =
                new HashMap<>();


        public void agregarCiudad(String ciudad) {

            adyacencia.putIfAbsent(
                    ciudad,
                    new ArrayList<>()
            );
        }


        public void agregarRuta(
                String origen,
                String destino,
                int distancia,
                int tiempo
        ) {

            agregarCiudad(origen);
            agregarCiudad(destino);

            adyacencia.get(origen)
                    .add(
                            new Arista(
                                    destino,
                                    distancia,
                                    tiempo
                            )
                    );
        }


        public void bloquearRuta(
                String origen,
                String destino
        ) {

            for (Arista arista : getOrDefault(origen)) {

                if (arista.destino.equals(destino)) {

                    arista.bloqueada = true;

                    System.out.println(
                            "Ruta bloqueada: "
                                    + origen
                                    + " -> "
                                    + destino
                    );
                }
            }
        }


        private List<Arista> getOrDefault(
                String ciudad
        ) {

            return adyacencia.getOrDefault(
                    ciudad,
                    new ArrayList<>()
            );
        }


        // Algoritmo de Dijkstra
        public void dijkstra(
                String origen,
                String destino,
                boolean usarTiempo
        ) {

            Map<String, Integer> costos =
                    new HashMap<>();

            Map<String, String> anterior =
                    new HashMap<>();


            for (String ciudad :
                    adyacencia.keySet()) {

                costos.put(
                        ciudad,
                        Integer.MAX_VALUE
                );
            }


            costos.put(origen, 0);


            PriorityQueue<String> cola =
                    new PriorityQueue<>(
                            Comparator.comparingInt(
                                    costos::get
                            )
                    );


            cola.add(origen);


            Set<String> visitados =
                    new HashSet<>();


            while (!cola.isEmpty()) {

                String actual = cola.poll();


                if (visitados.contains(actual)) {
                    continue;
                }


                visitados.add(actual);


                for (Arista arista :
                        getOrDefault(actual)) {

                    // Ignorar rutas bloqueadas
                    if (arista.bloqueada) {
                        continue;
                    }


                    int peso;

                    if (usarTiempo) {
                        peso = arista.tiempo;
                    } else {
                        peso = arista.distancia;
                    }


                    int nuevoCosto =
                            costos.get(actual)
                                    + peso;


                    if (nuevoCosto <
                            costos.get(
                                    arista.destino
                            )) {

                        costos.put(
                                arista.destino,
                                nuevoCosto
                        );


                        anterior.put(
                                arista.destino,
                                actual
                        );


                        cola.add(
                                arista.destino
                        );
                    }
                }
            }


            // Reconstrucción del camino
            List<String> ruta =
                    new ArrayList<>();


            String actual = destino;


            while (actual != null) {

                ruta.add(actual);

                actual =
                        anterior.get(actual);
            }


            Collections.reverse(ruta);


            System.out.println(
                    "Ruta: "
                            + String.join(
                                    " -> ",
                                    ruta
                            )
            );


            System.out.println(
                    "Costo total: "
                            + costos.get(destino)
            );
        }
    }


    // 3. CAMINO CRÍTICO
    // =====================================================

    public static void caminoCritico() {

        /*
         * Actividades:
         *
         * A = 4
         * B depende de A = 3
         * C depende de A = 2
         * D depende de B = 5
         * E depende de C y D = 6
         */


        int A = 4;

        int B = A + 3;

        int C = A + 2;

        int D = B + 5;


        // E debe esperar a que terminen C y D
        int inicioE =
                Math.max(C, D);


        int E =
                inicioE + 6;


        System.out.println(
                "Camino crítico: A -> B -> D -> E"
        );


        System.out.println(
                "Duración total: "
                        + E
        );
    }


    // PROGRAMA PRINCIPAL
    // =====================================================

    public static void main(String[] args) {


        // ABB
        // -------------------------------------------------

        ArbolBinarioBusqueda arbol =
                new ArbolBinarioBusqueda();


        int[] codigos = {

                50, 30, 70,
                20, 40, 60,
                80, 10, 25,
                35, 45, 65,
                90
        };


        for (int codigo : codigos) {
            arbol.insertar(codigo);
        }


        System.out.println(
                "\n===== ÁRBOL BINARIO DE BÚSQUEDA ====="
        );


        System.out.print(
                "\nInorden: "
        );

        arbol.inorden(arbol.raiz);


        System.out.print(
                "\nPreorden: "
        );

        arbol.preorden(arbol.raiz);


        System.out.print(
                "\nPostorden: "
        );

        arbol.postorden(arbol.raiz);


        System.out.println(
                "\n\n===== BÚSQUEDA DEL 65 ====="
        );


        boolean encontrado =
                arbol.buscar(65);


        System.out.println(
                "Encontrado: "
                        + encontrado
        );


        // Eliminaciones solicitadas
        arbol.eliminar(10);
        arbol.eliminar(25);
        arbol.eliminar(30);


        System.out.print(
                "\nÁrbol después de las eliminaciones: "
        );

        arbol.inorden(arbol.raiz);


        // GRAFO
        // -------------------------------------------------

        System.out.println(
                "\n\n===== GRAFO DE RUTAS ====="
        );


        Grafo grafo =
                new Grafo();


        grafo.agregarRuta(
                "A", "B", 5, 10
        );

        grafo.agregarRuta(
                "A", "C", 10, 15
        );

        grafo.agregarRuta(
                "B", "C", 3, 5
        );

        grafo.agregarRuta(
                "B", "D", 7, 12
        );

        grafo.agregarRuta(
                "C", "D", 1, 3
        );

        grafo.agregarRuta(
                "C", "E", 8, 10
        );

        grafo.agregarRuta(
                "D", "E", 2, 4
        );

        grafo.agregarRuta(
                "E", "B", 6, 8
        );


        // Restricción de la actividad
        grafo.bloquearRuta(
                "C",
                "E"
        );


        System.out.println(
                "\nRuta mínima por distancia:"
        );


        grafo.dijkstra(
                "A",
                "E",
                false
        );


        System.out.println(
                "\nRuta mínima por tiempo:"
        );


        grafo.dijkstra(
                "A",
                "E",
                true
        );


        // CAMINO CRÍTICO
        // -------------------------------------------------

        System.out.println(
                "\n===== CAMINO CRÍTICO ====="
        );


        caminoCritico();
    }
}
