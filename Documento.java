import java.io.*;
import java.nio.file.*;
import java.util.*;

// Classe responsável por tratar o conteúdo do arquivo e construir o vocabulário usando a tabela hash
public class Documento {
    private String nome; // Nome do arquivo
    private String caminho; // Caminho em que o arquivo se encontra
    private HashTable vocabulario; // Tabea hash com a frequência
    private static Set<String> stopWords = carregarStopWords(); // Lista de stop words
    
    public Documento(String nome, String caminho) throws IOException {
        this.nome = nome;
        this.caminho = caminho;
        this.vocabulario = new HashTable(100); // Tabela hash inicial
        processarDocumento(); // Processa o conteúdo do arquivo
    }
    
    // Lê o arquivo, normaliza o conteúdo e insere as palavras na tabela hash
    private void processarDocumento() throws IOException {
        String conteudo = new String(Files.readAllBytes(Paths.get(caminho)));

        // Normalização
        conteudo = conteudo.toLowerCase(); // Converte para minúsculo
        conteudo = conteudo.replaceAll("[^a-záàâãéèêíïóôõöúçñ\\s]", " "); // Apaga símbolos
        String[] tokens = conteudo.split("\\s+"); // Tokenização
        
        // Percorre o texto e remove as stop words
        for (String token : tokens) {
            token = token.trim();
            if (!token.isEmpty() && !stopWords.contains(token)) {
                Integer freq = vocabulario.get(token); // Frequência atual
                // Atualiza a frequência
                if (freq == null) {
                    vocabulario.put(token, 1);
                } else {
                    vocabulario.put(token, freq + 1);
                }
            }
        }
    }
    
    // Lista de stop words em português
    private static Set<String> carregarStopWords() {
        Set<String> stops = new HashSet<>();
        String[] palavras = {
            "a", "o", "e", "é", "de", "da", "do", "em", "um", "uma",
            "os", "as", "dos", "das", "para", "com", "por", "que", "no",
            "na", "nos", "nas", "ao", "aos", "à", "às", "se", "ou", "mas",
            "mais", "muito", "como", "ser", "foi", "são", "pela", "pelo",
            "seu", "sua", "seus", "suas", "este", "esse", "aquele", "isto",
            "isso", "aquilo", "ele", "ela", "eles", "elas", "eu", "tu",
            "você", "nós", "vós", "vocês", "meu", "minha", "teu", "tua",
            "dele", "dela", "deles", "delas", "mesmo", "mesma", "também",
            "até", "após", "antes", "durante", "desde", "entre", "sobre",
            "sob", "sem", "contra", "já", "ainda", "onde", "quando", "enquanto",
            "porque", "porquê", "pois", "portanto", "assim", "então", "nem",
            "só", "apenas", "somente", "menos", "lá", "cá", "aqui", "aí",
            "ali", "outro", "outra", "outros", "outras", "qual", "quais"
        };
        for (String palavra : palavras) {
            stops.add(palavra);
        }
        return stops;
    }
    
    public String getNome() {
        return nome;
    }
    
    public HashTable getVocabulario() {
        return vocabulario;
    }
    
    // Devolve o conjunto de palavras únicas
    public Set<String> getPalavras() {
        return vocabulario.getChaves();
    }
    
    // Devolve a frequência de uma palavra
    public int getFrequencia(String palavra) {
        Integer freq = vocabulario.get(palavra);
        return freq != null ? freq : 0;
    }
}