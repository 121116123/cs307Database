# proj2

1. 匿名发言：posts里面写的是unknown，author_send_post 里面是author真正的名字
2. 屏蔽，拉黑：搞一个 blocker_blocked，存的是用户名字，不需要往loader里面写，在main里面，每次查询之类的直接循环去找，屏蔽别人的时候直接加一条





## 展示

1. 匿名：在发布post和reply subreply时，可以选择匿名，这时候post reply subreply 里面会显示author unknown，但是在查自己发布的post时，可以查到，并且可以看到是匿名发布还是实名发布
2. 屏蔽：可以屏蔽和取消屏蔽，在点赞收藏转发时，看不到屏蔽的人发的帖子（匿名发的可以看到）
3. 热搜榜