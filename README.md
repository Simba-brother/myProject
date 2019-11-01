# myProject
这是一个旅游网项目。


## 总的来说：
*前端用JQuery进行Ajax异步请求后台服务器的Servlet（这里用BaseServlet 进行了方法的分发）。运用JdbcTamplate对数据库进行查询与封装。运用Jackson对对象进行jsonxu序列化到前端。然后JQuery进行前端Dom操作，展示数据。其中后台从Servlet取得前台参数或从sessoin域中取得参数进行用BeanUtils进行简单封装。然后调用Service层（例如redis 操作），最后DAO层。*

## 环境配置: Idea IDE; Maven.
可以新建一个Maven项目然后，点击右边Mave侧边选项框点击+号，选中项目中的pom.xml；然后在congfig中配置maven tomcat7:run  ok了！，配置好maven的本地仓库。这一点可以在pom.xml中配置指定的本地仓库（jar）。

##注册功能：
    注册表单通过引入前端异步框剪JQuery，通过Ajax异步post请求到UserServlet的regist方法（boolean），传递表单中的数据到后台Servlet.UserServlet extends BaseServlet. 而BaseServlet extends HttpServlet并且重写了service 方法，负责根据访问路径进行方法的分发（简化了Servlet）.因为，根据路径可以访问到UserServlet 然后tomact会找到BaseServlet的service方法，根据访问路径（字符串）运用反射机制 分发方法。

    通过BeanUtils（org.apache.commons.beanutils.BeanUtils;）将表单传过来的数据封装 成User对象。接着到UserService层。Service层通过调用UserDao层进行数据的先查找后插入。在Dao层通过JdbcTemplate和 对查找出来的记录进行对象封装。如果可以注册，通过MailUtils发送一条带有唯一UuidUtil的的超链接给用户的邮箱。在此之前要授权第三方邮箱发送。ResultInfo有flag , errorMsg, data来封装Servcice 最终返回的结果， 最后Servlet使用Jackson将ResultInfo json序列化后传递给前端html页面。其前端获取数据然后用JQuery修改DOM就可以展示信息或跳转页面。

    对于前台表单中的数据输入的校验（正则表达式），都是在前端的编码工作。

    这里有一个类似于验证码的功能。是在前台页面加载完成后，再发一个请求到CheckCodeServlet。这里面通过Graphic 画笔画出验证码，存入session（因为跨请求了！）。
    因为浏览器有缓存为了使每一次访问CheckCodeServlet都可以生效，因此我们可以把访问路径最后加上一个永远在变化的参数new Date（）
##登陆功能：
    用户注册并点击激活连接后会跳转到login.html.
    用户填写表单。通过Ajax异步post请求到UserServlet的login方法（boolean）查询数据库 得到数据库中的User对象， 根据数据库字段status（用户是否激活）。来提醒用户就算密码和用户名都对了还要去邮箱激活。

    ps:登陆成功后需要将user对象存到Session中。因为网站要一直显示用户名。登陆成功后要跳转到index.html.

##用户退出功能：
    /user/exit; UserServlet exit(req, reps)被分发
    request.getSession().invalidate();//session自销毁
    response.sendRedirect(request.getContextPath()+"/login.html"); //因为为重定向，所以要加虚拟目录。因为一个服务器例如tomcat中可以有多个web项目，并且每个项目的虚拟目录都不一样

##用户登陆后跳到在index.html（此html组合header.html）。
    header.html页面一旦加载 CategoryServlet findAll 方法被分发就被访问 就查询数据库表tab_category。重要字段cid, 如国内游 cid = 5。
    同样调用Service层返回<List>再到dao层返回<List> List<Category>。最后servlet层通过Jackson ObjectMapper 序列成json 给前台（header.html）然后前台用JQuery操作Dom展现即可。

    优化：因为数据库表tab_category中的数据是不经常改变的所以可以使用redis优化(categoryService层)。使用Jedis。因为要有顺序所以运用redis的sortedSet数据结构。score就数cid, redis查出来是一个Set<Tuple>. 我们需要把转换成List<Category>以便方便Jackson ObjectMapper的序列化操作。

    此后每一个header.html的分类的li都会跳转到route_list.html携带者cid. 将来就可以通过cid在tab_route中查找路线对象了。重点字段rid

##分页展示：
    不仅减轻数据库查询消耗，并且为用户带来更好的体验。
    根据前面的cid 查tab_route表。 route_list页面一加载就去获得cid(从header.html 超链接过来的)，然后就请求了RouteServlet的分发到pageQyuery方法。收到的数pageBean对象。重要属性List<Route> .RouteService.pageQuery 返回PageBean<Route>.pb的总记录数是查出的总记录数，pb。每页size是 5 。总页数即可求。start = (currentpage-1)*pageSize.

##线路的搜索(查询)：
    前端搜索按钮绑定点击事件多加一个参数rname.跳转到route_list.html.

##路线的详情：
    在route_list.html中路线详情有一个超链接到route_detail.html且带有rid参数。
    route_detail.html页面一加载就访问RouteServlet 的findOne放方法 栽倒Service 层findOnd(rid):Route.通过RouteDao查tab_route_Img. 查出list注入到Route的routeImage中。根据route.sid(tab_route的外键（多的一方）)查出seller.注入到Route对象的Seller中。

##路线收藏：
    从sesson中得到user,进而得到uid. 前台是有rid的。tab_favorite rid uid两个外键。逻辑上会判断用户是否登陆，也就是UserServlet findOne 函数。
    这里面有几个前端展示小技巧：
    location.reload();
    $("#favorite").attr("disabled", "disabled");  //prop设置固有属性，<a>没有固有属性disabled
    $("#favorite").removeAttr("onclick")


     



