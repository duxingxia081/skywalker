use skywalker;

create table if not exists mho_skywalker_user (
	user_id int(11) not null primary key auto_increment comment '用户ID',
	user_name varchar(255) not null unique comment '账号',
	mobile_phone varchar(255) unique comment '手机号',
	wechat_id varchar(255) unique comment '微信ID',
	qq_id varchar(255) unique comment 'QQ_ID',
	password varchar(255) not null comment '密码',
	nickname varchar(255) comment '昵称',
	sign varchar(255) comment '个性签名',
	sex char comment '性别',
	address varchar(255) comment '地址',
	qr_code_image varchar(255) comment '二维码图',
	head_image varchar(255) comment '头像',
	cover_image varchar(255) comment '封面图',
	time_create datetime default current_timestamp comment '创建时间',
	is_delete char default 0 comment '是否删除'
);

create table if not exists mho_skywalker_user_token (
	id int(11) not null primary key auto_increment comment 'ID',
	user_id int not null comment '用户ID',
	constraint fk_token_user_id foreign key (user_id) references mho_skywalker_user(user_id),
	user_name varchar(255) not null comment '账号',
	constraint fk_token_user_name foreign key (user_name) references mho_skywalker_user(user_name),
	access_token varchar(255) not null comment 'Token',
	time_create datetime default current_timestamp comment '创建时间',
	is_delete char default 0 comment '是否删除'
);

create table if not exists mho_skywalker_role (
	role_id int(11) not null primary key auto_increment comment '角色ID',
	role_name varchar(255) not null unique comment '角色名',
	time_create datetime default current_timestamp comment '创建时间',
	is_delete char default 0 comment '是否删除'
);

create table if not exists mho_skywalker_user_role (
	id int(11) not null primary key auto_increment comment 'ID',
	user_id int not null comment '用户ID',
	constraint fk_user_id foreign key (user_id) references mho_skywalker_user(user_id),
	role_id int not null comment '角色ID',
	constraint fk_role_id1 foreign key (role_id) references mho_skywalker_role(role_id),
	time_create datetime default current_timestamp comment '创建时间',
	is_delete char default 0 comment '是否删除'
);
alter table mho_skywalker_user_role add constraint user_role_user_id_role_id unique(user_id,role_id);

create table if not exists mho_skywalker_chat_room (
	room_id int(11) not null primary key auto_increment comment '聊天室ID',
	room_name varchar(255) not null comment '聊天室名称',
	room_type char comment '聊天室类型（1.个人。2.群组）',
	time_create datetime default current_timestamp comment '创建时间',
	is_delete char default 0 comment '是否删除'
);

create table if not exists mho_skywalker_chat_room_user (
	id int(11) not null primary key auto_increment comment 'ID',
	room_id int not null comment '聊天室ID',
	constraint fk_chat_room_room_id foreign key (room_id) references mho_skywalker_chat_room(room_id),
	user_id int not null comment '用户ID',
	constraint fk_chat_room_user_id foreign key (user_id) references mho_skywalker_user(user_id),
	time_create datetime default current_timestamp comment '创建时间',
	is_delete char default 0 comment '是否删除'
);

create table if not exists mho_skywalker_chat_record (
	record_id int(11) not null primary key auto_increment comment '聊天记录ID',
	content text not null comment '聊天记录内容',
	record_time datetime not null comment '聊天记录时间',
	send_user_id int not null comment '发送人',
	constraint fk_chat_record_send_user_id foreign key (send_user_id) references mho_skywalker_user(user_id),
	room_id int not null comment '聊天室ID',
	time_create datetime default current_timestamp comment '创建时间',
	is_delete char default 0 comment '是否删除'
);

create table if not exists mho_skywalker_travel_notes (
	travel_notes_id int(11) not null primary key auto_increment comment '游记ID',
	post_user_id int not null comment '发布者ID',
	constraint travel_notes_post_user_id foreign key (post_user_id) references mho_skywalker_user(user_id),
	title varchar(255) not null comment '游记标题',
	content text comment '游记内容',
	address_name varchar(255) comment '游记地点',
	address_coordinate varchar(255) comment '游记地点坐标',
	time_create datetime default current_timestamp comment '创建时间',
	is_delete char default 0 comment '是否删除'
);

create table if not exists mho_skywalker_travel_notes_image (
	image_id int(11) not null primary key auto_increment comment '游记图片ID',
	travel_notes_id int not null comment '游记ID',
	constraint fk_image_travel_notes_id foreign key (travel_notes_id) references mho_skywalker_travel_notes(travel_notes_id),
	image_name varchar(255) not null comment '游记图片名称',
	image_url varchar(255) not null comment '游记图片地址',
	time_create datetime default current_timestamp comment '创建时间',
	is_delete char default 0 comment '是否删除'
);

