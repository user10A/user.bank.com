--запрос на вывод пользователей у которых на балансе больше 10 000
select u.id, u.user_name, u.email, a.balance AS total_balance
from users u
         join accounts a ON u.id = a.user_id
where a.balance > 10000
group by u.id, u.user_name, u.email, a.balance;

--запрос на вывод по почте
select *
from users u
where u.email = 'johndoe@example.com';

-- запрос на вывод общею сумма в системе
select SUM(a.balance) AS total_system_balance
from accounts a;

SELECT last_value FROM user_seq;


ALTER SEQUENCE user_seq RESTART WITH 21;