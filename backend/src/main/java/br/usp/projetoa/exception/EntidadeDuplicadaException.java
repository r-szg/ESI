package br.usp.projetoa.exception;


public class EntidadeDuplicadaException extends RuntimeException {
    public EntidadeDuplicadaException(String entidade, String id) {
        super(entidade + " com ID " + id + " já existe.");
    }
}
