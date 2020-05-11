create database food;

\c food

create schema if not exists food_service;
set schema 'food_service';

--////////////////////////////////////s/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
create sequence food_service.fd07_tipo_ingrediente_id_seq;

create table if not exists food_service.fd07_tipo_ingrediente
(
    id          bigint       not null DEFAULT nextval('food_service.fd07_tipo_ingrediente_id_seq'),
    descricao   varchar(200) null,
    dt_cadastro timestamp             default now(),
    ativo       boolean               default true null,
    primary key (id)
);

create unique index uq_fd07_tipo_ingrediente on food_service.fd07_tipo_ingrediente (id);
alter table food_service.fd07_tipo_ingrediente
    owner to postgres;

--/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
create sequence food_service.fd01_ingrediente_id_seq;

create table if not exists food_service.fd01_ingrediente
(
    id          bigint                       not null DEFAULT nextval('food_service.fd01_ingrediente_id_seq'),
    descricao   varchar(200)                 null,
    custo       decimal(19, 6) default 0.0   not null,
    id_tipo     bigint                       null,
    dt_cadastro timestamp      default now() not null,
    ativo       boolean        default true  null,
    primary key (id)
);

create unique index uq_fd01_ingrediente on food_service.fd01_ingrediente (id);

alter table food_service.fd01_ingrediente
    add constraint fd01_ingrediente_id_tipo_fkey foreign key (id_tipo) references food_service.fd07_tipo_ingrediente (id);

alter table food_service.fd01_ingrediente
    owner to postgres;

--/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
create sequence food_service.fd02_lanche_id_seq;

create table if not exists food_service.fd02_lanche
(
    id          bigint                       not null DEFAULT nextval('food_service.fd02_lanche_id_seq'),
    descricao   varchar(200)                 null,
    valor_total decimal(19, 6) default 0     not null,
    dt_cadastro timestamp      default now() not null,
    ativo       boolean        default true  null,
    primary key (id)
);

create unique index uq_fd02_lanche on food_service.fd02_lanche (id);
alter table food_service.fd02_lanche
    owner to postgres;

--/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

create table food_service.fd03_ingrediente_lanche
(
    id_lanche      bigint    not null,
    id_ingrediente bigint    not null,
    qtde           decimal(19, 6) default 1.0 null,
    dt_cadastro    timestamp null DEFAULT now(),
    ativo          bool      null default true,
    PRIMARY KEY (id_lanche, id_ingrediente)
);

alter table food_service.fd03_ingrediente_lanche
    add constraint fd03_ingrediente_lanche_id_lanche_fkey foreign key (id_lanche) references food_service.fd02_lanche (id);
alter table food_service.fd03_ingrediente_lanche
    add constraint fd03_ingrediente_lanche_id_ingrediente_fkey foreign key (id_ingrediente) references food_service.fd01_ingrediente (id);

--/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
create sequence food_service.fd04_lanche_pedido_id_seq;

create table if not exists food_service.fd04_lanche_pedido
(
    id                   bigint                       not null DEFAULT nextval('food_service.fd04_lanche_pedido_id_seq'),
    nome_cliente         varchar(200)                 null,
    telefone             varchar(14)                  null,
    valor_itens          decimal(19, 6) default 0.0   not null,
    valor_desconto_total decimal(19, 6) default 0.0   not null,
    valor_total          decimal(19, 6) default 0.0   not null,
    dt_cadastro          timestamp      default now() not null,
    "ativo"              bool                         null default true,
    primary key (id)
);

create unique index uq_fd01_pedido on food_service.fd04_lanche_pedido (id);
alter table food_service.fd04_lanche_pedido
    owner to postgres;

--/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
create sequence food_service.fd05_item_lanche_pedido_id_seq;

create table if not exists food_service.fd05_item_lanche_pedido
(
    id               bigint                     not null DEFAULT nextval('food_service.fd05_item_lanche_pedido_id_seq'),
    id_lanche_pedido bigint                     null,
    id_lanche        bigint                     null,
    item             integer                    null,
    qtde             decimal(24, 5) default 1.0 null,
    valor_item       decimal(19, 6) default 0.0 null,
    valor_total      decimal(19, 6) default 0.0 null,
    valor_desconto   decimal(19, 6) default 0.0 null,
    dt_cadastro      timestamp      default now(),
    ativo            bool                       null default true,
    primary key (id)
);

alter table food_service.fd05_item_lanche_pedido
    add constraint fd05_item_lanche_pedido_id_lanchepedido_fkey foreign key (id_lanche_pedido) references food_service.fd04_lanche_pedido;
alter table food_service.fd05_item_lanche_pedido
    add constraint fd05_item_lanche_pedido_id_lanche_fkey foreign key (id_lanche) references food_service.fd02_lanche;
alter table food_service.fd04_lanche_pedido
    owner to postgres;

--/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
create sequence food_service.fd06_adicional_item_lanche_pedido_id_seq;

create table if not exists food_service.fd06_adicional_item_lanche_pedido
(
    id                    bigint not null default nextval('food_service.fd06_adicional_item_lanche_pedido_id_seq'),
    id_item_lanche_pedido bigint null,
    id_ingrediente        bigint null,
    qtde                  decimal(24, 5)  default 1.0 null,
    valor_custo           decimal(19, 6)  default 0.0 null,
    dt_cadastro           timestamp       default now(),
    ativo                 bool   null     default true,
    primary key (id)
);

create unique index uq_fd06_adicional_item_lanche_pedido on food_service.fd06_adicional_item_lanche_pedido (id);

