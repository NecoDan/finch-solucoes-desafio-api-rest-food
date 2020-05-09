create schema if not exists food_service;
set schema 'food_service';

--/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
create sequence if not exists food_service.fd06_tipo_ingrediente_id_seq;

create table if not exists food_service.fd06_tipo_ingrediente
(
    id          bigint       not null DEFAULT nextval('food_service.fd06_tipo_ingrediente_id_seq'),
    descricao   varchar(200) null,
    dt_cadastro timestamp             default now(),
    ativo       boolean               default true null,
    primary key (id)
);

create unique index if not exists uq_fd06_tipo_ingrediente on food_service.fd06_tipo_ingrediente (id);
alter table food_service.fd06_tipo_ingrediente
    owner to postgres;

--/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
create sequence if not exists food_service.fd01_ingrediente_id_seq;

create table if not exists food_service.fd01_ingrediente
(
    id          bigint                       not null DEFAULT nextval('food_service.fd01_ingrediente_id_seq'),
    descricao   varchar(200)                 null,
    custo       decimal(19, 2) default 0     not null,
    id_tipo     bigint                       null,
    dt_cadastro timestamp      default now() not null,
    ativo       boolean        default true  null,
    primary key (id)
);

create unique index if not exists uq_fd01_ingrediente on food_service.fd01_ingrediente (id);

alter table food_service.fd01_ingrediente
    add constraint fd01_ingrediente_id_tipo_fkey foreign key (id_tipo) references food_service.fd06_tipo_ingrediente (id);

alter table food_service.fd01_ingrediente
    owner to postgres;

--/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
create sequence if not exists food_service.fd02_lanche_id_seq;

create table if not exists food_service.fd02_lanche
(
    id          bigint                       not null DEFAULT nextval('food_service.fd02_lanche_id_seq'),
    descricao   varchar(200)                 null,
    valor_total decimal(19, 6) default 0     not null,
    dt_cadastro timestamp      default now() not null,
    ativo       boolean        default true  null,
    primary key (id)
);

create unique index if not exists uq_fd02_lanche on food_service.fd02_lanche (id);
alter table food_service.fd02_lanche
    owner to postgres;

--/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

create table food_service.fd03_ingrediente_lanche
(
    "id_lanche"      bigint    not null,
    "id_ingrediente" bigint    not null,
    "qtde"           decimal(19, 6) default 1.0 null,
    "dt_cadastro"    timestamp null DEFAULT now(),
    "ativo"          bool      null default true,
    PRIMARY KEY ("id_lanche", "id_ingrediente")
);

ALTER TABLE "food_service"."fd03_ingrediente_lanche"
    ADD CONSTRAINT "fd03_ingrediente_lanche_id_lanche_fkey" FOREIGN KEY ("id_lanche") REFERENCES "food_service"."fd02_lanche" ("id") ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE "food_service"."fd03_ingrediente_lanche"
    ADD CONSTRAINT "fd03_ingrediente_lanche_id_ingrediente_fkey" FOREIGN KEY ("id_ingrediente") REFERENCES "food_service"."fd01_ingrediente" ("id") ON DELETE NO ACTION ON UPDATE NO ACTION;

--/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
create sequence if not exists food_service.fd04_lanche_pedido_id_seq;

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

create unique index if not exists uq_fd01_pedido on food_service.fd04_lanche_pedido (id);
alter table food_service.fd04_lanche_pedido
    owner to postgres;

--/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
create sequence if not exists food_service.fd05_item_lanche_pedido_id_seq;

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
    "ativo"          bool                       null default true,
    primary key (id)
);

alter table food_service.fd05_item_lanche_pedido
    add constraint fd05_item_lanche_pedido_id_lanchepedido_fkey foreign key (id_lanche_pedido) references food_service.fd04_lanche_pedido;
alter table food_service.fd05_item_lanche_pedido
    add constraint fd05_item_lanche_pedido_id_lanche_fkey foreign key (id_lanche) references food_service.fd02_lanche;
alter table food_service.fd04_lanche_pedido
    owner to postgres;

--/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
create sequence if not exists food_service.fd06_adicional_item_lanche_pedido_id_seq;

