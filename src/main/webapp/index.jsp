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

    <h3>/roles</h3>
    <p>POST /roles => <a href="/roles">Requires JSON body with a name field. Create the role if the name is not already in use.</a></p>
    <p>GET /roles => <a href="/roles">Retrieves all roles and returns a Json Array string</a></p>
    <p>GET /roles/{id} => <a href="/roles/1">Retrieves the requested role as a Json String</a></p>
    <p>PUT /roles/{id} => <a href="/roles/1">Update the role specified, requires a JSON body with the field (name) to be updated</a></p>
    <p>DELETE /roles/{id} => <a href="/roles/1">Deletes the requested role if it is not assigned to users</a></p>

    <h3>/authors</h3>
    <p>POST /authors => <a href="/authors">Requires JSON body with a name field. Create the author if the name is not already in use.</a></p>
    <p>GET /authors => <a href="/authors">Retrieves all authors and returns a Json Array string</a></p>
    <p>GET /authors/{id} => <a href="/authors/1">Retrieves the requested author as a Json String</a></p>
    <p>PUT /authors/{id} => <a href="/authors/1">Update the author specified, requires a JSON body with the field (name) to be updated</a></p>
    <p>DELETE /authors/{id} => <a href="/authors/1">Deletes the requested author if it is not assigned to a partition (to do)</a></p>
</body>
</html>