package com.leo.teste.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;



public class UnexpectedSituationException extends RuntimeException {
 public UnexpectedSituationException(String msg){
  super(msg);


 }

 }