import java.util.Random;

public class TesteDesempenho {
    private static final int [] TAMANHOS_TESTE = {1000, 5000, 10000};

    public static void main(String[] args){
        System.out.println("=== TESTE DE DESEMPENHO - HashTable e AVLTree ===");

        testarDesempenhoHashTable();
        System.out.println();
        testarDesempenhoAVL();
    }

    private static void testarDesempenhoHashTable(){
        System.out.println(">> Teste de desempenho da HashTable");

        for(int n : TAMANHOS_TESTE){
            int capacidadeInicial = n/2;
            HashTable tabela = new HashTable(capacidadeInicial);

            long inicio = System.nanoTime();

            for (int i = 0; i < n; i++){
                String chave = "palavra" + i;
                tabela.put(chave, i);
            }

            long fim = System.nanoTime();
            double tempoMs = (fim - inicio) / 1000000.0;

            System.out.println("\n--- N = " + n + " inserções ---");
            System.out.println("Tempo de inserção: " + String.format("%.3f", tempoMs) + " ms");
            System.out.println("Tamanho final da tabela: " + tabela.getTamanho());
            System.out.println("Número de elementos: " + tabela.getElementos());
            System.out.println("Fator de carga: " + String.format("%.3f", tabela.getFatorCarga()));
            System.out.println("Colisões registradas: " + tabela.getColisoes());

        }
    }

    private static void testarDesempenhoAVL(){
        System.out.println(">> Teste de desempenho da AVL");

        Random random = new Random(42);

        for(int n : TAMANHOS_TESTE){
            AVLTree arvore = new AVLTree();

            long inicio = System.nanoTime();

            for (int i = 0; i < n; i++){
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