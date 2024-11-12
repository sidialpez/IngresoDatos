package com.sesion6.clase.Interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

public class MyInterceptor implements HandlerInterceptor {
    //este nos da el formato de la hora, la primer inicia y la otra bloquea
    private final LocalTime starTime = LocalTime.of(10,0);

    private final LocalTime endTime = LocalTime.of(17,0);
    private final List<String> ipsPermitidas = Arrays.asList(
        "0:0:0:0:0:0:0:1",
        "127.0.0.1"
    );


    /*@Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception{

        LocalTime currentTime = LocalTime.now();
        if(currentTime.isBefore(starTime) || currentTime.isAfter(endTime)){
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            String jsonResponse = "{\"message\":\"No se permiten solicitudes" +
                    "fuera del horario laboral(10 AM - 5 PM)\"}";
            response.getWriter().write(jsonResponse);
            response.getWriter().flush();
            return false;
        }
        System.out.println("Estamos dentro del horario laboral");
        return true;
    }*/

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception{

       String clientIp = request.getRemoteAddr();
       if(!ipsPermitidas.contains(clientIp)){
           response.setStatus(HttpServletResponse.SC_FORBIDDEN);
           response.setContentType("application/json");
           response.setCharacterEncoding("UTF-8");
           String jsonResponse = "{\"message\":\"No se permite solicitudes" +
                   "de esa IP\"}";
           response.getWriter().write(jsonResponse);
           response.getWriter().flush();
           return false;
       }
        System.out.println("Acceso Permitido");
       return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response,
                           Object handler, org.springframework.web.servlet.ModelAndView modelAndView)
            throws Exception{

        String requestUrl = request.getRequestURI();
        String clienteIp = request.getRemoteAddr();
        String httpMethod= request.getMethod();

        System.out.println("Solicitud manejando: "+httpMethod+" "+requestUrl+" "+clienteIp);

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                                Object handler, Exception exception) throws Exception{
        System.out.println("After completion invocado");
        //simular alguna tarea para guardar en bitacora

    }

}
