
    create table STAMPSTOR.alarm_event_tbx (
        alarm_event_id bigint not null,
        alarm_number integer,
        location integer,
        version integer,
		event_timestamp timestamp,
        primary key (alarm_event_id)
    );

    create table STAMPSTOR.alarms_definition_tbx (
        alarm_definition_id bigint not null,
        alarm_number integer,
        description varchar(255),
        location integer,
        alarm_name varchar(255),
        notification varchar(255),
        notification_category integer,
        severity integer,
        version integer,
        primary key (alarm_definition_id)
    );

    create table STAMPSTOR.carrier_mes_tbx (
        carrier_id bigint not null,
        buffer integer,
        carriernumber integer,
        currentlocation integer,
        destination integer,
        dienumber integer,
        originationlocation integer,
        productionrundate timestamp,
        productionrunnumber integer,
        quantity integer,
        status integer,
        tagid integer,
        updatedate timestamp,
        version integer,
        primary key (carrier_id)
    );

    create table STAMPSTOR.carrier_tbx (
        carrier_id bigint not null,
        buffer integer,
        carrier_number integer,
        carrier_status integer,
        load_timestamp timestamp,
        press_info integer,
        production_run_no integer,
        quantity integer,
        reprocess smallint not null,
        production_run_timestamp timestamp,
        unload_timestamp timestamp,
        version integer,
        current_location bigint,
        destination bigint,
        die bigint,
        primary key (carrier_id)
    );

    create table STAMPSTOR.defect_tbl (
        defect_id bigint not null,
        carrier_number integer,
        defect_repaired smallint,
        defect_timestamp timestamp,
        defect_type integer,
        productionrunnumber integer,
        rework_method integer,
        version integer,
        x_area integer,
        y_area varchar(255),
        primary key (defect_id)
    );

    create table STAMPSTOR.die_tbx (
        die_id bigint not null,
        bpm_part_number varchar(255),
        description varchar(255),
        die_number integer,
        part_production_volume integer,
        version integer,
        primary key (die_id)
    );

    create table STAMPSTOR.model_tbx (
        model_id bigint not null,
        description varchar(255),
        name varchar(255),
        version integer,
        left_die bigint,
        right_die bigint,
        primary key (model_id)
    );

    create table STAMPSTOR.order_mgr_tbx (
        order_mgr_id bigint not null,
        line_name varchar(255),
        max_delivery_capacity integer,
        version integer,
        delivery_stop bigint,
        left_consumption_exit bigint,
        left_consumption_stop bigint,
        right_consumption_exit bigint,
        right_consumption_stop bigint,
        primary key (order_mgr_id)
    );

    create table STAMPSTOR.order_mgr_tbx_orders (
        order_mgr_tbx bigint not null,
        orders bigint not null,
        primary key (order_mgr_tbx, orders),
        unique (orders)
    );

    create table STAMPSTOR.stop_tbx (
        stop_id bigint not null,
        conveyor_id integer,
        description varchar(255),
        name varchar(255),
        stop_type integer,
        version integer,
        primary key (stop_id)
    );

    create table STAMPSTOR.weld_order_tbx (
        order_id bigint not null,
        left_consumed_qty integer,
        left_delivered_qty integer,
        left_qty integer,
        order_sequence integer,
        order_status integer,
        right_consumed_qty integer,
        right_delivered_qty integer,
        right_qty integer,
        version integer,
        model bigint,
        order_mgr bigint,
        primary key (order_id)
    );

    alter table STAMPSTOR.carrier_tbx 
        add constraint FKD23F9203BB4A65B7 
        foreign key (die) 
        references STAMPSTOR.die_tbx;

    alter table STAMPSTOR.carrier_tbx 
        add constraint FKD23F9203F0FE6DE6 
        foreign key (current_location) 
        references STAMPSTOR.stop_tbx;

    alter table STAMPSTOR.carrier_tbx 
        add constraint FKD23F920358A08639 
        foreign key (destination) 
        references STAMPSTOR.stop_tbx;

    alter table STAMPSTOR.model_tbx 
        add constraint FK882D2DB421BAD97F 
        foreign key (left_die) 
        references STAMPSTOR.die_tbx;

    alter table STAMPSTOR.model_tbx 
        add constraint FK882D2DB45DC6E914 
        foreign key (right_die) 
        references STAMPSTOR.die_tbx;

    alter table STAMPSTOR.order_mgr_tbx 
        add constraint FK3181C93229546530 
        foreign key (right_consumption_exit) 
        references STAMPSTOR.stop_tbx;

    alter table STAMPSTOR.order_mgr_tbx 
        add constraint FK3181C932295AB414 
        foreign key (right_consumption_stop) 
        references STAMPSTOR.stop_tbx;

    alter table STAMPSTOR.order_mgr_tbx 
        add constraint FK3181C932FD000989 
        foreign key (left_consumption_stop) 
        references STAMPSTOR.stop_tbx;

    alter table STAMPSTOR.order_mgr_tbx 
        add constraint FK3181C932FCF9BAA5 
        foreign key (left_consumption_exit) 
        references STAMPSTOR.stop_tbx;

    alter table STAMPSTOR.order_mgr_tbx 
        add constraint FK3181C9329A8D3F98 
        foreign key (delivery_stop) 
        references STAMPSTOR.stop_tbx;

    alter table STAMPSTOR.order_mgr_tbx_orders 
        add constraint FKC6E43A92EA4C82C5 
        foreign key (order_mgr_tbx) 
        references STAMPSTOR.order_mgr_tbx;

    alter table STAMPSTOR.order_mgr_tbx_orders 
        add constraint FKC6E43A9223C09C84 
        foreign key (orders) 
        references STAMPSTOR.weld_order_tbx;

    alter table STAMPSTOR.weld_order_tbx 
        add constraint FK1000FB20A178BF3A 
        foreign key (order_mgr) 
        references STAMPSTOR.order_mgr_tbx;

    alter table STAMPSTOR.weld_order_tbx 
        add constraint FK1000FB20134B0149 
        foreign key (model) 
        references STAMPSTOR.model_tbx;

    create table STAMPSTOR.hibernate_unique_key (
         next_hi integer 
    );

    insert into STAMPSTOR.hibernate_unique_key values ( 0 );
