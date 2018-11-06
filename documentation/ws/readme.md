# MusicPlay WS API Documentation

## Swagger

#### Preview
https://app.swaggerhub.com/apis-docs/iepscf/musicplay-ws

#### Documentation
https://swagger.io/docs/specification/about

#### Yaml Content

```yaml
openapi: 3.0.0
info:
  description: MusicPlay Web Services
  version: "1.0.2"
  title: MusicPlay Web Services
  license:
    name: Apache 2.0
    url: 'http://www.apache.org/licenses/LICENSE-2.0.html'
tags:
  - name: login
    description: User authentication
  - name: users
    description: Users parameters
  - name: roles
    description: User roles on the system
  - name: partitions
    description: Partitions catalog on the system
  - name: authors
    description: Partitions authors
paths:
  /account/login:
    post:
      tags:
        - login
      summary: Authenticate users
      operationId: verifyCredentials
      responses:
        '200':
          description: Valid credentials
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/User'
        '401':
          description: Invalid credentials
      requestBody:
        content:
          application/json:
            schema:
              $ref: '#/components/schemas/LoginSchema'
        description: User login and password
        required: true
  /users:
    get:
      tags:
        - users
      summary: Get all users
      operationId: getUsers
      responses:
        '200':
          description: OK
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/Users'
    post:
      tags:
        - users
      summary: Create a user
      operationId: addUserJson
      requestBody:
        content:
          application/json:
            schema:
              $ref: '#/components/schemas/UserCreationSchema'
        description: Create user with login and password specified.
        required: true
      responses:
        400:
          description: login already exists or roles specified do not all exist on the system
        201:
          description: user created
          links: 
            GetUserById:
              operationId: getUserById
              parameters:
                message: 'path/users/{id}'
        
      
  /users/{id}:
    get:
      tags:
        - users
      summary: Get a specific user
      operationId: getUserById
      parameters:
        - in: path
          name: id
          required: true
          schema:
            type: integer
            minimum: 1
          description: The user ID
      responses:
        400:
          description: Unsupported request URL
        404:
          description: User not found
        200:
          description: OK
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/User'
    put:
      tags:
        - users
      summary: Update a specific user
      operationId: updateUserById
      parameters:
        - in: path
          name: id
          required: true
          schema:
            type: integer
            minimum: 1
          description: The user ID
      requestBody:
        content:
          application/json:
            schema:
              $ref: '#/components/schemas/UserUpdateSchema'
        description: Update user password and/or roles
        required: true
      responses:
        400:
          description: Unsupported request URL
        404:
          description: User not found
        201:
          description: User {id} updated
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/User'
    delete:
      tags:
        - users
      summary: Delete a specific user
      operationId: deleteUserById
      parameters:
        - in: path
          name: id
          required: true
          schema:
            type: integer
            minimum: 1
          description: The user ID
      responses:
        400:
          description: Unsupported request URL
        404:
          description: User not found
        200:
          description: User deleted
  /roles:
    get:
      tags:
        - roles
      summary: Get all roles
      operationId: getUserRoles
      responses:
        '200':
          description: OK
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/Roles'
    post:
      tags:
        - roles
      summary: Create a role
      operationId: addUserRoleJson
      requestBody:
        content:
          application/json:
            schema:
              $ref: '#/components/schemas/RoleUpdateAndCreationSchema'
        description: Create role with the name specified.
        required: true
      responses:
        400:
          description: role already exists
        201:
          description: role created
          links: 
            GetUserById:
              operationId: getUserRoleById
              parameters:
                message: 'path/roles/{id}'
  /roles/{id}:
    get:
      tags:
        - roles
      summary: Get a specific role
      operationId: getUserRoleById
      parameters:
        - in: path
          name: id
          required: true
          schema:
            type: integer
            minimum: 1
          description: The role ID
      responses:
        400:
          description: Unsupported request URL
        404:
          description: Role not found
        200:
          description: OK
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/Role'
    put:
      tags:
        - roles
      summary: Update a specific role
      operationId: updateUserRoleById
      parameters:
        - in: path
          name: id
          required: true
          schema:
            type: integer
            minimum: 1
          description: The role ID
      requestBody:
        content:
          application/json:
            schema:
              $ref: '#/components/schemas/RoleUpdateAndCreationSchema'
        description: Update role name
        required: true
      responses:
        400:
          description: Unsupported request URL / Role already exists
        404:
          description: Role not found
        201:
          description: Role {id} updated
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/Role'
    delete:
      tags:
        - roles
      summary: Delete a specific role
      operationId: deleteUserRoleById
      parameters:
        - in: path
          name: id
          required: true
          schema:
            type: integer
            minimum: 1
          description: The role ID
      responses:
        400:
          description: Unsupported request URL
        404:
          description: Role not found
        200:
          description: Role deleted
components:
  schemas:
    LoginSchema:
      type: object
      required: ["login", "password"]
      properties:
        login:
          type: string
        password:
          type: string
    UserCreationSchema:
      type: object
      required: ["login", "password"]
      properties:
        login:
          type: string
        password:
          type: string
        roles:
          type: array
          items:
            type: string
    UserUpdateSchema:
      type: object
      properties:
        password:
          type: string
        roles:
          type: array
          items:
            type: string
    User:
      type: object
      properties:
        id:
          type: integer
        login:
          type: string
        roles:
          type: array
          items:
            type: string
    Users:
      type: array
      items:
        $ref: '#/components/schemas/User'
    Role:
      type: object
      properties:
        id:
          type: integer
        name:
          type: string
    Roles:
      type: array
      items:
        $ref: '#/components/schemas/Role'
    RoleUpdateAndCreationSchema:
      type: object
      required: ["name"]
      properties:
        name:
          type: string
servers:
  - url: 'http://10.3.13.220:8081/musicplay-ws'
```
