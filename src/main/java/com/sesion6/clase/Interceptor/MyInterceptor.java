package com.sesion6.clase.Interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

public class MyInterceptor implements HandlerInterceptor {
    // Hora de fin de acceso permitido
    private final LocalTime endTime = LocalTime.of(17, 0);
    private final List<String> ipsPermitidas = Arrays.asList(
            "0:0:0:0:0:0:0:1",
            "127.0.0.1"
    );

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // Obtener la IP del cliente desde el header X-Forwarded-For si está disponible
        String clientIp = request.getHeader("X-Forwarded-For");
        if (clientIp == null) {
            clientIp = request.getRemoteAddr();
        }

        // Permitir todas las IPs si el entorno es producción (detectado por la variable de entorno RAILWAY_ENVIRONMENT)
        if (System.getenv("RAILWAY_ENVIRONMENT") != null || ipsPermitidas.contains(clientIp)) {
            System.out.println("Acceso Permitido");
            return true;
        }

        // Rechazar la solicitud si la IP no es permitida
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        String jsonResponse = "{\"message\":\"No se permiten solicitudes de esa IP\"}";
        response.getWriter().write(jsonResponse);
        response.getWriter().flush();
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response,
                           Object handler, org.springframework.web.servlet.ModelAndView modelAndView)
            throws Exception {
        String requestUrl = request.getRequestURI();
        String clienteIp = request.getRemoteAddr();
        String httpMethod = request.getMethod();

        System.out.println("Solicitud manejada: " + httpMethod + " " + requestUrl + " desde IP " + clienteIp);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                                Object handler, Exception exception) throws Exception {
        System.out.println("After completion invocado");
        // Simular alguna tarea para guardar en bitácora, si es necesario
    }
}
