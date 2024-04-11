# Remember_java

## 依赖项

- springboot 2.7.3
- mybatis-plus 3.4.2

## api

### user

- login

```json
{
  "url": "/user/login",
  "method": "post",
  "role": "user",
  "requestBody": {
    "content": "application/json",
    "params": {
      "username": "admin",
      "password": "admin"
    }
  },
  "responses": {
    "code": "200/400",
    "message": "*",
    "data": {
      "jwt": "*"
    }
  }
}
```

- register

```json
{
  "url": "/user/register",
  "method": "post",
  "role": "user",
  "requestBody": {
    "content": "application/json",
    "params": {
      "username": "admin",
      "password": "admin",
      "phone": "12345678911",
      "email": "123456@qq.com",
      "birthday": "2024-06-02 12:12:12"
    }
  },
  "responses": {
    "code": "200/400",
    "message": "*",
    "data": {
    }
  }
}
```