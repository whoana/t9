select
	 max(tr.tracking_date)
	,min(tr.tracking_date)
  from top0503 tr
 where tr.tracking_date >= '20220422000000000000'
   and tr.tracking_date <= '20220422239999999999'
;

-- 10만건 중 100건 1초
select
	 *
  from top0503 tr
 where tr.tracking_date >= '20220427000000000000'
   and tr.tracking_date <= to_char(now(), 'yyyymmdd') || '999999999999'
order by tr.tracking_date desc
--OFFSET 0 limit 20 -- 오라클도 된다.
OFFSET 10 ROWS FETCH NEXT 10 ROWS only
;



select version();
--PostgreSQL 14.2 (Debian 14.2-1.pgdg110+1) on x86_64-pc-linux-gnu, compiled by gcc (Debian 10.2.1-6) 10.2.1 20210110, 64-bit
select
	*
  from top0503 tr
 where tr.tracking_date >= '20220427000000000000'
   and tr.tracking_date <= to_char(now(), 'yyyymmdd') || '999999999999'
order by tr.tracking_date desc
--OFFSET 0 limit 20; 오라클도 된다.
OFFSET 10 ROWS FETCH FIRST 10 ROWS only
; 

select
	*
  from top0503 tr
 where tr.tracking_date >= '20220427000000000000'
   and tr.tracking_date <= to_char(now(), 'yyyymmdd') || '999999999999'
   and tr.integration_id = 'TEST004'
order by tr.tracking_date desc
--OFFSET 0 limit 20; 오라클도 된다.
OFFSET 10 ROWS FETCH FIRST 10 ROWS only
; 


select to_char(now(), 'yyyymmddHH24miss')
;

-- 366816
select
	count(*)
  from top0503 tr
 where tr.tracking_date >= '20220427' || '000000000000'
   and tr.tracking_date <= to_char(now(), 'yyyymmdd') || '999999999999'
;

 