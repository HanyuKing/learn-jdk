SELECT
    t1.request_at AS Day,
    CASE

        WHEN t2.cancel_total IS NULL THEN
            0 ELSE round(t2.cancel_total / t1.total, 2) END AS 'Cancellation Rate'

FROM
    (
        SELECT
            a.request_at,
            count(*) AS total
        FROM
            Trips a,
            Users b,
            Users c
        WHERE
            a.client_id = b.users_id
          AND a.driver_id = c.users_id
          AND c.banned = 'No'
          AND b.banned = 'No'
          AND a.request_at >= '2013-10-01'
          AND a.request_at <= '2013-10-03'
        GROUP BY
            a.request_at) t1
        LEFT JOIN (
        SELECT
            a.request_at,
            count(*) AS cancel_total
        FROM
            Trips a,
            Users b,
            Users c
        WHERE
            a.client_id = b.users_id
          AND a.driver_id = c.users_id
          AND c.banned = 'No'
          AND b.banned = 'No'
          AND a.STATUS IN ( 'cancelled_by_driver', 'cancelled_by_client' )
          AND a.request_at >= '2013-10-01'
          AND a.request_at <= '2013-10-03'
        GROUP BY
            a.request_at
    ) t2 ON t1.request_at = t2.request_at