create table if not exists mho_skywalker_travel_notes_like (
	like_id int(11) not null primary key auto_increment comment '游记点赞ID',
	travel_notes_id int not null comment '游记ID',
	constraint fk_like_travel_notes_id foreign key (travel_notes_id) references mho_skywalker_travel_notes(travel_notes_id),
	dolike_user_id int not null comment '点赞人ID',
	constraint travel_notes_like_dolike_user_id foreign key (dolike_user_id) references mho_skywalker_user(user_id),
	time_create datetime default current_timestamp comment '创建时间',
	is_delete char default 0 comment '是否删除'
);

create table if not exists mho_skywalker_travel_notes_message (
	message_id int(11) not null primary key auto_increment comment '游记评论ID',
	travel_notes_id int not null comment '游记ID',
	constraint fk_message_travel_notes_id foreign key (travel_notes_id) references mho_skywalker_travel_notes(travel_notes_id),
	parent_message_id int comment '父评论ID',
	send_message_user_id int not null comment '评论人ID',
	content varchar(255) not null comment '评论内容',
	time_create datetime default current_timestamp comment '创建时间',
	is_delete char default 0 comment '是否删除'
);

create table if not exists mho_skywalker_websocket_message (
	message_id int(11) not null primary key auto_increment comment 'websocket消息ID',
	type varchar(100) not null comment '消息类型',
	type_id int(11) not null comment '当前消息类型中对应的记录ID',
	from_user_id int not null comment '消息发送人ID',
	constraint websocket_message_from_user_id foreign key (from_user_id) references mho_skywalker_user(user_id),
	to_user_id int not null comment '消息发送人ID',
	constraint websocket_message_to_user_id foreign key (to_user_id) references mho_skywalker_user(user_id),
	readed char default 0 comment '是否已读',
	time_create datetime default current_timestamp comment '创建时间',
	is_delete char default 0 comment '是否删除'
);

create table if not exists mho_skywalker_active_type (
	type_id int(11) not null primary key auto_increment comment '活动类型ID',
	type_name varchar(100) not null comment '活动类型名称',
	type_image varchar(255) not null comment '活动类型图片',
	time_create datetime default current_timestamp comment '创建时间',
	is_delete char default 0 comment '是否删除'
);

create table if not exists mho_skywalker_active (
	active_id int(11) not null primary key auto_increment comment '活动ID',
	active_title varchar(100) not null comment '活动标题',
	post_user_id int not null comment '发布者ID',
	constraint fk_active_post_user_id foreign key (post_user_id) references mho_skywalker_user(user_id),
	type_id int(11) not null comment '活动类型ID',
	constraint fk_active_type_id foreign key (type_id) references mho_skywalker_active_type(type_id),
	start_address_name varchar(255) comment '活动开始地点名称',
	start_address_coordinate varchar(255) comment '活动开始地点坐标',
	end_address_name varchar(255) comment '活动目的地地点名称',
	end_address_coordinate varchar(255) comment '活动目的地地点坐标',
	go_time datetime comment '出发日期',
	days int(11) comment '天数',
	charge varchar(255) comment '费用',
	content text comment '活动内容',
	cover_image varchar(255) comment '封面图片',
	time_create datetime default current_timestamp comment '创建时间',
	is_delete char default 0 comment '是否删除'
);
create table if not exists mho_skywalker_active_image (
	active_image_id int(11) not null primary key auto_increment comment '活动图片ID',
	active_id int not null comment '活动ID',
	constraint fk_active_image_active_id foreign key (active_id) references mho_skywalker_active(active_id),
	image_name varchar(255) not null comment '活动图片名称',
	image_url varchar(255) not null comment '活动图片地址',
	time_create datetime default current_timestamp comment '创建时间',
	is_delete char default 0 comment '是否删除'
);
create table if not exists mho_skywalker_active_user (
	id int(11) not null primary key auto_increment comment 'ID',
	active_id int not null comment '活动ID',
	constraint fk_active_active_id foreign key (active_id) references mho_skywalker_active(active_id),
	user_id int not null comment '用户ID',
	constraint fk_active_user_id foreign key (user_id) references mho_skywalker_user(user_id),
	time_create datetime default current_timestamp comment '创建时间',
	is_delete char default 0 comment '是否删除'
);
create table if not exists mho_skywalker_active_leave_message (
	leave_message_id int(11) not null primary key auto_increment comment '留言Id',
	active_id int not null comment '活动ID',
	constraint fk_active_leave_message_active_id foreign key (active_id) references mho_skywalker_active(active_id),
	user_id int not null comment '用户ID',
	constraint fk_active_leave_message_user_id foreign key (user_id) references mho_skywalker_user(user_id),
	parent_leave_message_id int(11) comment '父留言ID',
	content varchar(255) not null comment '留言内容',
	time_create datetime default current_timestamp comment '创建时间',
	is_delete char default 0 comment '是否删除'
);
insert into mho_skywalker_role (role_id, role_name) values (1, 'ROLE_ADMIN');
insert into mho_skywalker_role (role_id, role_name) values (2, 'ROLE_USER');
