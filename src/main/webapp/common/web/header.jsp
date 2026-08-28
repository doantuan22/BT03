<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>${empty pageTitle ? 'IoTStar' : pageTitle}</title>
    <style>
        * {
            box-sizing: border-box;
        }

        body {
            margin: 0;
            min-height: 100vh;
            background: #f2f4f7;
            color: #222;
            font-family: "Segoe UI", Arial, Helvetica, sans-serif;
            display: flex;
            flex-direction: column;
        }

        a {
            color: #2563eb;
            text-decoration: none;
        }

        a:hover {
            text-decoration: underline;
        }

        .container {
            width: 100%;
            max-width: 1000px;
            margin: 0 auto;
            padding: 0 16px;
        }

        /* Header / topbar */
        .site-header {
            background: #1f2937;
            color: #fff;
        }

        .topbar {
            display: flex;
            align-items: center;
            justify-content: space-between;
            flex-wrap: wrap;
            padding: 14px 16px;
        }

        .brand {
            color: #fff;
            font-size: 1.25rem;
            font-weight: 700;
            letter-spacing: 0.5px;
        }

        .brand:hover {
            text-decoration: none;
            opacity: 0.9;
        }

        .right-topbar {
            list-style: none;
            display: flex;
            align-items: center;
            gap: 8px;
            margin: 0;
            padding: 0;
        }

        .right-topbar li {
            display: inline-flex;
            align-items: center;
        }

        .right-topbar a {
            color: #e5e7eb;
        }

        .right-topbar a:hover {
            color: #fff;
        }

        /* Main content area */
        .main-content {
            flex: 1;
            padding: 40px 16px;
            display: flex;
            justify-content: center;
        }

        /* Cards (login / register / dashboard) */
        .auth-wrapper {
            width: 100%;
            display: flex;
            justify-content: center;
        }

        .card,
        .auth-card,
        .dashboard-card {
            background: #fff;
            border-radius: 10px;
            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.08);
            padding: 32px;
            width: 100%;
        }

        .auth-card {
            max-width: 420px;
        }

        .dashboard-card {
            max-width: 640px;
        }

        .card h2,
        .auth-card h2,
        .dashboard-card h2 {
            margin-top: 0;
            margin-bottom: 20px;
            font-size: 1.5rem;
            color: #111827;
        }

        /* Forms */
        .form-group {
            margin-bottom: 16px;
        }

        .form-group label {
            display: block;
            margin-bottom: 6px;
            font-size: 0.9rem;
            color: #374151;
        }

        .form-control {
            width: 100%;
            padding: 10px 12px;
            border: 1px solid #d1d5db;
            border-radius: 6px;
            font-size: 1rem;
            background: #fff;
        }

        .form-control:focus {
            outline: none;
            border-color: #2563eb;
            box-shadow: 0 0 0 3px rgba(37, 99, 235, 0.15);
        }

        .form-check {
            margin-bottom: 20px;
            font-size: 0.9rem;
        }

        .form-check label {
            display: flex;
            align-items: center;
            gap: 6px;
        }

        /* Buttons */
        .btn {
            display: inline-block;
            padding: 10px 18px;
            border-radius: 6px;
            border: 1px solid transparent;
            font-size: 1rem;
            cursor: pointer;
            text-align: center;
        }

        .btn:hover {
            text-decoration: none;
        }

        .btn-block {
            display: block;
            width: 100%;
        }

        .btn-primary {
            background: #2563eb;
            border-color: #2563eb;
            color: #fff;
        }

        .btn-primary:hover {
            background: #1d4ed8;
        }

        .btn-outline {
            background: #fff;
            border-color: #d1d5db;
            color: #374151;
        }

        .btn-outline:hover {
            background: #f3f4f6;
        }

        /* Alerts */
        .alert {
            padding: 10px 14px;
            border-radius: 6px;
            margin-bottom: 16px;
            font-size: 0.9rem;
        }

        .alert-danger {
            background: #fef2f2;
            color: #b91c1c;
            border: 1px solid #fecaca;
        }

        /* Misc text */
        .auth-alt {
            margin-top: 20px;
            margin-bottom: 0;
            font-size: 0.9rem;
            text-align: center;
            color: #4b5563;
        }

        .dashboard-card p {
            color: #374151;
        }

        /* Footer */
        .site-footer {
            background: #1f2937;
            color: #9ca3af;
            text-align: center;
            padding: 16px;
            font-size: 0.85rem;
        }

        /* Landing page */
        .landing-card {
            text-align: center;
        }

        .landing-actions {
            display: flex;
            gap: 12px;
            justify-content: center;
            margin-top: 24px;
            flex-wrap: wrap;
        }
    </style>
</head>
<body>
<header class="site-header">
    <div class="container topbar">
        <a class="brand" href="${pageContext.request.contextPath}/">IoTStar</a>
        <jsp:include page="/common/web/topbar.jsp"/>
    </div>
</header>
<main class="container main-content">
