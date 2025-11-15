import java.util.*;

public class AVLTree {
    private class No {
        double chave;
        List<Resultado> resultados;
        No esquerda, direita;
        int altura;
        
        No(double chave, Resultado resultado) {
            this.chave = chave;
            this.resultados = new ArrayList<>();
            this.resultados.add(resultado);
            this.altura = 1;
        }
    }
    
    private No raiz;
    private int rotacoesSimples;
    private int rotacoesDuplas;
    
    public AVLTree() {
        this.raiz = null;
        this.rotacoesSimples = 0;
        this.rotacoesDuplas = 0;
    }
    
    public void inserir(double similaridade, Resultado resultado) {
        raiz = inserirRec(raiz, similaridade, resultado);
    }
    
    private No inserirRec(No no, double chave, Resultado resultado) {
        if (no == null) {
            return new No(chave, resultado);
        }
        
        if (Math.abs(chave - no.chave) < 0.0001) {
            no.resultados.add(resultado);
            return no;
        } else if (chave < no.chave) {
            no.esquerda = inserirRec(no.esquerda, chave, resultado);
        } else {
            no.direita = inserirRec(no.direita, chave, resultado);
        }
        
        no.altura = 1 + Math.max(altura(no.esquerda), altura(no.direita));
        int balance = getBalance(no);
        
        if (balance > 1 && chave < no.esquerda.chave) {
            rotacoesSimples++;
            return rotacaoDireita(no);
        }
        
        if (balance < -1 && chave > no.direita.chave) {
            rotacoesSimples++;
            return rotacaoEsquerda(no);
        }
        
        if (balance > 1 && chave > no.esquerda.chave) {
            rotacoesDuplas++;
            no.esquerda = rotacaoEsquerda(no.esquerda);
            return rotacaoDireita(no);
        }
        
        if (balance < -1 && chave < no.direita.chave) {
            rotacoesDuplas++;
            no.direita = rotacaoDireita(no.direita);
            return rotacaoEsquerda(no);
        }
        
        return no;
    }
    
    private int altura(No no) {
        return no == null ? 0 : no.altura;
    }
    
    private int getBalance(No no) {
        return no == null ? 0 : altura(no.esquerda) - altura(no.direita);
    }
    
    private No rotacaoDireita(No y) {
        No x = y.esquerda;
        No T2 = x.direita;
        x.direita = y;
        y.esquerda = T2;
        y.altura = Math.max(altura(y.esquerda), altura(y.direita)) + 1;
        x.altura = Math.max(altura(x.esquerda), altura(x.direita)) + 1;
        return x;
    }
    
    private No rotacaoEsquerda(No x) {
        No y = x.direita;
        No T2 = y.esquerda;
        y.esquerda = x;
        x.direita = T2;
        x.altura = Math.max(altura(x.esquerda), altura(x.direita)) + 1;
        y.altura = Math.max(altura(y.esquerda), altura(y.direita)) + 1;
        return y;
    }
    
    public List<Resultado> getTopK(int k) {
        List<Resultado> todos = new ArrayList<>();
        percorrerEmOrdemDecrescente(raiz, todos);
        List<Resultado> topK = new ArrayList<>();
        for (int i = 0; i < Math.min(k, todos.size()); i++) {
            topK.add(todos.get(i));
        }
        return topK;
    }
    
    private void percorrerEmOrdemDecrescente(No no, List<Resultado> lista) {
        if (no != null) {
            percorrerEmOrdemDecrescente(no.direita, lista);
            lista.addAll(no.resultados);
            percorrerEmOrdemDecrescente(no.esquerda, lista);
        }
    }
    
    public List<Resultado> getTodosResultados() {
        List<Resultado> todos = new ArrayList<>();
        percorrerEmOrdem(raiz, todos);
        return todos;
    }
    
    private void percorrerEmOrdem(No no, List<Resultado> lista) {
        if (no != null) {
            percorrerEmOrdem(no.esquerda, lista);
            lista.addAll(no.resultados);
            percorrerEmOrdem(no.direita, lista);
        }
    }
    
    public int getRotacoesSimples() {
        return rotacoesSimples;
    }
    
    public int getRotacoesDuplas() {
        return rotacoesDuplas;
    }
    
    public int getTotalRotacoes() {
        return rotacoesSimples + rotacoesDuplas;
    }
}