# proj2

1. 匿名发言：posts里面写的是unknown，author_send_post 里面是author真正的名字
2. 屏蔽，拉黑：搞一个 blocker_blocked，存的是用户名字，不需要往loader里面写，在main里面，每次查询之类的直接循环去找，屏蔽别人的时候直接加一条





## 展示

1. 登录：

   1. 可以已有帐户或是新建用户
   2. 已有帐户会检测是不是真的有，如果没有会要求重新输入或注册
   3. 注册若id已有账号，可以选择使用那个账号或者重新输入账号
   4. 注册时若昵称已被使用，需要换一个新的

2. author favorite: 

   1. 可以根据postid直接点赞，也可以通过搜索category找到相关主题的posts；
   2. 如果没有这个postid，可以选择重新输入或放弃
   3. 有一个页面显示，在用category查找时

3. 匿名：

   1. 在发布post和reply subreply时，可以选择匿名
   2. 这时候post reply subreply 里面会显示author unknown， 其他人也不知道这个是谁发的，在参数查找时可以看出
   3. 但是在查自己发布的post时，可以查到，并且可以看到是匿名发布还是实名发布

4. 屏蔽：

   1. 可以屏蔽和取消屏蔽
   2. 在点赞收藏转发时，看不到屏蔽的人发的帖子（匿名发的可以看到）

5. 热搜榜

   1. 会输出按like排名的postid
   2. 有一个页面展示

6. 多参数搜索

   1. 由于用category搜索已经在2中写过，这边展示 author time 两个参数的搜索，看某段时间某个用户发布的post

      