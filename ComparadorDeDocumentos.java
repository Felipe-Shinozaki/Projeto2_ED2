import java.util.*;

public class ComparadorDeDocumentos {
    
    public double calcularSimilaridade(Documento doc1, Documento doc2) {
        Set<String> todasPalavras = new HashSet<>();
        todasPalavras.addAll(doc1.getPalavras());
        todasPalavras.addAll(doc2.getPalavras());
        
        if (todasPalavras.isEmpty()) {
            return 0.0;
        }
        
        double produtoEscalar = 0.0;
        double magnitudeDoc1 = 0.0;
        double magnitudeDoc2 = 0.0;
        
        for (String palavra : todasPalavras) {
            int freq1 = doc1.getFrequencia(palavra);
            int freq2 = doc2.getFrequencia(palavra);
            produtoEscalar += freq1 * freq2;
            magnitudeDoc1 += freq1 * freq1;
            magnitudeDoc2 += freq2 * freq2;
        }
        
        magnitudeDoc1 = Math.sqrt(magnitudeDoc1);
        magnitudeDoc2 = Math.sqrt(magnitudeDoc2);
        
        if (magnitudeDoc1 == 0.0 || magnitudeDoc2 == 0.0) {
            return 0.0;
        }
        
        return produtoEscalar / (magnitudeDoc1 * magnitudeDoc2);
    }
}