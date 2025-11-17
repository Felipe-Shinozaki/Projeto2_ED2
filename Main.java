import java.io.*;
import java.nio.file.*;
import java.util.*;

/**
 * Verificador de Similaridade de Textos
 * Estrutura de Dados II - Universidade Presbiteriana Mackenzie
 * 
 * Autores: Felipe Hideki Rodrigues Shinozaki - 10438584
 *          Pedro de Souza Zequi - 10419805
 */
public class Main {
    private static PrintWriter logWriter;
    
    public static void main(String[] args) {
        if (args.length < 3) {
            System.out.println("Uso: java Main <diretorio_documentos> <limiar> <modo> [argumentos_opcionais]");
            System.out.println("Modos: lista | topK <K> | busca <doc1> <doc2>");
            return;
        }
        
        try {
            logWriter = new PrintWriter(new FileWriter("resultado.txt"));
            
            String diretorio = args[0];
            double limiar = Double.parseDouble(args[1]);
            String modo = args[2];
            
            printLn("=== VERIFICADOR DE SIMILARIDADE DE TEXTOS ===");
            
            // Processar documentos
            List<Documento> documentos = carregarDocumentos(diretorio);
            printLn("Total de documentos processados: " + documentos.size());
            
            // Calcular similaridades
            AVLTree arvore = new AVLTree();
            ComparadorDeDocumentos comparador = new ComparadorDeDocumentos();
            List<Resultado> todosResultados = new ArrayList<>();
            
            if (modo.equals("busca") && args.length >= 5) {
                // Modo busca: comparar dois arquivos específicos
                String arquivo1 = args[3];
                String arquivo2 = args[4];
                
                Documento doc1 = encontrarDocumento(documentos, arquivo1);
                Documento doc2 = encontrarDocumento(documentos, arquivo2);
                
                if (doc1 != null && doc2 != null) {
                    printLn("\nComparando: " + arquivo1 + " <-> " + arquivo2);
                    double sim = comparador.calcularSimilaridade(doc1, doc2);
                    printLn("Similaridade calculada: " + String.format("%.2f", sim));
                    printLn("Métrica utilizada: Cosseno");
                } else {
                    printLn("Erro: Um ou ambos os arquivos não foram encontrados.");
                }
            } else {
                // Calcular todas as similaridades
                int totalPares = 0;
                for (int i = 0; i < documentos.size(); i++) {
                    for (int j = i + 1; j < documentos.size(); j++) {
                        Documento doc1 = documentos.get(i);
                        Documento doc2 = documentos.get(j);
                        
                        double similaridade = comparador.calcularSimilaridade(doc1, doc2);
                        Resultado resultado = new Resultado(doc1.getNome(), doc2.getNome(), similaridade);
                        todosResultados.add(resultado);
                        arvore.inserir(similaridade, resultado);
                        totalPares++;
                    }
                }
                
                printLn("Total de pares comparados: " + totalPares);
                printLn("Função hash utilizada: hashMultiplicativo");
                printLn("Métrica de similaridade: Cosseno");
                printLn("Total de rotações na AVL: " + arvore.getTotalRotacoes());
                
                if (modo.equals("lista")) {
                    // Modo lista: exibir todos os pares acima do limiar
                    List<Resultado> resultadosFiltrados = new ArrayList<>();
                    for (Resultado r : todosResultados) {
                        if (r.getSimilaridade() >= limiar) {
                            resultadosFiltrados.add(r);
                        }
                    }
                    
                    resultadosFiltrados.sort((a, b) -> Double.compare(b.getSimilaridade(), a.getSimilaridade()));
                    
                    printLn("\nPares com similaridade >= " + limiar + ":");
                    printLn("---------------------------------");
                    for (Resultado r : resultadosFiltrados) {
                        printLn(r.getDoc1() + " <-> " + r.getDoc2() + " = " + String.format("%.2f", r.getSimilaridade()));
                    }
                    
                } else if (modo.equals("topK") && args.length >= 4) {
                    // Modo topK: exibir os K pares mais semelhantes
                    int k = Integer.parseInt(args[3]);
                    List<Resultado> topK = arvore.getTopK(k);
                    
                    printLn("\nTop " + k + " pares mais semelhantes:");
                    printLn("---------------------------------");
                    for (Resultado r : topK) {
                        printLn(r.getDoc1() + " <-> " + r.getDoc2() + " = " + String.format("%.2f", r.getSimilaridade()));
                    }
                }
                
                // Exibir par com menor similaridade
                if (!todosResultados.isEmpty()) {
                    Resultado menor = todosResultados.stream()
                        .min((a, b) -> Double.compare(a.getSimilaridade(), b.getSimilaridade()))
                        .get();
                    
                    printLn("\nPares com menor similaridade:");
                    printLn("---------------------------------");
                    printLn(menor.getDoc1() + " <-> " + menor.getDoc2() + " = " + String.format("%.2f", menor.getSimilaridade()));
                }
            }
            
            logWriter.close();
            
        } catch (Exception e) {
            System.err.println("Erro: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static List<Documento> carregarDocumentos(String diretorio) throws IOException {
        List<Documento> documentos = new ArrayList<>();
        File dir = new File(diretorio);
        
        if (!dir.exists() || !dir.isDirectory()) {
            throw new IOException("Diretório não encontrado: " + diretorio);
        }
        
        File[] arquivos = dir.listFiles((d, name) -> name.endsWith(".txt"));
        if (arquivos != null) {
            for (File arquivo : arquivos) {
                Documento doc = new Documento(arquivo.getName(), arquivo.getAbsolutePath());
                documentos.add(doc);
            }
        }
        
        return documentos;
    }
    
    private static Documento encontrarDocumento(List<Documento> documentos, String nome) {
        for (Documento doc : documentos) {
            if (doc.getNome().equals(nome)) {
                return doc;
            }
        }
        return null;
    }
    
    private static void printLn(String texto) {
        System.out.println(texto);
        if (logWriter != null) {
            logWriter.println(texto);
        }
    }
}