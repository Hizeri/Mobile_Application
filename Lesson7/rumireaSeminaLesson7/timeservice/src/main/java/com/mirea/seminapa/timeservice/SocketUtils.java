package com.mirea.seminapa.timeservice;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class SocketUtils {

    // Метод создаёт BufferedReader для чтения данных от сервера
    public static BufferedReader getReader(Socket socket) throws IOException {
        return new BufferedReader(
                new InputStreamReader(socket.getInputStream())
        );
    }

    // Метод создаёт PrintWriter для отправки данных на сервер
    // В нашем задании он не используется, но оставляем по методичке
    public static PrintWriter getWriter(Socket socket) throws IOException {
        return new PrintWriter(socket.getOutputStream(), true);
    }
}