create table if not exists food_service.fd06_adicional_item_lanche_pedido
(
    id                    bigint not null default nextval('food_service.fd06_adicional_item_lanche_pedido_id_seq'),
    id_item_lanche_pedido bigint null,
    id_ingrediente        bigint null,
    qtde                  decimal(19, 6)  default 1.0 null,
    valor_custo           decimal(19, 6)  default 1.0 null,
    dt_cadastro           timestamp       default now(),
    ativo                 bool   null     default true,
    primary key (id)
);

create unique index if not exists uq_fd06_adicional_item_lanche_pedido on food_service.fd06_adicional_item_lanche_pedido (id);

alter table food_service.fd06_adicional_item_lanche_pedido
    add constraint fd06_adicional_item_lanche_pedido_id_item_fkey foreign key (id_item_lanche_pedido) references food_service.fd05_item_lanche_pedido;

alter table food_service.fd06_adicional_item_lanche_pedido
    add constraint fd06_adicional_item_lanche_pedido_id_ingrediente_fkey foreign key (id_ingrediente) references food_service.fd01_ingrediente;

alter table food_service.fd06_adicional_item_lanche_pedido
    owner to postgres;

--/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
--/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
--> INSERÇÕES DE REGISTROS DEFAULT NAS TABELAS

insert into food_service.fd06_tipo_ingrediente (descricao)
values ('Carne');
insert into food_service.fd06_tipo_ingrediente (descricao)
values ('Queijo');
insert into food_service.fd06_tipo_ingrediente (descricao)
values ('Hortaliça');
insert into food_service.fd06_tipo_ingrediente (descricao)
values ('Bacon');
insert into food_service.fd06_tipo_ingrediente (descricao)
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

insert into food_service.fd03_item_ingrediente_lanche (id_lanche, id_ingrediente, qtde, valor_total)
values (1, 2, 1.00, 2.00);
insert into food_service.fd03_item_ingrediente_lanche (id_lanche, id_ingrediente, qtde, valor_total)
values (4, 2, 1.00, 2.00);
insert into food_service.fd03_item_ingrediente_lanche (id_lanche, id_ingrediente, qtde, valor_total)
values (1, 3, 1.00, 3.00);
insert into food_service.fd03_item_ingrediente_lanche (id_lanche, id_ingrediente, qtde, valor_total)
values (2, 3, 1.00, 3.00);
insert into food_service.fd03_item_ingrediente_lanche (id_lanche, id_ingrediente, qtde, valor_total)
values (4, 3, 1.00, 3.00);
insert into food_service.fd03_item_ingrediente_lanche (id_lanche, id_ingrediente, qtde, valor_total)
values (3, 3, 1.00, 3.00);
insert into food_service.fd03_item_ingrediente_lanche (id_lanche, id_ingrediente, qtde, valor_total)
values (4, 4, 1.00, 0.80);
insert into food_service.fd03_item_ingrediente_lanche (id_lanche, id_ingrediente, qtde, valor_total)
values (3, 4, 1.00, 0.80);
insert into food_service.fd03_item_ingrediente_lanche (id_lanche, id_ingrediente, qtde, valor_total)
values (1, 5, 1.00, 1.50);
insert into food_service.fd03_item_ingrediente_lanche (id_lanche, id_ingrediente, qtde, valor_total)
values (2, 5, 1.00, 1.50);
insert into food_service.fd03_item_ingrediente_lanche (id_lanche, id_ingrediente, qtde, valor_total)
values (4, 5, 1.00, 1.50);
insert into food_service.fd03_item_ingrediente_lanche (id_lanche, id_ingrediente, qtde, valor_total)
values (3, 5, 1.00, 1.50);

--/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
--> ATUALIZAÇÃO VALORES DO LANCHE DEFAULT

update food_service.fd02_lanche
set valor_total = (select sum(valor_total) from food_service.fd03_item_ingrediente_lanche where id_lanche in (1))
where id in (1);
update food_service.fd02_lanche
set valor_total = (select sum(valor_total) from food_service.fd03_item_ingrediente_lanche where id_lanche in (2))
where id in (2);
update food_service.fd02_lanche
set valor_total = (select sum(valor_total) from food_service.fd03_item_ingrediente_lanche where id_lanche in (3))
where id in (3);
update food_service.fd02_lanche
set valor_total = (select sum(valor_total) from food_service.fd03_item_ingrediente_lanche where id_lanche in (4))
where id in (4);
