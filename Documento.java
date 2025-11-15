import java.io.*;
import java.nio.file.*;
import java.util.*;

public class Documento {
    private String nome;
    private String caminho;
    private HashTable vocabulario;
    private static Set<String> stopWords = carregarStopWords();
    
    public Documento(String nome, String caminho) throws IOException {
        this.nome = nome;
        this.caminho = caminho;
        this.vocabulario = new HashTable(100);
        processarDocumento();
    }
    
    private void processarDocumento() throws IOException {
        String conteudo = new String(Files.readAllBytes(Paths.get(caminho)));
        conteudo = conteudo.toLowerCase();
        conteudo = conteudo.replaceAll("[^a-záàâãéèêíïóôõöúçñ\\s]", " ");
        String[] tokens = conteudo.split("\\s+");
        
        for (String token : tokens) {
            token = token.trim();
            if (!token.isEmpty() && !stopWords.contains(token)) {
                Integer freq = vocabulario.get(token);
                if (freq == null) {
                    vocabulario.put(token, 1);
                } else {
                    vocabulario.put(token, freq + 1);
                }
            }
        }
    }
    
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
    
    public Set<String> getPalavras() {
        return vocabulario.getChaves();
    }
    
    public int getFrequencia(String palavra) {
        Integer freq = vocabulario.get(palavra);
        return freq != null ? freq : 0;
    }
}