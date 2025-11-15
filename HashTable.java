import java.util.*;

public class HashTable {
    private static final int TIPO_HASH_DIVISAO = 1;
    private static final int TIPO_HASH_MULTIPLICATIVO = 2;
    
    private static class Entry {
        String chave;
        Integer valor;
        Entry proximo;
        
        Entry(String chave, Integer valor) {
            this.chave = chave;
            this.valor = valor;
            this.proximo = null;
        }
    }
    
    private Entry[] tabela;
    private int tamanho;
    private int elementos;
    private int tipoHash;
    private int colisoes;
    private static final double A = 0.6180339887;
    
    public HashTable(int capacidade) {
        this.tamanho = proximoPrimo(capacidade);
        this.tabela = new Entry[tamanho];
        this.elementos = 0;
        this.tipoHash = TIPO_HASH_MULTIPLICATIVO;
        this.colisoes = 0;
    }
    
    private int hashDivisao(String chave) {
        int hash = 0;
        for (int i = 0; i < chave.length(); i++) {
            hash = (hash * 31 + chave.charAt(i)) % tamanho;
        }
        return Math.abs(hash);
    }
    
    private int hashMultiplicativo(String chave) {
        long hash = 0;
        for (int i = 0; i < chave.length(); i++) {
            hash = hash * 31 + chave.charAt(i);
        }
        double produto = Math.abs(hash) * A;
        double fracao = produto - Math.floor(produto);
        return (int) Math.floor(tamanho * fracao);
    }
    
    private int hash(String chave) {
        if (tipoHash == TIPO_HASH_DIVISAO) {
            return hashDivisao(chave);
        } else {
            return hashMultiplicativo(chave);
        }
    }
    
    public void put(String chave, Integer valor) {
        int indice = hash(chave);
        Entry entrada = tabela[indice];
        Entry atual = entrada;
        
        while (atual != null) {
            if (atual.chave.equals(chave)) {
                atual.valor = valor;
                return;
            }
            atual = atual.proximo;
        }
        
        Entry novaEntrada = new Entry(chave, valor);
        novaEntrada.proximo = tabela[indice];
        tabela[indice] = novaEntrada;
        elementos++;
        
        if (entrada != null) {
            colisoes++;
        }
        
        if ((double) elementos / tamanho > 0.75) {
            rehash();
        }
    }
    
    public Integer get(String chave) {
        int indice = hash(chave);
        Entry entrada = tabela[indice];
        
        while (entrada != null) {
            if (entrada.chave.equals(chave)) {
                return entrada.valor;
            }
            entrada = entrada.proximo;
        }
        return null;
    }
    
    public Set<String> getChaves() {
        Set<String> chaves = new HashSet<>();
        for (int i = 0; i < tamanho; i++) {
            Entry entrada = tabela[i];
            while (entrada != null) {
                chaves.add(entrada.chave);
                entrada = entrada.proximo;
            }
        }
        return chaves;
    }
    
    private void rehash() {
        Entry[] tabelaAntiga = tabela;
        tamanho = proximoPrimo(tamanho * 2);
        tabela = new Entry[tamanho];
        elementos = 0;
        colisoes = 0;
        
        for (Entry entrada : tabelaAntiga) {
            while (entrada != null) {
                put(entrada.chave, entrada.valor);
                entrada = entrada.proximo;
            }
        }
    }
    
    private int proximoPrimo(int n) {
        while (!ehPrimo(n)) {
            n++;
        }
        return n;
    }
    
    private boolean ehPrimo(int n) {
        if (n <= 1) return false;
        if (n <= 3) return true;
        if (n % 2 == 0 || n % 3 == 0) return false;
        for (int i = 5; i * i <= n; i += 6) {
            if (n % i == 0 || n % (i + 2) == 0) {
                return false;
            }
        }
        return true;
    }
    
    public int getTamanho() {
        return tamanho;
    }
    
    public int getElementos() {
        return elementos;
    }
    
    public int getColisoes() {
        return colisoes;
    }
    
    public double getFatorCarga() {
        return (double) elementos / tamanho;
    }
}