package modelo;

import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

public class Campo {
    private final int linha;
    private final int coluna;

    private boolean aberto;
    private boolean minado;
    private boolean marcado;

    private List<Campo> vizinhos = new ArrayList<>();
    private List<CampoObservador> observadores = new ArrayList<>();


    Campo(int linha, int coluna) {
        this.linha = linha;
        this.coluna = coluna;
    }

    public void registrarObservador(CampoObservador observador) {
        observadores.add(observador);
    }

    private void notificarObservadores(CampoEvento evento){
        observadores.stream()
                .forEach(o -> o.eventoOcorreu(this, evento));
    }



    boolean adicionarVizinho(Campo vizinho) {
        boolean linhaDiferente = linha != vizinho.linha;
        boolean colunaDiferente = coluna != vizinho.coluna;
        boolean diagonal = linhaDiferente && colunaDiferente;

        int deltaLinha = Math.abs(linha - vizinho.linha);
        int deltaColuna = Math.abs(coluna - vizinho.coluna);
        int deltaTotal = deltaLinha + deltaColuna;

        if(deltaTotal == 1 && !diagonal){
            vizinhos.add(vizinho);
            return true;
        } else if(deltaTotal == 2 && diagonal){
            vizinhos.add(vizinho);
            return true;
        } else {
            return false;
        }
    }

    public void alternarMarcacao(){
        if(!aberto){
            marcado = !marcado;

            if(marcado){
                notificarObservadores(CampoEvento.MARCAR);
            } else{
                notificarObservadores(CampoEvento.DESMARCAR);
            }
        }
    }

    public boolean abrir(){
        if(!aberto && !marcado){
            aberto = true;

            if(minado){
                notificarObservadores(CampoEvento.EXPLODIR);
                return true;
            }

            setAberto(true);

            if(vizinhancaSegura()){
                vizinhos.forEach(v -> v.abrir());
            }
            return true;

        } else {
            return false;
        }
    }

    public boolean vizinhancaSegura() {
        return vizinhos.stream().noneMatch(v -> v.minado);
    }

    void minar() {
        minado = true;
    }


    public boolean isMarcado() {
        return marcado;
    }

    void setAberto(boolean aberto) {
        this.aberto = aberto;
        if(aberto){
            notificarObservadores(CampoEvento.ABRIR);
        }
    }

    public boolean isAberto(){
        return aberto;
    }

    public boolean isMinado(){
        return minado;
    }

    public int getLinha() {
        return linha;
    }

    public int getColuna() {
        return coluna;
    }

    boolean objetivoAlcancado(){
        boolean desvendado = !minado && aberto;
        boolean protegido = minado && marcado;
        return desvendado || protegido;
    }

    public int minasNaVizinhaca(){
        return (int)vizinhos.stream().filter(v -> v.minado).count();
    }

    void reiniciar(){
        aberto = false;
        minado = false;
        marcado = false;
        notificarObservadores(CampoEvento.REINICIAR);
    }

    // todo remover?
    public String toString(){
        if(marcado){
            return "❌";
        } else if(aberto && minado) {
            return "\uD83D\uDCA3";
        } else if(aberto && minasNaVizinhaca() > 0) {
            return Long.toString(minasNaVizinhaca());
        } else if(aberto) {
            return " ";
        } else {
            return "☕";
        }
    }

}
