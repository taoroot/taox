# taox

host 配置:

127.0.0.1 auth-server register-server

# 启动顺序

EurekaApplication :8848/

GatewayApplication :8008/

UpmsApplication :6513/

AuthApplication :6628/

MallApplication :8800/

# 然后访问 

127.0.0.1:8008/mall

