package br.com.finch.api.food.service.negocio;

public enum Promocao {

    SEM_PROMOCAO(new PromocaoZerada()),

    LIGHT(new PromocaoLight()),

    MUITA_CARNE(new PromocaoMuitaCarne()),

    MUITO_QUEIJO(new PromocaoMuitoQueijo());

    private final IRegraCalculoPromocao regraCalculoPromocaoDesconto;

    Promocao(IRegraCalculoPromocao regraCalculoPromocaoDesconto) {
        this.regraCalculoPromocaoDesconto = regraCalculoPromocaoDesconto;
    }

    public IRegraCalculoPromocao getRegraCalculoPromocaoDesconto() {
        return this.regraCalculoPromocaoDesconto;
    }
}
