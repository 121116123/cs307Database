create database proj_1 encoding ='utf8';


--建表
drop table posts;

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

drop table replies;

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

drop table author cascade ;

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



-------------导数据，用load_author
-- 处理author
ALTER TABLE author
    ADD COLUMN self_increasing_id serial;

DELETE
FROM author
WHERE self_increasing_id NOT IN (SELECT self_increasing_id
                                 FROM (SELECT self_increasing_id,
                                              ROW_NUMBER() OVER (PARTITION BY name ORDER BY phone ) AS row_number
                                       FROM author) t
                                 WHERE t.row_number = 1);

SELECT name, COUNT(*)
FROM author
GROUP BY name
HAVING COUNT(*) > 1;

ALTER TABLE author ADD CONSTRAINT unique_my_column UNIQUE (name);

drop table post cascade ;

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

drop table reply cascade ;

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
drop table subreply cascade ;

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
    foreign key (replyid) references reply (id)

);


--处理author
-- ALTER TABLE author
--     ADD COLUMN self_increasing_id serial;
--
-- DELETE
-- FROM author
-- WHERE self_increasing_id NOT IN (SELECT self_increasing_id
--                                  FROM (SELECT self_increasing_id,
--                                               ROW_NUMBER() OVER (PARTITION BY name ORDER BY phone ) AS row_number
--                                        FROM author) t
--                                  WHERE t.row_number = 1);
--
-- SELECT name, COUNT(*)
-- FROM author
-- GROUP BY name
-- HAVING COUNT(*) > 1;


--填post
insert into post(ID, title, content, posting_time, posting_city, category)
select postID, title, content, postingTime, postingCity, category
from posts;


--填reply
insert into reply(author, content, postID, stars)
select replyAuthor, replyContent, postID, replyStars
from replies;
UPDATE reply
SET group_num = subquery.group_num
FROM (SELECT id, row_number() OVER (PARTITION BY postid ) AS group_num
      FROM reply) AS subquery
where reply.id = subquery.id;


--填subreply
insert into subreply(author, content, stars, replyid)
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


-----------------写关系表
drop table leader_follower cascade ;

create table leader_follower
(
    leader   varchar,
    follower varchar,
    primary key (leader, follower),
    foreign key (leader) references author (name),
    foreign key (follower) references author (name)
);

INSERT INTO leader_follower (leader, follower)
SELECT author AS leader, unnest(authorfollowedby) AS follower
FROM posts;

drop table author_send_post cascade ;

create table author_send_post
(
    author varchar,
    postid int,
    primary key (author, postid),
    foreign key (author) references author (name),
    foreign key (postid) references post (ID)
);

insert into author_send_post(author, postid)
select Author as author, postID as postid
from posts;

drop table author_favorite_post cascade ;

create table author_favorite_post
(
    postid int,
    author varchar,
    primary key (postid, author),
    foreign key (postid) references post (ID),
    foreign key (author) references author (name)
);
insert into author_favorite_post(postid, author)
select postID as postid, unnest(authorFavorite) as author
from posts;

drop table author_share_post cascade ;

create table author_share_post
(
    postid int,
    author varchar,
    primary key (postid, author),
    foreign key (postid) references post (ID),
    foreign key (author) references author (name)
);
insert into author_share_post(postid, author)
select postID as postid, unnest(authorShared) as author
from posts;

drop table author_like_post cascade ;

create table author_like_post
(
    postid int,
    author varchar,
    primary key (postid, author),
    foreign key (postid) references post (ID),
    foreign key (author) references author (name)
);
insert into author_like_post(postID, author)
select postID as postid, unnest(authorLiked) as author
from posts;


--test
select count(ID)
from post;

select count(author)
from author_like_post
where postid = 163;

select count(author)
from author_like_post
where postid = 28;

select count(author)
from author_like_post
where postid = 6;

select count(author)
from author_favorite_post
where postid = 163;

select count(author)
from author_favorite_post
where postid = 28;

select count(author)
from author_favorite_post
where postid = 6;

select count(author)
from author_share_post
where postid = 163;

select count(author)
from author_share_post
where postid = 28;

select count(author)
from author_share_post
where postid = 6;

select *
from author
where name = 'novel_expert';

select *
from author
where name = 'creative_hat';

select *
from author
where name = 'embarrassed_guitar';

WITH x AS (SELECT COUNT(author) AS cnt, postid
           FROM author_like_post
           GROUP BY postid)
SELECT cnt, postid
FROM x
WHERE cnt = (SELECT MAX(cnt) FROM x)
order by postid;

WITH x AS (SELECT COUNT(author) AS cnt, postid
           FROM author_favorite_post
           GROUP BY postid)
SELECT cnt, postid
FROM x
WHERE cnt = (SELECT MAX(cnt) FROM x)
order by postid;

WITH x AS (SELECT COUNT(author) AS cnt, postid
           FROM author_share_post
           GROUP BY postid)
SELECT cnt, postid
FROM x
WHERE cnt = (SELECT MAX(cnt) FROM x)
order by postid;

with x as (select count(follower) as cnt, leader
           from leader_follower
           group by leader)
select cnt, leader
from x
where cnt = (select max(cnt)
             from x)
order by leader;

with x as (select count(leader) as cnt, follower
           from leader_follower
           group by follower)
select cnt, follower
from x
where cnt = (select max(cnt) from x)
order by follower;

select ID
from post
where posting_time = (select min(posting_time)
                      from post)
;

select ID
from post
where posting_time = (select max(posting_time)
                      from post)
;

select count(ID)
from post
where extract(year from posting_time) = 2020;

select count(ID)
from post
where extract(year from posting_time) = 2021;

select count(ID)
from post
where extract(year from posting_time) = 2022
;

--test结束