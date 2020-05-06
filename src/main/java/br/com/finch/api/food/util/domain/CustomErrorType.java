package br.com.finch.api.food.util.domain;

public class CustomErrorType {

	private String erroMensagem;

	public CustomErrorType(String erroMensagem) {
		this.erroMensagem = erroMensagem;
	}

	public String getErroMensagem() {
		return erroMensagem;
	}
}
