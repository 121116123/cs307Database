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