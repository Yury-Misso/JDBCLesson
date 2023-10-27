<%--<%@ taglib uri="jakarta.tags.core" prefix="c" %>--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!doctype html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Урок JDBC Список самолетов</title>
    <style>
        body, html {
            height: 100%;
            margin: 0;
            font-family: 'Helvetica Neue', Helvetica, Arial, sans-serif; /* Шрифт, аналогичный используемому Apple */
            background-color: #f9f9f9; /* Светлый фон */
        }

        .home-button {
            position: absolute;
            top: 20px;
            right: 20px;
            padding: 10px 20px;
            border: none;
            border-radius: 12px;
            background-color: #007aff; /* Синий цвет фона */
            color: white;
            font-size: 16px;
            cursor: pointer;
            transition: background-color 0.3s; /* Анимация */
        }

        .home-button:hover {
            background-color: #005bb5; /* Темно-синий цвет при наведении */
        }

        .center-container {
            display: flex;
            flex-direction: column;
            /*justify-content: center;*/
            align-items: center;
            height: 100%;
        }

        table {
            border-collapse: collapse;
            margin-top: 20px;
        }

        th, td {
            border: 1px solid #ddd;
            padding: 8px;
            text-align: center;
        }

        th {
            background-color: #007aff; /* Синий цвет фона */
            color: white;
        }
    </style>
</head>
<body>

<button class="home-button" onclick="location.href='${pageContext.request.contextPath}/';">На главную</button>

<div class="center-container">
    <h1>Список аэропортов:</h1>
    <table>
        <tr>
            <th>Код</th>
            <th>Название аэропорта</th>
            <th>Город</th>
            <th>Координаты</th>
            <th>Часовой пояс</th>
        </tr>
        <tbody>
        <c:forEach var="airPort" items="${airPorts}">
            <tr>
                <td>${airPort.airportCode}</td>
                <td>${airPort.airportName}</td>
                <td>${airPort.city}</td>
                <td>${airPort.coordinates}</td>
                <td>${airPort.timeZone}</td>
            </tr>
        </c:forEach>
        </tbody>
        <tr>
            <th>Код</th>
            <th>Название аэропорта</th>
            <th>Город</th>
            <th>Координаты</th>
            <th>Часовой пояс</th>
        </tr>
    </table>
</div>

</body>
</html>
