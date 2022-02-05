package visao;

import modelo.Campo;
import modelo.CampoEvento;
import modelo.CampoObservador;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class BotaoCampo extends JButton implements CampoObservador, MouseListener {


    private final Color BG_PADRAO = new Color(184,184,184);
    private final Color BG_MARCADO = new Color(150,250,247);
    private final Color BG_EXPLOSAO = new Color(189,66,68);
    private final Color TEXTO_VERDE = new Color(0,100,0);



    private Campo campo;

    public BotaoCampo(Campo campo) {
        this.campo = campo;
        setBorder(BorderFactory.createBevelBorder(0));
        setBackground(BG_PADRAO);

        addMouseListener(this);

        campo.registrarObservador(this);
    }

    @Override
    public void eventoOcorreu(Campo campo, CampoEvento evento) {
        switch (evento){
            case ABRIR:
                aplicarEstiloAbrir();
                break;
            case MARCAR:
                aplicarEstiloMarcar();
                break;
            case EXPLODIR:
                aplicarEstiloExplodir();
                break;
            default:
                aplicarEstiloPadrao();
                setBorder(BorderFactory.createBevelBorder(0));
        }
        //garante que componente seja renderizado da forma correta.
        SwingUtilities.invokeLater(() ->{
            repaint();
            validate();
        });

    }

    private void aplicarEstiloPadrao() {
        setBackground(BG_PADRAO);
        setText("");
    }

    private void aplicarEstiloExplodir() {
        setBackground(BG_EXPLOSAO);
        setOpaque(true);
        setForeground(Color.BLACK);
        setText("\uD83D\uDCA3");
    }

    private void aplicarEstiloMarcar() {
        setBackground(BG_MARCADO);
        setText("❗");
    }

    private void aplicarEstiloAbrir() {

        setBorder(BorderFactory.createLineBorder(Color.gray));

        if(campo.isMinado()){
            //setBackground(BG_EXPLOSAO);
            setForeground(Color.DARK_GRAY);
            setText("\uD83D\uDCA3");
            return;
        }

        setBackground(BG_PADRAO);

        switch(campo.minasNaVizinhaca()){
            case 1:
                setForeground(TEXTO_VERDE);
                break;
            case 2:
                setForeground(Color.BLUE);
                break;
            case 3:
                setForeground(Color.YELLOW);
                break;
            case 4:
            case 5:
            case 6:
                setForeground(Color.RED);
                break;
            default:
                setForeground(Color.PINK);
        }
        String valor;
        if (!campo.vizinhancaSegura()) valor = campo.minasNaVizinhaca() + "";
        else valor = "";
        setText(valor);

    }

    //Interface Eventos mouse

    @Override
    public void mousePressed(MouseEvent e) {

        if(e.getButton() == 1) { //botão esquerdo
            campo.abrir();
        } else {
            campo.alternarMarcacao(); //botão direito e scroll...
        }
    }

    public void mouseClicked(MouseEvent e) {
    }
    public void mouseReleased(MouseEvent e) {
    }
    public void mouseEntered(MouseEvent e) {
    }
    public void mouseExited(MouseEvent e) {
    }
}
