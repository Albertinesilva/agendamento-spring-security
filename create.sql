
    create table agendamentos (
        data_consulta date,
        id bigint generated by default as identity,
        id_especialidade bigint,
        id_horario bigint,
        id_medico bigint,
        id_paciente bigint,
        primary key (id)
    );

    create table especialidades (
        id bigint generated by default as identity,
        descricao TEXT,
        titulo varchar(255) not null unique,
        primary key (id)
    );

    create table horas (
        hora_minuto time(6) not null unique,
        id bigint generated by default as identity,
        primary key (id)
    );

    create table medicos (
        crm integer not null unique,
        data_inscricao date not null,
        id bigint generated by default as identity,
        id_usuario bigint unique,
        nome varchar(255) not null unique,
        primary key (id)
    );

    create table medicos_tem_especialidades (
        id_especialidade bigint not null,
        id_medico bigint not null,
        primary key (id_especialidade, id_medico)
    );

    create table pacientes (
        data_nascimento date not null,
        id bigint generated by default as identity,
        id_usuario bigint unique,
        nome varchar(255) not null unique,
        primary key (id)
    );

    create table perfis (
        id bigint generated by default as identity,
        descricao varchar(255) not null unique,
        primary key (id)
    );

    create table usuarios (
        ativo boolean not null,
        codigo_verificador varchar(6),
        id bigint generated by default as identity,
        email varchar(255) not null unique,
        senha varchar(255) not null,
        primary key (id)
    );

    create table usuarios_tem_perfis (
        perfil_id bigint not null,
        usuario_id bigint not null
    );

    create index idx_especialidade_titulo 
       on especialidades (titulo);

    create index idx_hora_minuto 
       on horas (hora_minuto);

    create index idx_usuario_email 
       on usuarios (email);

    alter table if exists agendamentos 
       add constraint FKjhihg6sao9p1o3motjb0np9le 
       foreign key (id_especialidade) 
       references especialidades;

    alter table if exists agendamentos 
       add constraint FKlclwrj7i8bkger1cww58bj8wf 
       foreign key (id_horario) 
       references horas;

    alter table if exists agendamentos 
       add constraint FK70w3x8i8mc0ys8k47unu2jb9d 
       foreign key (id_medico) 
       references medicos;

    alter table if exists agendamentos 
       add constraint FKto8l4lwoob2ebmvhvlg0d3t1d 
       foreign key (id_paciente) 
       references pacientes;

    alter table if exists medicos 
       add constraint FKhei0g6fy15d5komevfsk1qepe 
       foreign key (id_usuario) 
       references usuarios;

    alter table if exists medicos_tem_especialidades 
       add constraint FKsqgjqveg6ipicklj9ejaeknmr 
       foreign key (id_especialidade) 
       references especialidades;

    alter table if exists medicos_tem_especialidades 
       add constraint FK3t0i9oahaf4xg18s2ijyjeo5b 
       foreign key (id_medico) 
       references medicos;

    alter table if exists pacientes 
       add constraint FK34je9ip2cpgvy3m4ove9lmmqk 
       foreign key (id_usuario) 
       references usuarios;

    alter table if exists usuarios_tem_perfis 
       add constraint FKewye59sxbuklud72lsswd1mn1 
       foreign key (perfil_id) 
       references perfis;

    alter table if exists usuarios_tem_perfis 
       add constraint FKg6l7ittcd3wnixu65x04veyq6 
       foreign key (usuario_id) 
       references usuarios;
