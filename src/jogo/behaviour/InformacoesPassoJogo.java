package jogo.behaviour;

import jade.lang.acl.ACLMessage;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@Getter
@RequiredArgsConstructor
@ToString
public class InformacoesPassoJogo implements Serializable{
    private final ACLMessage message;
    private final String mensagemDaInterface;
}
