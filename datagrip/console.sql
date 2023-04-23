create database proj_1 encoding ='utf8';


--建表
create table Posts
(
    postID                 int,
    title                  varchar,
    category               varchar[],
    content                varchar,
    postingTime            timestamp,
    postingCity            varchar,
    Author                 varchar,
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
    postID                int,
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
    ID                varchar unique,
    registration_time timestamp,
    phone             varchar,
    name              varchar not null
);


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


drop table reply;
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
    id        serial,
    author    varchar,
    content   varchar not null,
    postID    int     not null,
    stars     int,
    group_num int

);


create table subreply
(
    /*
     id是根据replyid分组后的编号
     authorid content stars已知
     replyid是指向reply的外键
     */
    id       serial,
    authorID varchar,
    content  varchar not null,
    stars    int,
    replyid  int     not null

);



----建表结束


--把原始的两个表的内容放进自己构建的四个表
insert into post(ID, title, content, posting_time, posting_city, category)
select postID, title, content, postingTime, postingCity, category
from posts;

insert into author(name, id, phone, registration_time)
select Author, authorID, authoPhone, authorRegistrationTime
from posts;
--要把authorFollowedBy，authorFavorite，authorShared，authorLiked，reply的，subreply的，但是不在author中的，编个号，搞一个合理的注册时间插进去？？？？？？


insert into reply(author, content, postID, stars)
select replyAuthor, replyContent, postID, replyStars
from replies;
UPDATE reply
SET group_num = subquery.group_num
FROM (SELECT id, row_number() OVER (PARTITION BY postid ) AS group_num
      FROM reply) AS subquery
where reply.id = subquery.id;
select *
from reply
order by postID, group_num;


insert into subreply(authorID, content, stars, replyid)
SELECT secondaryReplyAuthor, secondaryReplyContent, secondaryReplyStars, id
from replies
         join reply r
              on Replies.postID = r.postID and Replies.replyContent = r.content and Replies.replyAuthor = r.author and
                 Replies.replyStars = r.stars;

UPDATE subreply
SET id = subquery.row_num
FROM (SELECT id, ROW_NUMBER() OVER (PARTITION BY replyid ORDER BY id) AS row_num
      FROM subreply) AS subquery
WHERE subreply.id = subquery.id;
select *
from subreply
order by replyid,id;

--test

select array_length(authorShared, 1)
from Posts
where postID = 6;

select *
from Posts
where authorID = '77299519840727839X'
  and authoPhone = '18889727122'
  and authorRegistrationTime = '2010-06-04 11:07:08';

--test结束