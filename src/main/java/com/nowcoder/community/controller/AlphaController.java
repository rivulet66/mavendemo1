package com.nowcoder.community.controller;

import com.nowcoder.community.service.AlphaService;
import com.nowcoder.community.util.CommunityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
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
public class AlphaController {

    @Autowired
    private AlphaService alphaService;

    @RequestMapping("/hello")
    @ResponseBody
    public String sayHello(){
        return "Hello Spring Boot.";
    }

    @RequestMapping("/data")
    @ResponseBody
    public String getData(){
        return alphaService.find();
    }

    // 如何获得请求数据
    @RequestMapping("/http")
    public void http(HttpServletRequest request, HttpServletResponse response){
        System.out.println(request.getMethod());
        System.out.println(request.getServletPath());
//        System.out.println(request.getHeaderNames());
        Enumeration<String> enumeration = request.getHeaderNames();
        while(enumeration.hasMoreElements()){ // 请求头
            String name = enumeration.nextElement();
            String value = request.getHeader(name);
            System.out.println(name + ": " + value);
        }

        //请求体
        System.out.println(request.getParameter("code"));

        //返回响应数据
        response.setContentType("text/html;charset=utf-8");
        try{
            PrintWriter writer = response.getWriter();
            writer.write("<h1>牛客网</h1>");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //GET请求
    // /students?current=1&limit=20  ?获取参数
    @RequestMapping(path="/students", method = RequestMethod.GET)
    @ResponseBody
    public String getStudents(
            @RequestParam(name = "current", required = false, defaultValue = "1") int current,
            @RequestParam(name = "limit", required = false, defaultValue = "10") int limit){
        System.out.println(current);
        System.out.println(limit);
        return "some students";
    }

    // 查询1个学生 /student/123 把参数拼到url中
    @RequestMapping(path="/student/{id}", method=RequestMethod.GET)
    @ResponseBody
    public String getStudent(@PathVariable("id") int id){
        System.out.println(id);
        return "a student";
    }

    //POST请求
    @RequestMapping(path="/student", method=RequestMethod.POST)
    @ResponseBody
    public String saveStudent(String name, int age){ //与表单中的参数一致，就可自动传过来
        System.out.println(name);
        System.out.println(age);
        return "success";
    }

    //如何向浏览器返回相应数据——动态html
    /**
     * 一、相应HTML数据的2种方法
     * 1.把model的数据都装到一个对象(ModelAndView)里
     * 2.把model的数据都装到一个参数(Model)里
     */
    //方法一：
    @RequestMapping(path="/teacher", method = RequestMethod.GET)
    public ModelAndView getTeacher(){
        //把model的数据都装到一个对象里
        ModelAndView mav = new ModelAndView();
        mav.addObject("name", "张三");
        mav.addObject("age", 30);
        mav.setViewName("/demo/view");
        return mav;
    }

    @RequestMapping(path="/school", method=RequestMethod.GET)
    public String getSchool(Model model){
        //把model数据都装到一个参数里
        model.addAttribute("name", "北京大学");
        model.addAttribute("age", 80);
        return "/demo/view";
    }

    /**
     * 二、响应JSON数据——异步请求
     * Java对象 -> JSON字符串 -> JS对象 (跨语言)
     * 1.
     */

    @RequestMapping(path="/emp", method=RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getEmp(){ //返回一个员工的数据
        Map<String, Object> emp = new HashMap<>();
        emp.put("name", "张三");
        emp.put("age", 23);
        emp.put("salary", 8000.0);
        return emp;
    }

    //返回一组员工的数据
    @RequestMapping(path="/emps", method=RequestMethod.GET)
    @ResponseBody
    public List<Map<String, Object>> getEmps(){
        List<Map<String, Object>> list = new ArrayList<>();
        Map<String, Object> emp = new HashMap<>();
        emp.put("name", "张三");
        emp.put("age", 23);
        emp.put("salary", 8000.0);
        list.add(emp);

        emp = new HashMap<>();
        emp.put("name", "李四");
        emp.put("age", 24);
        emp.put("salary", 9000.0);
        list.add(emp);

        emp = new HashMap<>();
        emp.put("name", "王五");
        emp.put("age", 25);
        emp.put("salary", 10000.0);
        list.add(emp);

        return list;
    }

    //创建cookie
    @RequestMapping(path="/cookie/set", method=RequestMethod.GET)
    @ResponseBody
    public String setCookie(HttpServletResponse response){
        //创建cookie
        Cookie cookie = new Cookie("code", CommunityUtil.generateUUID());
        //设置cookie的生效范围
        cookie.setPath("/community/alpha");
        cookie.setMaxAge(60*10);//设置cookie对象可存活600s，即10min
        //发送cookie
        response.addCookie(cookie);

        return "set cookie";
    }

    @RequestMapping(path="/cookie/get", method=RequestMethod.GET)
    @ResponseBody
    public String getCookie(@CookieValue("code") String code){
        System.out.println(code);
        return "get cookie";
    }

    //设置session
    @RequestMapping(path="/session/set", method=RequestMethod.GET)
    @ResponseBody
    public String setSession(HttpSession session){
        session.setAttribute("id", 1);
        session.setAttribute("name", "Test");
        return "set session";
    }

    @RequestMapping(path="/session/get", method=RequestMethod.GET)
    @ResponseBody
    public String getSession(HttpSession session){
        System.out.println(session.getAttribute("id"));
        System.out.println(session.getAttribute("name"));
        return "get session";
    }

}
