import java.util.Random;

public class TesteDesempenho {

    private static final int[] TAMANHOS_TESTE = {1000, 5000, 10000};

    public static void main(String[] args) {
        System.out.println("=== TESTE DE DESEMPENHO - HashTable e AVLTree ===");

        testarDesempenhoHashTable();  
        System.out.println();
        testarDesempenhoAVL();        
    }

    private static void testarDesempenhoHashTable() {
        System.out.println(">>> Teste de desempenho da HashTable (Divisão x Multiplicativo)");

        for (int n : TAMANHOS_TESTE) {
            int capacidadeInicial = n / 2;

            HashTable tabelaDivisao = new HashTable(capacidadeInicial);
            tabelaDivisao.usarHashDivisao();

            long inicioDiv = System.nanoTime();
            for (int i = 0; i < n; i++) {
                String chave = "palavra" + i;
                tabelaDivisao.put(chave, i);
            }
            long fimDiv = System.nanoTime();
            double tempoDivMs = (fimDiv - inicioDiv) / 1000000.0;

            HashTable tabelaMult = new HashTable(capacidadeInicial);
            tabelaMult.usarHashMultiplicativo();

            long inicioMult = System.nanoTime();
            for (int i = 0; i < n; i++) {
                String chave = "palavra" + i;
                tabelaMult.put(chave, i);
            }
            long fimMult = System.nanoTime();
            double tempoMultMs = (fimMult - inicioMult) / 1000000.0;

            System.out.println("\n=== N = " + n + " inserções ===");

            System.out.println("-> Método da Divisão:");
            System.out.println("Tempo de inserção: " + String.format("%.3f", tempoDivMs) + " ms");
            System.out.println("Tamanho final da tabela: " + tabelaDivisao.getTamanho());
            System.out.println("Número de elementos: " + tabelaDivisao.getElementos());
            System.out.println("Fator de carga: " + String.format("%.3f", tabelaDivisao.getFatorCarga()));
            System.out.println("Colisões registradas: " + tabelaDivisao.getColisoes());

            System.out.println("\n-> Método Multiplicativo:");
            System.out.println("Tempo de inserção: " + String.format("%.3f", tempoMultMs) + " ms");
            System.out.println("Tamanho final da tabela: " + tabelaMult.getTamanho());
            System.out.println("Número de elementos: " + tabelaMult.getElementos());
            System.out.println("Fator de carga: " + String.format("%.3f", tabelaMult.getFatorCarga()));
            System.out.println("Colisões registradas: " + tabelaMult.getColisoes());
        }
    }

    private static void testarDesempenhoAVL() {
        System.out.println(">>> Teste de desempenho da AVLTree");

        Random random = new Random(42);

        for (int n : TAMANHOS_TESTE) {
            AVLTree arvore = new AVLTree();

            long inicio = System.nanoTime();

            for (int i = 0; i < n; i++) {
                double similaridade = random.nextDouble();
                Resultado resultado = new Resultado("A" + i, "B" + i, similaridade);
                arvore.inserir(similaridade, resultado);
            }

            long fim = System.nanoTime();
            double tempoMs = (fim - inicio) / 1000000.0;

            System.out.println("\n--- N = " + n + " inserções ---");
            System.out.println("Tempo de inserção: " + String.format("%.3f", tempoMs) + " ms");
            System.out.println("Rotações simples: " + arvore.getRotacoesSimples());
            System.out.println("Rotações duplas: " + arvore.getRotacoesDuplas());
            System.out.println("Total de rotações: " + arvore.getTotalRotacoes());
        }
    }
}
