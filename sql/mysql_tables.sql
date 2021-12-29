-- 项目用户表
CREATE TABLE `tb_user` (
  `id` int(13) NOT NULL COMMENT '主键ID',
  `username` varchar(64) DEFAULT NULL COMMENT '用户名称',
  `password` varchar(64) DEFAULT NULL COMMENT '用户密码',
  `role` varchar(16) DEFAULT NULL COMMENT '用户角色(admin-管理员，guest-宾客)',
  `permission` varchar(16) DEFAULT NULL COMMENT '用户权限',
  `ban` char(4) DEFAULT NULL COMMENT '用户状态(1-可用，0停用)',
  `phone` varchar(16) DEFAULT NULL COMMENT '电话号码',
  `email` varchar(64) DEFAULT NULL COMMENT '邮箱地址',
  `created` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间(年-月-日 时:分:秒)',
  `updated` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间(年-月-日 时:分:秒)',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户表';

-- 产品菜单树形结构表
CREATE TABLE `tb_menu_tree` (
  `id` varchar(24) NOT NULL COMMENT '类别ID',
  `parent_id` varchar(24) DEFAULT NULL COMMENT '父类别ID',
  `menu_name` varchar(64) DEFAULT NULL COMMENT '菜单名称',
  `role` varchar(24) DEFAULT NULL COMMENT '菜单角色(admin-管理员，guest-宾客)',
  `permission` char(4) DEFAULT NULL COMMENT '菜单权限',
  `create_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间(年-月-日 时:分:秒)',
  `create_name` varchar(24) DEFAULT NULL COMMENT '创建用户',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间(年-月-日 时:分:秒)',
  `update_name` varchar(24) DEFAULT NULL COMMENT '更新用户',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='菜单树形结构表';
