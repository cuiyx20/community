package com.nowcoder.community.controller;

import com.nowcoder.community.service.AlphaService;
import com.nowcoder.community.util.CommunityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

@Controller
@RequestMapping("/alpha")
//上面的两个注解是SpringMVC的注解，alpha是浏览器的访问名
public class AlphaController {

    @Autowired
    private AlphaService alphaService;     //因为controller在处理浏览器请求的过程中需要调用service业务组件来处理，所以这种依赖关系就使用依赖注入的方式实现



    @RequestMapping("/hello")
    @ResponseBody
    //声明这是一个字符串，不是一个网页
    public String sayHello(){
        return "Hello Spring Boot.";
    }

    @RequestMapping("/data")
    @ResponseBody
    //得有这个声明，返回浏览器的路径
    public String getData(){
        return alphaService.find();
    }

    //在Controller中需要对请求和响应做出处理

    @RequestMapping("/http")
    //给出返回路径，方便浏览器查询

    //这个方法是为了理解处理请求和响应的步骤，更简便的使用封装之后的工具如何处理在后面的方法
    public void http(HttpServletRequest request, HttpServletResponse response){         //没有返回值，是因为可以用response对象直接想浏览器输出，不依赖于返回值
        ///DispatcherServlet前端控制器直接就会将响应和请求对象分别在底层传回
        //处理请求就是读取传入的请求中包含的数据

        //获取请求数据
        System.out.println(request.getMethod());   //获取请求的方法
        System.out.println(request.getServletPath());      //获取请求的路径
        //上面两个是请求的第一行数据，也就是请求行的内容
        Enumeration<String> enumeration = request.getHeaderNames();      //获取请求消息头【有很多数据，做了封装，可以通过这个方法获得】，请求行是key-value结构，放到迭代器Enumeration中
        while(enumeration.hasMoreElements()){     //遍历迭代器【和iteration是差不多的】，获取内容
            String name = enumeration.nextElement();       //name是key，可以通过get得到它的value
            String value = request.getHeader(name);
            System.out.println(name + ": " + value);
        }
        //上面是获得请求的消息头，若干行数据

        //下面是获取请求体的内容
        System.out.println(request.getParameter("code"));
        System.out.println(request.getParameter("name"));
        //http://localhost:8080/alpha/http/?code=123&name=zhangsan

        //返回响应数据
        response.setContentType("text/html;charset = utf-8");      //返回内容类型，是网页还是什么；这里表示返回一个网页，支持中文
        try(PrintWriter writer = response.getWriter();//获取输出流，需要处理一下异常，使用try-catch块  ——这个输入流是一个资源，我们最后使用完应该在finally中关掉，但是Java7有一个新的语法，把资源在小括号里创建，就会自动加一个finally，在finally中关掉，方便很多——前提是这个对象必须要有close()方法
        ) {
            writer.write("<h1>牛客网</h1>");          //这个只是帮助理解MVC的例子，所以这里没用封装好的工具，如果不使用，就是这样一行行write网页内容，但是实际项目中我们不会这么做，这里就做一个简单的了解
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //使用封装之后的方法来处理请求和响应

    //对请求数据的处理——简便方式
    //Get请求——浏览器向服务器获取请求

    //查询所有的学生——/students?current=1&limit=20——这是浏览器给的请求数据，表示获取的对象是学生，当前是第几页，一页显示多少数据
    @RequestMapping(path = "/students",method = RequestMethod.GET)         //这个注解有很多参数，比如路径，处理的方法——这样声明就是只能处理Get方法
    @ResponseBody       //表示只是返回一个简单的字符串，不是网页
    public String getStudents(@RequestParam(name = "current",required = false,defaultValue = "1") int current,@RequestParam(name = "limit",required = false,defaultValue = "10") int limit){
                             //通过@RequestParam注解，对参数的注入做更详细的说明，也就是浏览器的路径中的参数怎么获取来跟我们的方法的形参一一对应，后面两个表示如果浏览器的请求中可以没有这两个参数，我们的默认值是多少
        System.out.println(current);
        System.out.println(limit);
        return "some students";
    }

    //查询一个学生，根据学生的id——/student/123 ——假设要获取的学生id是123，那么我们可以把学生的id编排到路径中，但是获取方式也有所不同：
    @RequestMapping(path = "/student/{id}",method = RequestMethod.GET)
    @ResponseBody
    public String getStudent(@PathVariable("id") int id){           //从路径中获取参数的方法使用注解@PathVariable
        System.out.println(id);
        return "a student";
    }


    //POST请求——浏览器向服务器提交数据——这个需要浏览器打开一个带表单的网页，向表单中填写数据并提交之后才能提交给服务器，所以我们需要写一个静态网页，获取一下
    //不用get请求是因为get请求传参会在路径上带参数，明文不安全；其二路径是有长度限制的，如果参数很多项，get请求传不下，所以通常提交数据的时候不用get请求，而是用post请求
    @RequestMapping(path = "/student",method = RequestMethod.POST)
    @ResponseBody
    public String saveStudent(String name,int age){
        System.out.println(name);
        System.out.println(age);
        return "success";
    }


    //响应HTML数据的两种办法——返回给浏览器一个HTML网页
    @RequestMapping(path = "/teacher",method = RequestMethod.GET)
    //不加@ResponseBody——加了就是字符串，不加就默认返回一个网页
    public ModelAndView getTeacher(){
        /*
        Spring MVC的原理：所有组件都是由DispatchServlet【前端控制器】调度的，它会调Controller的某个方法，这个方法会返回model数据，以及视图相关的数据。
        它会把model和视图相关的数据都提交给模板引擎，由模板引擎渲染，生成动态的html。
        所以这个ModelAndView的对象中封装的就是要给DispatchServlet返回的model和view两份数据
         */
        ModelAndView mav = new ModelAndView();
        //向对象中传数据，要看模板里需要多少个变量
        mav.addObject("name","张三");
        mav.addObject("age",30);
        mav.setViewName("/demo/view");//要把模板放到templates中，这里设置一下路径，在其中的下级目录——templates中默认存放的是html文件，所以这里可以省略掉后缀，注意view是文件名，省略了.html
        return mav;       //model和view数据都装进这个对象里
    }
    @RequestMapping(path = "/school",method = RequestMethod.GET)
    public String getSchool(Model model){     //简化版返回动态模板网页的方法 ——不返回ModelAndView类型了，而是返回String类型
                  //这个model对象不是我们自己创建的——DispatchServlet在调这个方法时发现有model对象，就会自动实例化这个model对象，传进来
                    //DispatchServlet持有这个对象的引用，在方法内部向这个model传数据，DispatchServlet持有的对象也能得到数据
                    //这个方法的model装在参数里，view的视图直接返回，返回的view值给了DispatchServlet，而DispatchServlet又持有model的引用
        model.addAttribute("name","吉林大学");
        model.addAttribute("age",75);
        return "/demo/view";     //返回的是网页的路径，也就是templates下的下级目录
    }

    //服务器向浏览器响应别的数据——JSON数据——在异步请求当中——当前网页不动，不刷新，但是访问了服务器，返回一个结果【这个结果显然不是html，不然应该换一个网页显示或者网页刷新】，这种请求就是异步请求
    //Java对象 -> JSON字符串 ->JS对象

    @RequestMapping(path = "/emp",method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> getEmp(){
        Map<String,Object> emp = new HashMap<>();
        emp.put("name","张三");
        emp.put("age",23);
        emp.put("salary",8000.00);
        return emp;         //返回给浏览器的是一个JSON字符串——key-value结构 {"name":"张三","salary":8000.0,"age":23}
    }

    //上面的方法返回的是一个员工，有些情况可能我们需要返回一组员工。实现方法类似
    @RequestMapping(path = "/emps",method = RequestMethod.GET)
    @ResponseBody
    public List<Map<String,Object>> getEmps(){
        //[{"name":"于文文","salary":8000.0,"age":32},{"name":"李斯","salary":9000.0,"age":30},{"name":"金晨","salary":10000.0,"age":30}]
        List<Map<String,Object>> list = new ArrayList<>();
        Map<String,Object> emp = new HashMap<>();
        emp.put("name","于文文");
        emp.put("age",32);
        emp.put("salary",8000.00);
        list.add(emp);

        emp = new HashMap<>();
        emp.put("name","李斯");
        emp.put("age",30);
        emp.put("salary",9000.00);
        list.add(emp);

        emp = new HashMap<>();
        emp.put("name","金晨");
        emp.put("age",30);
        emp.put("salary",10000.00);
        list.add(emp);

        return list;         //返回给浏览器的是一个JSON字符串——key-value结构 {"name":"张三","salary":8000.0,"age":23}
    }

    //cookie示例
    @RequestMapping(path="/cookie/set",method=RequestMethod.GET)
    @ResponseBody
    public String setCookie(HttpServletResponse response){
        //创建cookie
        Cookie cookie = new Cookie("code", CommunityUtil.generateUUID());
        //设置cookie生效的范围
        cookie.setPath("/community/alpha");
        //设置cookie的生存时间
        cookie.setMaxAge(60 * 10);       //cookie生成后默认存到内存里，关掉浏览器就消失了，如果想让生存时间更长就设置一下，这样就存到了硬盘里，关掉浏览器也存在，超时就消失
        //发送cookie
        response.addCookie(cookie);
        return "set cookie";
    }

    @RequestMapping(path="/cookie/get",method=RequestMethod.GET)
    @ResponseBody
    public String getCookie(@CookieValue("code") String code){
        System.out.println(code);
        return "get cookie";
    }

    //session示例
    @RequestMapping(path="/session/set",method=RequestMethod.GET)
    @ResponseBody
    public String setSession(HttpSession session){      //和cookie不同，session可由SpringMVC自动创建并注入，只需要像request\response\model之类的做个声明就可以使用
        session.setAttribute("id",1);
        session.setAttribute("name","Test");
        return "set session";
    }
    @RequestMapping(path="/session/get",method=RequestMethod.GET)
    @ResponseBody
    public String getSession(HttpSession session){
        System.out.println(session.getAttribute("id"));
        System.out.println(session.getAttribute("name"));
        return "get session";
    }

    //ajax示例
    @RequestMapping(path = "/ajax",method = RequestMethod.POST)
    @ResponseBody
    public String testAjax(String name, int age){
        System.out.println(name);
        System.out.println(age);
        return CommunityUtil.getJSONString(0,"操作成功！");
    }
}
