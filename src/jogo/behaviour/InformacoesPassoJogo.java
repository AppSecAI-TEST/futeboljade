package jogo.behaviour;

import jade.lang.acl.ACLMessage;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@RequiredArgsConstructor
@ToString
public class InformacoesPassoJogo {
    private final ACLMessage message;
    private final String mensagemDaInterface;
}
