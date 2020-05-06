package br.com.finch.api.food.service.negocio;

public enum Promocao {

    SEM_PROMOCAO(new PromocaoZerada()),

    LIGHT(new PromocaoLight()),

    MUITA_CARNE(new PromocaoMuitaCarne()),

    MUITO_QUEIJO(new PromocaoMuitoQueijo());

    private IRegraCalculoPromocao regraCalculoDesconto;

    Promocao(IRegraCalculoPromocao regraCalculoDesconto) {
        this.regraCalculoDesconto = regraCalculoDesconto;
    }

    public IRegraCalculoPromocao getRegraCalculoDesconto() {
        return this.regraCalculoDesconto;
    }
}
