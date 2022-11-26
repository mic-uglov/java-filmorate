INSERT INTO genres
SELECT *
FROM (
    SELECT 1, 'Комедия'
    UNION ALL
    SELECT 2, 'Драма'
    UNION ALL
    SELECT 3, 'Мультфильм'
    UNION ALL
    SELECT 4, 'Триллер'
    UNION ALL
    SELECT 5, 'Документальный'
    UNION ALL
    SELECT 6, 'Боевик'
)
WHERE NOT EXISTS (SELECT NULL FROM genres);

INSERT INTO mpa_rating
SELECT *
FROM (
    SELECT 1, 'G', 'у фильма нет возрастных ограничений'
    UNION ALL
    SELECT 2, 'PG', 'детям рекомендуется смотреть фильм с родителями'
    UNION ALL
    SELECT 3, 'PG-13', 'детям до 13 лет просмотр не желателен'
    UNION ALL
    SELECT 4, 'R', 'лицам до 17 лет просматривать фильм можно только в присутствии взрослого'
    UNION ALL
    SELECT 5, 'NC-17', 'лицам до 18 лет просмотр запрещён'
)
WHERE NOT EXISTS (SELECT NULL FROM mpa_rating);