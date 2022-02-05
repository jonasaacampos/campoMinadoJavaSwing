package visao;

import modelo.Tabuleiro;

import javax.swing.*;
import java.awt.*;

public class PainelTabuleiro extends JPanel {

    public PainelTabuleiro(Tabuleiro tabuleiro) {
        setLayout(new GridLayout(tabuleiro.getLinhas(), tabuleiro.getColunas()));

        tabuleiro.paraCadaCampo(campo -> add(new BotaoCampo(campo)));
        tabuleiro.registrarObservador(e ->{

            SwingUtilities.invokeLater(() ->{
                if(e.isGanhou()){
                    JOptionPane.showMessageDialog(this, "\uD83C\uDF89\uD83C\uDF89Você Ganhou!\uD83C\uDF89\uD83C\uDF89");
                }else{
                    JOptionPane.showMessageDialog(this, "Não foi desta vez...️");
                }
                tabuleiro.reiniciar();

            });

        });

    }
}
