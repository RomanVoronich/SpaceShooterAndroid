# Запуск и настройка игры

## Что нужно
Установленное node.js (ver>=4.2.*) npm (ver>=2.14)
Прежде чем запустить - нужно установить пакеты npm
```
cd DodgeAndShoot/server
npm install
```
Необходимо прописать в DodgeAndShoot/core/src/com/neolabs/utils/Constants.java константу END_POINT_IO на свой IP адрес сервера node.js

## Как запустить сервер
```
cd DodgeAndShoot/server
node index.js
```

## Что еще нужно для удачного запуска
Необходимо чтобы телефоны на которых производится тестирование игры были подключены к интернету и с них был доступен сервер node.js

Также есть возможность протестировать web версию игры на webgl в браузере, для этого необходимо в адресе набрать http://192.168.1.33:3000

