create table Posts
(
    postID                 int unique,
    title                  varchar,
    category               varchar[],
    content                varchar,
    postingTime            timestamp,
    postingCity            varchar,
    Author                 varchar unique,
    authorRegistrationTime timestamp,
    authorID               varchar,
    authoPhone             varchar,
    authorFollowedBy       varchar[],
    authorFavorite         varchar[],
    authorShared           varchar[],
    authorLiked            varchar[]

);

create table Replies
(
    postID                int ,
    replyContent          varchar,
    replyStars            int,
    replyAuthor           varchar,
    secondaryReplyContent varchar,
    secondaryReplyStars   int,
    secondaryReplyAuthor  varchar
);

create table author
(
    /*
    id是已知数据中的和自己编造的
    time 是已知和编造
    phone是已给信息的
    name是每种的author
     */
    ID                varchar unique primary key,
    registration_time timestamp,
    phone             varchar,
    name              varchar not null
);

ALTER TABLE author
    ADD COLUMN self_increasing_id serial;

DELETE
FROM author
WHERE self_increasing_id NOT IN (SELECT self_increasing_id
                                 FROM (SELECT self_increasing_id,
                                              ROW_NUMBER() OVER (PARTITION BY name ORDER BY phone ) AS row_number
                                       FROM author) t
                                 WHERE t.row_number = 1);

ALTER TABLE author ADD CONSTRAINT unique_my_column UNIQUE (name);

create table post
(
    /*
     id是已知的postid
     title是已知
     content是已知
     postingtime是已知的时间戳
     city已知
     categroy是用了元组的varchar
     */
    ID           int primary key unique not null,
    title        varchar,
    content      varchar,
    posting_time timestamp,
    posting_city varchar,
    category     varchar
);

create table reply
(
    /*
     id是自增的，每个reply有独一无二的id
     author已知
     content已知
     post是指向post的一个外键
     stars已知
     groupnum是按照postid分组后的编号
     */
    id        serial primary key,
    author    varchar,
    content   varchar not null,
    postID    int     not null,
    stars     int,
    group_num int,
    foreign key (author) references author (name),
    foreign key (postID) references post (ID)
);


create table subreply
(
    /*
     id是根据replyid分组后的编号
     authorid content stars已知
     replyid是指向reply的外键
     */
    id      serial,
    author  varchar,
    content varchar not null,
    stars   int,
    replyid int     not null,
    primary key (id, replyid),
    foreign key (replyid) references reply (id),
    foreign key (author) references author(name)
);

UPDATE reply
SET group_num = subquery.group_num
FROM (SELECT id, row_number() OVER (PARTITION BY postid ) AS group_num
      FROM reply) AS subquery
where reply.id = subquery.id;

UPDATE subreply
SET id = subquery.row_num
FROM (SELECT id, ROW_NUMBER() OVER (PARTITION BY replyid ORDER BY id) AS row_num
      FROM subreply) AS subquery
WHERE subreply.id = subquery.id;

create table leader_follower
(
    leader   varchar,
    follower varchar,
    primary key (leader, follower),
    foreign key (leader) references author (name),
    foreign key (follower) references author (name)
);

create table author_send_post
(
    author varchar,
    postid int,
    primary key (author, postid),
    foreign key (author) references author (name),
    foreign key (postid) references post (ID)
);

create table author_favorite_post
(
    postid int,
    author varchar,
    primary key (postid, author),
    foreign key (postid) references post (ID),
    foreign key (author) references author (name)
);
create table author_share_post
(
    postid int,
    author varchar,
    primary key (postid, author),
    foreign key (postid) references post (ID),
    foreign key (author) references author (name)
);
create table author_like_post
(
    postid int,
    author varchar,
    primary key (postid, author),
    foreign key (postid) references post (ID),
    foreign key (author) references author (name)
);