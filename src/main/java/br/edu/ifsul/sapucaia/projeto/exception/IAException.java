package br.edu.ifsul.sapucaia.projeto.exception;

public class IAException extends RuntimeException{
    public IAException(String message, Throwable cause){
        super(message, cause);
    }
}
