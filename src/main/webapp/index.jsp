<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>WebServices</title>
</head>
<body>
    <h1>MusicPlay Web Services</h1>
    <h3>/users</h3>
    <p>POST /users => <a href="/users">Requires JSON body with user login, password, salt fields</a></p>
    <p>GET /users => <a href="/users">Retrieves all users and returns a Json Array string</a></p>
    <p>GET /users/{id} => <a href="/users/1">Retrieves the requested user as a Json String</a></p>
    <p>PUT /users/{id} => <a href="/users/1">Update the user specified, requires a JSON body with the field or fields to be updated</a></p>
    <p>DELETE /users/{id} => <a href="/users/1">Deletes the requested user</a></p>

    <h3>/account</h3>
    <p>POST /account/login => <a href="/account/login">Check credentials (login + password in body)</a></p>
</body>
</html>