alter table food_service.fd06_adicional_item_lanche_pedido
    add constraint fd06_adicional_item_lanche_pedido_id_item_fkey foreign key (id_item_lanche_pedido) references food_service.fd05_item_lanche_pedido;

alter table food_service.fd06_adicional_item_lanche_pedido
    add constraint fd06_adicional_item_lanche_pedido_id_ingrediente_fkey foreign key (id_ingrediente) references food_service.fd01_ingrediente;

alter table food_service.fd06_adicional_item_lanche_pedido
    owner to postgres;

--/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
--/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
--> INSERÇÕES DE REGISTROS DEFAULT NAS TABELAS

insert into food_service.fd07_tipo_ingrediente (descricao)
values ('Carne');
insert into food_service.fd07_tipo_ingrediente (descricao)
values ('Queijo');
insert into food_service.fd07_tipo_ingrediente (descricao)
values ('Hortaliça');
insert into food_service.fd07_tipo_ingrediente (descricao)
values ('Bacon');
insert into food_service.fd07_tipo_ingrediente (descricao)
values ('Outros');

insert into food_service.fd01_ingrediente (descricao, custo, id_tipo)
values ('Alface', 0.40, 3);
insert into food_service.fd01_ingrediente (descricao, custo, id_tipo)
values ('Bacon', 2.00, 4);
insert into food_service.fd01_ingrediente (descricao, custo, id_tipo)
values ('Hamburguer', 3.00, 1);
insert into food_service.fd01_ingrediente (descricao, custo, id_tipo)
values ('Ovo', 0.80, 5);
insert into food_service.fd01_ingrediente (descricao, custo, id_tipo)
values ('Queijo', 1.50, 2);

insert into food_service.fd02_lanche (descricao)
values ('X-Bacon');
insert into food_service.fd02_lanche (descricao)
values ('X-Burger');
insert into food_service.fd02_lanche (descricao)
values ('X-Egg Bacon');
insert into food_service.fd02_lanche (descricao)
values ('X-Egg');

insert into food_service.fd03_ingrediente_lanche (id_lanche, id_ingrediente, qtde)
values (1, 2, 1.00);
insert into food_service.fd03_ingrediente_lanche (id_lanche, id_ingrediente, qtde)
values (1, 3, 1.00);
insert into food_service.fd03_ingrediente_lanche (id_lanche, id_ingrediente, qtde)
values (1, 5, 1.00);
insert into food_service.fd03_ingrediente_lanche (id_lanche, id_ingrediente, qtde)
values (2, 3, 1.00);
insert into food_service.fd03_ingrediente_lanche (id_lanche, id_ingrediente, qtde)
values (2, 5, 1.00);
insert into food_service.fd03_ingrediente_lanche (id_lanche, id_ingrediente, qtde)
values (3, 3, 1.00);
insert into food_service.fd03_ingrediente_lanche (id_lanche, id_ingrediente, qtde)
values (3, 4, 1.00);
insert into food_service.fd03_ingrediente_lanche (id_lanche, id_ingrediente, qtde)
values (3, 5, 1.00);
insert into food_service.fd03_ingrediente_lanche (id_lanche, id_ingrediente, qtde)
values (3, 2, 1.00);
insert into food_service.fd03_ingrediente_lanche (id_lanche, id_ingrediente, qtde)
values (4, 3, 1.00);
insert into food_service.fd03_ingrediente_lanche (id_lanche, id_ingrediente, qtde)
values (4, 4, 1.00);
insert into food_service.fd03_ingrediente_lanche (id_lanche, id_ingrediente, qtde)
values (4, 5, 1.00);

--/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
--> ATUALIZAÇÃO VALORES DO LANCHE DEFAULT

with temp_soma_total AS (
    select sum(qtde * ingrediente.custo) as soma_valor_total
    from food_service.fd03_ingrediente_lanche as item_ingrediente
             inner join food_service.fd01_ingrediente as ingrediente
                        on (ingrediente.id = item_ingrediente.id_ingrediente)
    where item_ingrediente.id_lanche = 1
)
update food_service.fd02_lanche
set valor_total = temp_soma_total.soma_valor_total
from temp_soma_total
where id = 1;

with temp_soma_total AS (
    select sum(qtde * ingrediente.custo) as soma_valor_total
    from food_service.fd03_ingrediente_lanche as item_ingrediente
             inner join food_service.fd01_ingrediente as ingrediente
                        on (ingrediente.id = item_ingrediente.id_ingrediente)
    where item_ingrediente.id_lanche = 2
)
update food_service.fd02_lanche
set valor_total = temp_soma_total.soma_valor_total
from temp_soma_total
where id = 2;


with temp_soma_total AS (
    select sum(qtde * ingrediente.custo) as soma_valor_total
    from food_service.fd03_ingrediente_lanche as item_ingrediente
             inner join food_service.fd01_ingrediente as ingrediente
                        on (ingrediente.id = item_ingrediente.id_ingrediente)
    where item_ingrediente.id_lanche = 3
)
update food_service.fd02_lanche
set valor_total = temp_soma_total.soma_valor_total
from temp_soma_total
where id = 3;

with temp_soma_total AS (
    select sum(qtde * ingrediente.custo) as soma_valor_total
    from food_service.fd03_ingrediente_lanche as item_ingrediente
             inner join food_service.fd01_ingrediente as ingrediente
                        on (ingrediente.id = item_ingrediente.id_ingrediente)
    where item_ingrediente.id_lanche = 4
)
update food_service.fd02_lanche
set valor_total = temp_soma_total.soma_valor_total
from temp_soma_total
where id = 4;